package com.cgq.educenter.controller;

import com.cgq.commonutils.JwtUtils;
import com.cgq.educenter.entity.UcenterMember;
import com.cgq.educenter.service.UcenterMemberService;
import com.cgq.educenter.utils.ConstantWXUtils;
import com.cgq.educenter.utils.HttpClientUtils;
import com.cgq.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
public class WXApiController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    //扫描二维码信息，
    @GetMapping("callback")
    public String callback(String code,String state){

        try {


//        System.out.println(code + " 哈哈哈哈 " + state); //061MSnFa1mgcBB0fPrIa1Tnsu52MSnFf 哈哈哈哈 cgq
            //1.获取code值，临时票据，类似验证码


            //2.用code请求微信固定地址，得到access_token 和 openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            //提供参数
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWXUtils.WX_OPEN_APP_ID,
                    ConstantWXUtils.WX_OPEN_APP_SECRET,
                    code
            );

            //httpclient请求此地址得到两个值
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            //字符串转换为map
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String accessToken = (String)mapAccessToken.get("access_token");
            String openid = (String)mapAccessToken.get("openid");



            //把信息添加进数据库
            //判断数据库是否已经存在此用户信息
           UcenterMember member = ucenterMemberService.getOpenIdUser(openid);
           if(member == null){
               //如果数据库存在，就直接从数据库获取，不要去微信获取
               //3.用accessToken和openid请求微信固定地址，获取扫描信息
               String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                       "?access_token=%s" +
                       "&openid=%s";

               //参数
               String userInfoUrl = String.format(
                       baseUserInfoUrl,
                       accessToken,
                       openid
               );

               String userInfo = HttpClientUtils.get(userInfoUrl);

               //获取返回的信息
               HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
               String nickname = (String) userInfoMap.get("nickname");
               String headimgurl = (String) userInfoMap.get("headimgurl");

               //添加
               member = new UcenterMember();
               member.setOpenid(openid);
               member.setNickname(nickname);
               member.setAvatar(headimgurl);
               ucenterMemberService.save(member);

           }
            //根据jwt生成token，通过路径传递，因为cookie不支持跨域，
            String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());

            //返回首页面
            return "redirect:http://localhost:3000?token=" + token;

        }catch (Exception e){

            throw new GuliException(20001,"登录失败");
        }



    }

    //生成二维码
    @GetMapping("login")
    public String getWX()  {

        //微信开发授权平台地址
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //对redirect——urlURLEncoder编码
        String redirectUrl = ConstantWXUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        对%s赋值
       String url = String.format(
                baseUrl,
                ConstantWXUtils.WX_OPEN_APP_ID,
               redirectUrl,
                "cgq"  //state
        );

        //重定向请求微信固定地址
        return "redirect:" + url;
    }
}

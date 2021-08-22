package com.cgq.oos.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.cgq.oos.service.ossService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cgq.oos.utils.ConstantPropertiesUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class ossServiceImpl implements ossService {

    //上传文件
    @Override
    public String uploadFileAvatar(MultipartFile file) {


        String endpoint = ConstantPropertiesUtil.END_POINT;
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;


        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();

            //1.在文件名称添加随机值防止文件名重复
            String uuid = UUID.randomUUID().toString().replaceAll("-","");//把-替换为空
            fileName = uuid + fileName;

            //2.把文件按日期分类
            //2021/07/11/01.jpg
            String date = new DateTime().toString("yyyy/MM/dd");
            //拼接
            fileName = date + "/" + fileName;

            // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
            // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
            //获取文件名称
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //访问文件路径返回  ，拼接
            // https://eduprojectcgq.oss-cn-shanghai.aliyuncs.com/abcdef.jpg

            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return url;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}

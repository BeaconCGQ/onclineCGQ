package com.cgq.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgq.commonutils.JwtUtils;
import com.cgq.commonutils.vo.UcenterMember;
import com.cgq.eduservice.entity.EduComment;
import com.cgq.eduservice.feignclient.UserClient;
import com.cgq.eduservice.mapper.EduCommentMapper;
import com.cgq.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgq.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author cgq
 * @since 2021-08-20
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    @Autowired
    private EduCommentMapper commentMapper;

    @Autowired
    private UserClient userClient;

    //分页查询评论
    @Override
    public Map<String, Object> getCommentPage(Long page, Long limit,String courseId) {

        QueryWrapper<EduComment> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().eq(EduComment::getCourseId,courseId);
        queryWrapper.lambda().orderByDesc(EduComment::getGmtCreate);

        Page<EduComment> pages = new Page<>(page,limit);

        Page<EduComment> pageParam = commentMapper.selectPage(pages, queryWrapper);

        List<EduComment> commentList = pageParam.getRecords();

        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return map;
    }

    @Override
    public void saveComment(EduComment eduComment, HttpServletRequest request) {

        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        if(StringUtils.checkValNull(memberId)){
            throw new GuliException(20001,"请先登录");
        }

        eduComment.setMemberId(memberId);

        UcenterMember user = userClient.getInfo(memberId);

        String nickname = user.getNickname();
        String avatar= user.getAvatar();


        eduComment.setNickname(nickname);
        eduComment.setAvatar(avatar);

        commentMapper.insert(eduComment);

    }
}

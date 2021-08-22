package com.cgq.eduservice.service;

import com.cgq.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author cgq
 * @since 2021-08-20
 */
public interface EduCommentService extends IService<EduComment> {

    Map<String,Object> getCommentPage(Long page, Long limit,String courseId);

    void saveComment(EduComment eduComment, HttpServletRequest request);
}

package com.cgq.eduservice.controller.front;

import com.cgq.commonutils.R;
import com.cgq.eduservice.entity.EduComment;
import com.cgq.eduservice.service.EduCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/eduservice/educomment")
public class EduCommentController {

  @Autowired
  private EduCommentService commentService;

  //分页查询课件评论功能
    @PostMapping("getCommentPage/{page}/{limit}")
    public R getCommentPage(@PathVariable Long page,
                            @PathVariable Long limit,
                            String courseId){

     Map<String,Object> commentMap = commentService.getCommentPage(page,limit,courseId);

      return R.ok().data("commentMap",commentMap);
    }

    //添加评论
    @PostMapping("addComment")
    public R addComment(@RequestBody EduComment eduComment, HttpServletRequest request){

        commentService.saveComment(eduComment,request);

        return R.ok();
    }
}

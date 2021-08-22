package com.cgq.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "Course查询对象", description = "课程查询对象封装")
@Data
public class CourseQuery {

    private static final long serialVersionUID = 2L;

    @ApiModelProperty(value = "课程名称,模糊查询")
    private String courseName;

    @ApiModelProperty(value = "课程状态 Normal已发布 Draft未发布")
    private String courseStatus;

}


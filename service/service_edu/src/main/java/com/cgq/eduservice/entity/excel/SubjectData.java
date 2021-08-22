package com.cgq.eduservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SubjectData {

    //设置列对应的属性
    @ExcelProperty(index = 0)
    private String oneSubjectName;

    //设置列对应的属性
    @ExcelProperty(index = 1)
    private String twoSubjectName;
}

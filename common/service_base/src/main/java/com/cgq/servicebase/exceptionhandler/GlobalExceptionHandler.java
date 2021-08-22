package com.cgq.servicebase.exceptionhandler;

import com.cgq.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //指定出现什么异常执行此方法
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){

        e.printStackTrace();
        return R.error().message("执行全局异常，o(╥﹏╥)o");
    }

    //自定义异常
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R errorGuli(GuliException e){

        log.error(e.getMessage()); //写到D盘文件中
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}

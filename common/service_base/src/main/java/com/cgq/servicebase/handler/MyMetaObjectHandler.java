package com.cgq.servicebase.handler;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;


@Configuration
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //插入填充创建时间和修改时间
        this.setFieldValByName("gmtCreate",new Date(),metaObject);
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }
}

package com.cgq.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class ExcelTest {

    public static void main(String[] args) {

        //实现excel写操作
        //1.设置写入文件夹地址和excel文件名称
        String filename = "D:\\44\\write.xlsx";
//
//        //2.调用easyexcel方法实现写操作
//
//        EasyExcel.write(filename, Demo.class).sheet("学生列表").doWrite(getData());


        //读操作

        EasyExcel.read(filename,Demo.class,new ExcelListener()).sheet().doRead();

    }

    private static List<Demo> getData() {

        List<Demo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            Demo demo = new Demo();
            demo.setSno(i);
            demo.setSname("lucy" + i);

            list.add(demo);
        }
        return list;

    }
}

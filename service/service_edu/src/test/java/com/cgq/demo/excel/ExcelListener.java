package com.cgq.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class ExcelListener extends AnalysisEventListener<Demo> {

    //一行一行解析excel
    @Override
    public void invoke(Demo demo, AnalysisContext analysisContext) {
        System.out.println("**" + demo);
    }

    //读取表头
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {

        System.out.println("表头" + headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}

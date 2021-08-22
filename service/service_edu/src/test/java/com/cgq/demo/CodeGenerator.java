package com.cgq.demo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {

    public static void main(String[] args) {

        //代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");//当前项目路径（C:\Users\86173\IdeaProjects\GenratorMP）
        String pwd = "C:\\Users\\86173\\IdeaProjects\\guli_parent\\service\\service_edu";
        //设置生成路径
        gc.setOutputDir("C:\\Users\\86173\\IdeaProjects\\guli_parent\\service\\service_edu" + "/src/main/java");
        //生成作者
        gc.setAuthor("cgq");
        //生成代码是否要开所在文件夹
        gc.setOpen(false);
        //实体属性 Swagger2 注解
        gc.setSwagger2(true);
//        在mapper.xml生成基础ResultMap
        gc.setBaseResultMap(true);
        //文件生成覆盖
        gc.setFileOverride(false);
        gc.setIdType(IdType.ASSIGN_ID);
        gc.setDateType(DateType.ONLY_DATE);

        //根据表生成类名 %s=表名
        gc.setEntityName("%s");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");



        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/guli?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        //模块名
        pc.setModuleName("eduservice");
        //包名
        pc.setParent("com.cgq");
        mpg.setPackageInfo(pc);




        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

//        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                //设置xml生成位置
                return pwd + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        //设置mapper接口包不生成xml
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);



        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //表名生成策略：驼峰命名 ps_pro -- PsPro
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //列名生成策略：驼峰命名  last_name --- lastName
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);

        //controller层是否用RestController
        strategy.setRestControllerStyle(false);
        // 公共父类
//        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");

        //生成表名 ,多个逗号分隔
        strategy.setInclude("edu_comment");
        //按表前缀名生成
//        strategy.setLikeTable(new LikeTable("psm_"));
        //驼峰转连字符 false为 psm_product -->controller @RequestMapping("/psm/psmProduct");  true为/psm/psm_product
//        strategy.setControllerMappingHyphenStyle(true);
        //设置表替换前缀,生成类就不会带上表的前缀psm_
//        strategy.setTablePrefix("psm_");
        mpg.setStrategy(strategy);

        mpg.execute();
    }
}

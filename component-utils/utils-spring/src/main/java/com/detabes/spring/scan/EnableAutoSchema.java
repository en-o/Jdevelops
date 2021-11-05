package com.detabes.spring.scan;

import com.detabes.spring.properties.DataBaseProperties;
import com.detabes.spring.schema.LocalDataSourceLoader;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动是检查库存在与否，进行自动建库
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
//1.把需要注入spring容器的类加入进来
@Import({DataBaseProperties.class, LocalDataSourceLoader.class})
//2.设置扫描路径：最好是直接把本项目所有类的共有目录放进去
@ComponentScan("com.detabes.spring.**")
public  @interface EnableAutoSchema {

}
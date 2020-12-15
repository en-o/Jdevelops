package com.detabes.annotation.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author tn
 * @date 2020/4/18 22:15
 * @description cloud项目启动注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public @interface DatabstechCloudBoot {
    @AliasFor(annotation = SpringBootApplication.class, attribute = "scanBasePackages")
    String[] scanComponentPackages() default {};
//    String[] scanComponentPackages() default {"com.databstech.**"};
}

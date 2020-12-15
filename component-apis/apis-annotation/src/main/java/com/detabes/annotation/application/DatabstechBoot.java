package com.detabes.annotation.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


/**
 * @author tn
 * @date 2020/4/18 22:15
 * @description 单机启动注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication
public @interface DatabstechBoot {
    @AliasFor(annotation = SpringBootApplication.class, attribute = "scanBasePackages")
//    String[] scanComponentPackages() default {"com.databstech.**"};
    String[] scanComponentPackages() default {};
}

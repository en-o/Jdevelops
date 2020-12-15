package com.detabes.annotation.mapping;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 *  合并注解
 *  <pre>
 *      艾特RestController
 *      艾特RequestMapping
 *  </pre>
 * @author tn
 * @version 1
 * @ClassName PathRestController
 * @description 合并注解
 * @date 2020/6/16 12:16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
public @interface PathRestController {
    @AliasFor("path")
    String[] value() default {};

    @AliasFor("value")
    String[] path() default {};
}


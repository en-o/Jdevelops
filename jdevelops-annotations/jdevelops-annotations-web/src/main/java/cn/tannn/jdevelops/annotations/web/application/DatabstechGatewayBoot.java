package cn.tannn.jdevelops.annotations.web.application;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.util.AntPathMatcher;

import java.lang.annotation.*;

/**
 * gateway启动注解
 * @author tn
 * @date 2020/4/18 22:15
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication
@EnableDiscoveryClient
@Import(value = AntPathMatcher.class)
public @interface DatabstechGatewayBoot {
    @AliasFor(annotation = SpringBootApplication.class, attribute = "scanBasePackages")
    String[] scanComponentPackages() default {};
}

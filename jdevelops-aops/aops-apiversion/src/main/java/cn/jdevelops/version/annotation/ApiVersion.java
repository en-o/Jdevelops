package cn.jdevelops.version.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * 版本控制注解
 * @author tnnn
 * <a href="https://www.cnblogs.com/amuge/articles/13821162.html">参考</a>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {
    double value() default 1.0;
}

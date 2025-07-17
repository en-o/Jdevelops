package cn.tannn.jdevelops.jdectemplate.annotations;

import java.lang.annotation.*;

/**
 * 排序注解
 * 用于定义默认排序规则
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlOrderBy {
    /**
     * 排序字段和方向
     * 例如: "id DESC", "name ASC, create_time DESC"
     */
    String value();
}

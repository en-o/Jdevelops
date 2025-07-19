package cn.tannn.jdevelops.jdectemplate.annotations;

import cn.tannn.jdevelops.jdectemplate.enums.PageType;

import java.lang.annotation.*;

/**
 * 分页信息注解[用于标识分页相关字段]
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlPage {
    /**
     * 分页类型
     */
    PageType type();
}

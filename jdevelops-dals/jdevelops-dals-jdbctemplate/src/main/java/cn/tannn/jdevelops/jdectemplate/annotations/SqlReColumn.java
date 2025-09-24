package cn.tannn.jdevelops.jdectemplate.annotations;

import cn.tannn.jdevelops.jdectemplate.enums.QueryType;
import cn.tannn.jdevelops.jdectemplate.sql.NullHandleStrategy;

import java.lang.annotation.*;

/**
 * jdbc查询 ：字段映射注解[返回实体的映射]
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlReColumn {

    /**
     * 返回的字段别名,注意用下划线
     */
    String alias() default "";

}

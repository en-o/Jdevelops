package cn.jdevelops.data.ddss.annotation;

import java.lang.annotation.*;

/**
 * 多数据源注解-数据源的名字 (DbName的配合注解，实体方式用)
 * @author tan
 */
@Target({ ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DbNamed {
}

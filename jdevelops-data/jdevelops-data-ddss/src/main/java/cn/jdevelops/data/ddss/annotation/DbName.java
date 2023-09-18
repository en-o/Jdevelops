package cn.jdevelops.data.ddss.annotation;

import java.lang.annotation.*;
/**
 * 多数据源注解-数据源的名字 (在FIELD上是要配合 DbNamed才有效)
 * @author tan
 */
@Target({ ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DbName {
}

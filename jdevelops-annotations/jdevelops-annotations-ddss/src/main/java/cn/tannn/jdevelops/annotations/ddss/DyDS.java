package cn.tannn.jdevelops.annotations.ddss;

import java.lang.annotation.*;

/**
 * 多数据源注解 - 如果前端动态指定配合 @DbName  choose dataSource
 * @author tan
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DyDS {

    /**
     * 数据源名 <br/>
     * 当value 为空时 则选择@DbName的参数值
     * @see 配置文件.dynamic.datasource 下的第一个key  或者  数据库.dy_datasource.datasource_name
     * @return 数据源名
     */
    String value() default "";

}


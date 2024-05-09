package cn.tannn.jdevelops.annotations.es;


import cn.tannn.jdevelops.annotations.es.constant.EsDdlAuto;
import cn.tannn.jdevelops.annotations.es.constant.EsDynamic;

import java.lang.annotation.*;

/**
 * es 索引 (加上这个注解后默认就会将所有字段加入mappings，默认类型keyword)
 * @author tan
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EsIndex {

    /**
     * 索引名
     */
    String name();

    /**
     * create模式[默认不创建]
     */
    EsDdlAuto ddlAuto() default EsDdlAuto.NONE;

    /**
     * 映射模式
     */
    EsDynamic dynamic() default EsDynamic.strict;



}

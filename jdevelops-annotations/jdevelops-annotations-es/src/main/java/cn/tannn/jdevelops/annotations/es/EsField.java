package cn.tannn.jdevelops.annotations.es;


import cn.tannn.jdevelops.annotations.es.basic.EsFieldBasic;
import cn.tannn.jdevelops.annotations.es.basic.EsFieldMultiType;

import java.lang.annotation.*;

/**
 * es 索引字段
 * {@link https://www.elastic.co/guide/en/elasticsearch/reference/8.9/mapping-params.html}
 * @author tan
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EsField {

    /**
     * 还没使用，默认用法的实体字段名
     * 字段名是否驼峰[true:保持bean中的字段不变，false:下划线隔开]
     */
    boolean camel() default true;

    /**
     * 基础元数据信信息填写
     * @return
     */
    EsFieldBasic basic();

    /**
     * 字段多类型 {@link https://www.elastic.co/guide/en/elasticsearch/reference/8.9/multi-fields.html}
     */
    EsFieldMultiType[] fields() default  {};
}

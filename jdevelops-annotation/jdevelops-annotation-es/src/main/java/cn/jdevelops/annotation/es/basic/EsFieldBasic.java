package cn.jdevelops.annotation.es.basic;


import cn.jdevelops.annotation.es.constant.EsType;
import cn.jdevelops.annotation.es.constant.EsTypeDataFormat;

import java.lang.annotation.*;

/**
 * es 索引字段 基础参数
 * {@link https://www.elastic.co/guide/en/elasticsearch/reference/8.9/mapping-params.html}
 * @author tan
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EsFieldBasic {

    /**
     * 字段类型
     */
    EsType type();

    /**
     * type.data 时间字段格式[不写则不构建此属性] {@link  EsTypeDataFormat}
     */
    String[] format() default {};

    /**
     *
     * The index option controls whether field values are indexed. It accepts true or false and defaults to true.
     *
     * Indexing a field creates data structures that enable the field to be queried efficiently.
     * Numeric types, date types, the boolean type, ip type,
     * geo_point type and the keyword type can also be queried when they are not indexed but only have doc values enabled.
     * Queries on these fields are slow as a full scan of the index has to be made.
     * All other fields are not queryable.
     *
     * {@link  https://www.elastic.co/guide/en/elasticsearch/reference/8.9/mapping-index.html}
     * ps == true不会生成index属性
     */
    boolean index() default true;

    /**
     *  if set to true, allows the exception to be ignored
     *
     * https://www.elastic.co/guide/en/elasticsearch/reference/8.9/ignore-malformed.html
     * ps == false不会生成ignoreMalformed属性
     */
    boolean ignoreMalformed() default false;


    /**
     * 分词器 e.g english
     * ps: 只能在 text 类型字段里设置分词噢，程序没有做判断，请自行做判断
     */
    String analyzer() default "";

}

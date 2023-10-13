package cn.jdevelops.annotation.es.basic;

import java.lang.annotation.*;

/**
 * es 索引字段
 * {@link https://www.elastic.co/guide/en/elasticsearch/reference/7.16/multi-fields.html}
 * @author tan
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EsFieldMultiType {
    /**
     * 多类型的字段别名
     */
    String alias();

    /**
     * 字段类型
     */
    EsFieldBasic basic();
}

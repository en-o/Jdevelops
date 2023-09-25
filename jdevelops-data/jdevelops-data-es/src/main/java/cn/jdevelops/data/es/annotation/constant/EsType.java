package cn.jdevelops.data.es.annotation.constant;

/**
 * es类型
 * {@link https://www.elastic.co/guide/en/elasticsearch/reference/8.9/mapping-types.html}
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/2114:07
 */
public enum EsType {

    integer,
    /**
     * 不分词
     */
    keyword,
    /**
     * 嵌套类型，用在对象上
     */
    nested,
    /**
     * 会分词
     */
    text,
    /**
     * 默认 format STRICT_DATE_OPTIONAL_TIME+EPOCH_MILLIS
     */
    date,
    ip,
    LONG,
    BOOLEAN,
    ;

}

package cn.tannn.jdevelops.annotations.es.constant;

/**
 * 时间格式
 * {@link https://www.elastic.co/guide/en/elasticsearch/reference/8.9/date.html}
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/21 14:26
 */
public interface EsTypeDataFormat {
    String STRICT_DATE_OPTIONAL_TIME ="strict_date_optional_time";
    String EPOCH_MILLIS ="epoch_millis";
    String YYYYMMDDHHMMSS ="yyyy-MM-dd HH:mm:ss";
    String YYYYMMDD =" yyyy-MM-dd";
    String YYYYMM ="yyyy-MM";
    String YYYY ="yyyy";

}

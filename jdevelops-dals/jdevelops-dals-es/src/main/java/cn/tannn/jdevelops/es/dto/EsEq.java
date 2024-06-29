package cn.tannn.jdevelops.es.dto;

import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;

/**
 * 等于匹配条件
 * PS:
 * <p>
 *     name ='张三' OR name ='李四'
 * </p>
 * @author lxw
 * @version V1.0
 * @date 2020/6/2
 **/
public class EsEq implements Serializable {


    /**
     * 字段代码，字段类型是text，必须添加“.keyword”
     */
    private String field;

    /**
     * 字段值集合
     */
    private List<String> fieldValue;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<String> getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(List<String> fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EsEq.class.getSimpleName() + "[", "]")
                .add("field='" + field + "'")
                .add("fieldValue=" + fieldValue)
                .toString();
    }
}

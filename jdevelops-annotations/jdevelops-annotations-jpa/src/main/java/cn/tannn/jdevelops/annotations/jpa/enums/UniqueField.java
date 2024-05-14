package cn.tannn.jdevelops.annotations.jpa.enums;


/**
 * 字段名
 *
 * @author tn
 * @version 1
 * @date 2021/1/27 0:09
 */

public enum UniqueField {
    /**
     * id字段
     */
    ID("id", "唯一id", Integer.class),
    IDS("id", "唯一id", String.class),
    UUID("uuid", "唯一编码UUID", Integer.class),
    UUIDS("uuid", "唯一编码UUID", String.class),

    ;

    /**
     * 字段名
     */
    private final String fieldName;
    /**
     * 备注
     */
    private final String fieldNameRemark;
    /**
     * 类型
     */
    private final Class aClass;

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldNameRemark() {
        return fieldNameRemark;
    }

    public Class getaClass() {
        return aClass;
    }

    UniqueField(String fieldName, String fieldNameRemark, Class aClass) {
        this.fieldName = fieldName;
        this.fieldNameRemark = fieldNameRemark;
        this.aClass = aClass;
    }
}

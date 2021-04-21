package com.detabes.jpa.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段名
 *
 * @author tn
 * @version 1
 * @date 2021/1/27 0:09
 */
@Getter
@AllArgsConstructor
public enum FieldName {
    /**
     * id字段
     */
    ID("id", "唯一id", Integer.class),
    UUID("uuid", "唯一编码UUID", String.class),

    ;

    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 备注
     */
    private String fieldNameRemark;
    /**
     * 类型
     */
    private Class aClass;
}

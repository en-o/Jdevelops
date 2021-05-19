package com.detabes.search.es.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 指定条件
 *
 * @author lxw
 * @version V1.0
 * @date 2020/6/2
 **/
@Getter
@Setter
@ToString
public class SpecialDTO implements Serializable {


    /**
     * 字段代码
     */
    private String field;

    /**
     * 字段值
     */
    private String fieldValue;
}

package com.detabes.mybatis.server.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lmz
 * @projectName bpm
 * @packageName com.cr.code.bpm.vo
 * @company Peter
 * @date 2020/8/26  14:14
 * @description
 */
@Data
public class QueryVo implements Serializable {
    /**
     * 查询字段名称
     */
    private String key;
    /**
     * 字段值
     */
    private String value;
    /**
     * and or
     */
    private String condition;
    /**
     * 连接类型，如like,equals,gt,ge,lt,le
     */
    private String type;
}

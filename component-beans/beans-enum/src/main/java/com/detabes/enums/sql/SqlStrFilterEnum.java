package com.detabes.enums.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tn
 * @version 1
 * @ClassName StringEnum
 * @description 字符串枚举
 * @date 2020/12/14 16:34
 */
@Getter
@AllArgsConstructor
public enum SqlStrFilterEnum {
    /** 删除 */
    DELETE("DELETE","删除"),
    /** ASCII码 */
    ASCII("ASCII","ASCII码"),
    /** 更新 */
    UPDATE("UPDATE","更新"),
    /** 查询 */
    SELECT("SELECT","查询"),
    /** 截取 */
    SUBSTR("SUBSTR(","截取"),
    /** 统计 */
    COUNT("COUNT(","统计"),
    /** 或 */
    OR(" OR ","或"),
    /** 并且 */
    AND(" AND ","并且"),
    /** 删除表 */
    DROP("DROP","删除表"),
    /** 执行Sql */
    EXECUTE("EXECUTE","执行Sql"),
    /** 存储过程 */
    EXEC("EXEC","存储过程"),
    /** 删除表 */
    TRUNCATE("TRUNCATE","清空表"),
    /** INTO */
    INTO("INTO","INTO"),
    /** 批处理 */
    DECLARE("DECLARE","批处理"),
    /** 备份还原 */
    MASTER("MASTER","备份还原"),
    /** 单引号 */
    RSQUO("'","单引号"),
    ;

    private String str;
    private String remark;

}

package cn.tannn.jdevelops.annotations.jdbctemplate.proxysql;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当前功能的主注解，用于配合 查询 删除 更新 新增 注解
 * <p> 作用在字段上
 * <p> 标记位,给标记字段添加代理
 * @author tnnn
 * @date 2022-08-01 11:50:342
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface JdbcTemplate {
}

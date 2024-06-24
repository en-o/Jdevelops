package cn.tannn.jdevelops.annotations.jdbctemplate;


import java.lang.annotation.*;

/**
 * 当前功能的主注解，用于配合 查询 删除 更新 新增 注解
 * <p> 作用在查询类或者接口上
 * <p> 标记位
 * @author tnnn
 * @date 2022-08-01 11:50:342
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface JdbcTemplate {
}

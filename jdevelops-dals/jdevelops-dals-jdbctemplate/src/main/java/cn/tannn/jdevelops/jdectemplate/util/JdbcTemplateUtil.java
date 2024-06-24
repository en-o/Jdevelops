package cn.tannn.jdevelops.jdectemplate.util;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * jdbc
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/24 下午1:44
 */
public class JdbcTemplateUtil {

    /**
     * 返回值为 String, Integer 基本类型时使用的方法
     * @param jdbcTemplate jdbcTemplate
     * @param resultRawType     返回的类型
     * @param resolver sql
     * @param resultActualType    返回的具体类型
     * @return Object 数据
     */
    public static Object getJdbcTemplateSql(JdbcTemplate jdbcTemplate,
                                                           String resultRawType,
                                                           Object resolver,
                                                           String resultActualType ) throws ClassNotFoundException {
        if (resultRawType.equals(String.class.getName())
            || resultRawType.equals(Integer.class.getName())) {
            return getJdbcTemplateSqlContextBaseType(jdbcTemplate,
                    resultRawType, resolver, resultActualType);
        }
        return getJdbcTemplateSqlContextMapType(jdbcTemplate,
                resultRawType, resolver, resultActualType);
    }


    /**
     * 返回值为 String, Integer 基本类型时使用的方法
     * @param jdbcTemplate jdbcTemplate
     * @param resultRawType     返回的类型
     * @param resolver sql
     * @param resultActualType    返回的具体类型
     * @return Object 数据
     */
    public static Object getJdbcTemplateSqlContextBaseType(JdbcTemplate jdbcTemplate,
                                                           String resultRawType,
                                                           Object resolver,
                                                           String resultActualType ) throws ClassNotFoundException {
        Class<?> rvt = Class.forName(resultRawType);
        Class<?> query = Class.forName(resultActualType);
        if (rvt.getName().equals(List.class.getName())) {
            return jdbcTemplate.queryForList(resolver.toString(),
                    query);
        } else {
            return jdbcTemplate.queryForObject(resolver.toString(),
                    query);
        }
    }


    /**
     * 返回值为Bean, Map 型时使用的方法
     *
     * @param jdbcTemplate jdbcTemplate
     * @param resultRawType     返回的类型
     * @param resolver sql
     * @param resultActualType    返回的具体类型
     * @return Object 数据
     */
    public static Object getJdbcTemplateSqlContextMapType(JdbcTemplate jdbcTemplate
            ,  String resultRawType
            , Object resolver
            , String resultActualType ) throws ClassNotFoundException {
        Class<?> rvt = Class.forName(resultRawType);
        Class<?> query = Class.forName(resultActualType);
        BeanPropertyRowMapper beanPropertyRowMapper = new BeanPropertyRowMapper<>(query);
        if (rvt.getName().equals(List.class.getName())) {
            return jdbcTemplate.query(resolver.toString(),
                    beanPropertyRowMapper);

        } else {
            return jdbcTemplate.queryForObject(resolver.toString(),
                    beanPropertyRowMapper);
        }
    }
}

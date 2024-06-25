package cn.tannn.jdevelops.jdectemplate.util;

import ch.qos.logback.classic.db.names.TableName;
import cn.tannn.jdevelops.result.request.Paging;
import cn.tannn.jdevelops.result.response.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.List;

/**
 * jdbc
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/24 下午1:44
 */
public class JdbcTemplateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcTemplateUtil.class);

    /**
     * 返回值为 String, Integer 基本类型时使用的方法
     *
     * @param jdbcTemplate     jdbcTemplate
     * @param resultRawType    返回的类型 PS: List<String> 中的 List
     * @param resolver         sql
     * @param resultActualType 返回的具体类型 PS: List<String> 中的 String
     * @param args             参数
     * @return Object 数据
     */
    public static Object getJdbcTemplateSql(JdbcTemplate jdbcTemplate,
                                            String resultRawType,
                                            Object resolver,
                                            String resultActualType,
                                            Object[] args) throws ClassNotFoundException {
        String sql = resolver.toString();
        LOG.debug("jdbctemplate ========> sql {}", sql);
        if (resultRawType.equals(String.class.getName())
            || resultRawType.equals(Integer.class.getName())) {
            return getJdbcTemplateSqlContextBaseType(jdbcTemplate,
                    resultRawType, sql, resultActualType);
        }
        return getJdbcTemplateSqlContextMapType(jdbcTemplate,
                resultRawType, sql, resultActualType, args);
    }


    /**
     * 返回值为 String, Integer 基本类型时使用的方法
     *
     * @param jdbcTemplate     jdbcTemplate
     * @param resultRawType    返回的类型
     * @param sql              sql
     * @param resultActualType 返回的具体类型
     * @return Object 数据
     */
    public static Object getJdbcTemplateSqlContextBaseType(JdbcTemplate jdbcTemplate,
                                                           String resultRawType,
                                                           String sql,
                                                           String resultActualType) throws ClassNotFoundException {
        Class<?> rvt = Class.forName(resultRawType);
        Class<?> query = Class.forName(resultActualType);
        if (rvt.isAssignableFrom(List.class)) {
            return jdbcTemplate.queryForList(sql, query);
        } else {
            return jdbcTemplate.queryForObject(sql, query);
        }
    }


    /**
     * 返回值为Bean, Map 型时使用的方法
     *
     * @param jdbcTemplate     jdbcTemplate
     * @param resultRawType    返回的类型
     * @param sql              sql
     * @param resultActualType 返回的具体类型
     * @param args             参数
     * @return Object 数据
     */
    public static Object getJdbcTemplateSqlContextMapType(JdbcTemplate jdbcTemplate
            , String resultRawType
            , String sql
            , String resultActualType
            , Object[] args) throws ClassNotFoundException {
        Class<?> rvt = Class.forName(resultRawType);
        if (rvt.isAssignableFrom(List.class)) {
            return jdbcTemplate.query(sql, rowMapper(resultActualType));
        } else if (rvt.isAssignableFrom(PageResult.class)) {
            Paging paging = new Paging();
            for (Object arg : args) {
                if (arg instanceof Paging) {
                    paging = (Paging) arg;
                }
            }
            StringBuilder sqlb = new StringBuilder(sql);
            sqlb.append(" LIMIT ");
            sqlb.append(paging.getPageSize());
            sqlb.append(" OFFSET ");
            sqlb.append(paging.getPageIndex() * paging.getPageSize());

            List<?> query = jdbcTemplate.query(sqlb.toString(), rowMapper(resultActualType));
            return PageResult.page(paging.realPageIndex()
                    , paging.getPageSize()
                    , amount(jdbcTemplate, JdbcUtils.tableName(sql))
                    , query);
        } else {
            return jdbcTemplate.queryForObject(sql, rowMapper(resultActualType));
        }
    }

    private static RowMapper<?> rowMapper(String resultActualType) throws ClassNotFoundException {
        if (resultActualType.equals(Integer.class.getName())) {
            return new SingleColumnRowMapper<>(Integer.class);
        } else {
            return new BeanPropertyRowMapper<>(Class.forName(resultActualType));
        }
    }


    /**
     * 总数
     *
     * @return 总数
     */
    private static Long amount(JdbcTemplate jdbcTemplate, String tableName) {
        try {
            // 获取总记录数
            String countSql = "SELECT COUNT(*) FROM " + tableName;
            return jdbcTemplate.queryForObject(countSql, long.class);
        } catch (Exception e) {
            LOG.error("Error executing count {} ", tableName, e);
        }
        return 0L;
    }


}

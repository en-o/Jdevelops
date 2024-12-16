package cn.tannn.jdevelops.jdectemplate.util;

import cn.tannn.jdevelops.result.request.Paging;
import cn.tannn.jdevelops.result.response.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * jdbc
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/24 下午1:44
 */
public class InteriorJdbcTemplateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(InteriorJdbcTemplateUtil.class);

    /**
     * 返回值为 String, Integer 基本类型时使用的方法
     *
     * @param jdbcTemplate     jdbcTemplate
     * @param resultRawType    返回的类型 PS: List<String> 中的 List [通过这个判断是返回list还是sting还是map还是PageResult]
     * @param resolverSql      他aop获取到的完整sql
     * @param resultActualType 返回的具体类型 PS: List<String> 中的 String
     * @param args             所有查询参数,[除了分页其他的参数已经被解析到sql里了]
     * @return Object 数据
     */
    public static Object getJdbcTemplateSql(JdbcTemplate jdbcTemplate,
                                            String resultRawType,
                                            Object resolverSql,
                                            String resultActualType,
                                            Object[] args) throws ClassNotFoundException {
        String sql = resolverSql.toString();
        LOG.debug("jdbctemplate ========> sql {}", sql);
        if (JdbcUtils.isBasicType(resultRawType)) {
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
            try {
                return jdbcTemplate.queryForObject(sql, query);
            } catch (EmptyResultDataAccessException e) {
                LOG.warn("ResultData data empty", e);
                return null;
            }
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
            return jdbcTemplate.query(sql, JdbcUtils.rowMapper(resultActualType));
        } else if (rvt.isAssignableFrom(PageResult.class)) {
            Paging paging = new Paging();
            for (Object arg : args) {
                if (arg instanceof Paging) {
                    paging = (Paging) arg;
                }
            }
            return paging(jdbcTemplate, sql, resultActualType, paging);
        } else {
            try {
                return jdbcTemplate.queryForObject(sql, JdbcUtils.rowMapper(resultActualType));
            } catch (EmptyResultDataAccessException e) {
                LOG.warn("ResultData data empty", e);
                return null;
            }
        }
    }

    /**
     * 分页
     *
     * @param jdbcTemplate     jdbcTemplate
     * @param sql              sql [处理好占位符的完整sql]
     * @param resultActualType 返回的具体类型
     * @param paging           分页参数
     * @return PageResult
     */
    private static PageResult<?> paging(JdbcTemplate jdbcTemplate
            , String sql
            , String resultActualType
            , Paging paging) throws ClassNotFoundException {
        StringBuilder sqlb = new StringBuilder(sql);
        sqlb.append(" LIMIT ");
        sqlb.append(paging.getPageSize());
        sqlb.append(" OFFSET ");
        sqlb.append(paging.getPageIndex() * paging.getPageSize());
        List<?> query;
        if (JdbcUtils.isBasicType(resultActualType)) {
            query = jdbcTemplate.queryForList(sqlb.toString(), Class.forName(resultActualType));
        } else {
            query = jdbcTemplate.query(sqlb.toString(), JdbcUtils.rowMapper(resultActualType));
        }

        return PageResult.page(paging.realPageIndex()
                , paging.getPageSize()
                , amountSql(jdbcTemplate, sql)
                , query);
    }


    /**
     * 分页
     *
     * @param jdbcTemplate     jdbcTemplate
     * @param sql              sql[存在占位符的sql]
     * @param resultActualType 返回的具体类型
     * @param paging           分页参数
     * @param args             sql里占位符的参数 (不含有分页参数)
     * @return PageResult
     */
    public static <T> PageResult<T>  paging(JdbcTemplate jdbcTemplate
            , String sql
            , Class<T> resultActualType
            , Paging paging
            , Object... args) {
        StringBuilder sqlb = new StringBuilder(sql);
        sqlb.append(" LIMIT ");
        sqlb.append(paging.getPageSize());
        sqlb.append(" OFFSET ");
        sqlb.append(paging.getPageIndex() * paging.getPageSize());
        List<T> query;
        if (args == null) {
            if (JdbcUtils.isBasicType(resultActualType)) {
                query = jdbcTemplate.queryForList(sqlb.toString(), resultActualType);
            } else {
                query = jdbcTemplate.query(sqlb.toString(), JdbcUtils.rowMapper2(resultActualType));
            }
        } else {
            if (JdbcUtils.isBasicType(resultActualType)) {
                query = jdbcTemplate.queryForList(sqlb.toString(), resultActualType, args);
            } else {
                query = jdbcTemplate.query(sqlb.toString(), JdbcUtils.rowMapper2(resultActualType), args);
            }
        }
        return PageResult.page(paging.realPageIndex()
                , paging.getPageSize()
                , amountSql(jdbcTemplate, sql, args)
                , query);
    }


    /**
     * 总数
     *
     * @param sql 查询的sql (这个sql是没有占位符的完整sql)
     * @return 总数
     */
    public static Long amountSql(JdbcTemplate jdbcTemplate, String sql) {
        try {
            String countSql = "SELECT COUNT(*) " + JdbcUtils.extractFromClause(sql);
            return jdbcTemplate.queryForObject(countSql, long.class);
        } catch (Exception e) {
            LOG.error("Error executing count {} ", sql, e);
        }
        return 0L;
    }


    /**
     * 总数
     *
     * @param sql  查询的sql (这个sql是有占位符的完整sql)
     * @param args 参数
     * @return 总数
     */
    public static Long amountSql(JdbcTemplate jdbcTemplate, String sql, Object... args) {
        try {
            String countSql = "SELECT COUNT(*) " + JdbcUtils.extractFromClause(sql);
            // 获取总记录数
            return jdbcTemplate.queryForObject(countSql, long.class, args);
        } catch (Exception e) {
            LOG.error("Error executing count {} ", sql, e);
        }
        return 0L;
    }

    /**
     * 总数
     *
     * @return 总数
     */
    @Deprecated
    public static Long amount(JdbcTemplate jdbcTemplate, String tableName) {
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

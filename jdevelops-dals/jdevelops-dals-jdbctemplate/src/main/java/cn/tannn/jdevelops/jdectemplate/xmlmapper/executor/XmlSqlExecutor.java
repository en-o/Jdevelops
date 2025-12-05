package cn.tannn.jdevelops.jdectemplate.xmlmapper.executor;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlCommandType;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlContext;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlNode;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * XML SQL 执行器
 * 负责执行从 XML 解析的 SQL 语句
 *
 * @author tnnn
 */
public class XmlSqlExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(XmlSqlExecutor.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public XmlSqlExecutor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    /**
     * 执行 SQL 语句
     *
     * @param statement SQL 语句配置
     * @param parameter 参数对象
     * @param resultType 结果类型
     * @param <T> 结果类型泛型
     * @return 执行结果
     */
    public <T> Object execute(SqlStatement statement, Object parameter, Class<T> resultType) {
        // 构建 SQL
        SqlContext context = buildSql(statement, parameter);
        String sql = context.getSql();

        if (!StringUtils.hasText(sql)) {
            throw new IllegalArgumentException("Generated SQL is empty");
        }

        LOG.debug("Executing SQL: {}", sql);
        LOG.debug("Parameters: {}", context.isUseNamedParameters() ?
                context.getNamedParameters() : context.getParameters());

        try {
            // 根据命令类型执行不同的操作
            return switch (statement.getCommandType()) {
                case SELECT -> executeSelect(context, resultType, statement.isTryc());
                case INSERT -> executeUpdate(context);
                case UPDATE -> executeUpdate(context);
                case DELETE -> executeUpdate(context);
            };
        } catch (Exception e) {
            if (statement.isTryc()) {
                LOG.warn("SQL execution failed (tryc=true): {}", e.getMessage());
                return getDefaultValue(statement.getCommandType(), resultType);
            }
            throw e;
        }
    }

    /**
     * 构建 SQL
     */
    private SqlContext buildSql(SqlStatement statement, Object parameter) {
        SqlContext context = new SqlContext();
        context.setUseNamedParameters(true); // 默认使用命名参数

        // 应用所有 SQL 节点
        for (SqlNode sqlNode : statement.getSqlNodes()) {
            sqlNode.apply(context, parameter);
        }

        return context;
    }

    /**
     * 执行查询
     */
    @SuppressWarnings("unchecked")
    private <T> Object executeSelect(SqlContext context, Class<T> resultType, boolean tryc) {
        String sql = context.getSql();

        try {
            if (context.isUseNamedParameters()) {
                // 使用命名参数
                MapSqlParameterSource params = new MapSqlParameterSource();
                params.addValues(context.getNamedParameters());

                if (resultType == null || Map.class.isAssignableFrom(resultType)) {
                    // 返回 Map
                    List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(sql, params);
                    return isSingleResult(sql) ? (results.isEmpty() ? null : results.get(0)) : results;
                } else if (isSimpleType(resultType)) {
                    // 返回简单类型
                    List<T> results = namedParameterJdbcTemplate.queryForList(sql, params, resultType);
                    return isSingleResult(sql) ? (results.isEmpty() ? null : results.get(0)) : results;
                } else {
                    // 返回实体对象
                    RowMapper<T> rowMapper = new DataClassRowMapper<>(resultType);
                    List<T> results = namedParameterJdbcTemplate.query(sql, params, rowMapper);
                    return isSingleResult(sql) ? (results.isEmpty() ? null : results.get(0)) : results;
                }
            } else {
                // 使用位置参数
                Object[] params = context.getParameters().toArray();

                if (resultType == null || Map.class.isAssignableFrom(resultType)) {
                    List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, params);
                    return isSingleResult(sql) ? (results.isEmpty() ? null : results.get(0)) : results;
                } else if (isSimpleType(resultType)) {
                    List<T> results = jdbcTemplate.queryForList(sql, resultType, params);
                    return isSingleResult(sql) ? (results.isEmpty() ? null : results.get(0)) : results;
                } else {
                    // 返回实体对象
                    RowMapper<T> rowMapper = new DataClassRowMapper<>(resultType);
                    List<T> results = jdbcTemplate.query(sql, rowMapper, params);
                    return isSingleResult(sql) ? (results.isEmpty() ? null : results.get(0)) : results;
                }
            }
        } catch (EmptyResultDataAccessException e) {
            if (tryc) {
                return null;
            }
            throw e;
        }
    }

    /**
     * 执行更新操作（INSERT、UPDATE、DELETE）
     */
    private int executeUpdate(SqlContext context) {
        String sql = context.getSql();

        if (context.isUseNamedParameters()) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValues(context.getNamedParameters());
            return namedParameterJdbcTemplate.update(sql, params);
        } else {
            Object[] params = context.getParameters().toArray();
            return jdbcTemplate.update(sql, params);
        }
    }

    /**
     * 判断是否是单结果查询
     */
    private boolean isSingleResult(String sql) {
        String upperSql = sql.toUpperCase().trim();

        // 检查聚合函数查询（COUNT、SUM、AVG、MAX、MIN）
        if (upperSql.matches("^SELECT\\s+(COUNT|SUM|AVG|MAX|MIN)\\s*\\(.*")) {
            return true;
        }

        // 检查 LIMIT 1 或 TOP 1
        return upperSql.contains("LIMIT 1") || upperSql.contains("TOP 1");
    }

    /**
     * 判断是否是简单类型
     */
    private boolean isSimpleType(Class<?> type) {
        return type.isPrimitive() ||
                type == String.class ||
                type == Integer.class ||
                type == Long.class ||
                type == Double.class ||
                type == Float.class ||
                type == Boolean.class ||
                type == Byte.class ||
                type == Short.class ||
                type == Character.class ||
                Number.class.isAssignableFrom(type);
    }

    /**
     * 获取默认值（用于 tryc=true 时的异常处理）
     */
    private Object getDefaultValue(SqlCommandType commandType, Class<?> resultType) {
        if (commandType == SqlCommandType.SELECT) {
            return null; // 查询失败返回 null
        }
        return 0; // 更新操作失败返回 0
    }

    /**
     * 批量执行
     *
     * @param statement SQL 语句配置
     * @param parameters 参数对象列表
     * @return 影响行数数组
     */
    public int[] executeBatch(SqlStatement statement, List<?> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return new int[0];
        }

        // 使用第一个参数构建 SQL 模板
        SqlContext context = buildSql(statement, parameters.get(0));
        String sql = context.getSql();

        LOG.debug("Executing batch SQL: {}", sql);

        // 准备批量参数
        MapSqlParameterSource[] batchParams = new MapSqlParameterSource[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            SqlContext tempContext = buildSql(statement, parameters.get(i));
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValues(tempContext.getNamedParameters());
            batchParams[i] = params;
        }

        return namedParameterJdbcTemplate.batchUpdate(sql, batchParams);
    }
}

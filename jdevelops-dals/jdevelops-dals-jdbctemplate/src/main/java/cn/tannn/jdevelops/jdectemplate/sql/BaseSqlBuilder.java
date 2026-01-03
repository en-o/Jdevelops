package cn.tannn.jdevelops.jdectemplate.sql;

import cn.tannn.jdevelops.annotations.jdbctemplate.sql.enums.ParameterMode;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SQL构建器基础类
 */
public abstract class BaseSqlBuilder {
    public final StringBuilder sql;
    protected List<Object> positionalParams;
    protected MapSqlParameterSource namedParams;
    protected ParameterMode mode;
    protected boolean hasWhere = false;
    protected int paramCounter = 0;

    /**
     * 构造函数
     * @param baseSql 基础SQL语句
     * @param mode 参数模式
     */
    protected BaseSqlBuilder(String baseSql, ParameterMode mode) {
        if (!StringUtils.hasText(baseSql)) {
            throw new IllegalArgumentException("Base SQL cannot be null or empty");
        }
        if (mode == null) {
            throw new IllegalArgumentException("Parameter mode cannot be null");
        }

        this.sql = new StringBuilder(baseSql);
        this.mode = mode;

        if (mode == ParameterMode.POSITIONAL) {
            this.positionalParams = new ArrayList<>();
            this.namedParams = null;
        } else {
            this.positionalParams = null;
            this.namedParams = new MapSqlParameterSource();
        }
    }

    // 基础工具方法
    protected boolean isValidValue(Object value) {
        return value != null && (!(value instanceof String) || StringUtils.hasText((String) value));
    }

    protected boolean isValidColumn(String column) {
        return StringUtils.hasText(column);
    }

    protected boolean isValidOperator(String operator) {
        return StringUtils.hasText(operator);
    }

    protected boolean isValidParamName(String paramName) {
        return StringUtils.hasText(paramName);
    }

    protected String generateParamName(String base) {
        String cleanBase = base.replaceAll("[^a-zA-Z0-9_]", "");
        return cleanBase + (++paramCounter);
    }

    // Getter方法
    public String getSql() {
        return sql.toString();
    }

    public ParameterMode getMode() {
        return mode;
    }

    public Object[] getPositionalParams() {
        return mode == ParameterMode.POSITIONAL && positionalParams != null ?
                positionalParams.toArray() : new Object[0];
    }

    public List<Object> getPositionalParamsList() {
        return mode == ParameterMode.POSITIONAL ? positionalParams : new ArrayList<>();
    }

    public MapSqlParameterSource getNamedParams() {
        return mode == ParameterMode.NAMED ? namedParams : new MapSqlParameterSource();
    }

    /**
     * 合并另一个构建器的参数到当前构建器
     * @param sourceBuilder 源构建器
     */
    protected void mergeParameters(BaseSqlBuilder sourceBuilder) {
        if (sourceBuilder == null) {
            return;
        }

        // 确保模式匹配
        if (mode == ParameterMode.POSITIONAL && sourceBuilder.mode == ParameterMode.POSITIONAL) {
            // 合并位置参数
            List<Object> sourceParams = sourceBuilder.getPositionalParamsList();
            if (sourceParams != null && !sourceParams.isEmpty()) {
                if (this.positionalParams == null) {
                    this.positionalParams = new ArrayList<>();
                }
                this.positionalParams.addAll(sourceParams);
            }
        } else if (mode == ParameterMode.NAMED && sourceBuilder.mode == ParameterMode.NAMED) {
            // 合并命名参数
            MapSqlParameterSource sourceParams = sourceBuilder.getNamedParams();
            if (sourceParams != null) {
                if (this.namedParams == null) {
                    this.namedParams = new MapSqlParameterSource();
                }
                for (String paramName : sourceParams.getParameterNames()) {
                    this.namedParams.addValue(paramName, sourceParams.getValue(paramName));
                }
            }
        } else {
            throw new IllegalArgumentException("Cannot merge parameters from builders with different parameter modes");
        }
    }



    public boolean hasConditions() {
        return hasWhere;
    }

    public int getParameterCount() {
        if (mode == ParameterMode.POSITIONAL) {
            return positionalParams != null ? positionalParams.size() : 0;
        } else {
            return namedParams != null ? namedParams.getParameterNames().length : 0;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "sql='" + sql + '\'' +
                ", mode=" + mode +
                ", parameterCount=" + getParameterCount() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseSqlBuilder that = (BaseSqlBuilder) o;
        return Objects.equals(sql.toString(), that.sql.toString()) &&
                mode == that.mode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sql.toString(), mode);
    }
}

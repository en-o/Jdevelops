package cn.tannn.jdevelops.jdectemplate.xmlmapper.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQL 构建上下文
 * 用于在 SQL 节点处理过程中传递状态
 *
 * @author tnnn
 */
public class SqlContext {

    /**
     * SQL 语句构建器
     */
    private final StringBuilder sqlBuilder;

    /**
     * 参数列表（位置参数）
     */
    private final List<Object> parameters;

    /**
     * 命名参数映射
     */
    private final Map<String, Object> namedParameters;

    /**
     * 绑定的变量
     */
    private final Map<String, Object> bindings;

    /**
     * 参数计数器
     */
    private int parameterIndex = 0;

    /**
     * 是否使用命名参数
     */
    private boolean useNamedParameters;

    public SqlContext() {
        this.sqlBuilder = new StringBuilder();
        this.parameters = new ArrayList<>();
        this.namedParameters = new HashMap<>();
        this.bindings = new HashMap<>();
        this.useNamedParameters = true; // 默认使用命名参数
    }

    /**
     * 追加 SQL 片段
     */
    public void appendSql(String sql) {
        sqlBuilder.append(sql);
    }

    /**
     * 添加参数
     */
    public void addParameter(Object value) {
        parameters.add(value);
    }

    /**
     * 添加命名参数
     */
    public void addNamedParameter(String name, Object value) {
        namedParameters.put(name, value);
    }

    /**
     * 绑定变量
     */
    public void bind(String name, Object value) {
        bindings.put(name, value);
    }

    /**
     * 获取绑定的变量
     */
    public Object getBinding(String name) {
        return bindings.get(name);
    }

    /**
     * 获取构建的 SQL
     */
    public String getSql() {
        return sqlBuilder.toString().trim();
    }

    /**
     * 获取参数列表
     */
    public List<Object> getParameters() {
        return parameters;
    }

    /**
     * 获取命名参数
     */
    public Map<String, Object> getNamedParameters() {
        return namedParameters;
    }

    /**
     * 获取下一个参数索引
     */
    public int nextParameterIndex() {
        return parameterIndex++;
    }

    public boolean isUseNamedParameters() {
        return useNamedParameters;
    }

    public void setUseNamedParameters(boolean useNamedParameters) {
        this.useNamedParameters = useNamedParameters;
    }

    /**
     * 清空上下文
     */
    public void clear() {
        sqlBuilder.setLength(0);
        parameters.clear();
        namedParameters.clear();
        bindings.clear();
        parameterIndex = 0;
    }

    @Override
    public String toString() {
        return "SqlContext{" +
                "sql='" + getSql() + '\'' +
                ", parameters=" + parameters +
                ", namedParameters=" + namedParameters +
                '}';
    }
}

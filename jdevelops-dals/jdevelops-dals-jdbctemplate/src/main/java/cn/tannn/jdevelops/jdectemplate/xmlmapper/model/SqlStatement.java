package cn.tannn.jdevelops.jdectemplate.xmlmapper.model;

import java.util.List;

/**
 * SQL 语句模型
 * 对应 XML 中的 select、insert、update、delete 标签
 *
 * @author tnnn
 */
public class SqlStatement {

    /**
     * SQL 语句的唯一标识（对应方法名）
     */
    private String id;

    /**
     * SQL 类型
     */
    private SqlCommandType commandType;

    /**
     * 参数类型
     */
    private String parameterType;

    /**
     * 返回结果类型
     */
    private String resultType;

    /**
     * SQL 节点列表（包括静态 SQL 和动态标签）
     */
    private List<SqlNode> sqlNodes;

    /**
     * 是否吞掉异常
     */
    private boolean tryc;

    /**
     * 超时时间（秒）
     */
    private Integer timeout;

    public SqlStatement() {
    }

    public SqlStatement(String id, SqlCommandType commandType, String resultType, List<SqlNode> sqlNodes) {
        this.id = id;
        this.commandType = commandType;
        this.resultType = resultType;
        this.sqlNodes = sqlNodes;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SqlCommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(SqlCommandType commandType) {
        this.commandType = commandType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public List<SqlNode> getSqlNodes() {
        return sqlNodes;
    }

    public void setSqlNodes(List<SqlNode> sqlNodes) {
        this.sqlNodes = sqlNodes;
    }

    public boolean isTryc() {
        return tryc;
    }

    public void setTryc(boolean tryc) {
        this.tryc = tryc;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "SqlStatement{" +
                "id='" + id + '\'' +
                ", commandType=" + commandType +
                ", resultType='" + resultType + '\'' +
                ", sqlNodes=" + sqlNodes +
                '}';
    }
}

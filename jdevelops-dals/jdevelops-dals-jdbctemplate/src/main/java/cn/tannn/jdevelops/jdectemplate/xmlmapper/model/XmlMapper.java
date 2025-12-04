package cn.tannn.jdevelops.jdectemplate.xmlmapper.model;

import java.util.HashMap;
import java.util.Map;

/**
 * XML Mapper 模型
 * 对应一个完整的 XML Mapper 文件
 *
 * @author tnnn
 */
public class XmlMapper {

    /**
     * 命名空间（对应接口全限定名）
     */
    private String namespace;

    /**
     * SQL 语句映射
     */
    private Map<String, SqlStatement> sqlStatements;

    /**
     * SQL 片段映射（用于 include）
     */
    private Map<String, SqlFragment> sqlFragments;

    /**
     * XML 文件路径
     */
    private String xmlPath;

    public XmlMapper() {
        this.sqlStatements = new HashMap<>();
        this.sqlFragments = new HashMap<>();
    }

    public XmlMapper(String namespace) {
        this();
        this.namespace = namespace;
    }

    /**
     * 添加 SQL 语句
     */
    public void addSqlStatement(SqlStatement statement) {
        sqlStatements.put(statement.getId(), statement);
    }

    /**
     * 获取 SQL 语句
     */
    public SqlStatement getSqlStatement(String id) {
        return sqlStatements.get(id);
    }

    /**
     * 添加 SQL 片段
     */
    public void addSqlFragment(String id, SqlFragment fragment) {
        sqlFragments.put(id, fragment);
    }

    /**
     * 获取 SQL 片段
     */
    public SqlFragment getSqlFragment(String id) {
        return sqlFragments.get(id);
    }

    // Getters and Setters
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Map<String, SqlStatement> getSqlStatements() {
        return sqlStatements;
    }

    public void setSqlStatements(Map<String, SqlStatement> sqlStatements) {
        this.sqlStatements = sqlStatements;
    }

    public Map<String, SqlFragment> getSqlFragments() {
        return sqlFragments;
    }

    public void setSqlFragments(Map<String, SqlFragment> sqlFragments) {
        this.sqlFragments = sqlFragments;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    @Override
    public String toString() {
        return "XmlMapper{" +
                "namespace='" + namespace + '\'' +
                ", sqlStatements=" + sqlStatements.keySet() +
                ", sqlFragments=" + sqlFragments.keySet() +
                ", xmlPath='" + xmlPath + '\'' +
                '}';
    }
}

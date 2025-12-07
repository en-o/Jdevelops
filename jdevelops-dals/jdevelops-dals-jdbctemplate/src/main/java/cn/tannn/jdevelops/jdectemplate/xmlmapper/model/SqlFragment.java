package cn.tannn.jdevelops.jdectemplate.xmlmapper.model;

import java.util.List;

/**
 * SQL 片段
 * 对应 XML 中的 &lt;sql&gt; 标签，可以被 &lt;include&gt; 引用
 *
 * @author tnnn
 */
public class SqlFragment {

    /**
     * 片段 ID
     */
    private String id;

    /**
     * SQL 节点列表
     */
    private List<SqlNode> sqlNodes;

    public SqlFragment() {
    }

    public SqlFragment(String id, List<SqlNode> sqlNodes) {
        this.id = id;
        this.sqlNodes = sqlNodes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SqlNode> getSqlNodes() {
        return sqlNodes;
    }

    public void setSqlNodes(List<SqlNode> sqlNodes) {
        this.sqlNodes = sqlNodes;
    }

    @Override
    public String toString() {
        return "SqlFragment{" +
                "id='" + id + '\'' +
                ", sqlNodes=" + sqlNodes +
                '}';
    }
}

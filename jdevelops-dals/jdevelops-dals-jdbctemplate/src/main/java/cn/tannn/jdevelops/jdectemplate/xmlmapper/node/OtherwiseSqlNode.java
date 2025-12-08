package cn.tannn.jdevelops.jdectemplate.xmlmapper.node;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlContext;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlNode;

import java.util.List;

/**
 * Otherwise 默认节点
 * 对应 XML 中的 &lt;otherwise&gt; 标签
 * 必须在 &lt;choose&gt; 标签内使用，当所有 &lt;when&gt; 条件都不满足时执行
 *
 * @author tnnn
 */
public class OtherwiseSqlNode implements SqlNode {

    /**
     * 子节点列表
     */
    private final List<SqlNode> contents;

    public OtherwiseSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @Override
    public boolean apply(SqlContext context, Object parameter) {
        // 应用所有子节点
        boolean applied = false;
        for (SqlNode sqlNode : contents) {
            if (sqlNode.apply(context, parameter)) {
                applied = true;
            }
        }
        return applied;
    }

    public List<SqlNode> getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "OtherwiseSqlNode{contents=" + contents + '}';
    }
}

package cn.tannn.jdevelops.jdectemplate.xmlmapper.node;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlContext;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlNode;

import java.util.List;

/**
 * Choose 选择节点
 * 对应 XML 中的 &lt;choose&gt; 标签
 * 类似于 Java 的 switch-case 或 if-else if-else 结构
 *
 * @author tnnn
 */
public class ChooseSqlNode implements SqlNode {

    /**
     * when 条件节点列表
     */
    private final List<SqlNode> whenSqlNodes;

    /**
     * otherwise 默认节点（可选）
     */
    private final SqlNode otherwiseSqlNode;

    public ChooseSqlNode(List<SqlNode> whenSqlNodes, SqlNode otherwiseSqlNode) {
        this.whenSqlNodes = whenSqlNodes;
        this.otherwiseSqlNode = otherwiseSqlNode;
    }

    @Override
    public boolean apply(SqlContext context, Object parameter) {
        // 依次评估每个 when 条件
        for (SqlNode whenNode : whenSqlNodes) {
            if (whenNode.apply(context, parameter)) {
                // 找到第一个满足条件的 when，执行并返回
                return true;
            }
        }

        // 如果所有 when 都不满足，执行 otherwise（如果存在）
        if (otherwiseSqlNode != null) {
            return otherwiseSqlNode.apply(context, parameter);
        }

        return false;
    }

    public List<SqlNode> getWhenSqlNodes() {
        return whenSqlNodes;
    }

    public SqlNode getOtherwiseSqlNode() {
        return otherwiseSqlNode;
    }

    @Override
    public String toString() {
        return "ChooseSqlNode{" +
                "whenSqlNodes=" + whenSqlNodes +
                ", otherwiseSqlNode=" + otherwiseSqlNode +
                '}';
    }
}

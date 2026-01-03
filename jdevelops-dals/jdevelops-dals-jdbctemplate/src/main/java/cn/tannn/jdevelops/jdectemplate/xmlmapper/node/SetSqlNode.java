package cn.tannn.jdevelops.jdectemplate.xmlmapper.node;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlContext;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlNode;

import java.util.List;
import java.util.regex.Pattern;

/**
 * SET 子句节点
 * 对应 XML 中的 &lt;set&gt; 标签
 * 自动去除末尾的逗号
 *
 * @author tnnn
 */
public class SetSqlNode implements SqlNode {

    private static final Pattern COMMA_PATTERN = Pattern.compile(",\\s*$");

    private final List<SqlNode> contents;

    public SetSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @Override
    public boolean apply(SqlContext context, Object parameter) {
        // 创建临时上下文
        SqlContext tempContext = new SqlContext();
        tempContext.setUseNamedParameters(context.isUseNamedParameters());

        // 应用所有子节点到临时上下文
        boolean hasContent = false;
        for (SqlNode sqlNode : contents) {
            if (sqlNode.apply(tempContext, parameter)) {
                hasContent = true;
            }
        }

        if (!hasContent) {
            return false;
        }

        // 获取生成的 SQL
        String sql = tempContext.getSql().trim();
        if (sql.isEmpty()) {
            return false;
        }

        // 去除末尾的逗号
        sql = COMMA_PATTERN.matcher(sql).replaceFirst("");

        // 添加 SET 关键字
        context.appendSql(" SET " + sql);

        // 合并参数
        context.getParameters().addAll(tempContext.getParameters());
        context.getNamedParameters().putAll(tempContext.getNamedParameters());

        return true;
    }

    @Override
    public String toString() {
        return "SetSqlNode{contents=" + contents + '}';
    }
}

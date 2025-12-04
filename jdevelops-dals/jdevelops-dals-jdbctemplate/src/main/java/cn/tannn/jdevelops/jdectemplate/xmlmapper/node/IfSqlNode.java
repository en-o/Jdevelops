package cn.tannn.jdevelops.jdectemplate.xmlmapper.node;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlContext;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlNode;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.util.OgnlUtil;

import java.util.List;

/**
 * IF 条件节点
 * 对应 XML 中的 &lt;if&gt; 标签
 *
 * @author tnnn
 */
public class IfSqlNode implements SqlNode {

    /**
     * 条件表达式（OGNL 表达式）
     */
    private final String test;

    /**
     * 子节点列表
     */
    private final List<SqlNode> contents;

    public IfSqlNode(String test, List<SqlNode> contents) {
        this.test = test;
        this.contents = contents;
    }

    @Override
    public boolean apply(SqlContext context, Object parameter) {
        // 评估条件表达式
        if (evaluateTest(parameter)) {
            // 条件为真，应用所有子节点
            for (SqlNode sqlNode : contents) {
                sqlNode.apply(context, parameter);
            }
            return true;
        }
        return false;
    }

    /**
     * 评估条件表达式
     */
    private boolean evaluateTest(Object parameter) {
        try {
            return OgnlUtil.evaluateBoolean(test, parameter);
        } catch (Exception e) {
            // 条件评估失败，默认返回 false
            return false;
        }
    }

    public String getTest() {
        return test;
    }

    public List<SqlNode> getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "IfSqlNode{test='" + test + "', contents=" + contents + '}';
    }
}

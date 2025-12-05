package cn.tannn.jdevelops.jdectemplate.xmlmapper.node;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlContext;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlNode;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.util.OgnlUtil;

/**
 * 参数节点
 * 对应 #{parameter} 或 ${parameter} 形式的参数占位符
 *
 * @author tnnn
 */
public class ParameterSqlNode implements SqlNode {

    /**
     * 参数表达式
     */
    private final String expression;

    /**
     * 是否是动态 SQL（${} 形式）
     */
    private final boolean isDynamic;

    public ParameterSqlNode(String expression, boolean isDynamic) {
        this.expression = expression;
        this.isDynamic = isDynamic;
    }

    @Override
    public boolean apply(SqlContext context, Object parameter) {
        if (isDynamic) {
            // ${} 形式 - 直接替换为值（注意 SQL 注入风险）
            Object value = OgnlUtil.getValue(expression, parameter);
            context.appendSql(value != null ? value.toString() : "");
        } else {
            // #{} 形式 - 使用参数占位符
            Object value = OgnlUtil.getValue(expression, parameter);
            if (context.isUseNamedParameters()) {
                // 命名参数模式
                String paramName = generateParamName(context);
                context.appendSql(":" + paramName);
                context.addNamedParameter(paramName, value);
            } else {
                // 位置参数模式
                context.appendSql("?");
                context.addParameter(value);
            }
        }
        return true;
    }

    /**
     * 在 foreach 中应用参数（支持 item 和 index 变量）
     */
    public void applyWithItem(SqlContext context, Object parameter,
                              String itemName, Object itemValue,
                              String indexName, int indexValue) {
        if (isDynamic) {
            // ${} 形式
            Object value;
            if (expression.equals(itemName)) {
                value = itemValue;
            } else if (expression.equals(indexName)) {
                value = indexValue;
            } else if (expression.startsWith(itemName + ".")) {
                // 处理 user.username 形式
                String propertyPath = expression.substring(itemName.length() + 1);
                value = OgnlUtil.getValue(propertyPath, itemValue);
            } else {
                value = OgnlUtil.getValue(expression, parameter);
            }
            context.appendSql(value != null ? value.toString() : "");
        } else {
            // #{} 形式
            Object value;
            if (expression.equals(itemName)) {
                // 直接引用 item
                value = itemValue;
            } else if (expression.equals(indexName)) {
                // 直接引用 index
                value = indexValue;
            } else if (expression.startsWith(itemName + ".")) {
                // 处理 user.username 形式，从 itemValue 中获取属性
                String propertyPath = expression.substring(itemName.length() + 1);
                value = OgnlUtil.getValue(propertyPath, itemValue);
            } else {
                // 其他情况，从原始 parameter 获取
                value = OgnlUtil.getValue(expression, parameter);
            }

            if (context.isUseNamedParameters()) {
                String paramName = generateParamName(context);
                context.appendSql(":" + paramName);
                context.addNamedParameter(paramName, value);
            } else {
                context.appendSql("?");
                context.addParameter(value);
            }
        }
    }

    /**
     * 生成参数名
     */
    private String generateParamName(SqlContext context) {
        String baseName = expression.replaceAll("[^a-zA-Z0-9_]", "");
        return baseName + "_" + context.nextParameterIndex();
    }

    public String getExpression() {
        return expression;
    }

    public boolean isDynamic() {
        return isDynamic;
    }

    @Override
    public String toString() {
        return "ParameterSqlNode{" +
                "expression='" + expression + '\'' +
                ", isDynamic=" + isDynamic +
                '}';
    }
}

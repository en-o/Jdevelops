package cn.tannn.jdevelops.jdectemplate.xmlmapper.node;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlContext;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlNode;

/**
 * 静态文本节点
 * 表示 XML 中的纯文本内容
 *
 * @author tnnn
 */
public class TextSqlNode implements SqlNode {

    private final String text;

    public TextSqlNode(String text) {
        this.text = text;
    }

    @Override
    public boolean apply(SqlContext context, Object parameter) {
        if (text != null && !text.trim().isEmpty()) {
            context.appendSql(text);
            return true;
        }
        return false;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TextSqlNode{" + text + '}';
    }
}

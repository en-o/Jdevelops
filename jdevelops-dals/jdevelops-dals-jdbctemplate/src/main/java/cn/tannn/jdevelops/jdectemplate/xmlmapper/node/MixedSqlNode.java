package cn.tannn.jdevelops.jdectemplate.xmlmapper.node;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlContext;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 混合 SQL 节点
 * 包含静态文本和参数占位符的混合内容
 *
 * @author tnnn
 */
public class MixedSqlNode implements SqlNode {

    private static final Pattern PARAM_PATTERN = Pattern.compile("#\\{([^}]+)}|\\$\\{([^}]+)}");

    private final List<SqlNode> contents;

    public MixedSqlNode(String text) {
        this.contents = parseText(text);
    }

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @Override
    public boolean apply(SqlContext context, Object parameter) {
        boolean applied = false;
        for (SqlNode content : contents) {
            if (content.apply(context, parameter)) {
                applied = true;
            }
        }
        return applied;
    }

    /**
     * 解析文本，提取参数占位符
     */
    private List<SqlNode> parseText(String text) {
        List<SqlNode> nodes = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return nodes;
        }

        Matcher matcher = PARAM_PATTERN.matcher(text);
        int lastEnd = 0;

        while (matcher.find()) {
            // 添加前面的文本
            if (matcher.start() > lastEnd) {
                String staticText = text.substring(lastEnd, matcher.start());
                nodes.add(new TextSqlNode(staticText));
            }

            // 添加参数节点
            if (matcher.group(1) != null) {
                // #{} 形式
                nodes.add(new ParameterSqlNode(matcher.group(1), false));
            } else if (matcher.group(2) != null) {
                // ${} 形式
                nodes.add(new ParameterSqlNode(matcher.group(2), true));
            }

            lastEnd = matcher.end();
        }

        // 添加剩余的文本
        if (lastEnd < text.length()) {
            String staticText = text.substring(lastEnd);
            nodes.add(new TextSqlNode(staticText));
        }

        // 如果没有参数，返回整个文本作为静态节点
        if (nodes.isEmpty()) {
            nodes.add(new TextSqlNode(text));
        }

        return nodes;
    }

    @Override
    public String toString() {
        return "MixedSqlNode{contents=" + contents + '}';
    }
}

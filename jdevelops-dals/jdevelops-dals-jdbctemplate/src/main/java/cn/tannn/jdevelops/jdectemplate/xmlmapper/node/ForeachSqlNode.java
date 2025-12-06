package cn.tannn.jdevelops.jdectemplate.xmlmapper.node;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlContext;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlNode;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.util.OgnlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * FOREACH 循环节点
 * 对应 XML 中的 &lt;foreach&gt; 标签
 *
 * @author tnnn
 */
public class ForeachSqlNode implements SqlNode {

    private static final Logger LOG = LoggerFactory.getLogger(ForeachSqlNode.class);

    /**
     * 集合表达式
     */
    private final String collection;

    /**
     * 循环项变量名
     */
    private final String item;

    /**
     * 索引变量名
     */
    private final String index;

    /**
     * 开始字符
     */
    private final String open;

    /**
     * 分隔符
     */
    private final String separator;

    /**
     * 结束字符
     */
    private final String close;

    /**
     * 子节点列表
     */
    private final List<SqlNode> contents;

    public ForeachSqlNode(String collection, String item, String index,
                          String open, String separator, String close,
                          List<SqlNode> contents) {
        this.collection = collection;
        this.item = item;
        this.index = index;
        this.open = open;
        this.separator = separator;
        this.close = close;
        this.contents = contents;
    }

    @Override
    public boolean apply(SqlContext context, Object parameter) {
        // 获取集合对象
        if (LOG.isDebugEnabled()) {
            LOG.debug("Foreach: collection='{}', parameter type={}", collection,
                     parameter != null ? parameter.getClass().getSimpleName() : "null");
        }

        Object collectionValue = OgnlUtil.getValue(collection, parameter);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Foreach: collectionValue={}, type={}", collectionValue,
                     collectionValue != null ? collectionValue.getClass().getSimpleName() : "null");
        }

        if (collectionValue == null) {
            LOG.warn("Foreach: collection '{}' evaluated to null", collection);
            return false;
        }

        Collection<?> items = convertToCollection(collectionValue);
        if (items == null || items.isEmpty()) {
            LOG.warn("Foreach: collection '{}' is empty or not convertible to Collection", collection);
            return false;
        }

        // 添加开始字符
        if (StringUtils.hasText(open)) {
            context.appendSql(open);
        }

        // 遍历集合
        int idx = 0;
        for (Object itemValue : items) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Foreach iteration {}: itemValue={}", idx, itemValue);
            }

            if (idx > 0 && StringUtils.hasText(separator)) {
                context.appendSql(separator);
            }

            // 应用子节点（将 item 和 index 变量传递给子节点）
            // 注意：直接在主上下文上操作，不创建临时上下文，以保持参数索引连续
            for (SqlNode sqlNode : contents) {
                if (sqlNode instanceof ParameterSqlNode) {
                    // 处理参数节点
                    ((ParameterSqlNode) sqlNode).applyWithItem(context, parameter, item, itemValue, index, idx);
                } else {
                    // 其他节点类型，传递 item 值作为参数
                    sqlNode.apply(context, itemValue);
                }
            }

            idx++;
        }

        // 添加结束字符
        if (StringUtils.hasText(close)) {
            context.appendSql(close);
        }

        return true;
    }

    /**
     * 将对象转换为集合
     */
    @SuppressWarnings("unchecked")
    private Collection<?> convertToCollection(Object value) {
        if (value instanceof Collection) {
            return (Collection<?>) value;
        } else if (value instanceof Object[]) {
            return List.of((Object[]) value);
        }
        return null;
    }

    @Override
    public String toString() {
        return "ForeachSqlNode{" +
                "collection='" + collection + '\'' +
                ", item='" + item + '\'' +
                ", separator='" + separator + '\'' +
                '}';
    }
}

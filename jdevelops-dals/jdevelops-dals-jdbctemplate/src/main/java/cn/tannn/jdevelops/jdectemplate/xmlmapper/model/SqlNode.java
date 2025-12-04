package cn.tannn.jdevelops.jdectemplate.xmlmapper.model;

import java.util.Map;

/**
 * SQL 节点接口
 * 所有 SQL 片段都实现此接口
 *
 * @author tnnn
 */
public interface SqlNode {

    /**
     * 应用 SQL 节点，生成最终的 SQL 片段
     *
     * @param context SQL 构建上下文
     * @param parameter 参数对象
     * @return 是否成功应用
     */
    boolean apply(SqlContext context, Object parameter);

    /**
     * 节点类型
     */
    enum NodeType {
        /**
         * 静态文本
         */
        TEXT,

        /**
         * if 条件判断
         */
        IF,

        /**
         * foreach 循环
         */
        FOREACH,

        /**
         * where 子句
         */
        WHERE,

        /**
         * set 子句
         */
        SET,

        /**
         * trim 修剪
         */
        TRIM,

        /**
         * choose 选择
         */
        CHOOSE,

        /**
         * when 条件
         */
        WHEN,

        /**
         * otherwise 默认
         */
        OTHERWISE,

        /**
         * include 引用
         */
        INCLUDE,

        /**
         * bind 变量绑定
         */
        BIND
    }
}

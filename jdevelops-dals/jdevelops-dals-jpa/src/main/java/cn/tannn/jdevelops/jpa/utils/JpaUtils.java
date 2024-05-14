package cn.tannn.jdevelops.jpa.utils;

import cn.tannn.jdevelops.annotations.jpa.enums.SpecBuilderDateFun;
import cn.tannn.jdevelops.jpa.constant.SQLOperator;
import cn.tannn.jdevelops.result.bean.ColumnSFunction;
import cn.tannn.jdevelops.result.bean.ColumnUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.*;
import java.util.Date;

/**
 * Jpa项目里的工具类
 *
 * @author tn
 * @version 1
 * @date 2020/6/28 23:16
 */
public class JpaUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JpaUtils.class);
    private static final String SEPARATOR = ".";

    /**
     * 处理时间格式的key
     *
     * @param function  SpecBuilderDateFun
     * @param root      Root
     * @param builder   CriteriaBuilder
     * @param selectKey String
     * @param <B>       B
     */
    public static <B> Expression<String> functionTimeFormat(SpecBuilderDateFun function,
                                                            Root<B> root,
                                                            CriteriaBuilder builder,
                                                            String selectKey) {
        return builder
                .function(function.getName()
                        , String.class
                        , root.get(selectKey)
                        , builder.literal(function.getSqlFormat()));
    }


    /**
     * 格式化时间数据的值为字符串
     * bean:  LocalDateTime
     * sql：  timestamp
     * e.g.  mysql： date_format(user0_.create_time, "SQL 的 时间类型")
     * pgssql： to_char(user0_.create_time, "SQL 的 时间类型")
     *
     * @param function  SpecBuilderDateFun
     * @param root      Root
     * @param builder   CriteriaBuilder
     * @param selectKey String
     * @param <B>       B
     */
    public static <B> Expression<String> functionTimeFormat(SpecBuilderDateFun function,
                                                            Root<B> root,
                                                            CriteriaBuilder builder,
                                                            ColumnSFunction<B, ?> selectKey) {

        return builder
                .function(function.getName()
                        , String.class
                        , root.get(ColumnUtil.getFieldName(selectKey))
                        , builder.literal(function.getSqlFormat()));
    }


    /**
     * sql  date函数 固定死的
     * e.g.   DATE ( user0_.create_time ) =?
     *
     * @param root      Root
     * @param builder   CriteriaBuilder
     * @param selectKey String
     * @param <B>       B
     */
    public static <B> Expression<Date> functionTime(Root<B> root,
                                                    CriteriaBuilder builder,
                                                    String selectKey) {
        return builder
                .function("date"
                        , Date.class
                        , root.get(selectKey));
    }


    /**
     * 字符串的key名转jpa要用的对象
     * @param root {@link Root}
     * @param fieldName 字段名,可以处理 `address.name` 这种key
     * @return Expression
     */
    public static Expression str2Path(Root<?> root
            , String fieldName) {
        Path<?> path;

        if (fieldName.contains(SEPARATOR)) {
            String[] names = fieldName.split("\\" + SEPARATOR);
            path = root.get(names[0]);
            for (int i = 1; i < names.length; i++) {
                path = path.get(names[i]);
            }
        } else {
            path = root.get(fieldName);
        }
        return path;
    }

    /**
     * 字符串的key名转jpa要用的对象
     * @param root {@link Root}
     * @param builder {@link CriteriaBuilder}}
     * @param function {@link SpecBuilderDateFun}}
     * @param fieldName 字段名,可以处理 `address.name` 这种key
     * @return Expression
     */
    public static Expression str2Path(Root<?> root, CriteriaBuilder builder,SpecBuilderDateFun function, String fieldName) {
        if (null != function && !function.equals(SpecBuilderDateFun.NULL)) {
            return JpaUtils.functionTimeFormat(function, root, builder, fieldName);
        }
        return str2Path(root,fieldName);
    }


    /**
     * @param operator   {@link SQLOperator}
     * @param builder    {@link CriteriaBuilder}
     * @param value      添加值
     * @param expression 添加表达书 {@link Path#get(String)}
     * @return Predicate
     */
    public static Predicate getPredicate(
            SQLOperator operator
            , CriteriaBuilder builder
            , Expression expression
            , Object... value
    ) {
        switch (operator) {
            case EQ:
                return builder.equal(expression, value[0]);
            case NE:
                return builder.notEqual(expression, value[0]);
            case LIKE:
                return builder.like(expression, "%" + value[0] + "%");
            case NOTLIKE:
                return builder.notLike(expression, "%" + value[0] + "%");
            case LLIKE:
                return builder.like(expression, "%" + value[0]);
            case RLIKE:
                return builder.like(expression, value + "%");
            case LT:
                return builder.lessThan(expression, (Comparable) value[0]);
            case GT:
                return builder.greaterThan(expression, (Comparable) value[0]);
            case LTE:
                return builder.lessThanOrEqualTo(expression, (Comparable) value[0]);
            case GTE:
                return builder.greaterThanOrEqualTo(expression, (Comparable) value[0]);
            case ISNULL:
                return builder.isNull(expression);
            case ISNOTNULL:
                return builder.isNotNull(expression);
            case BETWEEN:
                return builder.between(expression, value[0].toString(), value[1].toString());
            case IN:
                return builder.in(expression).value(value);
            case NOTIN:
                return builder.not(builder.in(expression).value(value));
            default:
                LOG.warn("占不支持的表达式: {}", operator);
                return null;
        }
    }

}

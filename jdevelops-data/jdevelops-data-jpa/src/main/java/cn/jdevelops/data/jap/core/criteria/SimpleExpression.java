package cn.jdevelops.data.jap.core.criteria;

import javax.persistence.criteria.*;
import java.util.Date;

/**
 * sql 表达式
 * 简单表达式对象， 是所有简单操作表达式的父类， 如EqExpression, GtExpression等;
 *
 * @author tn
 */
public class SimpleExpression implements ExpandCriterion {
    /**
     * 属性名
     */
    private String fieldName;
    /**
     * 对应值
     */
    private Object value;
    /**
     * 计算符
     */
    private Operator operator;

    public SimpleExpression(String fieldName, Object value, Operator operator) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public SimpleExpression(String fieldName, Operator operator) {
        this.fieldName = fieldName;
        this.operator = operator;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getValue() {
        return value;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        Path expression;
        if (fieldName.contains(".")) {
            String[] names = fieldName.split(".");
            expression = root.get(names[0]);
            for (int i = 1; i < names.length; i++) {
                expression = expression.get(names[i]);
            }
        } else {
            expression = root.get(fieldName);
        }
        Expression<Date> function = null;
        if (value instanceof Date) {
            function = builder.function("to_date",
                    Date.class, root.get(fieldName));
        }

        switch (operator) {
            case EQ:
                return builder.equal(expression, value);
            case NE:
                return builder.notEqual(expression, value);
            case LIKE:
                return builder.like(expression, "%" + value + "%");
            case NOTLIKE:
                return builder.notLike(expression, "%" + value + "%");
            case LLIKE:
                return builder.like(expression, "%" + value);
            case RLIKE:
                return builder.like(expression, value + "%");
            case LT:
                if (function != null) {
                    return builder.lessThan(expression, (Date) value);
                } else {
                    return builder.lessThan(expression, (Comparable) value);
                }
            case GT:
                if (function != null) {
                    return builder.greaterThan(expression, (Date) value);
                } else {
                    return builder.greaterThan(expression, (Comparable) value);
                }
            case LTE:
                if (function != null) {
                    return builder.lessThanOrEqualTo(expression, (Date) value);
                } else {
                    return builder.lessThanOrEqualTo(expression, (Comparable) value);
                }
            case GTE:
                if (function != null) {
                    return builder.greaterThanOrEqualTo(expression, (Date) value);
                } else {
                    return builder.greaterThanOrEqualTo(expression, (Comparable) value);
                }
            case ISNULL:
                return builder.isNull(expression);
            case ISNOTNULL:
                return builder.isNotNull(expression);
            case BETWEEN:
                String[] split = value.toString().split(",");
                if (split.length == 2) {
                    return builder.between(expression, split[0], split[1]);
                } else {
                    return null;
                }
            default:
                return null;
        }
    }

}

package cn.jdevelops.data.jap.core.criteria;

import cn.jdevelops.data.jap.enums.SpecBuilderDateFun;
import cn.jdevelops.data.jap.util.IObjects;
import cn.jdevelops.data.jap.util.JpaUtils;

import javax.persistence.criteria.*;

/**
 * sql 表达式
 * 简单表达式对象， 是所有简单操作表达式的父类， 如EqExpression, GtExpression等;
 *
 * @author tn
 */
public class SimpleExpression implements ExpandCriterion {

    private static final String SEPARATOR = ".";

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

    /**
     * true表示会判断value是否为空，空则不做查询条件，不空则做查询条件
     * 默认不判空
     */
    private Boolean ignoreNull;

    /**
     * 函数处理，
     */
    private SpecBuilderDateFun function;

    public SimpleExpression(String fieldName, Object value, Operator operator, Boolean ignoreNull) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
        this.ignoreNull = ignoreNull;
    }

    public SimpleExpression(String fieldName, Object value, Operator operator, SpecBuilderDateFun function, Boolean ignoreNull) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
        this.function = function;
        this.ignoreNull = ignoreNull;
    }

    public SimpleExpression(String fieldName, Operator operator, Boolean ignoreNull) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.ignoreNull = ignoreNull;
    }

    public SimpleExpression(String fieldName, Operator operator, SpecBuilderDateFun function, Boolean ignoreNull) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.function = function;
        this.ignoreNull = ignoreNull;
    }


    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        // 构建 查询key
        Expression expression = str2Path(root, builder);
        if (Boolean.TRUE.equals(getIgnoreNull()) && IObjects.isaBoolean(value)) {
            return null;
        }
        // 构建查询
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
                return builder.lessThan(expression, (Comparable) value);
            case GT:
                return builder.greaterThan(expression, (Comparable) value);
            case LTE:
                return builder.lessThanOrEqualTo(expression, (Comparable) value);
            case GTE:
                return builder.greaterThanOrEqualTo(expression, (Comparable) value);
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


    /**
     * 字符串的key名转jpa要用的对象
     */
    public Expression str2Path(Root<?> root, CriteriaBuilder builder) {
        Path<?> path;
        if (null != function && !function.equals(SpecBuilderDateFun.NULL)) {
            return JpaUtils.functionTimeFormat(function, root, builder, fieldName);
        }
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


    public Boolean getIgnoreNull() {
        if (ignoreNull == null) {
            return false;
        }
        return ignoreNull;
    }

    public void setIgnoreNull(Boolean ignoreNull) {
        this.ignoreNull = ignoreNull;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public SpecBuilderDateFun getFunction() {
        return function;
    }

    public void setFunction(SpecBuilderDateFun function) {
        this.function = function;
    }

    @Override
    public String toString() {
        return "SimpleExpression{" +
                "fieldName='" + fieldName + '\'' +
                ", value=" + value +
                ", operator=" + operator +
                ", ignoreNull=" + ignoreNull +
                ", function=" + function +
                '}';
    }
}

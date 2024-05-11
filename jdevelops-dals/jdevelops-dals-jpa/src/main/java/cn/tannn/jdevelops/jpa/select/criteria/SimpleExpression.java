package cn.tannn.jdevelops.jpa.select.criteria;


import cn.tannn.jdevelops.annotations.jpa.enums.SpecBuilderDateFun;
import cn.tannn.jdevelops.jpa.utils.IObjects;
import cn.tannn.jdevelops.jpa.utils.JpaUtils;

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
     * 空值验证 <br/>
     *
     * true: 空值不作为查询参数 <br/>
     * false: 需要查询为空的数据
     */
    private Boolean ignoreNull;

    /**
     * ignoreNull = true 有效  <br/>
     * true: 值是否为 [null,""," "]  <br/>
     * false: 值是否为 null
     */
    private Boolean ignoreNullEnhance;

    /**
     * 函数处理，
     */
    private SpecBuilderDateFun function;



    public SimpleExpression(String fieldName,
                            Object value,
                            Operator operator,
                            SpecBuilderDateFun function,
                            Boolean ignoreNull,
                            Boolean ignoreNullEnhance) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
        this.function = function;
        this.ignoreNull = ignoreNull;
        this.ignoreNullEnhance = ignoreNullEnhance;
    }

    public SimpleExpression(String fieldName,
                            Operator operator,
                            SpecBuilderDateFun function,
                            Boolean ignoreNull,
                            Boolean ignoreNullEnhance) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.function = function;
        this.ignoreNull = ignoreNull;
        this.ignoreNullEnhance = ignoreNullEnhance;
    }


    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        // 构建 查询key
        Expression expression = str2Path(root, builder);
        if (Boolean.TRUE.equals(getIgnoreNull()) && IObjects.isNull(value, getIgnoreNullEnhance())) {
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
            return true;
        }
        return ignoreNull;
    }

    public void setIgnoreNull(Boolean ignoreNull) {
        this.ignoreNull = ignoreNull;
    }

    /**
     * ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     */
    public Boolean getIgnoreNullEnhance() {
        if (ignoreNullEnhance == null) {
            return true;
        }
        return ignoreNullEnhance;
    }

    public void setIgnoreNullEnhance(Boolean ignoreNullEnhance) {
        this.ignoreNullEnhance = ignoreNullEnhance;
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
                ", ignoreNullEnhance=" + ignoreNullEnhance +
                ", function=" + function +
                '}';
    }
}

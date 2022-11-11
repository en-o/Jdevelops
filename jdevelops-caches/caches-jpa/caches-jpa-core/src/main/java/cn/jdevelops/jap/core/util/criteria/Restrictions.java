package cn.jdevelops.jap.core.util.criteria;


import cn.jdevelops.jap.util.IObjects;
import cn.jdevelops.map.core.bean.ColumnUtil;

import java.util.Collection;

/**
 * jpa 查询工具之构建sql  具体实现
 *
 * @author tn
 */
public class Restrictions {

    /**
     * 等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static SimpleExpression eq(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isNull(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.EQ);
    }


    /**
     * 等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static <T> SimpleExpression eq(ColumnUtil.SFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isNull(value)) {
            return null;
        }
        return new SimpleExpression(ColumnUtil.getFieldName(fieldName), value, ExpandCriterion.Operator.EQ);
    }

    /**
     * 不等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static SimpleExpression ne(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isNull(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.NE);
    }

    /**
     * 不等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static <T> SimpleExpression ne(ColumnUtil.SFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isNull(value)) {
            return null;
        }
        return new SimpleExpression(ColumnUtil.getFieldName(fieldName), value, ExpandCriterion.Operator.NE);
    }

    /**
     * 模糊匹配
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static SimpleExpression like(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isaBoolean(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.LIKE);
    }


    /**
     * 模糊匹配
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static <T>  SimpleExpression like(ColumnUtil.SFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isaBoolean(value)) {
            return null;
        }
        return new SimpleExpression(ColumnUtil.getFieldName(fieldName), value, ExpandCriterion.Operator.LIKE);
    }

    /**
     * 模糊不包含
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  字段名
     * @param value      字段值
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static SimpleExpression notLike(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isaBoolean(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.NOTLIKE);
    }

    /**
     * 模糊不包含
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  字段名
     * @param value      字段值
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static <T>  SimpleExpression notLike(ColumnUtil.SFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isaBoolean(value)) {
            return null;
        }
        return new SimpleExpression(ColumnUtil.getFieldName(fieldName), value, ExpandCriterion.Operator.NOTLIKE);
    }

    /**
     *
     * 左模糊匹配
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static SimpleExpression llike(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isaBoolean(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.LLIKE);
    }

    /**
     *
     * 左模糊匹配
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static <T> SimpleExpression llike(ColumnUtil.SFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isaBoolean(value)) {
            return null;
        }
        return new SimpleExpression(ColumnUtil.getFieldName(fieldName), value, ExpandCriterion.Operator.LLIKE);
    }


    /**
     * 右模糊匹配
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static SimpleExpression rlike(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isaBoolean(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.RLIKE);
    }


    /**
     * 右模糊匹配
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static <T> SimpleExpression rlike(ColumnUtil.SFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isaBoolean(value)) {
            return null;
        }
        return new SimpleExpression(ColumnUtil.getFieldName(fieldName), value, ExpandCriterion.Operator.RLIKE);
    }


    /**
     * 大于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static SimpleExpression gt(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isNull(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.GT);
    }

    /**
     * 大于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static <T> SimpleExpression gt(ColumnUtil.SFunction<T, ?>  fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isNull(value)) {
            return null;
        }
        return new SimpleExpression(ColumnUtil.getFieldName(fieldName), value, ExpandCriterion.Operator.GT);
    }

    /**
     * 小于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static SimpleExpression lt(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isNull(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.LT);
    }

    /**
     * 小于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static <T> SimpleExpression lt(ColumnUtil.SFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isNull(value)) {
            return null;
        }
        return new SimpleExpression(ColumnUtil.getFieldName(fieldName), value, ExpandCriterion.Operator.LT);
    }

    /**
     * 大于等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static SimpleExpression gte(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isNull(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.GTE);
    }

    /**
     * 大于等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static <T> SimpleExpression gte(ColumnUtil.SFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isNull(value)) {
            return null;
        }
        return new SimpleExpression(ColumnUtil.getFieldName(fieldName), value, ExpandCriterion.Operator.GTE);
    }

    /**
     * 小于等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static SimpleExpression lte(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isNull(value)) {
            return null;
        }
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.LTE);
    }

    /**
     * 小于等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public static <T> SimpleExpression lte(ColumnUtil.SFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isNull(value)) {
            return null;
        }
        return new SimpleExpression(ColumnUtil.getFieldName(fieldName), value, ExpandCriterion.Operator.LTE);
    }

    /**
     * 并且
     *
     * @param criterions criterions
     * @return LogicalExpression
     */
    public static LogicalExpression and(ExpandCriterion... criterions) {
        return new LogicalExpression(criterions, ExpandCriterion.Operator.AND);
    }

    /**
     * 或者
     *
     * @param criterions criterions
     * @return LogicalExpression
     */
    public static LogicalExpression or(ExpandCriterion... criterions) {
        return new LogicalExpression(criterions, ExpandCriterion.Operator.OR);
    }

    /**
     * 包含于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return LogicalExpression
     */
    @SuppressWarnings("rawtypes")
    public static LogicalExpression in(String fieldName, Collection value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isaBoolean(value)) {
            return null;
        }
        SimpleExpression[] ses = new SimpleExpression[value.size()];
        int i = 0;
        for (Object obj : value) {
            ses[i] = new SimpleExpression(fieldName, obj, ExpandCriterion.Operator.EQ);
            i++;
        }
        return new LogicalExpression(ses, ExpandCriterion.Operator.OR);
    }

    /**
     * 包含于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return LogicalExpression
     */
    @SuppressWarnings("rawtypes")
    public static <T> LogicalExpression in(ColumnUtil.SFunction<T, ?> fieldName, Collection value, boolean ignoreNull) {
        if (ignoreNull && IObjects.isaBoolean(value)) {
            return null;
        }
        SimpleExpression[] ses = new SimpleExpression[value.size()];
        int i = 0;
        for (Object obj : value) {
            ses[i] = new SimpleExpression(ColumnUtil.getFieldName(fieldName), obj, ExpandCriterion.Operator.EQ);
            i++;
        }
        return new LogicalExpression(ses, ExpandCriterion.Operator.OR);
    }


    /**
     * 等于空值
     *
     * @param fieldName fieldName
     * @return SimpleExpression
     */
    public static SimpleExpression isNull(String fieldName) {
        return new SimpleExpression(fieldName, ExpandCriterion.Operator.ISNULL);
    }


    /**
     * 等于空值
     *
     * @param fieldName fieldName
     * @return SimpleExpression
     */
    public static <T> SimpleExpression isNull(ColumnUtil.SFunction<T, ?> fieldName) {
        return new SimpleExpression(ColumnUtil.getFieldName(fieldName), ExpandCriterion.Operator.ISNULL);
    }

    /**
     * 空值
     *
     * @param fieldName fieldName
     * @return SimpleExpression
     */
    public static SimpleExpression isNotNull(String fieldName) {
        return new SimpleExpression(fieldName, ExpandCriterion.Operator.ISNOTNULL);
    }


    /**
     * 空值
     *
     * @param fieldName fieldName
     * @return SimpleExpression
     */
    public static <T> SimpleExpression isNotNull(ColumnUtil.SFunction<T, ?> fieldName) {
        return new SimpleExpression(ColumnUtil.getFieldName(fieldName), ExpandCriterion.Operator.ISNOTNULL);
    }
}

package cn.jdevelops.data.jap.core.criteria;

import cn.jdevelops.data.jap.util.IObjects;
import cn.jdevelops.map.core.bean.ColumnSFunction;
import cn.jdevelops.map.core.bean.ColumnUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

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
    public static <T> SimpleExpression eq(ColumnSFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
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
    public static <T> SimpleExpression ne(ColumnSFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
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
    public static <T>  SimpleExpression like(ColumnSFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
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
    public static <T>  SimpleExpression notLike(ColumnSFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
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
    public static <T> SimpleExpression llike(ColumnSFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
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
    public static <T> SimpleExpression rlike(ColumnSFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
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
    public static <T> SimpleExpression gt(ColumnSFunction<T, ?>  fieldName, Object value, boolean ignoreNull) {
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
    public static <T> SimpleExpression lt(ColumnSFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
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
    public static <T> SimpleExpression gte(ColumnSFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
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
    public static <T> SimpleExpression lte(ColumnSFunction<T, ?> fieldName, Object value, boolean ignoreNull) {
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
        ExpandCriterion[] expandCriteria = Arrays.stream(criterions)
                .filter(Objects::nonNull)
                .toArray(ExpandCriterion[]::new);
        return expandCriteria.length>0?new LogicalExpression(expandCriteria, ExpandCriterion.Operator.OR):null;
    }

    /**
     * 或者
     *
     * @param criterions criterions
     * @return LogicalExpression
     */
    public static LogicalExpression or(ExpandCriterion... criterions) {
        ExpandCriterion[] expandCriteria = Arrays.stream(criterions)
                .filter(Objects::nonNull)
                .toArray(ExpandCriterion[]::new);
        return expandCriteria.length>0?new LogicalExpression(expandCriteria, ExpandCriterion.Operator.OR):null;
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
    public static <T> LogicalExpression in(ColumnSFunction<T, ?> fieldName, Collection value, boolean ignoreNull) {
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
    public static <T> SimpleExpression isNull(ColumnSFunction<T, ?> fieldName) {
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
    public static <T> SimpleExpression isNotNull(ColumnSFunction<T, ?> fieldName) {
        return new SimpleExpression(ColumnUtil.getFieldName(fieldName), ExpandCriterion.Operator.ISNOTNULL);
    }
}

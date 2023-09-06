package cn.jdevelops.data.jap.core.criteria;

import cn.jdevelops.data.jap.enums.SpecBuilderDateFun;
import cn.jdevelops.data.jap.util.IObjects;

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
     * 并且
     *
     * @param criterions criterions
     * @return LogicalExpression
     */
    public static LogicalExpression and(ExpandCriterion... criterions) {
        ExpandCriterion[] expandCriteria = Arrays.stream(criterions)
                .filter(Objects::nonNull)
                .toArray(ExpandCriterion[]::new);
        return expandCriteria.length > 0 ? new LogicalExpression(expandCriteria, ExpandCriterion.Operator.OR) : null;
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
        return expandCriteria.length > 0 ? new LogicalExpression(expandCriteria, ExpandCriterion.Operator.OR) : null;
    }


    /**
     * 等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @return SimpleExpression
     */
    public static SimpleExpression eq(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance) {
        return eq(fieldName, value, ignoreNull, ignoreNullEnhance, SpecBuilderDateFun.NULL);
    }


    /**
     * 等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param function   处理数据格式
     * @return SimpleExpression
     */
    public static SimpleExpression eq(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance, SpecBuilderDateFun function) {
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.EQ, function, ignoreNull, ignoreNullEnhance);
    }


    /**
     * 不等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @return SimpleExpression
     */
    public static SimpleExpression ne(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance) {
        return ne(fieldName, value, ignoreNull, ignoreNullEnhance, SpecBuilderDateFun.NULL);
    }

    /**
     * 不等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param function   处理数据格式
     * @return SimpleExpression
     */
    public static SimpleExpression ne(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance, SpecBuilderDateFun function) {
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.NE, function, ignoreNull, ignoreNullEnhance);
    }

    /**
     * 模糊匹配
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @return SimpleExpression
     */
    public static SimpleExpression like(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance) {
        return like(fieldName, value, ignoreNull, ignoreNullEnhance, SpecBuilderDateFun.NULL);
    }

    /**
     * 模糊匹配
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param function   处理数据格式
     * @return SimpleExpression
     */
    public static SimpleExpression like(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance, SpecBuilderDateFun function) {
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.LIKE, function, ignoreNull, ignoreNullEnhance);
    }


    /**
     * 模糊不包含
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  字段名
     * @param value      字段值
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @return SimpleExpression
     */
    public static SimpleExpression notLike(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance) {
        return notLike(fieldName, value, ignoreNull, ignoreNullEnhance, SpecBuilderDateFun.NULL);
    }

    /**
     * 模糊不包含
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  字段名
     * @param value      字段值
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param function   处理数据格式
     * @return SimpleExpression
     */
    public static SimpleExpression notLike(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance, SpecBuilderDateFun function) {
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.NOTLIKE, function, ignoreNull, ignoreNullEnhance);
    }


    /**
     * 左模糊匹配
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @return SimpleExpression
     */
    public static SimpleExpression llike(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance) {
        return llike(fieldName, value, ignoreNull, ignoreNullEnhance, SpecBuilderDateFun.NULL);
    }


    /**
     * 左模糊匹配
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param function   处理数据格式
     * @return SimpleExpression
     */
    public static SimpleExpression llike(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance, SpecBuilderDateFun function) {
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.LLIKE, function, ignoreNull, ignoreNullEnhance);
    }


    /**
     * 右模糊匹配
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @return SimpleExpression
     */
    public static SimpleExpression rlike(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance) {
        return rlike(fieldName, value, ignoreNull, ignoreNullEnhance, SpecBuilderDateFun.NULL);
    }

    /**
     * 右模糊匹配
     * ps：实体类型为 int Integer Long Float Double 等数字类型时不要使用like 会报错
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param function   处理数据格式
     * @return SimpleExpression
     */
    public static SimpleExpression rlike(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance, SpecBuilderDateFun function) {
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.RLIKE, function, ignoreNull, ignoreNullEnhance);
    }

    /**
     * 大于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @return SimpleExpression
     */
    public static SimpleExpression gt(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance) {
        return gt(fieldName, value, ignoreNull, ignoreNullEnhance, SpecBuilderDateFun.NULL);
    }

    /**
     * 大于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param function   处理数据格式
     * @return SimpleExpression
     */
    public static SimpleExpression gt(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance, SpecBuilderDateFun function) {
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.GT, function, ignoreNull, ignoreNullEnhance);
    }


    /**
     * 小于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @return SimpleExpression
     */
    public static SimpleExpression lt(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance) {
        return lt(fieldName, value, ignoreNull, ignoreNullEnhance, SpecBuilderDateFun.NULL);
    }

    /**
     * 小于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param function   处理数据格式
     * @return SimpleExpression
     */
    public static SimpleExpression lt(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance, SpecBuilderDateFun function) {
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.LT, function, ignoreNull, ignoreNullEnhance);
    }

    /**
     * 大于等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @return SimpleExpression
     */
    public static SimpleExpression gte(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance) {
        return gte(fieldName, value, ignoreNull, ignoreNullEnhance, SpecBuilderDateFun.NULL);
    }

    /**
     * 大于等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param function   处理数据格式
     * @return SimpleExpression
     */
    public static SimpleExpression gte(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance, SpecBuilderDateFun function) {
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.GTE, function, ignoreNull, ignoreNullEnhance);
    }


    /**
     * 小于等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @return SimpleExpression
     */
    public static SimpleExpression lte(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance) {
        return lte(fieldName, value, ignoreNull, ignoreNullEnhance, SpecBuilderDateFun.NULL);
    }

    /**
     * 小于等于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param function   处理数据格式
     * @return SimpleExpression
     */
    public static SimpleExpression lte(String fieldName, Object value, boolean ignoreNull,  boolean ignoreNullEnhance, SpecBuilderDateFun function) {
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.LTE, function, ignoreNull, ignoreNullEnhance);
    }


    /**
     * 包含于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @return LogicalExpression
     */
    @SuppressWarnings("rawtypes")
    public static LogicalExpression in(String fieldName, Collection value, boolean ignoreNull,  boolean ignoreNullEnhance) {
        return in(fieldName, value, ignoreNull, ignoreNullEnhance, SpecBuilderDateFun.NULL);
    }


    /**
     * 包含于
     *
     * @param fieldName  实体字段名
     * @param value      value
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param function   处理数据格式
     * @return LogicalExpression
     */
    @SuppressWarnings("rawtypes")
    public static LogicalExpression in(String fieldName, Collection value, boolean ignoreNull,  boolean ignoreNullEnhance, SpecBuilderDateFun function) {
        if (ignoreNull && IObjects.isaBoolean(value)) {
            return null;
        }
        SimpleExpression[] ses = new SimpleExpression[value.size()];
        int i = 0;
        for (Object obj : value) {
            SimpleExpression simpleExpression = new SimpleExpression(fieldName, obj, ExpandCriterion.Operator.EQ, function, ignoreNull, ignoreNullEnhance);
            ses[i] = simpleExpression;
            i++;
        }
        return new LogicalExpression(ses, ExpandCriterion.Operator.OR);
    }


    /**
     * field is null
     *
     * @param fieldName fieldName
     * @return SimpleExpression
     */
    public static SimpleExpression isNull(String fieldName) {
        return isNull(fieldName, SpecBuilderDateFun.NULL);
    }

    /**
     * field is null
     *
     * @param fieldName fieldName
     * @param function  处理数据格式
     * @return SimpleExpression
     */
    public static SimpleExpression isNull(String fieldName, SpecBuilderDateFun function) {
        return new SimpleExpression(fieldName, ExpandCriterion.Operator.ISNULL, function, false,true);
    }


    /**
     * field not null
     *
     * @param fieldName fieldName
     * @return SimpleExpression
     */
    public static SimpleExpression isNotNull(String fieldName) {
        return isNotNull(fieldName, SpecBuilderDateFun.NULL);
    }

    /**
     * field not null
     *
     * @param fieldName fieldName
     * @param function  处理数据格式
     * @return SimpleExpression
     */
    public static SimpleExpression isNotNull(String fieldName, SpecBuilderDateFun function) {
        return new SimpleExpression(fieldName, ExpandCriterion.Operator.ISNOTNULL, function, false,true);
    }


    /**
     * 范围查询[x,y]
     *
     * @param fieldName  实体字段名
     * @param value      value {value: 1,2}
     * @param ignoreNull 空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @return SimpleExpression
     */
    public static SimpleExpression between(String fieldName, String value, boolean ignoreNull,  boolean ignoreNullEnhance) {
        return between(fieldName, value, ignoreNull, ignoreNullEnhance, SpecBuilderDateFun.NULL);
    }


    /**
     * 范围查询[x,y]
     *
     * @param fieldName  实体字段名
     * @param value      value {value: 1,2}
     * @param ignoreNull true表示会判断value是否为空，空则不做查询条件，不空则做查询条件
     * @param function   处理数据格式
     * @return SimpleExpression
     */
    public static SimpleExpression between(String fieldName, String value, boolean ignoreNull,  boolean ignoreNullEnhance, SpecBuilderDateFun function) {
        return new SimpleExpression(fieldName, value, ExpandCriterion.Operator.BETWEEN, function, ignoreNull, ignoreNullEnhance);
    }


}

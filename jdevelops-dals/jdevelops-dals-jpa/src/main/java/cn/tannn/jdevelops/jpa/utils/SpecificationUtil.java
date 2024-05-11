package cn.tannn.jdevelops.jpa.utils;

import cn.tannn.jdevelops.annotations.jpa.JpaSelectOperator;
import cn.tannn.jdevelops.result.bean.ColumnSFunction;
import cn.tannn.jdevelops.result.bean.ColumnUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * CustomerSpecs
 * <code>
 * ## 原始示例
 * userService.getJpaBasicsDao().findAll(
 * address().and(
 * loginPwd().or(phone()).or(name())
 * ).or(
 * loginPwd().and(
 * phone().or(name())
 * )
 * )
 * ).forEach(System.out::println);
 * public static Specification<User> name() {
 * return (root, query, builder) -> builder.like(root.get(User::getName), "%用户%");
 * }
 * <p>
 * public static Specification<User> loginPwd() {
 * return (root, query, builder) -> builder.like(root.get("loginPwd"), "123");
 * }
 * <p>
 * public static Specification<User> address() {
 * return (root, query, builder) -> builder.like(root.get("address"), "重庆");
 * }
 * <p>
 * public static Specification<User> phone() {
 * return (root, query, builder) -> builder.like(root.get("phone"), "123");
 * }
 * <p>
 * public static Specification<User> userNo() {
 * return (root, query, builder) -> builder.like(root.get("phone"), "123");
 * }
 * </code>
 *
 * @author tn
 * @version 1
 * @date 2021-12-14 15:29
 */

public final class SpecificationUtil<T> {

    private static final SpecificationUtil<?> INSTANCE = new SpecificationUtil();


    private SpecificationUtil() {
    }

    /**
     * get SpringBeanUtils.
     *
     * @return SpringBeanUtils
     */
    public static <T> SpecificationUtil<T> getInstance() {
        return (SpecificationUtil<T>) INSTANCE;
    }


    /**
     * 等于
     *
     * @param key               键 (实体字段非数据库字段)
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> eq(String key, Object value, boolean ignoreNull, boolean ignoreNullEnhance) {
        if (ignoreNull && IObjects.isNull(value, ignoreNullEnhance)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.equal(root.get(key), value);
        }

    }

    /**
     * 等于
     *
     * @param fn                实体字段
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> eq(ColumnSFunction<T, ?> fn, Object value, boolean ignoreNull, boolean ignoreNullEnhance) {
        return eq(ColumnUtil.getFieldName(fn), value, ignoreNull, ignoreNullEnhance);
    }

    /**
     * 不相等
     *
     * @param key               键 (实体字段非数据库字段)
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> notEq(String key, Object value, boolean ignoreNull, boolean ignoreNullEnhance) {
        if (ignoreNull && IObjects.isNull(value, ignoreNullEnhance)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.notEqual(root.get(key), value);
        }
    }

    /**
     * 不相等
     *
     * @param fn                实体字段
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> notEq(ColumnSFunction<T, ?> fn, Object value, boolean ignoreNull, boolean ignoreNullEnhance) {
        return notEq(ColumnUtil.getFieldName(fn), value, ignoreNull, ignoreNullEnhance);
    }

    /**
     * 大于
     *
     * @param key               键 (实体字段非数据库字段)
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> gt(String key, Comparable value, boolean ignoreNull, boolean ignoreNullEnhance) {
        if (ignoreNull && IObjects.isNull(value, ignoreNullEnhance)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.greaterThan(root.get(key), value);
        }
    }

    /**
     * 大于
     *
     * @param fn                实体字段
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> gt(ColumnSFunction<T, ?> fn, Comparable value, boolean ignoreNull, boolean ignoreNullEnhance) {
        return gt(ColumnUtil.getFieldName(fn), value, ignoreNull, ignoreNullEnhance);
    }

    /**
     * 大于等于
     *
     * @param key               键 (实体字段非数据库字段)
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> ge(String key, Comparable value, boolean ignoreNull, boolean ignoreNullEnhance) {
        if (ignoreNull && IObjects.isNull(value, ignoreNullEnhance)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get(key), value);
        }
    }

    /**
     * 大于等于
     *
     * @param fn                实体字段
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> ge(ColumnSFunction<T, ?> fn, Comparable value, boolean ignoreNull, boolean ignoreNullEnhance) {
        return ge(ColumnUtil.getFieldName(fn), value, ignoreNull, ignoreNullEnhance);
    }

    /**
     * 小于
     *
     * @param key               键 (实体字段非数据库字段)
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> lt(String key, Comparable value, boolean ignoreNull, boolean ignoreNullEnhance) {
        if (ignoreNull && IObjects.isNull(value, ignoreNullEnhance)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.lessThan(root.get(key), value);
        }
    }

    /**
     * 小于
     *
     * @param fn                实体字段
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> lt(ColumnSFunction<T, ?> fn, Comparable value, boolean ignoreNull, boolean ignoreNullEnhance) {
        return lt(ColumnUtil.getFieldName(fn), value, ignoreNull, ignoreNullEnhance);
    }

    /**
     * 小于等于
     *
     * @param key               键 (实体字段非数据库字段)
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> le(String key, Comparable value, boolean ignoreNull, boolean ignoreNullEnhance) {
        if (ignoreNull && IObjects.isNull(value, ignoreNullEnhance)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.lessThanOrEqualTo(root.get(key), value);
        }
    }


    /**
     * 小于等于
     *
     * @param fn                实体字段
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> le(ColumnSFunction<T, ?> fn, Comparable value, boolean ignoreNull, boolean ignoreNullEnhance) {
        return le(ColumnUtil.getFieldName(fn), value, ignoreNull, ignoreNullEnhance);
    }

    /**
     * not in
     *
     * @param key               实体字段
     * @param values            值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> not(String key, Collection<?> values, boolean ignoreNull, boolean ignoreNullEnhance) {
        if (ignoreNull && IObjects.isNull(values, ignoreNullEnhance)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.not(root.get(key).in(values));
        }

    }


    /**
     * not in
     *
     * @param fn                实体字段
     * @param values            值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> not(ColumnSFunction<T, ?> fn, Collection<?> values, boolean ignoreNull, boolean ignoreNullEnhance) {
        return not(ColumnUtil.getFieldName(fn), values, ignoreNull, ignoreNullEnhance);
    }


    /**
     * between
     * v1 <= key <= v2
     *
     * @param key               键 (实体字段非数据库字段)
     * @param v1                值1
     * @param v2                值2
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> between(String key, String v1, String v2, boolean ignoreNull, boolean ignoreNullEnhance) {
        if (ignoreNull && (IObjects.isNull(v1, ignoreNullEnhance) || IObjects.isNull(v2, ignoreNullEnhance))) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.between(root.get(key), v1, v2);
        }
    }


    /**
     * between
     * v1 <= key <= v2
     *
     * @param key               键 (实体字段非数据库字段)
     * @param v1v2              值(1,2) 英文逗号隔开
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> between(String key, String v1v2, boolean ignoreNull, boolean ignoreNullEnhance) {
        String v1;
        String v2;
        if (v1v2.contains(",")) {
            String[] split = v1v2.split(",");
            v1 = split[0];
            v2 = split[1];
        } else {
            v2 = null;
            v1 = null;
        }
        if (ignoreNull && (Objects.isNull(v1) || Objects.isNull(v2))) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.between(root.get(key), v1, v2);
        }
    }

    /**
     * between
     * v1 <= fn <= v2
     *
     * @param fn                实体字段
     * @param v1                值1
     * @param v2                值2
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> between(ColumnSFunction<T, ?> fn, String v1, String v2, boolean ignoreNull, boolean ignoreNullEnhance) {
        return between(ColumnUtil.getFieldName(fn), v1, v2, ignoreNull, ignoreNullEnhance);
    }


    /**
     * 模糊匹配
     *
     * @param key               键 (实体字段非数据库字段)
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> like(String key, String value, boolean ignoreNull, boolean ignoreNullEnhance) {
        if (ignoreNull && IObjects.isNull(value, ignoreNullEnhance)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.like(root.get(key), "%" + value + "%");
        }
    }


    /**
     * 模糊匹配
     *
     * @param fn                实体字段
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> like(ColumnSFunction<T, ?> fn, String value, boolean ignoreNull, boolean ignoreNullEnhance) {
        return like(ColumnUtil.getFieldName(fn), value, ignoreNull, ignoreNullEnhance);
    }


    /**
     * 左模糊
     * %xxx
     *
     * @param key               键 (实体字段非数据库字段)
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> lLike(String key, String value, boolean ignoreNull, boolean ignoreNullEnhance) {
        if (ignoreNull && IObjects.isNull(value, ignoreNullEnhance)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.like(root.get(key), "%" + value);
        }
    }


    /**
     * 左模糊
     * %xxx
     *
     * @param fn                实体字段
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> lLike(ColumnSFunction<T, ?> fn, String value, boolean ignoreNull, boolean ignoreNullEnhance) {
        return lLike(ColumnUtil.getFieldName(fn), value, ignoreNull, ignoreNullEnhance);
    }


    /**
     * 右模糊
     * %xxx
     *
     * @param key               键 (实体字段非数据库字段)
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> rLike(String key, String value, boolean ignoreNull, boolean ignoreNullEnhance) {
        if (ignoreNull && IObjects.isNull(value, ignoreNullEnhance)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.like(root.get(key), value + "%");
        }
    }


    /**
     * 右模糊
     * %xxx
     *
     * @param fn                实体字段
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> rLike(ColumnSFunction<T, ?> fn, String value, boolean ignoreNull, boolean ignoreNullEnhance) {
        return rLike(ColumnUtil.getFieldName(fn), value, ignoreNull, ignoreNullEnhance);
    }

    /**
     * 模糊不匹配
     *
     * @param key               键 (实体字段非数据库字段)
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> notLike(String key, String value, boolean ignoreNull, boolean ignoreNullEnhance) {
        if (ignoreNull && IObjects.isNull(value, ignoreNullEnhance)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.notLike(root.get(key), "%" + value + "%");
        }
    }

    /**
     * 模糊不匹配
     *
     * @param fn                实体字段
     * @param value             值
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return Specification
     */
    public Specification<T> notLike(ColumnSFunction<T, ?> fn, String value, boolean ignoreNull, boolean ignoreNullEnhance) {
        return notLike(ColumnUtil.getFieldName(fn), value, ignoreNull, ignoreNullEnhance);
    }

    /**
     * key值非空
     *
     * @param key 键 (实体字段非数据库字段)
     * @return Specification
     */
    public Specification<T> isNotNull(String key) {
        return (root, query, builder) -> builder.isNotNull(root.get(key));
    }

    /**
     * key值非空
     *
     * @param fn 实体字段
     * @return Specification
     */
    public Specification<T> isNotNull(ColumnSFunction<T, ?> fn) {
        return isNotNull(ColumnUtil.getFieldName(fn));
    }


    /**
     * key值空
     *
     * @param key 键 (实体字段非数据库字段) (实体字段非数据库字段)
     * @return Specification
     */
    public Specification<T> isNull(String key) {
        return (root, query, builder) -> builder.isNull(root.get(key));
    }


    /**
     * key值空
     *
     * @param fn 实体字段
     * @return Specification
     */
    public Specification<T> isNull(ColumnSFunction<T, ?> fn) {
        return isNull(ColumnUtil.getFieldName(fn));
    }


    /**
     * 排序
     *
     * @param key  键 (实体字段非数据库字段) (实体字段非数据库字段)
     * @param desc true desc
     * @return Specification
     */
    public Specification<T> orderBy(String key, boolean desc) {

        return (root, query, builder) -> {
            Predicate restriction = query.orderBy(builder.asc(root.get(key))).getRestriction();
            if (desc) {
                restriction = query.orderBy(builder.desc(root.get(key))).getRestriction();
            }
            return restriction;
        };
    }

    /**
     * 排序
     *
     * @param fn   实体字段
     * @param desc true desc
     * @return Specification
     */
    public Specification<T> orderBy(ColumnSFunction<T, ?> fn, boolean desc) {
        return orderBy(ColumnUtil.getFieldName(fn), desc);
    }


    /**
     * 判断数据库字段为时间格式的大于(=)小于(=)
     * ps: 测试过人大金仓(timestamp)和mysql(timestamp)
     *
     * @param lessThan          true <= , false >=
     * @param key               实体字段
     * @param value             值 （java.util.Date）
     *                          <p>
     *                          DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
     *                          Date vale =  DateTime.parse(value,formatter).toDate();
     *                          </p>
     * @param ignoreNull        空值验证 [true: 空值不作为查询参数 false: 需要查询为空的数据]
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     */
    private <D> Specification<T> selectTimeLessGreater(boolean lessThan,
                                                       String key,
                                                       Date value,
                                                       boolean ignoreNull,
                                                       boolean ignoreNullEnhance) {
        if (ignoreNull && IObjects.isNull(value, ignoreNullEnhance)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> {
                Expression<Date> function = builder.function("to_date",
                        Date.class, root.get(key));
                if (lessThan) {
                    return builder.lessThanOrEqualTo(function, value);
                } else {
                    return builder.greaterThanOrEqualTo(function, value);
                }
            };
        }
    }


    /**
     * 获取一个 空的  Specification 对应，用于 else无选择时使用
     *
     * @return Specification
     */
    public Specification<T> specification() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
    }


    /**
     * 自定义Specification
     *
     * @return Specification
     */
    public Specification<T> specification(Specification<T> spec) {
        return spec;
    }


    /**
     * 根据注解组装  jpa动态查询
     *
     * @param annotation JpaSelectOperator 注解
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @return SimpleExpression
     */
    public Specification<T> specification(JpaSelectOperator annotation,
                                          String fieldName,
                                          Object fieldValue) {
        switch (annotation.operator()) {
            case NE:
                return notEq(fieldName, fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance());
            case LIKE:
                return like(fieldName, String.valueOf(fieldValue), annotation.ignoreNull(), annotation.ignoreNullEnhance());
            case NOTLIKE:
                return notLike(fieldName, String.valueOf(fieldValue), annotation.ignoreNull(), annotation.ignoreNullEnhance());
            case LLIKE:
                return lLike(fieldName, String.valueOf(fieldValue), annotation.ignoreNull(), annotation.ignoreNullEnhance());
            case RLIKE:
                return rLike(fieldName, String.valueOf(fieldValue), annotation.ignoreNull(), annotation.ignoreNullEnhance());
            case LT:
                return lt(fieldName, (Integer) fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance());
            case GT:
                return gt(fieldName, (Integer) fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance());
            case LTE:
                return le(fieldName, (Integer) fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance());
            case GTE:
                return ge(fieldName, (Integer) fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance());
            case BETWEEN:
                return between(fieldName, String.valueOf(fieldValue), annotation.ignoreNull(), annotation.ignoreNullEnhance());
            case ISNULL:
                return isNull(fieldName);
            case ISNOTNULL:
                return isNotNull(fieldName);
            case EQ:
            default:
                return eq(fieldName, fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance());
        }
    }

}

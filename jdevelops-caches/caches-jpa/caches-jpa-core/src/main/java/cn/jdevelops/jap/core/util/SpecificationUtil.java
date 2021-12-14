package cn.jdevelops.jap.core.util;

import cn.hutool.core.util.ReflectUtil;
import cn.jdevelops.jap.annotation.JpaSelectOperator;
import cn.jdevelops.jap.enums.SQLConnect;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * CustomerSpecs
 *
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
 * return (root, query, builder) -> builder.like(root.get("name"), "%用户%");
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
 *
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
        return (SpecificationUtil<T>)INSTANCE;
    }


    /**
     * CommUtils.getSelectBean（）的替代品
     * ps: 跟getSelectBean一摸一样。灵活不高
     *
     * @param bean 实体
     * @return Specification
     */
    public Specification<T> customer(T bean) {
        Field[] fields = ReflectUtil.getFields(bean.getClass());
        Specification<T> eq = (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        for (Field field : fields) {
            String fieldName = field.getName();
            JpaSelectOperator annotation = field.getAnnotation(JpaSelectOperator.class);
            Object fieldValue = ReflectUtil.getFieldValue(bean, field);
            if (null != annotation) {
                boolean ignoreNull = annotation.ignoreNull();
                if (Objects.equals(annotation.connect(), SQLConnect.OR)) {
                    eq.or(specification(annotation, fieldName, fieldValue, ignoreNull));
                } else {
                    eq.and(specification(annotation, fieldName, fieldValue, ignoreNull));
                }
            } else {
                eq.and(eq(fieldName, fieldValue, true));
            }
        }
        return eq;
    }


    /**
     * 等于
     *
     * @param key        键 (实体字段非数据库字段)
     * @param value      值
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return Specification
     */
    public Specification<T> eq(String key, Object value, boolean ignoreNull) {
        if (ignoreNull && ObjectUtils.isEmpty(value)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.equal(root.get(key), value);
        }

    }

    /**
     * 不相等
     *
     * @param key        键 (实体字段非数据库字段)
     * @param value      值
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return Specification
     */
    public Specification<T> notEq(String key, Object value, boolean ignoreNull) {
        if (ignoreNull && ObjectUtils.isEmpty(value)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.notEqual(root.get(key), value);
        }
    }

    /**
     * 大于
     *
     * @param key        键 (实体字段非数据库字段)
     * @param value      值
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return Specification
     */
    public Specification<T> gt(String key, Integer value, boolean ignoreNull) {
        if (ignoreNull && ObjectUtils.isEmpty(value)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.gt(root.get(key), value);
        }
    }

    /**
     * 大于等于
     *
     * @param key        键 (实体字段非数据库字段)
     * @param value      值
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return Specification
     */
    public Specification<T> ge(String key, Integer value, boolean ignoreNull) {
        if (ignoreNull && ObjectUtils.isEmpty(value)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.ge(root.get(key), value);
        }
    }

    /**
     * 小于
     *
     * @param key        键 (实体字段非数据库字段)
     * @param value      值
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return Specification
     */
    public Specification<T> lt(String key, Integer value, boolean ignoreNull) {
        if (ignoreNull && ObjectUtils.isEmpty(value)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.ge(root.get(key), value);
        }
    }

    /**
     * 小于等于
     *
     * @param key        键 (实体字段非数据库字段)
     * @param value      值
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return Specification
     */
    public Specification<T> le(String key, Integer value, boolean ignoreNull) {
        if (ignoreNull && ObjectUtils.isEmpty(value)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.le(root.get(key), value);
        }
    }

    /**
     * not
     *
     * @param key 键 (实体字段非数据库字段)
     * @return Specification
     */
    public Specification<T> not(String key) {
        return (root, query, builder) -> builder.not(root.get(key));
    }


    /**
     * between
     * v1 <= key <= v2
     *
     * @param key        键 (实体字段非数据库字段)
     * @param v1         值1
     * @param v2         值2
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return Specification
     */
    public Specification<T> between(String key, String v1, String v2, boolean ignoreNull) {
        if (ignoreNull && (ObjectUtils.isEmpty(v1) || ObjectUtils.isEmpty(v2))) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.between(root.get(key), v1, v2);
        }
    }


    /**
     * 模糊匹配
     *
     * @param key        键 (实体字段非数据库字段)
     * @param value      值
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return Specification
     */
    public Specification<T> like(String key, String value, boolean ignoreNull) {
        if (ignoreNull && ObjectUtils.isEmpty(value)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.like(root.get(key), "%" + value + "%");
        }
    }

    /**
     * 左模糊
     * %xxx
     *
     * @param key        键 (实体字段非数据库字段)
     * @param value      值
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return Specification
     */
    public Specification<T> lLike(String key, String value, boolean ignoreNull) {
        if (ignoreNull && ObjectUtils.isEmpty(value)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.like(root.get(key), "%" + value);
        }
    }


    /**
     * 右模糊
     * %xxx
     *
     * @param key        键 (实体字段非数据库字段)
     * @param value      值
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return Specification
     */
    public Specification<T> rLike(String key, String value, boolean ignoreNull) {
        if (ignoreNull && ObjectUtils.isEmpty(value)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.like(root.get(key), value + "%");
        }
    }

    /**
     * 模糊不匹配
     *
     * @param key        键 (实体字段非数据库字段)
     * @param value      值
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return Specification
     */
    public Specification<T> notLike(String key, String value, boolean ignoreNull) {
        if (ignoreNull && ObjectUtils.isEmpty(value)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        } else {
            return (root, query, builder) -> builder.notLike(root.get(key), "%" + value + "%");
        }
    }

    /**
     * 非空
     *
     * @param key 键 (实体字段非数据库字段)
     * @return Specification
     */
    public Specification<T> isNotNull(String key) {
        return (root, query, builder) -> builder.isNotNull(root.get(key));
    }

    /**
     * 空
     *
     * @param key 键 (实体字段非数据库字段) (实体字段非数据库字段)
     * @return Specification
     */
    public Specification<T> isNull(String key) {
        return (root, query, builder) -> builder.isNull(root.get(key));
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
     * 根据注解组装  jpa动态查询
     *
     * @param annotation JpaSelectOperator 注解
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @param ignoreNull true 空值不做查询，false 不忽略空
     * @return SimpleExpression
     */
    public Specification<T> specification(JpaSelectOperator annotation,
                                          String fieldName,
                                          Object fieldValue,
                                          boolean ignoreNull) {
        switch (annotation.operator()) {
            case NE:
                return notEq(fieldName, fieldValue, ignoreNull);
            case LIKE:
                return like(fieldName, String.valueOf(fieldValue), ignoreNull);
            case NOTLIKE:
                return notLike(fieldName, String.valueOf(fieldValue), ignoreNull);
            case LLIKE:
                return lLike(fieldName, String.valueOf(fieldValue), ignoreNull);
            case RLIKE:
                return rLike(fieldName, String.valueOf(fieldValue), ignoreNull);
            case LT:
                return lt(fieldName, (Integer) fieldValue, ignoreNull);
            case GT:
                return gt(fieldName, (Integer) fieldValue, ignoreNull);
            case LTE:
                return le(fieldName, (Integer) fieldValue, ignoreNull);
            case GTE:
                return ge(fieldName, (Integer) fieldValue, ignoreNull);
            case ISNULL:
                return isNull(fieldName);
            case ISNOTNULL:
                return isNotNull(fieldName);
            case EQ:
            default:
                return eq(fieldName, fieldValue, ignoreNull);
        }
    }

}

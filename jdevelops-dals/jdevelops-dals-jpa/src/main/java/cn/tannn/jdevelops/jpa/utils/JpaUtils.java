package cn.tannn.jdevelops.jpa.utils;

import cn.hutool.core.util.ReflectUtil;
import cn.tannn.jdevelops.annotations.jpa.JpaUpdate;
import cn.tannn.jdevelops.annotations.jpa.enums.SpecBuilderDateFun;
import cn.tannn.jdevelops.jpa.constant.SQLOperator;
import cn.tannn.jdevelops.result.bean.ColumnSFunction;
import cn.tannn.jdevelops.result.bean.ColumnUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;


import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
     *
     * @param root      {@link Root}
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
     *
     * @param root      {@link Root}
     * @param builder   {@link CriteriaBuilder}}
     * @param function  {@link SpecBuilderDateFun}}
     * @param fieldName 字段名,可以处理 `address.name` 这种key
     * @see cn.tannn.jdevelops.jpa.select.EnhanceSpecification#beanWhere(Object)
     * @return Expression
     */
    public static Expression str2Path(Root<?> root, CriteriaBuilder builder, SpecBuilderDateFun function, String fieldName) {
        if (null != function && !function.equals(SpecBuilderDateFun.NULL)) {
            return JpaUtils.functionTimeFormat(function, root, builder, fieldName);
        }
        return str2Path(root, fieldName);
    }


    /**
     * @param operator   {@link SQLOperator}
     * @param builder    {@link CriteriaBuilder}
     * @param value      添加值
     * @param expression 添加表达式 {@link Root#get(String)}
     * @see cn.tannn.jdevelops.jpa.service.J2Service#deleteEq(String, Object)
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
                return expression.in(value);
            case NOTIN:
                return builder.not(expression.in(value));
            default:
                LOG.warn("占不支持的表达式: {}", operator);
                return null;
        }
    }

    /**
     * 更新数据
     * <p> 此方法不会用到 jpa的审计功能 </p>
     *
     * @param updateBean    需要更新的数据集
     * @param entityManager {@link EntityManager}
     * @param domainClass   entity的真实对象
     * @param operator      判断表达式  {@link SQLOperator}（ 等于，小于，模糊 ...)
     *                      <p> 1.  [根据 operator方式传值：in between 这种就传多个值，其他的根据情况而定，比如ISNULL这种就不用传值]
     * @param uniqueKey     指定 bean 用作更新的条件字段名[实体里的字段]
     *                      <p> 2. 为空的话
     *                      <p> 2.1 首先判断 bean里是个否标注了{@link JpaUpdate#unique()},标注了就使用他做条件
     *                      <p> 2.2 其次如何自定义的注解为标注那就回去拿实体的 {@link Id} 标注的字段名
     * @param <T>           updateBean
     * @param <B>           domainClass
     * @return int 0:表示没有更新
     * @see cn.tannn.jdevelops.jpa.service.J2Service#update(Object, SQLOperator, String...)
     */

    public static <T, B> int updateBean(T updateBean
            , EntityManager entityManager
            , Class<B> domainClass
            , SQLOperator operator
            , String... uniqueKey) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<B> update = criteriaBuilder.createCriteriaUpdate(domainClass);
        Root<B> updateRoot = update.from(domainClass);

        AtomicReference<String> jpaUpdateUnique = new AtomicReference<>();
        // 获取字段
        Field[] fields = ReflectUtil.getFields(updateBean.getClass(), field -> {
            // 忽略字段
            if ("serialVersionUID".equalsIgnoreCase(field.getName())) {
                return false;
            }
            JpaUpdate ignoreField = field.getAnnotation(JpaUpdate.class);
            if (ignoreField == null) {
                return true;
            }
            if (ignoreField.unique()) {
                jpaUpdateUnique.set(field.getName());
            }
            return !ignoreField.ignore();
        });

        // 获取主键名
        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<B> entityType = metamodel.entity(domainClass);

        Predicate where;
        // 忽略查询字段
        String ignoreField;
        if (uniqueKey == null || uniqueKey.length == 0) {
            if (!jpaUpdateUnique.get().isEmpty()) {
                ignoreField = jpaUpdateUnique.get();
            } else {
                SingularAttribute<? super B, ?> id = entityType.getId(entityType.getIdType().getJavaType());
                ignoreField = id.getName();
            }
            // 根据主键更新
            where = criteriaBuilder.equal(updateRoot.get(ignoreField)
                    , ReflectUtil.getFieldValue(updateBean, ignoreField));
        } else {
            // 根据传入的唯一key键
            ignoreField = uniqueKey[0];
            where = JpaUtils.getPredicate(operator
                    , criteriaBuilder
                    , updateRoot.get(ignoreField)
                    , ReflectUtil.getFieldValue(updateBean, ignoreField));
        }

        // 更新字段
        for (Field field : fields) {
            // 字段名
            String fieldName = field.getName();
            if (ignoreField.equalsIgnoreCase(fieldName)) {
                continue;
            }
            // 字段值
            Object fieldValue = ReflectUtil.getFieldValue(updateBean, field);

            JpaUpdate jpaUpdate = field.getAnnotation(JpaUpdate.class);

            if (null != jpaUpdate && jpaUpdate.autoTime() && field.getType().equals(LocalDateTime.class)) {
                // 强制更新时间
                update.set(updateRoot.get(fieldName), LocalDateTime.now());
            } else {
                if (fieldValue != null) {
                    // 设置更新值
                    update.set(updateRoot.get(fieldName), fieldValue);
                }
            }
        }
        if (where == null) {
            return 0;
        }
        // 应用更新的条件
        update.where(where);
        // 执行更新
        return entityManager.createQuery(update).executeUpdate();
    }


    /**
     * List<Predicate> -> Predicate
     *
     * @param criteriaBuilder {@link CriteriaBuilder}
     * @param predicates List
     * @return Predicate
     * @see cn.tannn.jdevelops.jpa.select.EnhanceSpecification#where
     */
    public static Predicate combinePredicates(CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (predicates == null || predicates.isEmpty()) {
            throw new IllegalArgumentException("Predicate list is empty or null");
        }
        Predicate result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            Predicate current = predicates.get(i);

            if (result.getOperator() == Predicate.BooleanOperator.AND
                    && current.getOperator() == Predicate.BooleanOperator.AND) {
                // 前后都是and
                result = criteriaBuilder.and(result, current);
            } else if (result.getOperator() == Predicate.BooleanOperator.OR
                    && current.getOperator() == Predicate.BooleanOperator.OR) {
                // 前后都是or
                result = criteriaBuilder.or(result, current);
            } else if (result.getOperator() == Predicate.BooleanOperator.OR
                    && current.getOperator() == Predicate.BooleanOperator.AND) {
                // 前面 or 后面 and,  result, current 用result的or连接 并重置为current的and
                result = criteriaBuilder.and(criteriaBuilder.or(result, current));
            } else if (result.getOperator() == Predicate.BooleanOperator.AND
                    && current.getOperator() == Predicate.BooleanOperator.OR) {
                // 跟第三个判断反着来
                result = criteriaBuilder.or(criteriaBuilder.and(result, current));
            } else {
                // 默认and
                result = criteriaBuilder.and(result, current);
            }
        }
        return result;
    }


    /**
     * 获取 specification 里的sql
     * <p>  不要瞎使用，我测试用的方法
     * @param entityManager EntityManager
     * @param entityClass entityClass
     * @param specification Specification
     * @return sql
     * @param <T> entityClass
     */
    public static <T> String toSql(EntityManager entityManager, Class<T> entityClass, Specification<T> specification) {
        // 创建CriteriaBuilder和CriteriaQuery
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        // 应用Specification
        criteriaQuery.where(specification.toPredicate(root, criteriaQuery, criteriaBuilder));
        // 创建查询对象
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        // 获取底层的Hibernate Query对象
        Query hibernateQuery = query.unwrap(Query.class);
        // 返回生成的SQL字符串
        return hibernateQuery.getQueryString();
    }
}

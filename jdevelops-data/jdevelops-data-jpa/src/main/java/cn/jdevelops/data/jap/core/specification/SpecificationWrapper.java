package cn.jdevelops.data.jap.core.specification;


import cn.jdevelops.api.result.util.bean.ColumnSFunction;
import cn.jdevelops.api.result.util.bean.ColumnUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Specification 包装类
 *  valueNotNull: false不添加值等于空的条件
 *  selectKey: 条件的key
 *  value: 条件的值
 * @author tan
 * @date 2023-03-24 10:59:17
 */
@Getter
@Setter
@NoArgsConstructor
public class SpecificationWrapper<B> {

    /**
     * 查询对象  （key = value ...）
     */
    private Root<B> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder builder;


    /**
     * 连接对象  （or  and ...）
     */
    private List<Predicate> predicates = new ArrayList<>();

    private static final String SEPARATOR = ".";

    public SpecificationWrapper(Root<B> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.root = root;
        this.query = query;
        this.builder = builder;
    }

    /**
     * or
     *
     * @param operator 操作
     * @return SpecificationWrapper
     */
    public SpecificationWrapper<B> or(Consumer<SpecificationWrapper<B>> operator) {
        return this.newWrapper(false, operator);
    }


    /**
     * and
     *
     * @param operator 操作
     * @return SpecificationWrapper
     */
    public SpecificationWrapper<B> and(Consumer<SpecificationWrapper<B>> operator) {
        return this.newWrapper(true, operator);
    }


    /**
     * 构建
     *
     * @param isConnect true用and(默认), fales用or
     * @param operator  操作
     * @return SpecificationWrapper
     */
    public SpecificationWrapper<B> newWrapper(boolean isConnect, Consumer<SpecificationWrapper<B>> operator) {
        SpecificationWrapper<B> specification = new SpecificationWrapper<>(root, query, builder);
        CriteriaBuilder newBuilder = specification.getBuilder();
        Predicate predicate = isConnect ? newBuilder.conjunction() : newBuilder.disjunction();
        operator.accept(specification);
        predicate.getExpressions().addAll(specification.getPredicates());
        predicates.add(predicate);
        return this;
    }

    // ================================== 查询空值 ==================================


    /**
     * 查询非空值
     */
    public SpecificationWrapper<B> isNull(boolean valueNotNull, ColumnSFunction<B, ?> selectKey) {
        return valueNotNull ? handle(selectKey, this::isNull) : this;
    }

    /**
     * 查询空值
     */
    public SpecificationWrapper<B> isNull(boolean valueNotNull, String selectKey) {
        return valueNotNull ? handle(selectKey, this::isNull) : this;
    }

    /**
     * 查询空值
     */
    public SpecificationWrapper<B> isNull(String selectKey) {
        return handle(selectKey, this::isNull);
    }

    /**
     * 查询空值
     */
    public SpecificationWrapper<B> isNull(ColumnSFunction<B, ?> selectKey) {
        return handle(selectKey, this::isNull);
    }

    /**
     * 查询空值
     */
    public SpecificationWrapper<B> isNull(Expression<?> x) {
        predicates.add(builder.isNull(x));
        return this;
    }


    // ================================== 查询非空值 ==================================


    /**
     * 查询非空值
     */
    public SpecificationWrapper<B> isNotNull(boolean valueNotNull, ColumnSFunction<B, ?> selectKey) {
        return valueNotNull ? handle(selectKey, this::isNotNull) : this;
    }

    /**
     * 查询非空值
     */
    public SpecificationWrapper<B> isNotNull(boolean valueNotNull, String selectKey) {
        return valueNotNull ? handle(selectKey, this::isNotNull) : this;
    }


    /**
     * 查询非空值
     */
    public SpecificationWrapper<B> isNotNull(String selectKey) {
        return handle(selectKey, this::isNotNull);
    }

    /**
     * 查询非空值
     */
    public SpecificationWrapper<B> isNotNull(ColumnSFunction<B, ?> selectKey) {
        return handle(selectKey, this::isNotNull);
    }


    /**
     * 查询非空值
     */
    public SpecificationWrapper<B> isNotNull(Expression<?> x) {
        predicates.add(builder.isNotNull(x));
        return this;
    }


    // ================================== 等于 ==================================

    /**
     * 等于
     */
    public SpecificationWrapper<B> eq(boolean valueNotNull, String selectKey, Object value) {
        return valueNotNull ? this.eq(selectKey, value) : this;
    }

    /**
     * 等于
     */
    public SpecificationWrapper<B> eq(boolean valueNotNull, ColumnSFunction<B, ?> selectKey, Object value) {
        return valueNotNull ? this.eq(selectKey, value) : this;
    }

    /**
     * 等于
     */
    public SpecificationWrapper<B> eq(ColumnSFunction<B, ?> selectKey, Object value) {
        return handle(selectKey, e -> this.eq(e, value));
    }

    /**
     * 等于
     */
    public SpecificationWrapper<B> eq(String selectKey, Object value) {
        return handle(selectKey, e -> this.eq(e, value));
    }

    /**
     * 等于
     */
    public SpecificationWrapper<B> eq(Expression<?> x, Object value) {
        predicates.add(builder.equal(x, value));
        return this;
    }


    // ================================== 不等于 ==================================

    /**
     * 不等于
     */
    public SpecificationWrapper<B> ne(boolean valueNotNull, String selectKey, Object value) {
        return valueNotNull ? this.ne(selectKey, value) : this;
    }

    /**
     * 不等于
     */
    public SpecificationWrapper<B> ne(boolean valueNotNull, ColumnSFunction<B, ?> selectKey, Object value) {
        return valueNotNull ? this.ne(selectKey, value) : this;
    }

    /**
     * 不等于
     */
    public SpecificationWrapper<B> ne(String selectKey, Object value) {
        return handle(selectKey, e -> this.ne(e, value));
    }

    /**
     * 不等于
     */
    public SpecificationWrapper<B> ne(ColumnSFunction<B, ?> selectKey, Object value) {
        return handle(selectKey, e -> this.ne(e, value));
    }

    /**
     * 不等于
     */
    public SpecificationWrapper<B> ne(Expression<?> x, Object value) {
        predicates.add(builder.notEqual(x, value));
        return this;
    }

    // ================================== 模糊查询 ==================================

    /**
     * 模糊查询的基方法，不要使用因为没有百分号
     */
    public SpecificationWrapper<B> like(boolean valueNotNull, String selectKey, String value) {
        return valueNotNull ? this.like(selectKey, value) : this;
    }


    /**
     * 模糊查询的基方法，不要使用因为没有百分号
     */
    public SpecificationWrapper<B> like(String selectKey, String value) {
        return handle(selectKey, e -> this.like(e, value));
    }

    /**
     * 模糊查询的基方法，不要使用因为没有百分号
     */
    public SpecificationWrapper<B> like(ColumnSFunction<B, ?> selectKey, String value) {
        return handle(selectKey, e -> this.like(e, value));
    }

    /**
     * 模糊查询的基方法，不要使用因为没有百分号
     */
    public SpecificationWrapper<B> like(Expression<String> path, String value) {
        predicates.add(builder.like(path, value));
        return this;
    }

    /**
     * 右模糊（值为空不查询）
     */
    public SpecificationWrapper<B> rlike(boolean valueNotNull, String selectKey, String value) {
        return valueNotNull ? this.rlike(selectKey, value) : this;
    }

    /**
     * 右模糊（值为空不查询）
     */
    public SpecificationWrapper<B> rlike(boolean valueNotNull, ColumnSFunction<B, ?> selectKey, String value) {
        return valueNotNull ? this.rlike(selectKey, value) : this;
    }

    /**
     * 右模糊（value%）
     */
    public SpecificationWrapper<B> rlike(String selectKey, String value) {
        this.like(selectKey, value + "%");
        return this;
    }

    /**
     * 右模糊（value%）
     */
    public SpecificationWrapper<B> rlike(ColumnSFunction<B, ?> selectKey, String value) {
        this.like(selectKey, value + "%");
        return this;
    }

    /**
     * 左模糊（值为空不查询）
     */
    public SpecificationWrapper<B> llike(boolean valueNotNull, String selectKey, String value) {
        return valueNotNull ? this.llike(selectKey, value) : this;
    }

    /**
     * 左模糊（值为空不查询）
     */
    public SpecificationWrapper<B> llike(boolean valueNotNull, ColumnSFunction<B, ?> selectKey, String value) {
        return valueNotNull ? this.llike(selectKey, value) : this;
    }

    /**
     * 左模糊（%value）
     */
    public SpecificationWrapper<B> llike(String selectKey, String value) {
        this.like(selectKey, "%" + value);
        return this;
    }

    /**
     * 左模糊（%value）
     */
    public SpecificationWrapper<B> llike(ColumnSFunction<B, ?> selectKey, String value) {
        this.like(selectKey, "%" + value);
        return this;
    }

    /**
     * 全模糊（值为空不查询）
     */
    public SpecificationWrapper<B> likes(boolean valueNotNull, String selectKey, String value) {
        return valueNotNull ? this.likes(selectKey, value) : this;
    }

    /**
     * 全模糊（值为空不查询）
     */
    public SpecificationWrapper<B> likes(boolean valueNotNull, ColumnSFunction<B, ?> selectKey, String value) {
        return valueNotNull ? this.likes(selectKey, value) : this;
    }

    /**
     * 全模糊（%value%）
     */
    public SpecificationWrapper<B> likes(String selectKey, String value) {
        this.like(selectKey, "%" + value + "%");
        return this;
    }

    /**
     * 全模糊（%value%）
     */
    public SpecificationWrapper<B> likes(ColumnSFunction<B, ?> selectKey, String value) {
        this.like(selectKey, "%" + value + "%");
        return this;
    }


    /**
     * not like
     */
    public SpecificationWrapper<B> nlike(boolean valueNotNull, String selectKey, String value) {
        return valueNotNull ? this.nlike(selectKey, value) : this;
    }

    /**
     * not like
     */
    public SpecificationWrapper<B> nlike(boolean valueNotNull, ColumnSFunction<B, ?> selectKey, String value) {
        return valueNotNull ? this.nlike(selectKey, value) : this;
    }

    /**
     * not like
     */
    public SpecificationWrapper<B> nlike(String selectKey, String value) {
        return handle(selectKey, e -> this.nlike(e, value));
    }

    /**
     * not like
     */
    public SpecificationWrapper<B> nlike(ColumnSFunction<B, ?> selectKey, String value) {
        return handle(selectKey, e -> this.nlike(e, value));
    }

    /**
     * not like
     */
    public SpecificationWrapper<B> nlike(Expression<String> path, String value) {
        predicates.add(builder.notLike(path, value));
        return this;
    }


    // ================================== 大于等于 ==================================

    /**
     * 大于等于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> ge(boolean valueNotNull, String selectKey, Y value) {
        return valueNotNull ? this.ge(selectKey, value) : this;
    }

    /**
     * 大于等于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> ge(boolean valueNotNull, ColumnSFunction<B, ?> selectKey, Y value) {
        return valueNotNull ? this.ge(selectKey, value) : this;
    }


    /**
     * 大于等于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> ge(String selectKey, Y value) {
        return handle(selectKey, e -> this.ge(e, value));
    }


    /**
     * 大于等于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> ge(ColumnSFunction<B, ?> selectKey, Y value) {
        return handle(selectKey, e -> this.ge(e, value));
    }

    /**
     * 大于等于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> ge
    (Expression<? extends Y> path, Y value) {
        predicates.add(builder.greaterThanOrEqualTo(path, value));
        return this;
    }

    // ================================== 小于等于 ==================================

    /**
     * 小于等于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> le(boolean valueNotNull, String selectKey, Y value) {
        return valueNotNull ? this.le(selectKey, value) : this;
    }

    /**
     * 小于等于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> le(boolean valueNotNull, ColumnSFunction<B, ?> selectKey, Y value) {
        return valueNotNull ? this.le(selectKey, value) : this;
    }


    /**
     * 小于等于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> le(String selectKey, Y value) {
        return handle(selectKey, e -> this.le(e, value));
    }


    /**
     * 小于等于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> le(ColumnSFunction<B, ?> selectKey, Y value) {
        return handle(selectKey, e -> this.le(e, value));
    }


    /**
     * 小于等于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> le
    (Expression<? extends Y> path, Y value) {
        predicates.add(builder.lessThanOrEqualTo(path, value));
        return this;
    }

    // ================================== 大于 ==================================

    /**
     * 大于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> gt(boolean valueNotNull, String selectKey, Y value) {
        return valueNotNull ? this.gt(selectKey, value) : this;
    }

    /**
     * 大于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> gt(boolean valueNotNull, ColumnSFunction<B, ?> selectKey, Y value) {
        return valueNotNull ? this.gt(selectKey, value) : this;
    }

    /**
     * 大于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> gt(String selectKey, Y value) {
        return handle(selectKey, e -> this.gt(e, value));
    }


    /**
     * 大于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> gt(ColumnSFunction<B, ?> selectKey, Y value) {
        return handle(selectKey, e -> this.gt(e, value));
    }

    /**
     * 大于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> gt
    (Expression<? extends Y> path, Y value) {
        predicates.add(builder.greaterThan(path, value));
        return this;
    }

    // ================================== 小于 ==================================

    /**
     * 小于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> lt(boolean valueNotNull, String selectKey, Y value) {
        return valueNotNull ? this.lt(selectKey, value) : this;
    }

    /**
     * 小于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> lt(boolean valueNotNull, ColumnSFunction<B, ?> selectKey, Y value) {
        return valueNotNull ? this.lt(selectKey, value) : this;
    }

    /**
     * 小于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> lt(String selectKey, Y value) {
        return handle(selectKey, e -> this.lt(e, value));
    }


    /**
     * 小于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> lt(ColumnSFunction<B, ?> selectKey, Y value) {
        return handle(selectKey, e -> this.lt(e, value));
    }

    /**
     * 小于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> lt
    (Expression<? extends Y> path, Y value) {
        predicates.add(builder.lessThan(path, value));
        return this;
    }

    // ================================== in ==================================

    /**
     * in
     */
    public SpecificationWrapper<B> in(boolean valueNotNull, String selectKey, Object... value) {
        return valueNotNull ? in(selectKey, value) : this;
    }

    /**
     * in
     */
    public SpecificationWrapper<B> in(boolean valueNotNull, ColumnSFunction<B, ?> selectKey, Object... value) {
        return valueNotNull ? in(selectKey, value) : this;
    }

    /**
     * in
     */
    public SpecificationWrapper<B> in(String selectKey, Object... value) {
        return handle(selectKey, e -> this.in(e, value));
    }

    /**
     * in
     */
    public SpecificationWrapper<B> in(ColumnSFunction<B, ?> selectKey, Object... value) {
        return handle(selectKey, e -> this.in(e, value));
    }

    /**
     * in
     */
    public SpecificationWrapper<B> in(boolean valueNotNull, String selectKey, Collection<?> value) {
        return valueNotNull ? in(selectKey, value) : this;
    }


    /**
     * in
     */
    public SpecificationWrapper<B> in(boolean valueNotNull, ColumnSFunction<B, ?> selectKey, Collection<?> value) {
        return valueNotNull ? in(selectKey, value) : this;
    }

    /**
     * in
     */
    public SpecificationWrapper<B> in(String selectKey, Collection<?> value) {
        return this.in(selectKey, value.toArray());
    }


    /**
     * in
     */
    public SpecificationWrapper<B> in(ColumnSFunction<B, ?> selectKey, Collection<?> value) {
        return this.in(selectKey, value.toArray());
    }


    /**
     * in
     */
    public <U> SpecificationWrapper<B> in(Expression<? extends U> expression, Object... value) {
        predicates.add(expression.in(value));
        return this;
    }

    // ================================== not in ==================================

    /**
     * not in
     */
    public SpecificationWrapper<B> notIn(boolean valueNotNull, String selectKey, Collection<?> value) {
        return valueNotNull ? notIn(selectKey, value) : this;
    }

    /**
     * not in
     */
    public SpecificationWrapper<B> notIn(boolean valueNotNull, ColumnSFunction<B, ?> selectKey, Collection<?> value) {
        return valueNotNull ? notIn(selectKey, value) : this;
    }

    /**
     * not in
     */
    public SpecificationWrapper<B> notIn(String selectKey, Collection<?> value) {
        return handle(selectKey, e -> this.notIn(e, value.toArray()));
    }

    /**
     * not in
     */
    public SpecificationWrapper<B> notIn(ColumnSFunction<B, ?> selectKey, Collection<?> value) {
        return handle(selectKey, e -> this.notIn(e, value.toArray()));
    }

    /**
     * not in
     */
    public SpecificationWrapper<B> notIn(boolean valueNotNull, String selectKey, Object... value) {
        return valueNotNull ? notIn(selectKey, value) : this;
    }


    /**
     * not in
     */
    public SpecificationWrapper<B> notIn(boolean valueNotNull, ColumnSFunction<B, ?> selectKey, Object... value) {
        return valueNotNull ? notIn(selectKey, value) : this;
    }

    /**
     * not in
     */
    public SpecificationWrapper<B> notIn(String selectKey, Object... value) {
        return handle(selectKey, e -> this.notIn(e, value));
    }

    /**
     * not in
     */
    public SpecificationWrapper<B> notIn(ColumnSFunction<B, ?> selectKey, Object... value) {
        return handle(selectKey, e -> this.notIn(e, value));
    }

    /**
     * not in
     */
    public <U> SpecificationWrapper<B> notIn(Expression<? extends U> expression, Object... value) {
        predicates.add(expression.in(value).not());
        return this;
    }

    // ================================== between ==================================

    /**
     * 之间 包头包尾[闭区间]
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> between(boolean valueNotNull,
                                                                             String selectKey, Y start, Y end) {
        return valueNotNull ? between(selectKey, start, end) : this;
    }

    /**
     * 之间 包头包尾[闭区间]
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> between(boolean valueNotNull,
                                                                             ColumnSFunction<B, ?> selectKey, Y start, Y end) {
        return valueNotNull ? between(selectKey, start, end) : this;
    }


    /**
     * 之间 包头包尾[闭区间]
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> between(String selectKey, Y start, Y end) {
        return handle(selectKey, e -> this.between(e, start, end));
    }


    /**
     * 之间 包头包尾[闭区间]
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> between(ColumnSFunction<B, ?> selectKey, Y start, Y end) {
        return handle(selectKey, e -> this.between(e, start, end));
    }


    /**
     * 之间 包头包尾[闭区间]
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> between(Expression<? extends Y> path,
                                                                             Y start, Y end) {
        predicates.add(builder.between(path, start, end));
        return this;
    }

    // ================================== join ==================================

    /**
     * join
     */
    public <U> Join<B, U> leftJoin(String fieldName) {
        return root.join(fieldName, JoinType.LEFT);
    }


    /**
     * 主方法，处理器
     */
    public SpecificationWrapper<B> handle(String selectKey, Consumer<Path> action) {
        Path<?> path;
        if (selectKey.contains(SEPARATOR)) {
            String[] arr = selectKey.split("\\" + SEPARATOR);
            path = this.leftJoin(arr[0]).get(arr[1]);
        } else {
            path = this.root.get(selectKey);
        }
        action.accept(path);
        return this;
    }

    /**
     * 主方法，处理器
     */
    public SpecificationWrapper<B> handle(ColumnSFunction<B, ?> selectKey, Consumer<Path> action) {
        return handle(ColumnUtil.getFieldName(selectKey),action);
    }
}

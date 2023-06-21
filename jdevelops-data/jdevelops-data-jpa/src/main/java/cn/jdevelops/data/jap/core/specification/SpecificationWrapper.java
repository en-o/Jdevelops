package cn.jdevelops.data.jap.core.specification;


import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Specification 包装类
 * valueNotNull: false不添加值等于空的条件
 * selectKey: 条件的key
 * value: 条件的值
 *
 * @author tan
 * @date 2023-03-24 10:59:17
 */

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
     * 查询空值
     */
    public SpecificationWrapper<B> isNull(String selectKey) {
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
    public SpecificationWrapper<B> isNotNull(String selectKey) {
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
        return valueNotNull ? handle(selectKey, e -> this.eq(e, value)) : this;
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
        return valueNotNull ? handle(selectKey, e -> this.ne(e, value)) : this;
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
        return valueNotNull ? handle(selectKey, e -> this.like(e, value)) : this;
    }


    /**
     * 模糊查询的基方法，不要使用因为没有百分号（value）
     */
    public SpecificationWrapper<B> like(Expression<String> path, String value) {
        predicates.add(builder.like(path, value));
        return this;
    }

    /**
     * 右模糊（value%）
     */
    public SpecificationWrapper<B> rlike(boolean valueNotNull, String selectKey, String value) {
        return this.like(valueNotNull, selectKey, value + "%");
    }


    /**
     *左模糊（%value）
     */
    public SpecificationWrapper<B> rlike(Expression<String> path, String value) {
        return this.like(path, value + "%");
    }


    /**
     * 左模糊（%value）
     */
    public SpecificationWrapper<B> llike(boolean valueNotNull, String selectKey, String value) {
        return this.like(valueNotNull, selectKey, "%" + value);
    }

    /**
     *左模糊（%value）
     */
    public SpecificationWrapper<B> llike(Expression<String> path, String value) {
        return this.like(path, "%" + value);
    }


    /**
     * 全模糊（%value%）
     */
    public SpecificationWrapper<B> likes(boolean valueNotNull, String selectKey, String value) {
        return this.like(valueNotNull, selectKey, "%" + value + "%");
    }


    /**
     * 全模糊（%value%）
     */
    public SpecificationWrapper<B> likes(Expression<String> path, String value) {
        return this.like(path, "%" + value + "%");
    }


    /**
     * not like
     */
    public SpecificationWrapper<B> nlike(boolean valueNotNull, String selectKey, String value) {
        return valueNotNull ? handle(selectKey, e -> this.nlike(e, value)) : this;
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
        return valueNotNull ? handle(selectKey, e -> this.ge(e, value)) : this;
    }

    /**
     * 大于等于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> ge(Expression<? extends Y> path, Y value) {
        predicates.add(builder.greaterThanOrEqualTo(path, value));
        return this;
    }

    // ================================== 小于等于 ==================================

    /**
     * 小于等于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> le(boolean valueNotNull, String selectKey, Y value) {
        return valueNotNull ? handle(selectKey, e -> this.le(e, value)) : this;
    }


    /**
     * 小于等于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> le(Expression<? extends Y> path, Y value) {
        predicates.add(builder.lessThanOrEqualTo(path, value));
        return this;
    }

    // ================================== 大于 ==================================

    /**
     * 大于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> gt(boolean valueNotNull, String selectKey, Y value) {
        return valueNotNull ? handle(selectKey, e -> this.gt(e, value)) : this;
    }


    /**
     * 大于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> gt(Expression<? extends Y> path, Y value) {
        predicates.add(builder.greaterThan(path, value));
        return this;
    }

    // ================================== 小于 ==================================

    /**
     * 小于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> lt(boolean valueNotNull, String selectKey, Y value) {
        return valueNotNull ? handle(selectKey, e -> this.lt(e, value)) : this;
    }


    /**
     * 小于
     */
    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> lt(Expression<? extends Y> path, Y value) {
        predicates.add(builder.lessThan(path, value));
        return this;
    }

    // ================================== in ==================================

    /**
     * in
     */
    public SpecificationWrapper<B> in(boolean valueNotNull, String selectKey, Collection<?> value) {
        return this.in(valueNotNull, selectKey, value.toArray());
    }

    /**
     * in
     */
    public SpecificationWrapper<B> in(boolean valueNotNull, String selectKey, Object... value) {
        return valueNotNull ? handle(selectKey, e -> this.in(e, value)) : this;
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
        return this.notIn(valueNotNull, selectKey, value.toArray());
    }

    /**
     * not in
     */
    public SpecificationWrapper<B> notIn(boolean valueNotNull, String selectKey, Object... value) {
        return valueNotNull ? handle(selectKey, e -> this.notIn(e, value)) : this;
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
        return valueNotNull ? handle(selectKey, e -> this.between(e, start, end)) : this;
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
     *
     * @param selectKey key
     * @param action    Consumer<Path>
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


    public Root<B> getRoot() {
        return root;
    }

    public void setRoot(Root<B> root) {
        this.root = root;
    }

    public CriteriaQuery<?> getQuery() {
        return query;
    }

    public void setQuery(CriteriaQuery<?> query) {
        this.query = query;
    }

    public CriteriaBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(CriteriaBuilder builder) {
        this.builder = builder;
    }

    public List<Predicate> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<Predicate> predicates) {
        this.predicates = predicates;
    }

    public SpecificationWrapper() {
    }

    public SpecificationWrapper(Root<B> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates) {
        this.root = root;
        this.query = query;
        this.builder = builder;
        this.predicates = predicates;
    }
}

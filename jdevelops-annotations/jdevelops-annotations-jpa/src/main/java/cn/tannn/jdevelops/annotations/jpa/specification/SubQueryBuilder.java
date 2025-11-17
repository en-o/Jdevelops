package cn.tannn.jdevelops.annotations.jpa.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * 子查询构建器
 */
public  class SubQueryBuilder<S> {
    private final Root<S> root;
    private final CriteriaBuilder builder;
    private final List<Predicate> predicates = new ArrayList<>();

    public SubQueryBuilder( Root<S> root, CriteriaBuilder builder) {
        this.root = root;
        this.builder = builder;
    }

    /**
     * 等于条件
     */
    public SubQueryBuilder<S> eq(String fieldName, Object value) {
        if (value != null) {
            predicates.add(builder.equal(root.get(fieldName), value));
        }
        return this;
    }

    /**
     * 不等于条件
     */
    public SubQueryBuilder<S> ne(String fieldName, Object value) {
        if (value != null) {
            predicates.add(builder.notEqual(root.get(fieldName), value));
        }
        return this;
    }

    /**
     * 模糊查询
     */
    public SubQueryBuilder<S> like(String fieldName, String value) {
        if (value != null) {
            predicates.add(builder.like(root.get(fieldName), "%" + value + "%"));
        }
        return this;
    }
    /**
     * 模糊查询 没有 %%
     */
    public SubQueryBuilder<S> likeCustom(String fieldName, String value) {
        if (value != null) {
            predicates.add(builder.like(root.get(fieldName), value ));
        }
        return this;
    }

    /**
     * IN 条件
     */
    public SubQueryBuilder<S> in(String fieldName, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            predicates.add(root.get(fieldName).in(values));
        }
        return this;
    }

    /**
     * IN 条件
     */
    public SubQueryBuilder<S> in(String fieldName, Object... values) {
        if (values != null && values.length > 0) {
            predicates.add(root.get(fieldName).in(values));
        }
        return this;
    }

    /**
     * 大于等于
     */
    public <Y extends Comparable<? super Y>> SubQueryBuilder<S> ge(String fieldName, Y value) {
        if (value != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(fieldName), value));
        }
        return this;
    }

    /**
     * 小于等于
     */
    public <Y extends Comparable<? super Y>> SubQueryBuilder<S> le(String fieldName, Y value) {
        if (value != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(fieldName), value));
        }
        return this;
    }

    /**
     * 大于
     */
    public <Y extends Comparable<? super Y>> SubQueryBuilder<S> gt(String fieldName, Y value) {
        if (value != null) {
            predicates.add(builder.greaterThan(root.get(fieldName), value));
        }
        return this;
    }

    /**
     * 小于
     */
    public <Y extends Comparable<? super Y>> SubQueryBuilder<S> lt(String fieldName, Y value) {
        if (value != null) {
            predicates.add(builder.lessThan(root.get(fieldName), value));
        }
        return this;
    }

    /**
     * 空值检查
     */
    public SubQueryBuilder<S> isNull(String fieldName) {
        predicates.add(builder.isNull(root.get(fieldName)));
        return this;
    }

    /**
     * 非空值检查
     */
    public SubQueryBuilder<S> isNotNull(String fieldName) {
        predicates.add(builder.isNotNull(root.get(fieldName)));
        return this;
    }

    /**
     * 自定义条件
     */
    public SubQueryBuilder<S> custom(Function<Root<S>, Predicate> predicateBuilder) {
        if (predicateBuilder != null) {
            Predicate predicate = predicateBuilder.apply(root);
            if (predicate != null) {
                predicates.add(predicate);
            }
        }
        return this;
    }

    public List<Predicate> getPredicates() {
        return predicates;
    }

    public Root<S> getRoot() {
        return root;
    }

    public CriteriaBuilder getBuilder() {
        return builder;
    }
}

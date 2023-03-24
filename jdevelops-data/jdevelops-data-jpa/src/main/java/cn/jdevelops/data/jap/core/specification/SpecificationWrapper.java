package cn.jdevelops.data.jap.core.specification;



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
     * @param operator 操作
     * @return SpecificationWrapper
     */
    public SpecificationWrapper<B> or(Consumer<SpecificationWrapper<B>> operator) {
        return this.newWrapper(false, operator);
    }


    /**
     * and
     * @param operator 操作
     * @return SpecificationWrapper
     */
    public SpecificationWrapper<B> and(Consumer<SpecificationWrapper<B>> operator) {
        return this.newWrapper(true, operator);
    }


    /**
     * 构建
     * @param isConnect true用and(默认), fales用or
     * @param operator 操作
     * @return
     */
    public SpecificationWrapper<B> newWrapper(boolean isConnect, Consumer<SpecificationWrapper<B>> operator) {
        SpecificationWrapper<B> specification = new SpecificationWrapper<>(root, query, builder);
        CriteriaBuilder newBuilder = specification.getBuilder();
        Predicate predicate = isConnect ? newBuilder.conjunction() : newBuilder.disjunction();
        operator.accept(specification);
        predicate.getExpressions().addAll(specification.getPredicates()) ;
        predicates.add(predicate);
        return this;
    }


    public SpecificationWrapper<B> isNull(boolean condition, String name) {
        return condition ? this.isNull(name) : this;
    }



    public SpecificationWrapper<B> isNull(String name) {
        return handle(name, this::isNull);
    }



    public SpecificationWrapper<B> isNull(Expression<?> x) {
        predicates.add(builder.isNull(x));
        return this;
    }

    public SpecificationWrapper<B> isNotNull(boolean condition, String name) {
        return condition ? this.isNotNull(name) : this;
    }

    public SpecificationWrapper<B> isNotNull(String name) {
        return handle(name, this::isNotNull);
    }

    public SpecificationWrapper<B> isNotNull(Expression<?> x) {
        predicates.add(builder.isNotNull(x));
        return this;
    }

    public SpecificationWrapper<B> eq(boolean condition, String name, Object value) {
        return condition ? this.eq(name, value) : this;
    }

    public SpecificationWrapper<B> eq(String name, Object value) {
        return handle(name, e -> this.eq(e, value));
    }

    public SpecificationWrapper<B> eq(Expression<?> x, Object value) {
        predicates.add(builder.equal(x, value));
        return this;
    }

    public SpecificationWrapper<B> ne(boolean condition, String name, Object value) {
        return condition ? this.ne(name, value) : this;
    }

    public SpecificationWrapper<B> ne(String name, Object value) {
        return handle(name, e -> this.ne(e, value));
    }

    public SpecificationWrapper<B> ne(Expression<?> x, Object value) {
        predicates.add(builder.notEqual(x, value));
        return this;
    }

    public SpecificationWrapper<B> like(boolean condition, String name, String value) {
        return condition ? this.like(name, value) : this;
    }

    public SpecificationWrapper<B> like(String name, String value) {
        return handle(name, e -> this.like(e, value));
    }

    public SpecificationWrapper<B> like(Expression<String> path, String value){
        predicates.add(builder.like(path, value));
        return this;
    }

    public SpecificationWrapper<B> startingWith(boolean condition, String name, String value) {
        return condition ? this.startingWith(name, value) : this;
    }

    public SpecificationWrapper<B> startingWith(String name, String value) {
        this.like(name,value+"%");
        return this;
    }

    public SpecificationWrapper<B> endingWith(boolean condition, String name, String value) {
        return condition ? this.endingWith(name, value) : this;
    }

    public SpecificationWrapper<B> endingWith(String name, String value) {
        this.like(name,"%"+value);
        return this;
    }

    public SpecificationWrapper<B> contains(boolean condition, String name, String value) {
        return condition ? this.contains(name, value) : this;
    }

    public SpecificationWrapper<B> contains(String name, String value){
        this.like(name,"%"+value+"%");
        return this;
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> ge(boolean condition, String name, Y value) {
        return condition ? this.ge(name, value) : this;
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> ge(String name, Y value) {
        return handle(name, e -> this.ge(e, value));
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> ge
            (Expression<? extends Y> path, Y value) {
        predicates.add(builder.greaterThanOrEqualTo(path, value));
        return this;
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> le(boolean condition, String name, Y value) {
        return condition ? this.le(name, value) : this;
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> le(String name, Y value) {
        return handle(name, e -> this.le(e, value));
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> le
            (Expression<? extends Y> path, Y value) {
        predicates.add(builder.lessThanOrEqualTo(path, value));
        return this;
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> gt(boolean condition, String name, Y value) {
        return condition ? this.gt(name, value) : this;
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> gt(String name, Y value) {
        return handle(name, e -> this.gt(e, value));
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> gt
            (Expression<? extends Y> path, Y value) {
        predicates.add(builder.greaterThan(path, value));
        return this;
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> lt(boolean condition, String name, Y value) {
        return condition ? this.lt(name, value) : this;
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> lt(String name, Y value) {
        return handle(name, e -> this.lt(e, value));
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> lt
            (Expression<? extends Y> path, Y value) {
        predicates.add(builder.lessThan(path, value));
        return this;
    }

    public SpecificationWrapper<B> in(boolean condition, String name, Object... value) {
        return condition ? in(name, value) : this;
    }

    public SpecificationWrapper<B> in(String name, Object... value) {
        return handle(name, e -> this.in(e, value));
    }

    public SpecificationWrapper<B> in(boolean condition, String name, Collection<?> value) {
        return condition ? in(name, value) : this;
    }

    public SpecificationWrapper<B> in(String name, Collection<?> value) {
        return this.in(name, value.toArray());
    }

    public <U> SpecificationWrapper<B> in(Expression<? extends U> expression, Object... value) {
        predicates.add(expression.in(value));
        return this;
    }

    public SpecificationWrapper<B> notIn(boolean condition, String name, Collection<?> value) {
        return condition ? notIn(name, value) : this;
    }

    public SpecificationWrapper<B> notIn(String name, Collection<?> value) {
        return handle(name, e -> this.notIn(e, value.toArray()));
    }

    public SpecificationWrapper<B> notIn(boolean condition, String name, Object... value) {
        return condition ? notIn(name, value) : this;
    }

    public SpecificationWrapper<B> notIn(String name, Object... value) {
        return handle(name, e -> this.notIn(e, value));
    }

    public <U> SpecificationWrapper<B> notIn(Expression<? extends U> expression, Object... value) {
        predicates.add(expression.in(value).not());
        return this;
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> between(boolean condition,
                                                                             String name, Y start, Y end){
        return condition ? between(name, start, end) : this;
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> between(String name, Y start, Y end){
        return handle(name, e -> this.between(e, start, end));
    }

    public <Y extends Comparable<? super Y>> SpecificationWrapper<B> between(Expression<? extends Y> path,
                                                                             Y start, Y end){
        predicates.add(builder.between(path, start, end));
        return this;
    }

    public <U> Join<B,U> leftJoin(String fieldName) {
        return root.join(fieldName, JoinType.LEFT);
    }

    public SpecificationWrapper<B> handle(String name, Consumer<Path> action) {
        Path<?> path;
        if(name.contains(SEPARATOR)){
            String[] arr = name.split("\\"+SEPARATOR);
            path = this.leftJoin(arr[0]).get(arr[1]);
        }else{
            path = this.root.get(name);
        }
        action.accept(path);
        return this;
    }

}

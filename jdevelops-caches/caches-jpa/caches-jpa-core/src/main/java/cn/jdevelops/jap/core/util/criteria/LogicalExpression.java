package cn.jdevelops.jap.core.util.criteria;

import cn.jdevelops.jap.util.IObjects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * sql表达式
 * 逻辑表达式对象， 用于指定两个表达式的逻辑关系， 有AndExpression和OrExpression两个子类；
 *
 * @author tt
 */
public class LogicalExpression implements ExpandCriterion {
    /**
     * 逻辑表达式中包含的表达式
     */
    private ExpandCriterion[] criterion;
    /**
     * 计算符
     */
    private Operator operator;

    public LogicalExpression(ExpandCriterion[] criterions, Operator operator) {
        this.criterion = criterions;
        this.operator = operator;
    }

    @Override
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        if (IObjects.isNull(this.criterion)) {
            return query.getRestriction();
        }
        for (ExpandCriterion expandCriterion : this.criterion) {
            predicates.add(expandCriterion.toPredicate(root, query, builder));
        }
        switch (operator) {
            case OR:
                return builder.or(predicates.toArray(new Predicate[predicates.size()]));
            case AND:
                return builder.and(predicates.toArray(new Predicate[predicates.size()]));
            default:
                return query.getRestriction();
        }


    }

}

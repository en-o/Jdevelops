package com.detabes.jap.core.util;

import com.detabes.jap.core.util.criteria.ExpandCriterion;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * jpa util
 * @param <T>
 * @author tn
 */
public class JPAUtilExpandCriteria<T> implements Specification<T>{
    private List<ExpandCriterion> criterionsAnd = new ArrayList<ExpandCriterion>();
    private List<ExpandCriterion> criterionsOr = new ArrayList<ExpandCriterion>();

    List<Predicate> predicatesAnd ;
    List<Predicate> predicatesOr;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {



        if (criterionsAnd!=null&&!criterionsAnd.isEmpty()) {
             predicatesAnd = new ArrayList<Predicate>();
            for(ExpandCriterion c : criterionsAnd){
                predicatesAnd.add(c.toPredicate(root, query,builder));
            }  
            // 将所有条件用 and 联合起来  
            if (predicatesAnd.size() > 0) {
                predicatesAnd.toArray(new Predicate[predicatesAnd.size()]);
            }
        }
        if (criterionsOr!=null&&!criterionsOr.isEmpty()) {
            predicatesOr = new ArrayList<Predicate>();
            for(ExpandCriterion c : criterionsOr){
                predicatesOr.add(c.toPredicate(root, query,builder));
            }
            // 将所有条件用 and 联合起来
            if (predicatesOr.size() > 0) {
                predicatesOr.toArray(new Predicate[predicatesOr.size()]);
            }
        }

        Predicate predicateAnds = null;
        Predicate predicateOrs = null;
        /** and */
        if(predicatesAnd!=null&&!predicatesAnd.isEmpty()) {
            predicateAnds = builder.and(predicatesAnd.toArray(new Predicate[predicatesAnd.size()]));
            predicateAnds = builder.and(predicateAnds);
        }
        /** or */
        if(predicatesOr!=null&&!predicatesOr.isEmpty()) {
            predicateOrs = builder.or(predicatesOr.toArray(new Predicate[predicatesOr.size()]));
            predicateOrs = builder.and(predicateOrs);
        }
        if(predicateOrs!=null&&predicateAnds!=null) {
            return query.where(predicateAnds,predicateOrs).getRestriction();
        }else {
            if(predicateAnds != null ){
                return query.where(predicateAnds).getRestriction();
            }else if(predicateOrs != null) {
                return query.where(predicateOrs).getRestriction();
            }else {
                return builder.conjunction();
            }
        }

    }  
    /** 
     * 增加简单条件表达式 
     * @Methods Name add 
     * @Create In 2012-2-8 By lee 
     */
    public void add(ExpandCriterion criterion){  
        if(criterion!=null){
            criterionsAnd.add(criterion);
        }
    }
    /**
     * 增加简单条件表达式
     * @Methods Name or
     * @Create In 2012-2-8 By lee
     */
    public void or(ExpandCriterion criterion){
        if(criterion!=null){
            criterionsOr.add(criterion);
        }
    }

    /**
     * <pr>
     *      public static void main(String[] args) {
     *     	使用示例Demo
     *     	JPAUtilExpandCriteria<Entity> c = new JPAUtilExpandCriteria<Entity>();
     *     	c.add(Restrictions.like("code", searchParam.getCode(), true));
     *     	        c.add(Restrictions.eq("level", searchParam.getLevel(), false));
     *     	        c.add(Restrictions.eq("mainStatus", searchParam.getMainStatus(), true));
     *     	        c.add(Restrictions.eq("flowStatus", searchParam.getFlowStatus(), true));
     *     	        c.add(Restrictions.eq("createUser.userName", searchParam.getCreateUser(), true));
     *     	        c.add(Restrictions.lte("submitTime", searchParam.getStartSubmitTime(), true));
     *     	        c.add(Restrictions.gte("submitTime", searchParam.getEndSubmitTime(), true));
     *     	        c.add(Restrictions.eq("needFollow", searchParam.getIsfollow(), true));
     *     	        c.add(Restrictions.ne("flowStatus", searchParam.getMainStatus() true));
     *     	        c.add(Restrictions.in("solveTeam.code",teamCodes, true));
     *     	repository.findAll(c);
     *        }
     * </pr>
     *
     */
}  
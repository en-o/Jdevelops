package com.detabes.jap.core.util.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * jpa 查询工具之构建sql
 * @author tnnn
 */
public interface ExpandCriterion {
	 public enum Operator {  
	        EQ, NE, LIKE, GT, LT, GTE, LTE, AND, OR  
	    }  
	    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,  
	            CriteriaBuilder builder);  
}
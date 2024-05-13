package cn.tannn.jdevelops.jpa.select.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * jpa 查询工具之构建sql
 * @author tnnn
 */
public interface ExpandCriterion {
	/**
	 *   Predicate 语句
	 * @param root  root
	 * @param query query
	 * @param builder builder
	 * @return Predicate
	 */
	Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
						  CriteriaBuilder builder);
}

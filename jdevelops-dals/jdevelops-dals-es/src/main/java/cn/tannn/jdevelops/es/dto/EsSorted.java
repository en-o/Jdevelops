package cn.tannn.jdevelops.es.dto;


import java.io.Serializable;
import java.util.StringJoiner;

/**
 * @author tn
 */
public class EsSorted implements Serializable {
	/**
	 * 排序字段
	 */
	private String orderBy;
	/**
	 * "正序0--Direction.ASC，反序1--Direction.DESC
	 */
	private Integer orderDesc;

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(Integer orderDesc) {
		this.orderDesc = orderDesc;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", EsSorted.class.getSimpleName() + "[", "]")
				.add("orderBy='" + orderBy + "'")
				.add("orderDesc=" + orderDesc)
				.toString();
	}
}

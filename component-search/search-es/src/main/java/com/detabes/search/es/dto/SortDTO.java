package com.detabes.search.es.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author tn
 */
@ToString
@Getter
@Setter
public class SortDTO  implements Serializable {
	/**
	 * 排序字段
	 */
	private String orderBy;
	/**
	 * "正序0--Direction.ASC，反序1--Direction.DESC
	 */
	private Integer orderDesc;
}

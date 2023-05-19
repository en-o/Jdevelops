package cn.jdevelops.search.es.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author tn
 */
@ToString
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
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

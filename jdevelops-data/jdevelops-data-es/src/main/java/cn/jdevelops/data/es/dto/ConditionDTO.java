package cn.jdevelops.data.es.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 检索条件接收实体
 *
 * @author lxw
 * @version V1.0
 * @date 2020/6/2
 **/
@ToString
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ConditionDTO implements Serializable {


	/**
	 * 字段代码
	 */
	private String field;

	/**
	 * 字段值
	 */
	private String fieldValue;

	/**
	 * 匹配类型 <br/>
	 * PS:
	 * <p>
	 * EQ 就是 EQUAL等于 <br/>
	 * GT 就是 GREATER THAN大于　<br/>
	 * LT 就是 LESS THAN小于 <br/>
	 * GE 就是 GREATER THAN OR EQUAL 大于等于 <br/>
	 * LE 就是 LESS THAN OR EQUAL 小于等于 <br/>
	 * LIKE  就是 模糊 <br/>
	 * <p/>
	 */
	private String symbol;

	/**
	 * 逻辑类型
	 * PS:
	 * <p>
	 * AND 就是 并且
	 * OR 就是 或者
	 * NOT 就是 非
	 * <p/>
	 */
	private String connectSymbol;
}

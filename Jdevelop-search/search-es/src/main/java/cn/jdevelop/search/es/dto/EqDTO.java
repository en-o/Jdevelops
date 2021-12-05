package cn.jdevelop.search.es.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 等于匹配条件
 * PS:
 * <p>
 *     name ='张三' OR name ='李四'
 * </p>
 * @author lxw
 * @version V1.0
 * @date 2020/6/2
 **/
@Getter
@Setter
@ToString
public class EqDTO implements Serializable {


    /**
     * 字段代码
     */
    private String field;

    /**
     * 字段值集合
     */
    private List<String> fieldValue;
}

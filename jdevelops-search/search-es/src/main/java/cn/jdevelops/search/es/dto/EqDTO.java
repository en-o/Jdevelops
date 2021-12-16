package cn.jdevelops.search.es.dto;

import lombok.*;

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
@ToString
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class EqDTO implements Serializable {


    /**
     * 字段代码，字段类型是text，必须添加“.keyword”
     */
    private String field;

    /**
     * 字段值集合
     */
    private List<String> fieldValue;
}

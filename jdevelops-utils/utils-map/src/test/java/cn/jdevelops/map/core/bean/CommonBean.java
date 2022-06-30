package cn.jdevelops.map.core.bean;

import lombok.Getter;
import lombok.Setter;


/**
 * 公共的实体类
 * @author tn
 * @date 2021-01-21 14:20
 */
@Getter
@Setter
public class CommonBean<B> extends JpaAuditFields<B> {

    private Integer id;
}

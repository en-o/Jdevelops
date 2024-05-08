package cn.tannn.jdevelops.result.domin.column;


import cn.tannn.jdevelops.result.domin.JpaAuditFields;

/**
 * 公共的实体类
 * @author tn
 * @date 2021-01-21 14:20
 */

public class CommonBean<B> extends JpaAuditFields<B> {

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

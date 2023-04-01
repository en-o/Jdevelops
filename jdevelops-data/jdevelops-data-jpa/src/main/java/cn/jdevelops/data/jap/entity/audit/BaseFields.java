package cn.jdevelops.data.jap.entity.audit;


import cn.jdevelops.api.result.bean.SerializableBean;

import java.time.LocalDateTime;

/**
 * 数据表中自动创建时间 公共类
 * @author tn
 * @version 1
 * @date 2020/5/26 22:12
 */

public class BaseFields<B> extends SerializableBean<B> {

    /**
     * 表示该字段为创建时间字段，在这个实体被insert的时候，会自动为其赋值
     */
    private LocalDateTime createTime;


    /**
     * 表示该字段为修改时间字段，在这个实体被update的时候，会自动为其赋值
     */
    private LocalDateTime updateTime;


    @Override
    public String toString() {
        return "BaseFields{" +
                "createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}

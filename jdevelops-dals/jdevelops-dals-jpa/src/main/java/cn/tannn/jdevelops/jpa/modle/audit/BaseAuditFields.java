package cn.tannn.jdevelops.jpa.modle.audit;


import cn.tannn.jdevelops.annotations.jpa.JpaUpdate;
import cn.tannn.jdevelops.result.bean.SerializableBean;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 *  数据表中自动创建时间 公共类
 * @author tn
 * @version 1
 * @date 2020/5/26 22:12
 */

public class BaseAuditFields<B> extends SerializableBean<B> {

    /**
     * 表示该字段为创建时间字段，在这个实体被insert的时候，会自动为其赋值
     */
    @JpaUpdate(ignore = true)
    private LocalDateTime createTime;

    /**
     * 表示该字段为创建人，在这个实体被insert的时候，会自动为其赋值
     */
    @JpaUpdate(ignore = true)
    private String createUserName;

    /**
     * 表示该字段为修改时间字段，在这个实体被update的时候，会自动为其赋值
     */
    @JpaUpdate(autoTime = true)
    private LocalDateTime updateTime;

    /**
     * 表示该字段为修改人，在这个实体被update的时候，会自动为其赋值
     */
    private String updateUserName;


    @Override
    public String toString() {
        return "BaseAuditFields{" +
                "createTime=" + createTime +
                ", createUserName='" + createUserName + '\'' +
                ", updateTime=" + updateTime +
                ", updateUserName='" + updateUserName + '\'' +
                '}';
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseAuditFields)) {
            return false;
        }
        BaseAuditFields<?> that = (BaseAuditFields<?>) o;
        return Objects.equals(createTime, that.createTime) && Objects.equals(createUserName, that.createUserName) && Objects.equals(updateTime, that.updateTime) && Objects.equals(updateUserName, that.updateUserName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createTime, createUserName, updateTime, updateUserName);
    }
}

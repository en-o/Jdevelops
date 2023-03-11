package cn.jdevelops.entity.basics.audit;

import cn.jdevelops.entity.basics.vo.SerializableVO;

import java.time.LocalDateTime;

/**
 *  数据表中自动创建时间 公共类
 * @author tn
 * @version 1
 * @date 2020/5/26 22:12
 */

public class BaseAuditFields<T> extends SerializableVO<T> {

    /**
     * 表示该字段为创建时间字段，在这个实体被insert的时候，会自动为其赋值
     */
    private LocalDateTime createTime;

    /**
     * 表示该字段为创建人，在这个实体被insert的时候，会自动为其赋值
     */
    private String createUserName;

    /**
     * 表示该字段为修改时间字段，在这个实体被update的时候，会自动为其赋值
     */
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
}

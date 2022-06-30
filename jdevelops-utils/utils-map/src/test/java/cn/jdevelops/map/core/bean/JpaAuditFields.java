package cn.jdevelops.map.core.bean;


import java.time.LocalDateTime;

/**
 * @author tn
 * @version 1
 *
 */
public class JpaAuditFields<T> extends BaseAuditFields<T> {

    /**
     * 表示该字段为创建时间字段，在这个实体被insert的时候，会自动为其赋值
     */
    @Override
    public LocalDateTime getCreateTime() {
        return super.getCreateTime();
    }

    /**
     * 表示该字段为创建人， @CreatedBy 在这个实体被created的时候，会自动为其赋值
     */

    @Override
    public String getCreateUserName() {
        return super.getCreateUserName();
    }

    /**
     * 表示该字段为修改时间字段，@LastModifiedDate在这个实体被update的时候，会自动为其赋值
     */

    @Override
    public LocalDateTime getUpdateTime() {
        return super.getUpdateTime();
    }

    /**
     * 表示该字段为修改人，@LastModifiedBy 在这个实体被update的时候，会自动为其赋值
     */
    @Override
    public String getUpdateUserName() {
        return super.getUpdateUserName();
    }



}

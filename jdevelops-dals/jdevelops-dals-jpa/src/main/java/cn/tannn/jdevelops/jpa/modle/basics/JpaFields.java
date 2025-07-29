package cn.tannn.jdevelops.jpa.modle.basics;

import cn.tannn.jdevelops.jpa.modle.audit.BaseFields;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 数据实体基础字段（不含数据库函数声明
 *
 * <p>  新建BaseDate，里面主要包含审计的公共字段，如新增人、新增时间、最后更新人、最后更新时间
 *
 * <pre>
 *   EntityListeners(AuditingEntityListener.class):声明实体监听器,用于实体修改时做处理
 *   MappedSuperclass:声明该类为实体父类,不会映射单独的表,而是把字段映射到子类表中
 * </pre>
 * @author tn
 * @date 2020-09-28 16:11
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
@SelectBeforeUpdate
@Access(AccessType.FIELD)
public class JpaFields<T> extends BaseFields<T> {

    /**
     * 表示该字段为创建时间字段，在这个实体被insert的时候，会自动为其赋值
     */
    @CreatedDate
    @Column(columnDefinition = "timestamp", updatable = false)
    @Access(AccessType.PROPERTY)
    @Comment("创建日期")
    @Override
    public LocalDateTime getCreateTime() {
        return super.getCreateTime();
    }



    /**
     * 表示该字段为修改时间字段，@LastModifiedDate在这个实体被update的时候，会自动为其赋值
     */
    @LastModifiedDate
    @Column(columnDefinition = "timestamp")
    @Access(AccessType.PROPERTY)
    @Comment("更新日期")
    @Override
    public LocalDateTime getUpdateTime() {
        return super.getUpdateTime();
    }


}

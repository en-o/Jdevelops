package com.detabes.jpa.server.entity.mysql;

import com.detabes.entity.basics.audit.BaseFields;
import com.detabes.entity.basics.vo.SerializableVO;
import com.detabes.result.page.ResourcePage;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author tn
 * @version 1
 * @ClassName BaseDate
 * @description 公共实体INT
 *
 *新建BaseDate，里面主要包含审计的公共字段，如新增人、新增时间、最后更新人、最后更新时间
 *
 * @EntityListeners(AuditingEntityListener.class):声明实体监听器,用于实体修改时做处理
 * @MappedSuperclass:声明该类为实体父类,不会映射单独的表,而是把字段映射到子类表中
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
    @Column(columnDefinition = "timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期' ")
    @Access(AccessType.PROPERTY)
    @Override
    public void setCreateTime(LocalDateTime createTime) {
        super.setCreateTime(createTime);
    }



    /**
     * 表示该字段为修改时间字段，@LastModifiedDate在这个实体被update的时候，会自动为其赋值
     */
    @LastModifiedDate
    @Column(columnDefinition = "timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期'")
    @Access(AccessType.PROPERTY)
    @Override
    public LocalDateTime getUpdateTime() {
        return super.getUpdateTime();
    }


    /**
     * page
     * @param page
     * @param clazz
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S extends SerializableVO> ResourcePage<List<T>> to(Page<S> page, Class<T> clazz) {
        if (page != null && !page.isEmpty()) {
            List<S> content = page.getContent();

            List<T> result = new ArrayList(content.size());

            Iterator var3 = content.iterator();

            while(var3.hasNext()) {
                SerializableVO abs = (SerializableVO)var3.next();
                result.add((T) abs.to(clazz));
            }

            return ResourcePage.page(page.getNumber(),
                    page.getSize(),
                    page.getTotalPages(),
                    page.getTotalElements(),
                    result);
        } else {
            return new ResourcePage();
        }
    }


}

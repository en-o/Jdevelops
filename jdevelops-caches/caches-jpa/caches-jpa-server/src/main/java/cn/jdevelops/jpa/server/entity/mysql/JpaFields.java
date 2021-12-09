package cn.jdevelops.jpa.server.entity.mysql;

import cn.jdevelops.entity.basics.audit.BaseFields;
import cn.jdevelops.entity.basics.vo.SerializableVO;
import cn.jdevelops.result.page.ResourcePage;
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
 * 公共实体INT
 * @author tn
 * @version 1
 *
 *新建BaseDate，里面主要包含审计的公共字段，如新增人、新增时间、最后更新人、最后更新时间
 *
 * <pre>
 *   EntityListeners(AuditingEntityListener.class):声明实体监听器,用于实体修改时做处理
 *   MappedSuperclass:声明该类为实体父类,不会映射单独的表,而是把字段映射到子类表中
 * </pre>
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
    public LocalDateTime getCreateTime() {
        return super.getCreateTime();
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
     * @param page 页面
     * @param clazz 实体转换对象
     * @param <T> 实体对象
     * @param <S> 分页对象
     * @return ResourcePage
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

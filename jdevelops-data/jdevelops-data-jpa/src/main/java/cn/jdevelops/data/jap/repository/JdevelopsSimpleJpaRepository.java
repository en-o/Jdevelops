package cn.jdevelops.data.jap.repository;

import cn.hutool.core.util.ReflectUtil;
import cn.jdevelops.api.result.util.bean.BeanCopyUtil;
import cn.jdevelops.data.jap.exception.JpaException;
import cn.jdevelops.data.jap.util.IObjects;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

/**
 * 自定义 Base Repository
 * @link  <a href="https://docs.spring.io/spring-data/jpa/docs/2.7.12/reference/html/#repositories.customize-base-repository">Customize the Base Repository</a>
 * @link  <a href="https://springdoc.cn/spring-data-jpa/#repositories.customize-base-repository">中文文档</a>
 * @author tan
 * @date 2023-06-14 15:02:09
 * @since 2.0.7
 */
@Repository
@Transactional(readOnly = true)
public class JdevelopsSimpleJpaRepository<B, ID> extends SimpleJpaRepository<B, ID> implements JpaBasicsRepository<B, ID>{

    private final EntityManager entityManager;

    public JdevelopsSimpleJpaRepository(JpaEntityInformation<B, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        // Keep the EntityManager around to used from the newly introduced methods.
        this.entityManager = entityManager;
    }


    @Override
    public <U> boolean deleteByUnique(List<U> unique, String selectKey) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaDelete<B> deletes = builder.createCriteriaDelete(getDomainClass());
        Root<B> deleteFrom = deletes.from(getDomainClass());
        Predicate predicate = builder.in(deleteFrom.get(selectKey)).in(unique);
        deletes.where(predicate);
        return entityManager.createQuery(deletes).executeUpdate()>=0;
    }

    @Override
    public boolean updateEntity(B t) throws JpaException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<B> update = criteriaBuilder.createCriteriaUpdate(getDomainClass());
        Root<B> deleteFrom = update.from(getDomainClass());

        Field[] fields = ReflectUtil.getFields(t.getClass());
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            // 字段名
            String fieldName = field.getName();
            if ("serialVersionUID".equals(fieldName)) {
                continue;
            }
            // 字段值
            Object fieldValue = ReflectUtil.getFieldValue(t, field);
            if(fieldValue != null ){
                // 设置更新值
                update.set(deleteFrom.get(fieldName), fieldValue);
            }
        }
        // 获取主键名
        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<B> entityType = metamodel.entity(getDomainClass());
        SingularAttribute<? super B, ?> id = entityType.getId(entityType.getIdType().getJavaType());

        // 根据主键更新
        Object fieldValue = ReflectUtil.getFieldValue(t, id.getName());
        Predicate condition = criteriaBuilder.equal(deleteFrom.get(id.getName()), fieldValue);

        // 应用更新的条件
        update.where(condition);
        // 执行更新
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        int i = entityManager.createQuery(update).executeUpdate();
        transaction.commit();
        return i>=0;
    }

    @Override
    public long delete(Specification<B> spec) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaDelete<B> delete = builder.createCriteriaDelete(getDomainClass());
        if (spec != null) {
            Predicate predicate = spec.toPredicate(delete.from(getDomainClass()), null, builder);

            if (predicate != null) {
                delete.where(predicate);
            }
        }
        return this.entityManager.createQuery(delete).executeUpdate();
    }



}

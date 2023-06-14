package cn.jdevelops.data.jap.service.impl;

import cn.hutool.core.util.ReflectUtil;
import cn.jdevelops.api.result.bean.SerializableBean;
import cn.jdevelops.api.result.request.PageDTO;
import cn.jdevelops.api.result.request.SortDTO;
import cn.jdevelops.api.result.request.SortPageDTO;
import cn.jdevelops.api.result.util.ListTo;
import cn.jdevelops.api.result.util.bean.ColumnSFunction;
import cn.jdevelops.api.result.util.bean.ColumnUtil;
import cn.jdevelops.data.jap.core.JPAUtilExpandCriteria;
import cn.jdevelops.data.jap.core.Specifications;
import cn.jdevelops.data.jap.repository.JpaBasicsRepository;
import cn.jdevelops.data.jap.exception.JpaException;
import cn.jdevelops.data.jap.result.JpaPageResult;
import cn.jdevelops.data.jap.service.J2Service;
import cn.jdevelops.data.jap.util.IObjects;
import cn.jdevelops.data.jap.util.JPageUtil;
import cn.jdevelops.data.jap.util.JpaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * 预约模块公共service实现
 *
 * @param <ID> 实体的主键类型
 * @param <B>  实体
 * @param <M>  实体的Dao层
 * @author tn
 * @version 1
 * @date 2021/1/23 12:03
 */
@Slf4j
@Component
public class J2ServiceImpl<M extends JpaBasicsRepository<B, ID>, B extends SerializableBean<B>, ID> implements J2Service<B> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private M commonDao;

    /**
     * 实体对象类型
     */
    private Class<B> domainClass;

    public J2ServiceImpl(Class<B> domainClass) {
        this.domainClass = domainClass;
    }

    @Override
    public M getJpaBasicsDao() {
        return commonDao;
    }

    @Override
    public B saveByBean(B bean) {
        return commonDao.save(bean);
    }


    @Override
    @Transactional
    public <U> boolean deleteByUnique(List<U> unique, String selectKey) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaDelete<B> deletes = builder.createCriteriaDelete(domainClass);
        Root<B> deleteFrom = deletes.from(domainClass);
        Predicate predicate = deleteFrom.get(selectKey).in(unique);
        deletes.where(predicate);
        return entityManager.createQuery(deletes).executeUpdate() >= 0;
    }


    @Override
    @Transactional
    public long delete(Specification<B> spec) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaDelete<B> delete = builder.createCriteriaDelete(domainClass);
        if (spec != null) {
            Predicate predicate = spec.toPredicate(delete.from(domainClass), null, builder);
            if (predicate != null) {
                delete.where(predicate);
            }
        }
        return this.entityManager.createQuery(delete).executeUpdate();
    }

    @Override
    public Boolean saveAllByBoolean(List<B> bean) {
        try {
            commonDao.saveAll(bean);
        } catch (Exception e) {
            log.error("保存失败", e);
            return false;
        }
        return true;
    }

    @Override
    public List<B> saveAllByBean(List<B> bean) {
        return commonDao.saveAll(bean);
    }

    @Override
    public Boolean saveByBoolean(B bean) {
        commonDao.save(bean);
        return true;
    }

    @Override
    @Transactional
    public <U> Boolean deleteByUnique(List<U> unique, ColumnSFunction<B, ?> uniqueKey) {
        String field = ColumnUtil.getFieldName(uniqueKey);
        return deleteByUnique(unique, field);
    }

    @Override
    @Transactional
    public <U> Boolean deleteByUnique(U unique, ColumnSFunction<B, ?> uniqueKey) {
        String field = ColumnUtil.getFieldName(uniqueKey);
        return deleteByUnique(Collections.singletonList(unique), field);
    }

    @Override
    @Transactional
    public Boolean updateByBean(B bean) {
        return updateByBean(bean, "");
    }

    @Override
    @Transactional
    public Boolean updateByBean(B bean, String uniqueKey) throws JpaException {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaUpdate<B> update = criteriaBuilder.createCriteriaUpdate(domainClass);
            Root<B> deleteFrom = update.from(domainClass);

            Field[] fields = ReflectUtil.getFields(bean.getClass());


            // 获取主键名
            Metamodel metamodel = entityManager.getMetamodel();
            EntityType<B> entityType = metamodel.entity(domainClass);
            SingularAttribute<? super B, ?> id = entityType.getId(entityType.getIdType().getJavaType());
            Predicate condition;
            String ignoreField ;
            if (IObjects.isBlank(uniqueKey)) {
                ignoreField = id.getName();
                // 根据主键更新
                condition = criteriaBuilder.equal(deleteFrom.get(id.getName()), ReflectUtil.getFieldValue(bean, id.getName()));
            } else {
                // 根据传入的唯一key键
                ignoreField = uniqueKey;
                condition = criteriaBuilder.equal(deleteFrom.get(uniqueKey), ReflectUtil.getFieldValue(bean, uniqueKey));
            }

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                // 字段名
                String fieldName = field.getName();
                if ("serialVersionUID".equals(fieldName)) {
                    continue;
                }
                // 字段值
                Object fieldValue = ReflectUtil.getFieldValue(bean, field);
                if (fieldValue != null && !fieldName.equals(ignoreField)) {
                    // 设置更新值
                    update.set(deleteFrom.get(fieldName), fieldValue);
                }
            }

            // 应用更新的条件
            update.where(condition);
            // 执行更新
            return entityManager.createQuery(update).executeUpdate()>= 0;
        } catch (Exception e) {
            throw new JpaException("更新出错", e);
        }
    }

    @Override
    @Transactional
    public Boolean updateByBean(B bean, ColumnSFunction<B, ?> uniqueKey) throws JpaException {
        String field = ColumnUtil.getFieldName(uniqueKey);
        return updateByBean(bean, field);
    }


    @Override
    public List<B> findAllBean() {
        return commonDao.findAll();
    }

    @Override
    public Optional<B> findBeanOne(ColumnSFunction<B, ?> selectKey, Object value) {
        Specification<B> where = Specifications.where(e -> e.eq(true, ColumnUtil.getFieldName(selectKey), value));
        return commonDao.findOne(where);
    }

    @Override
    public List<B> findBeanList(ColumnSFunction<B, ?> selectKey, Object value) {
        Specification<B> where = Specifications.where(e -> e.eq(IObjects.nonNull(value), ColumnUtil.getFieldName(selectKey), value));
        return commonDao.findAll(where);
    }

    @Override
    public <T> List<B> findComplex(T req, SortDTO sort) {
        JPAUtilExpandCriteria<B> selectRegionBean = JpaUtils.getSelectBean2(req);
        return commonDao.findAll(selectRegionBean, JPageUtil.getSv2S(sort));
    }

    @Override
    public <T> List<B> findComplex(T req) {
        JPAUtilExpandCriteria<B> selectRegionBean = JpaUtils.getSelectBean2(req);
        return commonDao.findAll(selectRegionBean);
    }

    @Override
    public <T, R> List<R> findComplex(T req, SortDTO sort, Class<R> clazz) {
        JPAUtilExpandCriteria<B> selectRegionBean = JpaUtils.getSelectBean2(req);
        return ListTo.to(clazz, commonDao.findAll(selectRegionBean, JPageUtil.getSv2S(sort)));
    }

    @Override
    public <R, T> JpaPageResult<R> findByBean(T req, PageDTO page, Class<R> clazz) {
        JPAUtilExpandCriteria<B> selectRegionBean = JpaUtils.getSelectBean2(req);
        Pageable pageable = JPageUtil.getPageable(page);
        Page<B> pages = commonDao.findAll(selectRegionBean, pageable);
        return JpaPageResult.toPage(pages, clazz);
    }

    @Override
    public <T> JpaPageResult<B> findByBean(T req, PageDTO page) {
        JPAUtilExpandCriteria<B> selectRegionBean = JpaUtils.getSelectBean2(req);
        Pageable pageable = JPageUtil.getPageable(page);
        Page<B> pages = commonDao.findAll(selectRegionBean, pageable);
        return JpaPageResult.toPage(pages);
    }

    @Override
    public <R, T> JpaPageResult<R> findByBean(T req, SortPageDTO sortPage, Class<R> clazz) {
        JPAUtilExpandCriteria<B> selectRegionBean = JpaUtils.getSelectBean2(req);
        Pageable pageable = JPageUtil.getPageable(sortPage);
        Page<B> pages = commonDao.findAll(selectRegionBean, pageable);
        return JpaPageResult.toPage(pages, clazz);
    }

    @Override
    public <T> JpaPageResult<B> findByBean(T req, SortPageDTO sortPage) {
        JPAUtilExpandCriteria<B> selectRegionBean = JpaUtils.getSelectBean2(req);
        Pageable pageable = JPageUtil.getPageable(sortPage);
        Page<B> pages = commonDao.findAll(selectRegionBean, pageable);
        return JpaPageResult.toPage(pages);
    }



}

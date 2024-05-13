package cn.tannn.jdevelops.jpa.service;

import cn.tannn.jdevelops.annotations.jpa.enums.SQLOperator;
import cn.tannn.jdevelops.jpa.exception.JpaException;
import cn.tannn.jdevelops.jpa.repository.JpaBasicsRepository;
import cn.tannn.jdevelops.jpa.request.PagingSorteds;
import cn.tannn.jdevelops.jpa.request.Pagings;
import cn.tannn.jdevelops.jpa.request.Sorteds;
import cn.tannn.jdevelops.jpa.select.EnhanceSpecification;
import cn.tannn.jdevelops.jpa.utils.JpaUtils;
import cn.tannn.jdevelops.result.bean.SerializableBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * jpa公共service
 *
 * @param <R>  Repository
 * @param <B>  Bean
 * @param <ID> ID
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/5/13 上午9:36
 */
@Component
public class J2ServiceImpl<R extends JpaBasicsRepository<B, ID>, B, ID> implements J2Service<B> {

    private static final Logger LOG = LoggerFactory.getLogger(J2ServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private R commonDao;

    /**
     * 实体对象类型
     */
    private Class<B> domainClass;

    public J2ServiceImpl(Class<B> domainClass) {
        this.domainClass = domainClass;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public R getJpaBasicsDao() {
        return commonDao;
    }


    @Override
    public List<B> saves(List<B> bean) {
        return commonDao.saveAllAndFlush(bean);
    }

    @Override
    public B saveOne(B bean) {
        return commonDao.save(bean);
    }

    @Override
    public <V> B saveOneByVo(V bean) {
        B entity = SerializableBean.to2(bean, domainClass);
        if (entity != null) {
            return commonDao.save(entity);
        }
        LOG.warn("=== [jdevelops-dals-jpa] vo to bean error, reuslt null");
        return null;
    }

    @Override
    public int delete(Specification<B> spec) {
        if (spec != null) {
            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaDelete<B> delete = builder.createCriteriaDelete(domainClass);
            // select  entity  from
            Root<B> from = delete.from(domainClass);
            // root  参数表示查询的根对象,通常是要查询的实体类
            // query 参数表示当前的 CriteriaQuery 对象,用于构建查询
            // cb    参数是 CriteriaBuilder 对象,用于构建查询的各个部分,如 Predicate、Order 等
            // 创建伪 CriteriaQuery 对象 ps: spec.toPredicate只认 CriteriaQuery不认CriteriaDelete
            CriteriaQuery<B> query = builder.createQuery(domainClass);
            Predicate predicate = spec.toPredicate(from, query, builder);
            if (predicate != null) {
                delete.where(predicate);
                return this.entityManager.createQuery(delete).executeUpdate();
            }
        }
        return 0;
    }

    @Override
    public int delete(String fieldName, Object value) {
        return delete(fieldName, SQLOperator.EQ, value);
    }

    @Override
    public int delete(String fieldName, SQLOperator operator, Object... value) {
        // 时间处理： JpaUtils.functionTimeFormat(function, root, builder, fieldName)
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaDelete<B> deletes = builder.createCriteriaDelete(domainClass);
        Root<B> deleteRoot = deletes.from(domainClass);
        Predicate where = JpaUtils.getPredicate(operator, builder, deleteRoot.get(fieldName), value);
        if (where == null) {
            throw new JpaException("占不支持的表达式: " + operator);
        }
        deletes.where(where);
        return entityManager.createQuery(deletes).executeUpdate();
    }

    @Override
    public <T> int delete(T wheres) {
        if(wheres == null){
            return 0;
        }
        Specification<B> objectSpecification = EnhanceSpecification.beanWhere(wheres);
        return delete(objectSpecification);
    }

    @Override
    public <T> Boolean update(T bean, SQLOperator operator) {
        return null;
    }

    @Override
    public <T> Boolean update(T bean, SQLOperator operator, String... uniqueKey) {
        return null;
    }

    @Override
    public Optional<B> findOnly(String fieldName, Object value) {
        return Optional.empty();
    }

    @Override
    public Optional<B> findOnly(String fieldName, Object value, String fieldName2, Object value2) {
        return Optional.empty();
    }

    @Override
    public Optional<B> findOnly(Specification<B> spec) {
        return Optional.empty();
    }

    @Override
    public List<B> finds() {
        return Collections.emptyList();
    }

    @Override
    public List<B> finds(String fieldName, SQLOperator operator, Object... value) {
        return Collections.emptyList();
    }

    @Override
    public List<B> finds(String fieldName, SQLOperator operator, Sorteds sort, Object... value) {
        return Collections.emptyList();
    }

    @Override
    public List<B> finds(Specification<B> spec, Sorteds sort) {
        return Collections.emptyList();
    }

    @Override
    public <T> List<B> findComplex(T req) {
        return Collections.emptyList();
    }

    @Override
    public <T> List<B> findComplex(T req, Sorteds sort) {
        return Collections.emptyList();
    }

    @Override
    public Page<B> findPage(Pagings pageable) {
        return null;
    }

    @Override
    public Page<B> findPage(PagingSorteds pageable) {
        return null;
    }

    @Override
    public <T> Page<B> findPage(T req, Pagings pageable) {
        return null;
    }

    @Override
    public <T> Page<B> findPage(T req, PagingSorteds pageable) {
        return null;
    }
}

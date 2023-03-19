package cn.jdevelops.data.jap.service.impl;

import cn.jdevelops.data.jap.core.JPAUtilExpandCriteria;
import cn.jdevelops.data.jap.dao.JpaBasicsDao;
import cn.jdevelops.data.jap.exception.JpaException;
import cn.jdevelops.data.jap.page.JpaPageResult;
import cn.jdevelops.data.jap.service.J2Service;
import cn.jdevelops.data.jap.util.JPageUtil;
import cn.jdevelops.data.jap.util.JpaUtils;
import cn.jdevelops.map.core.bean.ColumnUtil;
import cn.jdevelops.result.bean.SerializableBean;
import cn.jdevelops.result.request.PageDTO;
import cn.jdevelops.result.request.SortDTO;
import cn.jdevelops.result.request.SortPageDTO;
import cn.jdevelops.result.util.ListTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collections;
import java.util.List;


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
@NoRepositoryBean
public class J2ServiceImpl<M extends JpaBasicsDao<B, ID>, B extends SerializableBean<B>, ID> implements J2Service<B> {

    @Autowired
    private M commonDao;

    @Override
    public <M extends JpaBasicsDao<B, ID>, ID> M getJpaBasicsDao() {
        return (M) commonDao;
    }

    @Override
    public B saveByBean(B bean) {
        return commonDao.save(bean);
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
    public <U> Boolean deleteByUnique(List<U> unique, ColumnUtil.SFunction<B, ?> selectKey) {
        String field = ColumnUtil.getFieldName(selectKey);
        return commonDao.deleteByUnique(unique, field);
    }

    @Override
    public <U> Boolean deleteByUnique(U unique, ColumnUtil.SFunction<B, ?> selectKey) {
        String field = ColumnUtil.getFieldName(selectKey);
        return commonDao.deleteByUnique(Collections.singletonList(unique), field);
    }

    @Override
    public Boolean updateByBean(B bean) {
        try {
            commonDao.updateEntity(bean);
            return true;
        } catch (Exception e) {
            throw new JpaException("更新出错");
        }
    }

    @Override
    public B updateByBeanForBean(B bean) throws JpaException {
        return commonDao.updateEntity(bean);
    }

    @Override
    public Boolean updateByBean(B bean, ColumnUtil.SFunction<B, ?> selectKey) throws JpaException {
        try {
            String field = ColumnUtil.getFieldName(selectKey);
            commonDao.updateEntity(bean, field);
            return true;
        } catch (Exception e) {
            throw new JpaException("更新出错");
        }
    }

    @Override
    public B updateByBeanForBean(B bean, ColumnUtil.SFunction<B, ?> selectKey) throws JpaException {
        String field = ColumnUtil.getFieldName(selectKey);
        return commonDao.updateEntity(bean, field);
    }

    @Override
    public List<B> findAllBean() {
        return commonDao.findAll();
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
    public <R, T> JpaPageResult<R> findByBean(T req, PageDTO pageVO, Class<R> clazz) {
        JPAUtilExpandCriteria<B> selectRegionBean = JpaUtils.getSelectBean2(req);
        Pageable pageable = JPageUtil.getPageable(pageVO);
        Page<B> pages = commonDao.findAll(selectRegionBean, pageable);
        return JpaPageResult.toPage(pages, clazz);
    }

    @Override
    public <R, T> JpaPageResult<R> findByBean(T req, SortPageDTO sortPage, Class<R> clazz) {
        JPAUtilExpandCriteria<B> selectRegionBean = JpaUtils.getSelectBean2(req);
        Pageable pageable = JPageUtil.getPageable(sortPage);
        Page<B> pages = commonDao.findAll(selectRegionBean, pageable);
        return JpaPageResult.toPage(pages, clazz);
    }
}

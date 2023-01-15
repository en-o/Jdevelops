package cn.jdevelops.jpa.server.service.impl;

import cn.jdevelops.entity.basics.vo.SerializableVO;
import cn.jdevelops.jap.core.util.JpaUtils;
import cn.jdevelops.jap.core.util.JPAUtilExpandCriteria;
import cn.jdevelops.jap.core.util.JPageUtil;
import cn.jdevelops.jap.exception.JpaException;
import cn.jdevelops.jap.page.ResourceJpaPage;
import cn.jdevelops.jap.page.ResultJpaPageVO;
import cn.jdevelops.jpa.server.dao.JpaBasicsDao;
import cn.jdevelops.jpa.server.service.J2Service;
import cn.jdevelops.map.core.bean.ColumnUtil;
import cn.jdevelops.result.response.PageVO;
import cn.jdevelops.result.response.RoutinePageDTO;
import cn.jdevelops.result.response.SortVO;
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
 * @param <D> 实体的主键类型
 * @param <T> 实体
 * @param <M> 实体的Dao层
 * @author tn
 * @version 1
 * @date 2021/1/23 12:03
 */
@Slf4j
@NoRepositoryBean
public class J2ServiceImpl<M extends JpaBasicsDao<T, D>, T extends SerializableVO<T>, D> implements J2Service<T> {


    @Autowired
    private M commonDao;


    @Override
    public M getJpaBasicsDao() {
        return  commonDao;
    }

    @Override
    public T saveByBean(T bean) {
        return commonDao.save(bean);
    }

    @Override
    public Boolean saveAllByBoolean(List<T> bean) {
        try {
            commonDao.saveAll(bean);
        } catch (Exception e) {
            log.error("保存失败", e);
            return false;
        }
        return true;
    }

    @Override
    public List<T> saveAllByBean(List<T> bean) {
        return commonDao.saveAll(bean);
    }

    @Override
    public Boolean saveByBoolean(T bean) {
        commonDao.save(bean);
        return true;
    }

    @Override
    public <U> Boolean deleteByUnique(List<U> unique, String selectKey) {
        return commonDao.deleteByUnique(unique, selectKey);
    }

    @Override
    public <U> Boolean deleteByUnique(List<U> unique, ColumnUtil.SFunction<T, ?> selectKey) {
        String field = ColumnUtil.getFieldName(selectKey);
        return commonDao.deleteByUnique(unique, field);
    }

    @Override
    public <U> Boolean deleteByUnique(U unique, String selectKey) {
        return   commonDao.deleteByUnique(Collections.singletonList(unique), selectKey);
    }

    @Override
    public <U> Boolean deleteByUnique(U unique, ColumnUtil.SFunction<T, ?> selectKey) {
        String field = ColumnUtil.getFieldName(selectKey);
        return   commonDao.deleteByUnique(Collections.singletonList(unique), field);
    }


    @Override
    public Boolean updateByBean(T bean) {
        try {
            commonDao.updateEntity(bean);
            return true;
        } catch (Exception e) {
            throw new JpaException("更新出错");
        }
    }

    @Override
    public T updateByBeanForBean(T bean) throws JpaException {
        return commonDao.updateEntity(bean);
    }


    @Override
    public Boolean updateByBean(T bean, ColumnUtil.SFunction<T, ?> selectKey) throws JpaException {
        try {
            String field = ColumnUtil.getFieldName(selectKey);
            commonDao.updateEntity(bean, field);
            return true;
        } catch (Exception e) {
            throw new JpaException("更新出错");
        }
    }

    @Override
    public T updateByBeanForBean(T bean, ColumnUtil.SFunction<T, ?> selectKey) throws JpaException {
        String field = ColumnUtil.getFieldName(selectKey);
        return commonDao.updateEntity(bean, field);
    }


    @Override
    public List<T> findByBean(T t) {
        JPAUtilExpandCriteria<T> selectRegionBean = JpaUtils.getSelectBean(t);
        return commonDao.findAll(selectRegionBean);
    }

    @Override
    public <B> List<T> findComplex(B dto, SortVO sort) {
        JPAUtilExpandCriteria<T> selectRegionBean = JpaUtils.getSelectBean2(dto);
        return commonDao.findAll(selectRegionBean, JPageUtil.getSv2S(sort));
    }

    @Override
    public List<T> findAllBean() {
        return commonDao.findAll();
    }

    @Override
    public <R, B> ResourceJpaPage<R> findByBean(B t, PageVO pageVO, SortVO sortVO, Class<R> clazz) {
        JPAUtilExpandCriteria<T> selectRegionBean = JpaUtils.getSelectBean2(t);
        Pageable pageable = JPageUtil.getPageable(pageVO, sortVO);
        Page<T> pages = commonDao.findAll(selectRegionBean, pageable);
        return new ResourceJpaPage<>(pages, clazz);
    }


    @Override
    public <R, B> ResultJpaPageVO<R> findByBeanForVO(B t, PageVO pageVO, SortVO sortVO, Class<R> clazz) {
        JPAUtilExpandCriteria<T> selectRegionBean = JpaUtils.getSelectBean2(t);
        Pageable pageable = JPageUtil.getPageable(pageVO, sortVO);
        Page<T> pages = commonDao.findAll(selectRegionBean, pageable);
        return ResultJpaPageVO.success(new ResourceJpaPage<>(pages, clazz), "查询成功");
    }

    @Override
    public <R, B> ResourceJpaPage<R> findByBean(B t, RoutinePageDTO pageDTO, Class<R> clazz) {
        JPAUtilExpandCriteria<T> selectRegionBean = JpaUtils.getSelectBean2(t);
        Pageable pageable = JPageUtil.getPageable(pageDTO);
        Page<T> pages = commonDao.findAll(selectRegionBean, pageable);
        return new ResourceJpaPage<>(pages, clazz);
    }

    @Override
    public <R, B> ResultJpaPageVO<R> findByBeanForVO(B t, RoutinePageDTO pageDTO, Class<R> clazz) {
        JPAUtilExpandCriteria<T> selectRegionBean = JpaUtils.getSelectBean2(t);
        Pageable pageable = JPageUtil.getPageable(pageDTO);
        Page<T> pages = commonDao.findAll(selectRegionBean, pageable);
        return ResultJpaPageVO.success(new ResourceJpaPage<>(pages, clazz), "查询成功");
    }


}

package cn.jdevelops.jpa.server.service.impl;

import cn.jdevelops.entity.basics.vo.SerializableVO;
import cn.jdevelops.jap.core.util.CommUtils;
import cn.jdevelops.jap.core.util.JPAUtilExpandCriteria;
import cn.jdevelops.jap.core.util.JPageUtil;
import cn.jdevelops.jap.exception.JpaException;
import cn.jdevelops.jpa.server.dao.JpaBasicsDao;
import cn.jdevelops.jpa.server.service.J2Service;
import cn.jdevelops.map.core.bean.ColumnUtil;
import cn.jdevelops.result.page.ResourcePage;
import cn.jdevelops.result.response.PageVO;
import cn.jdevelops.result.response.RoutinePageDTO;
import cn.jdevelops.result.response.SortVO;
import cn.jdevelops.result.result.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;


/**
 * 预约模块公共service实现
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
        return this.commonDao;
    }

    @Override
    public T saveByBean(T bean) {
        return commonDao.save(bean);
    }

    @Override
    public Boolean saveAllByBoolean(List<T> bean) {
        try {
             commonDao.saveAll(bean);
        }catch (Exception e){
            log.error("保存失败",e);
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
    public Boolean deleteById(List<Integer> ids) {
        return commonDao.deleteByIdIn((List<D>) ids)>=0;
    }

    @Override
    public Boolean deleteById(Integer id) {
        try {
            commonDao.deleteById((D) id);
            return true;
        }catch (Exception e){
            log.error("根据id删除出错",e);
        }
        return false;
    }


    @Override
    public Boolean updateByBean(T bean) {
        try {
            return commonDao.updateEntity(bean);
        } catch (Exception e) {
            throw new JpaException("更新出错");
        }
    }

    @Override
    public Boolean updateByBean(T bean, String selectKey) throws Exception {
        return commonDao.updateEntity(bean,selectKey);
    }

    @Override
    public Boolean updateByBean(T bean, ColumnUtil.SFunction<T, ?> selectKey) throws Exception {
        String field = ColumnUtil.getFieldName(selectKey);
        return commonDao.updateEntity(bean,field);
    }


    @Override
    public T findById(Integer id) {
        return commonDao.findById((D) id).orElse(null);
    }

    @Override
    public List<T> findById(List<Integer> id) {
        return commonDao.findByIdIn((List<D>) id);
    }



    @Override
    public List<T> findByBean(T t) {
        JPAUtilExpandCriteria<T> selectRegionBean = CommUtils.getSelectBean(t);
        return commonDao.findAll(selectRegionBean);
    }

    @Override
    public <B> List<T> findComplex(B dto, SortVO sort) {
        JPAUtilExpandCriteria<T> selectRegionBean = CommUtils.getSelectBean2(dto);
        return commonDao.findAll(selectRegionBean, JPageUtil.getSv2S(sort));
    }

    @Override
    public List<T> findAllBean() {
        return commonDao.findAll();
    }

    @Override
    public <R,B> ResourcePage<List<R>> findByBean(B t, PageVO pageVO, SortVO sortVO, Class<R> clazz) {
        JPAUtilExpandCriteria<T> selectRegionBean = CommUtils.getSelectBean2(t);
        Pageable pageable = JPageUtil.getPageable(pageVO, sortVO);
        Page<T> pages = commonDao.findAll(selectRegionBean, pageable);
        return JPageUtil.to(pages, clazz);
    }


    @Override
    public <R,B> ResultVO<ResourcePage<List<R>>> findByBeanForVO(B t, PageVO pageVO, SortVO sortVO, Class<R> clazz) {
        JPAUtilExpandCriteria<T> selectRegionBean = CommUtils.getSelectBean2(t);
        Pageable pageable = JPageUtil.getPageable(pageVO, sortVO);
        Page<T> pages = commonDao.findAll(selectRegionBean, pageable);
        return ResultVO.success(JPageUtil.to(pages, clazz), "查询成功");
    }

    @Override
    public <R,B> ResourcePage<List<R>> findByBean(B t, RoutinePageDTO pageDTO, Class<R> clazz) {
        JPAUtilExpandCriteria<T> selectRegionBean = CommUtils.getSelectBean2(t);
        Pageable pageable = JPageUtil.getPageable(pageDTO);
        Page<T> pages = commonDao.findAll(selectRegionBean, pageable);
        return JPageUtil.to(pages, clazz);
    }

    @Override
    public <R,B> ResultVO<ResourcePage<List<R>>> findByBeanForVO(B t, RoutinePageDTO pageDTO, Class<R> clazz) {
        JPAUtilExpandCriteria<T> selectRegionBean = CommUtils.getSelectBean2(t);
        Pageable pageable = JPageUtil.getPageable(pageDTO);
        Page<T> pages = commonDao.findAll(selectRegionBean, pageable);
        return ResultVO.success(JPageUtil.to(pages, clazz), "查询成功");
    }




}

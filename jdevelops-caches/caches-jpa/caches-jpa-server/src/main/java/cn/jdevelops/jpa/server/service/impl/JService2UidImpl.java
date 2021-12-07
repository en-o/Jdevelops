package cn.jdevelops.jpa.server.service.impl;

import cn.jdevelops.entity.basics.vo.SerializableVO;
import cn.jdevelops.jap.core.util.CommUtils;
import cn.jdevelops.jap.core.util.JPAUtilExpandCriteria;
import cn.jdevelops.jap.core.util.JPageUtil;
import cn.jdevelops.jpa.server.dao.JpaBasics2UidDao;
import cn.jdevelops.jpa.server.enums.FieldName;
import cn.jdevelops.jpa.server.service.JService2Uid;
import cn.jdevelops.result.page.ResourcePage;
import cn.jdevelops.result.response.PageVO;
import cn.jdevelops.result.response.SortVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * 预约模块公共service实现
 *
 * @author tn
 * @version 1
 * @date 2021/1/23 12:03
 */
@Slf4j
@NoRepositoryBean
public class JService2UidImpl<T extends SerializableVO, D> implements JService2Uid<T> {

    @Autowired
    private JpaBasics2UidDao<T, D> commonDao;


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
        return Optional.ofNullable(commonDao.save(bean)).isPresent();
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
    public Boolean deleteByUuid(List<String> uuid) {
        return commonDao.deleteByUuidIn(uuid)>=0;
    }

    @Override
    public Boolean deleteByUuid(String uuid) {
        return commonDao.deleteByUuid(uuid)>=0;
    }

    @Override
    public Boolean updateByBean(T bean) {
        try {
            return commonDao.updateEntity(bean, "id");
        } catch (Exception e) {
            throw new RuntimeException("更新出错");
        }
    }

    @Override
    public Boolean updateByBean(T bean, FieldName fieldName) {
        try {
            return commonDao.updateEntity(bean, fieldName);
        } catch (Exception e) {
            throw new RuntimeException("更新出错");
        }
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
    public T findByUuid(String uuid) {
        return commonDao.findByUuid(uuid);
    }

    @Override
    public List<T> findByUuid(List<String> uuid) {
        return commonDao.findByUuidIn(uuid);
    }


    @Override
    public List<T> findByBean(T t) {
        JPAUtilExpandCriteria<T> selectRegionBean = CommUtils.getSelectBean(t);
        return commonDao.findAll(selectRegionBean);
    }

    @Override
    public List<T> findAllBean() {
        return commonDao.findAll();
    }

    @Override
    public <R> ResourcePage<List<R>> findByBean(T t, PageVO pageVO, SortVO sortVO, Class<R> clazz) {
        JPAUtilExpandCriteria<T> selectRegionBean = CommUtils.getSelectBean(t);
        Pageable pageable = JPageUtil.getPageable(pageVO, sortVO);
        Page<T> pages = commonDao.findAll(selectRegionBean, pageable);
        return JPageUtil.to(pages, clazz);
    }
}

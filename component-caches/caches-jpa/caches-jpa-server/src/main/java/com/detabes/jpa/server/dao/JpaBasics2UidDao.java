package com.detabes.jpa.server.dao;

import com.detabes.jap.core.util.CommUtils;
import com.detabes.jpa.server.enums.FieldName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 有uid的公共类
 *
 * @author tn
 * @className JpaBasics2UidDao
 * @date 2021-06-07 10:01
 */
public interface JpaBasics2UidDao<T, D> extends JpaRepository<T, D>, JpaSpecificationExecutor<T> {

    /**
     * 根据 UUID 查询
     *
     * @param uuid uuid
     * @return T
     */
    T findByUuid(String uuid);



    /**
     * 根据 UUID 查询
     *
     * @param uuid uuid
     * @return List<T>
     */
    List<T> findByUuidIn(List<String> uuid);

    /**
     * 根据 uuid删除 对象
     *
     * @param uuid
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    int deleteByUuidIn(List<String> uuid);


    /**
     * 根据 uuid删除 对象
     *
     * @param uuid
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    int deleteByUuid(String uuid);


    /**
     * 根据id删除批量删除
     *
     * @param ids id们
     * @return int
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    int deleteByIdIn(List<D> ids);


    /**
     * 根据id查询
     * @param id
     * @return
     */
    List<T> findByIdIn(List<D> id);


    /**
     * 更新 根据id
     *
     * @param t           实体类型的 数据
     * @param idFieldName 字段名只能是id
     * @return
     * @throws Exception
     */
    default Boolean updateEntity(T t, String idFieldName) throws Exception {
        /* 跟根据ID获取需要更新的数据的 原始数据 */
        T  oidCamera = findById((D) CommUtils.getFieldValueByName(FieldName.ID.getFieldName(), t))
                .orElse(null);
        /**
         *将新数据中非空字段 克隆到原始数据中 实现更新
         * <p> oidCamera.copy(scCameraEntity); </p>
         */
        /** 获取method对象，其中包含方法名称和参数列表*/
        Method setName = oidCamera.getClass().getMethod("copy", Object.class);
        /** 执行method，t为实例对象，后面是方法参数列表；setName没有返回值 */
        setName.invoke(oidCamera, t);
        /** 保存克隆之后的数据  且 saveAndFlush立即生效 */
        T save = saveAndFlush(oidCamera);
        return save != null;
    }

    /**
     * 更新 根据id，uuid
     *
     * @param t           实体类型的 数据
     * @param idFieldName 字段名
     * @return
     * @throws Exception
     */
    default Boolean updateEntity(T t, FieldName idFieldName) throws Exception {
        /* 跟根据ID获取需要更新的数据的 原始数据 */
        T oidCamera ;
        switch (idFieldName.getFieldName()){
            case "uuid":
                oidCamera = findByUuid((String) CommUtils.getFieldValueByName(idFieldName.getFieldName(), t));
                break;
            default:
                oidCamera = findById((D) CommUtils.getFieldValueByName(idFieldName.getFieldName(), t))
                        .orElse(null);
                break;
        }

        /**
         *将新数据中非空字段 克隆到原始数据中 实现更新
         * <p> oidCamera.copy(scCameraEntity); </p>
         */
        /** 获取method对象，其中包含方法名称和参数列表*/
        Method setName = oidCamera.getClass().getMethod("copy", Object.class);
        /** 执行method，t为实例对象，后面是方法参数列表；setName没有返回值 */
        setName.invoke(oidCamera, t);
        /** 保存克隆之后的数据  且 saveAndFlush立即生效 */
        T save = saveAndFlush(oidCamera);
        return save != null;
    }
}

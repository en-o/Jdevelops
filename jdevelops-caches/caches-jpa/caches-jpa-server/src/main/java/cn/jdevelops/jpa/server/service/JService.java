package cn.jdevelops.jpa.server.service;

import cn.jdevelops.result.page.ResourcePage;
import cn.jdevelops.result.response.PageVO;
import cn.jdevelops.result.response.RoutinePageDTO;
import cn.jdevelops.result.response.SortVO;
import cn.jdevelops.result.result.ResultVO;

import java.util.List;

/**
 * jpa公共service
 * 请使用J2Service
 * @param <T> 实体
 * @author tn
 * @date 2021-01-22 13:35
 */
@Deprecated
public interface JService<T> {

    /**
     * 保存数据 返回实体
     *
     * @param t 实体
     * @return T
     */
    T saveByBean(T t);

    /**
     * 保存list
     * @param bean bean
     * @return Boolean
     */
    Boolean saveAllByBoolean(List<T> bean);

    /**
     * 保存list
     * @param bean bean
     * @return List
     */
    List<T> saveAllByBean(List<T> bean);

    /**
     * 保存数据 返回 boolean
     *
     * @param t t
     * @return Boolean
     */
    Boolean saveByBoolean(T t);

    /**
     * 根据ids删除对象
     *
     * @param ids ids
     * @return Boolean
     */
    Boolean deleteById(final List<Integer> ids);

    /**
     * 根据id删除对象
     *
     * @param id id
     * @return Boolean
     */
    Boolean deleteById(final Integer id);


    /**
     * 更新数据 返回实体
     *
     * @param t 实体 id一定要有且键名为ID
     * @return Boolean
     */
    Boolean updateByBean(T t);


    /**
     * 根据 id 查询
     *  查询不到返回null
     * @param id id
     * @return T
     */
    T findById(final Integer id);

    /**
     * 根据 id 查询
     *
     * @param id id
     * @return T
     */
    List<T> findById(final List<Integer> id);


    /**
     * 默认可以不用的实现
     * 根据 t 中的数据查询 and
     *
     * @param t t
     * @return List<T>
     */
    List<T> findByBean(T t);

    /**
     * 查询所有
     * @return List
     */
    List<T> findAllBean();

    /**
     * 分页查询
     *
     * @param t      查询条件
     * @param pageVO 分页
     * @param sortVO 排序
     * @param clazz  返回实体类型
     * @param <R>    返回实体类型
     * @return ResourcePage<List < R>>
     */
    <R> ResourcePage<List<R>> findByBean(T t, PageVO pageVO, SortVO sortVO, Class<R> clazz);

    /**
     * 分页查询
     *
     * @param t      查询条件
     * @param pageVO 分页
     * @param sortVO 排序
     * @param clazz  返回实体类型
     * @param <R>    返回实体类型
     * @return ResourcePage<List < R>>
     */
    <R> ResultVO<ResourcePage<List<R>>> findByBeanForVO(T t, PageVO pageVO, SortVO sortVO, Class<R> clazz);

    /**
     * 分页查询
     *
     * @param t      查询条件
     * @param pageDTO 分页 排序
     * @param clazz  返回实体类型
     * @param <R>    返回实体类型
     * @return ResourcePage<List < R>>
     */
    <R> ResourcePage<List<R>> findByBean(T t, RoutinePageDTO pageDTO, Class<R> clazz);

    /**
     * 分页查询
     *
     * @param t      查询条件
     * @param pageDTO 分页 排序
     * @param clazz  返回实体类型
     * @param <R>    返回实体类型
     * @return ResourcePage<List < R>>
     */
    <R> ResultVO<ResourcePage<List<R>>> findByBeanForVO(T t, RoutinePageDTO pageDTO, Class<R> clazz);





}

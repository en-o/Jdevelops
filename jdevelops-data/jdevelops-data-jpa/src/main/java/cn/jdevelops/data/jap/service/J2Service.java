package cn.jdevelops.data.jap.service;

import cn.jdevelops.api.result.request.PageDTO;
import cn.jdevelops.api.result.request.SortDTO;
import cn.jdevelops.api.result.request.SortPageDTO;
import cn.jdevelops.api.result.util.bean.ColumnSFunction;
import cn.jdevelops.data.jap.exception.JpaException;
import cn.jdevelops.data.jap.repository.JpaBasicsRepository;
import cn.jdevelops.data.jap.result.JpaPageResult;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * jpa公共service
 *
 * @param <B> 实体
 * @author tn
 * @date 2021-01-22 13:35
 */
public interface J2Service<B> {

    /**
     * 获取 dao
     *
     * @param <M> dao
     * @return dao
     */
    <M extends JpaBasicsRepository<B, ID>, ID>  M getJpaBasicsDao();

    /**
     * 保存数据 返回实体
     *
     * @param bean 实体
     * @return T
     */
    B saveByBean(B bean);


    /**
     * 删除
     * @param unique 数据值
     * @param selectKey 数据key
     * @return boolean
     * @param <U> 数据值的类型
     */
    <U> boolean deleteByUnique(List<U> unique, String selectKey);


    /**
     * 自定义条件删除
     * @param spec Specification
     * @return long
     */
    long delete(Specification<B> spec);

    /**
     * 保存list
     *
     * @param bean bean
     * @return Boolean
     */
    Boolean saveAllByBoolean(List<B> bean);

    /**
     * 保存list
     *
     * @param bean bean
     * @return List
     */
    List<B> saveAllByBean(List<B> bean);

    /**
     * 保存数据 返回 boolean
     *
     * @param bean bean
     * @return Boolean
     */
    Boolean saveByBoolean(B bean);


    /**
     * 根据删除对象
     * ps： 失败会抛异常，如果有需要手动处理请接住他
     *
     * @param unique    唯一值
     * @param <U>       唯一值的类型
     * @param uniqueKey 唯一值的Key名
     * @return Boolean
     */
    <U> Boolean deleteByUnique(final List<U> unique, ColumnSFunction<B, ?> uniqueKey);


    /**
     * 根据删除对象
     * ps： 失败会抛异常，如果有需要手动处理请接住他
     *
     * @param unique    唯一值
     * @param <U>       唯一值的类型
     * @param uniqueKey 唯一值的Key名
     * @return Boolean
     */
    <U> Boolean deleteByUnique(final U unique, ColumnSFunction<B, ?> uniqueKey);


    /**
     * 更新数据 返回实体
     *
     * @param bean 实体 id一定要有且键名为ID
     * @return Boolean
     */
    <T> Boolean updateByBean(T bean);

    /**
     * 更新数据
     *
     * @param bean      实体 (指定的selectKey必须要有值)
     * @param uniqueKey 指定唯一键 (bean中必须要有selectKey的值)，e.g uuid
     * @return Boolean
     * @throws JpaException Exception
     */
    <T> Boolean updateByBean(T bean, String uniqueKey);

    /**
     * 更新数据
     *
     * @param bean      实体 (指定的selectKey必须要有值)
     * @param uniqueKey 指定唯一键 (bean中必须要有selectKey的值)，e.g uuid
     * @return Boolean
     * @throws JpaException Exception
     */
    <T> Boolean updateByBean(T bean, ColumnSFunction<T, ?> uniqueKey);


    /**
     * 查询所有
     *
     * @return List
     */
    List<B> findAllBean();


    /**
     * 查询
     * @param selectKey secret
     * @param value 值
     * @return B
     */
    Optional<B> findBeanOne(ColumnSFunction<B, ?> selectKey, Object value );



    /**
     * 查询
     * @param selectKey secret
     * @param value 值
     * @return B
     */
    Optional<B> findBeanOne(String selectKey, Object value );



    /**
     * 查询
     * @param selectKey secret
     * @param value 值
     * @return B
     */
    List<B> findBeanList( ColumnSFunction<B, ?> selectKey, Object value );


    /**
     * 查询
     * @param selectKey secret
     * @param value 值
     * @param sort  org.springframework.data.domain#Sort
     * @return B
     */
    List<B> findBeanList( ColumnSFunction<B, ?> selectKey, Object value, Sort sort );


    /**
     * 复杂查询
     *
     * @param req  数据实体的VO TDO BO PO等异形类
     * @param sort 排序
     * @return List<T> 返回数据库实体
     */
    <T> List<B> findComplex(T req, SortDTO sort);

    /**
     * 复杂查询
     *
     * @param req  数据实体的VO TDO BO PO等异形类
     * @return List<T> 返回数据库实体
     */
    <T> List<B> findComplex(T req);

    /**
     * 复杂查询
     *
     * @param req  数据实体的VO TDO BO PO等异形类
     * @param sort 排序
     * @param clazz 指定返回对象
     * @return List<T> 返回数据库实体
     */
    <T,R> List<R> findComplex(T req, SortDTO sort, Class<R> clazz);


    /**
     * 分页查询
     *
     * @param req      查询条件
     * @param page 分页
     * @param clazz  返回实体类型
     * @param <R>    返回实体类型
     * @param <T>  数据实体的VO TDO BO PO等异形类
     * @return ResourceJpaPage<List < VO>>
     */
    <R, T> JpaPageResult<R> findByBean(T req, PageDTO page, Class<R> clazz);


    /**
     * 分页查询
     *
     * @param req      查询条件
     * @param page 分页
     * @param <T>  数据实体的VO TDO BO PO等异形类
     * @return ResourceJpaPage<List < VO>>
     */
    <T> JpaPageResult<B> findByBean(T req, PageDTO page);


    /**
     * 分页查询
     *
     * @param req       查询条件实体
     * @param sortPage 分页 排序
     * @param clazz   返回实体类型
     * @param <R>     返回实体类型
     * @param <T>   数据实体的VO TDO BO PO等异形类
     * @return ResourceJpaPage<List < R>>
     */
    <R, T> JpaPageResult<R> findByBean(T req, SortPageDTO sortPage, Class<R> clazz);


    /**
     * 分页查询
     *
     * @param req       查询条件实体
     * @param sortPage 分页 排序
     * @param <T>   数据实体的VO TDO BO PO等异形类
     * @return ResourceJpaPage<List < R>>
     */
    <T> JpaPageResult<B> findByBean(T req, SortPageDTO sortPage);



}

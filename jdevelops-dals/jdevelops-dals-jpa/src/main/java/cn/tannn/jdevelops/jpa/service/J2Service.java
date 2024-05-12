package cn.tannn.jdevelops.jpa.service;


import cn.tannn.jdevelops.annotations.jpa.enums.SQLOperator;
import cn.tannn.jdevelops.jpa.repository.JpaBasicsRepository;
import cn.tannn.jdevelops.jpa.request.PagingSorteds;
import cn.tannn.jdevelops.jpa.request.Pagings;
import cn.tannn.jdevelops.jpa.request.Sorteds;
import cn.tannn.jdevelops.jpa.result.JpaPageResult;
import cn.tannn.jdevelops.result.bean.ColumnSFunction;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.Id;
import java.util.Collection;
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
    <ID, M extends JpaBasicsRepository<B, ID>> M getJpaBasicsDao();

    // ============ add

    /**
     * save
     *
     * @param bean bean
     * @return List of B
     */
    List<B> saves(List<B> bean);

    /**
     * save
     *
     * @param bean bean
     * @return B
     */
    B saveOne(B bean);


    /**
     * save
     *
     * @param bean bean
     * @param <V>  传入的对象可以是一个VO变形，但是必须要跟B对象对其字段，可以增减,不能不一样
     * @return B
     */
    <V> B saveOneByVo(V bean);

    // ============ delete

    /**
     * 自定义条件删除
     *
     * @param spec 自定义条件{@link  cn.tannn.jdevelops.jpa.utils.SpecificationUtil}
     * @return int (>0删除成功且为删除了多少条)
     */
    int delete(Specification<B> spec);

    /**
     * 删除  (fieldName=vale) 常用
     *
     * @param fieldName 实体里的字段名 建议{@link ColumnSFunction}
     * @param value     删除的条件
     * @return int (>0删除成功且为删除了多少条)
     */
    int delete(String fieldName, Object value);

    /**
     * 删除
     *
     * @param fieldName 实体里的字段名 建议{@link ColumnSFunction}
     * @param operator  判断表达式  {@link SQLOperator}（ 等于，小于，模糊 ...) (ps. in between 这种就传多个值，其他的根据情况而定，比如ISNULL这种就不用传值)
     * @param value     删除的条件
     * @return int (>0删除成功且为删除了多少条)
     */
    int delete(String fieldName, SQLOperator operator, Object... value);

    /**
     * 删除
     *
     * @param wheres   非空属性才会被删除 [请注意 '' ' ' 一律会被认为时空]
     * @param operator 判断表达式 {@link SQLOperator}  (ps. 此处单值操作有效， 如in,isnull,between等是无效的)
     * @return int (>0删除成功且为删除了多少条)
     */
    int delete(B wheres, SQLOperator operator);


    // ============ update

    /**
     * 更新数据 返回实体
     *
     * @param bean     实体,VO [@Id标注字段一定要有值，如果时vo的要一定要有实体里{@link Id}标注的字段]
     * @param operator 判断表达式 （ 等于，小于，模糊 ...
     * @return Boolean
     */
    <T> Boolean update(T bean, SQLOperator operator);

    /**
     * 更新数据 返回实体
     *
     * @param bean      实体,VO,DTO
     * @param operator  判断表达式  {@link SQLOperator}（ 等于，小于，模糊 ...) (ps. in between 这种就传多个值，其他的根据情况而定，比如ISNULL这种就不用传值)
     * @param uniqueKey 指定 bean 用作更新的条件字段名（实体里的字段
     * @return Boolean
     */
    <T> Boolean update(T bean, SQLOperator operator, String... uniqueKey);


    // ============ find only

    /**
     * 查询
     *
     * @param fieldName 实体里的字段名 建议{@link ColumnSFunction}
     * @param value     查询条件
     * @return B
     */
    Optional<B> findOnly(String fieldName, Object value);

    /**
     * 查询
     *
     * @param fieldName  实体里的字段名 建议{@link ColumnSFunction}
     * @param value      查询条件
     * @param fieldName2 实体里的字段名 建议{@link ColumnSFunction}
     * @param value2     查询条件
     * @return B
     */
    Optional<B> findOnly(String fieldName, Object value, String fieldName2, Object value2);

    /**
     * 查询
     *
     * @param spec 自定义条件{@link  cn.tannn.jdevelops.jpa.utils.SpecificationUtil}
     * @return B
     */
    Optional<B> findOnly(Specification<B> spec);

    // ============ find list

    /**
     * 查询所有
     *
     * @return List （如果想将Bean转换成VO，请使用{@link cn.tannn.jdevelops.result.utils.ListTo#to(Class, Collection)}）
     */
    List<B> finds();

    /**
     * 查询添加查询
     *
     * @param fieldName 实体里的字段名 建议{@link ColumnSFunction}
     * @param operator  判断表达式  {@link SQLOperator}（ 等于，小于，模糊 ...) (ps. in between 这种就传多个值，其他的根据情况而定，比如ISNULL这种就不用传值)
     * @param value     查询条件
     * @return list of B （如果想将Bean转换成VO，请使用{@link cn.tannn.jdevelops.result.utils.ListTo#to(Class, Collection)}）
     */
    List<B> finds(String fieldName, SQLOperator operator, Object... value);


    /**
     * 查询添加查询
     *
     * @param fieldName 实体里的字段名 建议{@link ColumnSFunction}
     * @param operator  判断表达式  {@link SQLOperator}（ 等于，小于，模糊 ...) (ps. in between 这种就传多个值，其他的根据情况而定，比如ISNULL这种就不用传值)
     * @param sort 排序 {@link Sorteds}
     * @param value     查询条件
     * @return list of B （如果想将Bean转换成VO，请使用{@link cn.tannn.jdevelops.result.utils.ListTo#to(Class, Collection)}）
     */
    List<B> finds(String fieldName, SQLOperator operator, Sorteds sort, Object... value);


    /**
     * 查询添加查询
     *
     * @param spec 自定义条件{@link  cn.tannn.jdevelops.jpa.utils.SpecificationUtil}
     * @param sort 排序 {@link Sorteds}
     * @return list of B （如果想将Bean转换成VO，请使用{@link cn.tannn.jdevelops.result.utils.ListTo#to(Class, Collection)}）
     */
    List<B> finds(Specification<B> spec, Sorteds sort);


    /**
     * 复杂查询
     *
     * @param req 数据实体的VO TDO BO PO等异形类
     * @return List<T> 返回数据库实体 （如果想将Bean转换成VO，请使用{@link cn.tannn.jdevelops.result.utils.ListTo#to(Class, Collection)}）
     */
    <T> List<B> findComplex(T req);

    /**
     * 复杂查询
     *
     * @param req  数据实体的VO TDO BO PO等异形类
     * @param sort 排序 {@link Sorteds}
     * @return List<T> 返回数据库实体 （如果想将Bean转换成VO，请使用{@link cn.tannn.jdevelops.result.utils.ListTo#to(Class, Collection)}）
     */
    <T> List<B> findComplex(T req, Sorteds sort);


    // ============ find page

    /**
     * 分页-排序
     * @param pageable 分页{@link Pagings}
     * @return Page of B
     */
    Page<B> findPage(Pagings pageable);

    /**
     * 分页-排序
     * @param pageable 分页{@link PagingSorteds}
     * @return Page of B
     */
    Page<B> findPage(PagingSorteds pageable);

    /**
     * 分页-查询
     *
     * @param req      查询条件
     * @param pageable 分页 {@link Pagings}
     * @param <T>  数据实体的VO TDO BO PO等异形类
     * @return page  如果想要处理成接口能返回的请使用{@link JpaPageResult#toPage(Page)}
     */
     <T> Page<B>  findPage(T req, Pagings pageable);

    /**
     * 分页-查询
     *
     * @param req      查询条件
     * @param pageable 分页 {@link PagingSorteds}
     * @param <T>  数据实体的VO TDO BO PO等异形类
     * @return page  如果想要处理成接口能返回的请使用{@link JpaPageResult#toPage(Page)}
     */
    <T> Page<B>  findPage(T req, PagingSorteds pageable);
}

package cn.jdevelops.data.jap.repository;

import cn.jdevelops.data.jap.core.JPAUtilExpandCriteria;
import cn.jdevelops.data.jap.core.criteria.Restrictions;
import cn.jdevelops.data.jap.enums.FieldName;
import cn.jdevelops.data.jap.exception.JpaException;
import cn.jdevelops.data.jap.util.ReflectUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.FluentQuery;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

/**
 * 公共dao层
 * <br></>
 * 默认了 两个方法  deleteByIdIn（删除），updateEntity（更新）
 * <br></>
 * {NoRepositoryBean：Spring Data Jpa在启动时就不会去实例化BaseRepository这个接口}
 * <br></>
 *
 * @author tn
 * JpaSpecificationExecutor ：JPA复杂查询
 * JpaRepository 普通查询
 * @date 2020/5/14 15:31
 */
@NoRepositoryBean
public interface JpaBasicsRepository<B, ID> extends JpaRepository<B, ID>, JpaSpecificationExecutor<B> {

    /**
     * 根据删除对象
     *
     * @param unique    唯一值
     * @param <U>       唯一值的类型
     * @param selectKey 唯一值的Key名
     * @return int
     */
    <U> boolean deleteByUnique(List<U> unique, String selectKey);


    /**
     * 更新 根据id(id必须要有值)
     *
     * @param t 实体类型的 数据
     * @return boolean
     * @throws JpaException JpaException
     */
    boolean updateEntity(B t) throws JpaException;
//    default B updateEntity(B t) throws JpaException {
//       try {
//           /* 跟根据ID获取需要更新的数据的 原始数据 */
//           B oidCamera = findById((ID) ReflectUtils.getFieldValueByName(FieldName.ID.getFieldName(), t))
//                   .orElse(null);
//           /*
//            *将新数据中非空字段 克隆到原始数据中 实现更新
//            * <p> oidCamera.copy(scCameraEntity); </p>
//            */
//           /* 获取method对象，其中包含方法名称和参数列表*/
//           Method setName = oidCamera.getClass().getMethod("copy", Object.class);
//           /* 执行method，t为实例对象，后面是方法参数列表；setName没有返回值 */
//           setName.invoke(oidCamera, t);
//           /* 保存克隆之后的数据  且 saveAndFlush立即生效 */
//           saveAndFlush(oidCamera);
//           return oidCamera;
//       }catch (Exception e){
//           throw new JpaException("更新失败！",e);
//       }
//    }


    /**
     * 更新 根据只指定key
     *
     * @param t         实体类型的 数据
     * @param selectKey 指定唯一键 (t中必须要有selectKey的值)，e.g uuid
     * @return Boolean
     * @throws JpaException JpaException
     */
    default B updateEntity(B t, String selectKey) throws JpaException {
        try {
            /* 跟根据ID获取需要更新的数据的 原始数据 */
            JPAUtilExpandCriteria<B> jpaSelect = new JPAUtilExpandCriteria<>();
            jpaSelect.add(Restrictions.eq(selectKey, ReflectUtils.getFieldValueByName(selectKey, t), false));
            B oidCamera = findOne(jpaSelect).orElseThrow(() -> new JpaException("更新失败，查询数据为空"));
            /*
             *将新数据中非空字段 克隆到原始数据中 实现更新
             * <p> oidCamera.copy(scCameraEntity); </p>
             */
            /* 获取method对象，其中包含方法名称和参数列表*/
            Method setName = oidCamera.getClass().getMethod("copy", Object.class);
            /* 执行method，t为实例对象，后面是方法参数列表；setName没有返回值 */
            setName.invoke(oidCamera, t);
            /* 保存克隆之后的数据  且 saveAndFlush立即生效 */
            saveAndFlush(oidCamera);
            return oidCamera;
        }catch (Exception e){
            String msg = "更新失败！";
            if(e instanceof JpaException){
                msg = e.getMessage();
            }
            throw new JpaException(msg,e);
        }
    }



    /**
     * 删除
     * Deletes by the {@link Specification} and returns the number of rows deleted.
     * <p>
     * database delete operations. The persistence context is not synchronized with the result of the bulk delete.
     * <p>
     * {@code CriteriaQuery}.
     * @param spec  Specification
     * @return  long
     */
    long delete(Specification<B> spec);


}

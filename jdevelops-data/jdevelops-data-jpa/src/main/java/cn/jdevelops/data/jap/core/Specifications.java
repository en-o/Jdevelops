package cn.jdevelops.data.jap.core;

import cn.hutool.core.util.ReflectUtil;
import cn.jdevelops.data.jap.annotation.JpaSelectIgnoreField;
import cn.jdevelops.data.jap.annotation.JpaSelectWrapperOperator;
import cn.jdevelops.data.jap.core.specification.OperatorWrapper;
import cn.jdevelops.data.jap.core.specification.SpecificationWrapper;
import cn.jdevelops.data.jap.enums.SQLOperatorWrapper;
import cn.jdevelops.data.jap.util.IObjects;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;


/**
 * Specification 查询
 * @author tan
 * @date 2023-03-24 10:59:17
 */
public class Specifications {

    /**
     *  自定义查询
     *  默认and连接（where x=1 and y=2 and (z=3 or g=4)） (and 为默认，or在action中定义)
     * @param action 查询参数
     * @param <B>  查询对象
     * @return Specification
     */
    public static <B> Specification<B> where(Consumer<SpecificationWrapper<B>> action) {
        return where(true, action);
    }

    /**
     *  自定义查询
     * @param isConnect 连接符： true用and(默认), fales用or （where x=1 and y=2 and (z=3 or g=4)） (and 为默认，or在action中定义)
     * @param action Query code
     * @param <B>  查询对象
     * @return Specification
     */
    public static <B> Specification<B> where(boolean isConnect, Consumer<SpecificationWrapper<B>> action) {
        return (Root<B> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            SpecificationWrapper<B> specification = new SpecificationWrapper<>(root, query, builder);
            action.accept(specification);
            List<Predicate> predicates = specification.getPredicates();
            Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);
            return isConnect?builder.and(arr):builder.or(arr);
        };
    }

    /**
     * 根据实体自动组装
     * 默认and连接（where x=1 and y=2 and (z=3 or g=4)） (and 为默认，or在action中定义)
     * @param bean 构造的查询对象
     * @return Specification
     * @param <R> 返回对象
     * @param <B> 查询对象
     */
    public static <R,B> Specification<R> beanWhere(B bean) {
        return beanWhere(true, bean, e -> {});
    }

    /**
     * 根据实体自动组装
     * 默认or连接（where x=1 or y=2 or (z=3 and g=4)） (or 为默认，and在action中定义)
     * @param bean 构造的查询对象
     * @return Specification
     * @param <R> 返回对象
     * @param <B> 查询对象
     */
    public static <R,B> Specification<R> beanWhereOr(B bean) {
        return beanWhere(false, bean, e -> {});
    }

    /**
     *  根据实体自动组装 + 自定义查询
     * @param isConnect 连接符： true用and(默认), fales用or （where x=1 and y=2 and (z=3 or g=4)） (and 为默认，or在action中定义)
     * @param action 操作
     * @param bean 构造的查询对象
     * @return Specification
     * @param <R> 返回对象
     * @param <B> 查询对象
     */
    public static <R,B> Specification<R> beanWhere(boolean isConnect ,B bean, Consumer<SpecificationWrapper<R>> action) {
        Field[] fields = ReflectUtil.getFields(bean.getClass());
        return where(isConnect, e -> {
            for (int i = 0, fieldsLength = fields.length; i < fieldsLength; i++) {
                Field field = fields[i];
                // 字段名
                String fieldName = field.getName();
                if ("serialVersionUID".equals(fieldName)) {
                    continue;
                }
                // 字段值
                Object fieldValue = ReflectUtil.getFieldValue(bean, field);
                // 字段被忽略
                JpaSelectIgnoreField ignoreField = field.getAnnotation(JpaSelectIgnoreField.class);
                if (IObjects.nonNull(ignoreField)) {
                    continue;
                }
                // 获取组装条件
                JpaSelectWrapperOperator query = field.getAnnotation(JpaSelectWrapperOperator.class);
                if (IObjects.nonNull(query)) {
                    // 空值就不查了
                    if(query.ignoreNull()&&IObjects.isNull(fieldValue)){
                        continue;
                    }
                    // 默认 eq，且空值也查询
                    SQLOperatorWrapper operator = IObjects.nonNull(query)? query.operatorWrapper():SQLOperatorWrapper.EQ;
                    // 如果 值等于 list 则 使用 In 操作
                    if(fieldValue instanceof Collection){
                        operator = SQLOperatorWrapper.IN;
                    }
                    // 构造 OperatorWrapper
                    OperatorWrapper wrapper = new OperatorWrapper(e,fieldValue);
                    // 自定义字段名
                    if(!IObjects.isBlank(query.fieldName())){
                        wrapper.setSelectKey(query.fieldName());
                    }else {
                        wrapper.setSelectKey(fieldName);
                        operator.consumer().accept(wrapper);
                    }
                }else {
                    // 没有注解所有属性都要处理成条件
                    // 构造 OperatorWrapper
                    OperatorWrapper wrapper = new OperatorWrapper(e,fieldValue);
                    wrapper.setSelectKey(fieldName);
                    SQLOperatorWrapper.EQ.consumer().accept(wrapper);
                }

            }
            action.accept(e);
        });
    }

}

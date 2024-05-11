package cn.tannn.jdevelops.jpa.select;

import cn.hutool.core.util.ReflectUtil;
import cn.tannn.jdevelops.annotations.jpa.JpaSelectIgnoreField;
import cn.tannn.jdevelops.annotations.jpa.JpaSelectOperator;
import cn.tannn.jdevelops.annotations.jpa.enums.SQLOperatorWrapper;
import cn.tannn.jdevelops.annotations.jpa.enums.SpecBuilderDateFun;
import cn.tannn.jdevelops.annotations.jpa.specification.OperatorWrapper;
import cn.tannn.jdevelops.annotations.jpa.specification.SpecificationWrapper;
import cn.tannn.jdevelops.jpa.utils.IObjects;
import cn.tannn.jdevelops.jpa.utils.JpaUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;


/**
 * 增强 Specification 查询
 *
 * @author tan
 * @date 2023-03-24 10:59:17
 */
public class EnhanceSpecification {
    private static final String SEPARATOR = ".";

    /**
     * 自定义查询
     * 默认and连接（where x=1 and y=2 and (z=3 or g=4)） (and 为默认，or在action中定义)
     *
     * @param action 查询参数
     * @param <B>    查询对象
     * @return Specification
     */
    public static <B> Specification<B> where(Consumer<SpecificationWrapper<B>> action) {
        return where(true, action);
    }

    /**
     * 自定义查询
     *
     * @param isConnect 连接符： true用and(默认), fales用or （where x=1 and y=2 and (z=3 or g=4)） (and 为默认，or在action中定义)
     * @param action    Query code
     * @param <B>       查询对象
     * @return Specification
     */
    public static <B> Specification<B> where(boolean isConnect, Consumer<SpecificationWrapper<B>> action) {
        return (Root<B> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            SpecificationWrapper<B> specification = new SpecificationWrapper<>(root, query, builder);
            action.accept(specification);
            List<Predicate> predicates = specification.getPredicates();
            Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);
            return isConnect ? builder.and(arr) : builder.or(arr);
        };
    }

    /**
     * 根据实体自动组装
     * 默认and连接（where x=1 and y=2 and (z=3 or g=4)） (and 为默认，or在action中定义)
     *
     * @param bean 构造的查询对象
     * @param <R>  返回对象
     * @param <B>  查询对象
     * @return Specification
     */
    public static <R, B> Specification<R> beanWhere(B bean) {
        return beanWhere(true, bean, e -> {
        });
    }

    /**
     * 根据实体自动组装
     * 默认or连接（where x=1 or y=2 or (z=3 and g=4)） (or 为默认，and在action中定义)
     *
     * @param bean 构造的查询对象
     * @param <R>  返回对象
     * @param <B>  查询对象
     * @return Specification
     */
    public static <R, B> Specification<R> beanWhereOr(B bean) {
        return beanWhere(false, bean, e -> {
        });
    }

    /**
     * 根据实体自动组装 + 自定义查询
     *
     * @param isConnect 连接符： true用and(默认), fales用or （where x=1 and y=2 and (z=3 or g=4)） (and 为默认，or在action中定义)
     * @param bean      构造的查询对象
     * @param action    除了bean还能自定义操作
     * @param <R>       返回对象
     * @param <B>       查询对象
     * @return Specification
     */
    public static <R, B> Specification<R> beanWhere(boolean isConnect, B bean, Consumer<SpecificationWrapper<R>> action) {
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
                JpaSelectOperator wrapperOperator = field.getAnnotation(JpaSelectOperator.class);

                if (IObjects.nonNull(wrapperOperator)) {
                    // 需要判空，然后空值就不查了
                    if (Boolean.TRUE.equals(wrapperOperator.ignoreNull())
                            && IObjects.isNull(fieldValue, wrapperOperator.ignoreNullEnhance())) {
                        continue;
                    }
//                    wrapperOperator.connect().equals(SQLConnect.AND)
                    // 默认 eq，且空值也查询
                    SQLOperatorWrapper operator = IObjects.nonNull(wrapperOperator) ? wrapperOperator.operatorWrapper() : SQLOperatorWrapper.EQ;
                    // 如果 值等于 list 则 使用 In 操作
                    if (fieldValue instanceof Collection) {
                        operator = SQLOperatorWrapper.IN;
                    }
                    // 使用自定义的名字
                    if (!IObjects.isBlank(wrapperOperator.fieldName())) {
                        fieldName = wrapperOperator.fieldName();
                    }
                    OperatorWrapper operatorWrapper = new OperatorWrapper(e, str2Path(e.getRoot()
                            ,e.getBuilder()
                            ,wrapperOperator.function()
                            ,fieldName
                    ), fieldValue);

                    operator.consumer().accept(operatorWrapper);
                } else {
                    // 没加查询注解的且没有被忽略的，默认设添加为 and  eq 查询条件 ， 且为空值就不查了
                    // 构造 OperatorWrapper // 空值就不查了
                    if (IObjects.nonNull(fieldValue)) {
                        OperatorWrapper wrapper = new OperatorWrapper(e, str2Path(e.getRoot()
                                ,e.getBuilder()
                                , SpecBuilderDateFun.NULL
                                ,fieldName
                        ), fieldValue);
                        SQLOperatorWrapper.EQ.consumer().accept(wrapper);
                    }
                }
            }
            action.accept(e);
        });
    }


    /**
     * 字符串的key名转jpa要用的对象
     */
    private static Expression str2Path(Root<?> root, CriteriaBuilder builder,SpecBuilderDateFun function, String fieldName) {
        Path<?> path;
        if (null != function && !function.equals(SpecBuilderDateFun.NULL)) {
            return JpaUtils.functionTimeFormat(function, root, builder, fieldName);
        }
        if (fieldName.contains(SEPARATOR)) {
            String[] names = fieldName.split("\\" + SEPARATOR);
            path = root.get(names[0]);
            for (int i = 1; i < names.length; i++) {
                path = path.get(names[i]);
            }
        } else {
            path = root.get(fieldName);
        }
        return path;
    }

}

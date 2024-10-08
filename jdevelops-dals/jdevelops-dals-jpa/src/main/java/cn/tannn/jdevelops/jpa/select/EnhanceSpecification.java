package cn.tannn.jdevelops.jpa.select;

import cn.hutool.core.util.ReflectUtil;
import cn.tannn.jdevelops.annotations.jpa.JpaSelectIgnoreField;
import cn.tannn.jdevelops.annotations.jpa.JpaSelectNullField;
import cn.tannn.jdevelops.annotations.jpa.JpaSelectOperator;
import cn.tannn.jdevelops.annotations.jpa.enums.SQLConnect;
import cn.tannn.jdevelops.annotations.jpa.enums.SQLOperatorWrapper;
import cn.tannn.jdevelops.annotations.jpa.enums.SpecBuilderDateFun;
import cn.tannn.jdevelops.annotations.jpa.specification.OperatorWrapper;
import cn.tannn.jdevelops.annotations.jpa.specification.SpecificationWrapper;
import cn.tannn.jdevelops.jpa.exception.JpaException;
import cn.tannn.jdevelops.jpa.utils.IObjects;
import cn.tannn.jdevelops.jpa.utils.JpaUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
    private static final Logger LOG = LoggerFactory.getLogger(EnhanceSpecification.class);

    /**
     * 自定义查询 (主方法）
     *
     * @param action Query code
     * @param <B>    查询对象
     * @return Specification
     */
    public static <B> Specification<B> where(Consumer<SpecificationWrapper<B>> action) {
        return (Root<B> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            SpecificationWrapper<B> specification = new SpecificationWrapper<>(root, query, builder);
            try {
                action.accept(specification);
            } catch (Exception e) {
                LOG.error("Error occurred while building query specification: ", e);
                // 根据需求行为，这里可以选择抛出运行时异常或者返回始终为真的Predicate
                throw new JpaException("Error building query specification", e);
            }
            List<Predicate> predicates = specification.getPredicates();
            if (predicates == null || predicates.isEmpty()) {
                // 返回个空的 = 没有where
                return query.getRestriction();
            } else {
                return JpaUtils.combinePredicates(builder, predicates);
            }
        };
    }

    /**
     * 根据实体自动组装
     *
     * @param bean 构造的查询对象 ，可以配合 {@link JpaSelectOperator}  {@link JpaSelectNullField}   {@link JpaSelectIgnoreField}
     * @param <R>  返回对象
     * @param <B>  查询对象
     * @return Specification
     */
    public static <R, B> Specification<R> beanWhere(B bean) {
        return beanWhere(bean, e -> {
        });
    }

    /**
     * 根据实体自动组装 + 自定义查询
     *
     * @param bean     构造的查询对象，可以配合 {@link JpaSelectOperator}  {@link JpaSelectNullField}   {@link JpaSelectIgnoreField}
     * @param operator 除了bean还能自定义操作
     * @param <R>      返回对象
     * @param <B>      查询对象
     * @return Specification
     */
    public static <R, B> Specification<R> beanWhere(B bean, Consumer<SpecificationWrapper<R>> operator) {
        if(bean == null){
            // 为空不处理条件了之际返回  getRestriction
            return (r,q,b) -> q.getRestriction();
        }
        Field[] fields = ReflectUtil.getFields(bean.getClass());
        return where(specification -> {
            /*
             * 从注解里得到 SpecificationWrapper 并组装数据
             * 这里类似 组装
             * <code>
                  specification.and(e2 -> {
                            e2.eq(true,"phone", "123");
                            e2.likes(true,"address", "重");
                        }
                );
             * <code>
             */
            for (int i = 0, fieldsLength = fields.length; i < fieldsLength; i++) {
                Field field = fields[i];
                // 字段名
                String fieldName = field.getName();
                if ("serialVersionUID".equals(fieldName)) {
                    continue;
                }
                if(Modifier.isStatic(field.getModifiers())){
                    continue;
                }
                // 字段值
                Object fieldValue = ReflectUtil.getFieldValue(bean, field);
                // 字段被忽略
                JpaSelectIgnoreField ignoreField = field.getAnnotation(JpaSelectIgnoreField.class);
                if (IObjects.nonNull(ignoreField)) {
                    continue;
                }
                // 或者字段注解 - 标记计算表达式，标记空值处理
                JpaSelectOperator wrapperOperator = field.getAnnotation(JpaSelectOperator.class);
                JpaSelectNullField valueNull = field.getAnnotation(JpaSelectNullField.class);

                if (IObjects.nonNull(wrapperOperator)) {
                    // =========== 空值处理
                    if (IObjects.isNull(valueNull)) {
                        valueNull = wrapperOperator.nullField();
                    }
                    // 需要判空，然后空值就不查了
                    if (Boolean.TRUE.equals(valueNull.ignoreNull())
                            && IObjects.isNull(fieldValue, valueNull.ignoreNullEnhance())) {
                        continue;
                    }


                    // =========== 得到注解标注的 SQLOperatorWrapper ， 里面有 SpecificationWrapper
                    SQLOperatorWrapper operatorBean = IObjects
                            .nonNull(wrapperOperator) ? wrapperOperator.operatorWrapper() : SQLOperatorWrapper.EQ;

                    // 如果 值等于 list 则 使用 In 操作
                    if (fieldValue instanceof Collection) {
                        operatorBean = SQLOperatorWrapper.IN;
                    }
                    // 使用自定义的名字
                    if (!IObjects.isBlank(wrapperOperator.fieldName())) {
                        fieldName = wrapperOperator.fieldName();
                    }
                    // =========== 构造 注解里的 OperatorWrapper 详细参数数据， key , value
                    OperatorWrapper operatorWrapper = new OperatorWrapper(specification, JpaUtils.str2Path(specification.getRoot()
                            , specification.getBuilder()
                            , wrapperOperator.function()
                            , fieldName
                    ), fieldValue);

                    // 将完整的参数体  operatorWrapper 传入  SQLOperatorWrapper
                    // 并根据 SQLOperatorWrapper.xx().name() 构造  SpecificationWrapper
                    // SpecificationWrapper的构建过程方法在枚举参数里完成的
                    operatorBean.consumer().accept(operatorWrapper, wrapperOperator.connect().equals(SQLConnect.AND));
                } else {
                    // 没加查询注解的且没有被忽略的，默认设添加为 and  eq 查询条件 ， 且为空值就不查了
                    // 构造 OperatorWrapper // 空值就不查了

                    // =========== 空值处理
                    if (IObjects.nonNull(valueNull) && Boolean.TRUE.equals(valueNull.ignoreNull())
                            && IObjects.isNull(fieldValue, valueNull.ignoreNullEnhance())) {
                        // 需要判空，然后空值就不查了
                        continue;
                    }
                    if (fieldValue != null) {
                        OperatorWrapper wrapper = new OperatorWrapper(specification, JpaUtils.str2Path(specification.getRoot()
                                , specification.getBuilder()
                                , SpecBuilderDateFun.NULL
                                , fieldName
                        ), fieldValue);
                        SQLOperatorWrapper.EQ.consumer().accept(wrapper, true);
                    }

                }
            }
            // 执行自定义的操作,得到过程和结果与上面的for差不多(operator 里面是处理过程，specification 是数据体）
            operator.accept(specification);
        });
    }


}

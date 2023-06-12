package cn.jdevelops.data.jap.util;

import cn.hutool.core.util.ReflectUtil;
import cn.jdevelops.api.result.util.bean.ColumnSFunction;
import cn.jdevelops.api.result.util.bean.ColumnUtil;
import cn.jdevelops.data.jap.annotation.JpaSelectIgnoreField;
import cn.jdevelops.data.jap.annotation.JpaSelectOperator;
import cn.jdevelops.data.jap.core.JPAUtilExpandCriteria;
import cn.jdevelops.data.jap.core.criteria.Restrictions;
import cn.jdevelops.data.jap.core.criteria.SimpleExpression;
import cn.jdevelops.data.jap.enums.SQLConnect;
import cn.jdevelops.data.jap.enums.SpecBuilderDateFun;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Jpa项目里的工具类
 *
 * @author tn
 * @version 1
 * @date 2020/6/28 23:16
 */
public class JpaUtils {


    /**
     * 多条件查询 的 默认全员AND
     *
     * @param bean 多条件查询
     * @param <T>  返回参数
     * @param <B>  实参类型
     * @return JPAUtilExpandCriteria
     */
    public static <T, B> JPAUtilExpandCriteria<T> getSelectBean2(B bean) {
        return getJpaUtilExpandCriteria(bean);
    }

    /**
     * 多条件查询 的 默认全员AND
     * PS: 建议使用 getSelectBean2
     *
     * @param bean 多条件查询
     * @param <T>  多条件查询
     * @return JPAUtilExpandCriteria
     */
    public static <T> JPAUtilExpandCriteria<T> getSelectBean(T bean) {
        return getJpaUtilExpandCriteria(bean);
    }


    /**
     * 组装查询条件
     *
     * @param bean 条件实体
     * @param <T>  返回实体
     * @param <B>  条件实体
     * @return JPAUtilExpandCriteria
     */
    private static <T, B> JPAUtilExpandCriteria<T> getJpaUtilExpandCriteria(B bean) {
        JPAUtilExpandCriteria<T> jpaSelect = new JPAUtilExpandCriteria<>();
        Field[] fields = ReflectUtil.getFields(bean.getClass());
        for (int i = 0, fieldsLength = fields.length; i < fieldsLength; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if ("serialVersionUID".equals(fieldName)) {
                continue;
            }
            // 字段被忽略
            JpaSelectIgnoreField ignoreField = field.getAnnotation(JpaSelectIgnoreField.class);
            if (IObjects.nonNull(ignoreField)) {
                continue;
            }
            JpaSelectOperator selectOperator = field.getAnnotation(JpaSelectOperator.class);
            Object fieldValue = ReflectUtil.getFieldValue(bean, field);
            if (IObjects.nonNull(selectOperator)) {
                // 使用自定义的名字
                if (!IObjects.isBlank(selectOperator.fieldName())) {
                    fieldName = selectOperator.fieldName();
                }
                SimpleExpression simpleExpression = jpaSelectOperatorSwitch(selectOperator, fieldName, fieldValue);
                if (Objects.equals(selectOperator.connect(), SQLConnect.OR)) {
                    jpaSelect.or(simpleExpression);
                } else {
                    jpaSelect.add(simpleExpression);
                }
            } else {
                // 没有注解所有属性都要处理成条件
                jpaSelect.add(Restrictions.eq(fieldName, fieldValue, true));
            }
        }
        return jpaSelect;
    }


    /**
     * 根据注解组装  jpa动态查询
     *
     * @param annotation JpaSelectOperator 注解
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @return SimpleExpression
     */
    public static SimpleExpression jpaSelectOperatorSwitch(JpaSelectOperator annotation,
                                                           String fieldName,
                                                           Object fieldValue) {

        if (annotation.sqlType().isInstance(Date.class) && fieldValue instanceof String) {
            // 创建 SimpleDateFormat 对象，指定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                // 将字符串解析为 Date 对象
                fieldValue = sdf.parse(fieldValue.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        switch (annotation.operator()) {
            case NE:
                return Restrictions.ne(fieldName, fieldValue, annotation.ignoreNull());
            case LIKE:
                return Restrictions.like(fieldName, fieldValue, annotation.ignoreNull());
            case NOTLIKE:
                return Restrictions.notLike(fieldName, fieldValue, annotation.ignoreNull());
            case LLIKE:
                return Restrictions.llike(fieldName, fieldValue, annotation.ignoreNull());
            case RLIKE:
                return Restrictions.rlike(fieldName, fieldValue, annotation.ignoreNull());
            case LT:
                return Restrictions.lt(fieldName, fieldValue, annotation.ignoreNull());
            case GT:
                return Restrictions.gt(fieldName, fieldValue, annotation.ignoreNull());
            case LTE:
                return Restrictions.lte(fieldName, fieldValue, annotation.ignoreNull());
            case GTE:
                return Restrictions.gte(fieldName, fieldValue, annotation.ignoreNull());
            case ISNULL:
                return Restrictions.isNull(fieldName);
            case ISNOTNULL:
                return Restrictions.isNotNull(fieldName);
            case BETWEEN:
                // 值以逗号隔开
                return Restrictions.between(fieldName, fieldValue.toString().trim(), annotation.ignoreNull());
            case EQ:
            default:
                return Restrictions.eq(fieldName, fieldValue, annotation.ignoreNull());
        }
    }


    /**
     * 处理时间格式的key
     *
     * @param function  SpecBuilderDateFun
     * @param root      Root
     * @param builder   CriteriaBuilder
     * @param selectKey String
     * @param <B> B
     */
    public static  <B>  Expression<String> functionTimeFormat(SpecBuilderDateFun function,
                                       Root<B> root,
                                       CriteriaBuilder builder,
                                       String selectKey) {
        return builder
                .function(function.getName()
                        , String.class
                        , root.get(selectKey)
                        , builder.literal(function.getSqlFormat()));
    }


    /**
     * 处理时间格式的key
     *
     * @param function  SpecBuilderDateFun
     * @param root      Root
     * @param builder   CriteriaBuilder
     * @param selectKey String
     * @param <B> B
     */
    public static  <B>  Expression<String> functionTimeFormat(SpecBuilderDateFun function,
                                                              Root<B> root,
                                                              CriteriaBuilder builder,
                                                              ColumnSFunction<B, ?> selectKey) {

        return builder
                .function(function.getName()
                        , String.class
                        , root.get(ColumnUtil.getFieldName(selectKey))
                        , builder.literal(function.getSqlFormat()));
    }
}

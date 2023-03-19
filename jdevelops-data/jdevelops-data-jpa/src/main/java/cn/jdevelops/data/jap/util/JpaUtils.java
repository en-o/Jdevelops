package cn.jdevelops.data.jap.util;

import cn.hutool.core.util.ReflectUtil;
import cn.jdevelops.data.jap.annotation.JpaSelectIgnoreField;
import cn.jdevelops.data.jap.annotation.JpaSelectOperator;
import cn.jdevelops.data.jap.core.JPAUtilExpandCriteria;
import cn.jdevelops.data.jap.core.criteria.Restrictions;
import cn.jdevelops.data.jap.core.criteria.SimpleExpression;
import cn.jdevelops.data.jap.enums.SQLConnect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
     * 根据字段名称获取对象的属性
     *
     * @param fieldName fieldName
     * @param target    目标
     * @return Object
     * @throws Exception Exception
     */
    public static Object getFieldValueByName(String fieldName, Object target) throws Exception {
        if (IObjects.isBlank(fieldName)) {
            fieldName = "id";
        }

        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        Method method = target.getClass().getMethod(getter, new Class[0]);
        return method.invoke(target, new Object[0]);
    }




    /**
     * 多条件查询 的 默认全员AND
     *
     * @param bean 多条件查询
     * @param <T>  返回参数
     * @param <B>  实参类型
     * @return JPAUtilExpandCriteria
     */
    public static <T,B> JPAUtilExpandCriteria<T> getSelectBean2(B bean) {
        return getJpaUtilExpandCriteria(bean);
    }

    /**
     * 多条件查询 的 默认全员AND
     * PS: 建议使用 getSelectBean2
     * @param bean 多条件查询
     * @param <T>  多条件查询
     * @return JPAUtilExpandCriteria
     */
    public static <T> JPAUtilExpandCriteria<T> getSelectBean(T bean) {
        return getJpaUtilExpandCriteria(bean);
    }


    /**
     * 组装查询条件
     * @param bean 条件实体
     * @return  JPAUtilExpandCriteria
     * @param <T> 返回实体
     * @param <B> 条件实体
     */
    private static <T,B>  JPAUtilExpandCriteria<T> getJpaUtilExpandCriteria(B bean) {
        JPAUtilExpandCriteria<T> jpaSelect = new JPAUtilExpandCriteria<>();
        Field[] fields = ReflectUtil.getFields(bean.getClass());
        for (Field field : fields) {
            String fieldName = field.getName();
            if ("serialVersionUID".equals(fieldName)) {
                continue;
            }
            // 字段被忽略
            JpaSelectIgnoreField ignoreField = field.getAnnotation(JpaSelectIgnoreField.class);
            if(IObjects.nonNull(ignoreField)){
                continue;
            }

            Object fieldValue = ReflectUtil.getFieldValue(bean, field);
            JpaSelectOperator selectOperator = field.getAnnotation(JpaSelectOperator.class);
            if (IObjects.nonNull(selectOperator)) {
                SimpleExpression simpleExpression = jpaSelectOperatorSwitch(selectOperator, fieldName, fieldValue);
                if(Objects.equals(selectOperator.connect(), SQLConnect.OR)){
                    jpaSelect.or(simpleExpression);
                }else {
                    jpaSelect.add(simpleExpression);
                }
            } else {
                jpaSelect.add(Restrictions.eq(fieldName, fieldValue, true));
            }
        }
        return jpaSelect;
    }


    /**
     * 根据注解组装  jpa动态查询
     *
     * @param annotation  JpaSelectOperator 注解
     * @param fieldName 字段名
     * @param fieldValue 字段值
     * @return SimpleExpression
     */
    public static SimpleExpression jpaSelectOperatorSwitch(JpaSelectOperator annotation,
                                                           String fieldName,
                                                           Object fieldValue) {
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
            case EQ:
            default:
                return Restrictions.eq(fieldName, fieldValue, annotation.ignoreNull());
        }
    }

}

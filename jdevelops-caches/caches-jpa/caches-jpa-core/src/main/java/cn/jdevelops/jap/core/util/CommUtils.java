package cn.jdevelops.jap.core.util;

import cn.hutool.core.util.ReflectUtil;
import cn.jdevelops.enums.string.StringEnum;
import cn.jdevelops.jap.annotation.JpaSelectOperator;
import cn.jdevelops.jap.core.util.criteria.Restrictions;
import cn.jdevelops.jap.core.util.criteria.SimpleExpression;
import cn.jdevelops.jap.enums.SQLConnect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Jpa项目里的工具类
 * 此工具类废弃，请使用 JpaUtils
 * @author tn
 * @version 1
 * @date 2020/6/28 23:16
 */
@Deprecated
public class CommUtils {

    /**
     * 根据字段名称获取对象的属性
     *
     * @param fieldName fieldName
     * @param target    目标
     * @return Object
     * @throws Exception Exception
     */
    public static Object getFieldValueByName(String fieldName, Object target) throws Exception {
        if (isBlank(fieldName)) {
            fieldName = "id";
        }

        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        Method method = target.getClass().getMethod(getter, new Class[0]);
        return method.invoke(target, new Object[0]);
    }

    public static boolean isBlank(final CharSequence idFieldName) {
        int strLen;
        if (idFieldName == null || StringEnum.NULL_STRING.getCode().contentEquals(idFieldName) || (strLen = idFieldName.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(idFieldName.charAt(i))) {
                return false;
            }
        }
        return true;
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
        return getTjpaUtilExpandCriteria(bean);
    }

    /**
     * 多条件查询 的 默认全员AND
     * PS: 建议使用 getSelectBean2
     * @param bean 多条件查询
     * @param <T>  多条件查询
     * @return JPAUtilExpandCriteria
     */
    public static <T> JPAUtilExpandCriteria<T> getSelectBean(T bean) {
        return getTjpaUtilExpandCriteria(bean);
    }


    private static <T,B>  JPAUtilExpandCriteria<T> getTjpaUtilExpandCriteria(B bean) {
        JPAUtilExpandCriteria<T> jpaSelect = new JPAUtilExpandCriteria<>();
        Field[] fields = ReflectUtil.getFields(bean.getClass());
        for (Field field : fields) {
            String fieldName = field.getName();
            if ("serialVersionUID".equals(fieldName)) {
                continue;
            }
            Object fieldValue = ReflectUtil.getFieldValue(bean, field);
            JpaSelectOperator annotation = field.getAnnotation(JpaSelectOperator.class);
            if (null != annotation) {
                SimpleExpression simpleExpression = JpaUtils.jpaSelectOperatorSwitch(annotation, fieldName, fieldValue);
                if(Objects.equals(annotation.connect(), SQLConnect.OR)){
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

}

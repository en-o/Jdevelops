package com.detabes.jap.core.util;

import cn.hutool.core.util.ReflectUtil;
import com.detabes.enums.string.StringEnum;
import com.detabes.jap.core.util.criteria.Restrictions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author tn
 * @version 1
 * @ClassName CommUtils
 * @description Jpa项目里的工具类
 * @date 2020/6/28 23:16
 */
public class CommUtils {

    /**
     * 根据字段名称获取对象的属性
     *
     * @param fieldName
     * @param target
     * @return
     * @throws Exception
     */
    public static Object getFieldValueByName(String fieldName, Object target) throws Exception {
        if (isBlank(fieldName)) {
            fieldName = "id";
        }

        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        Method method = target.getClass().getMethod(getter, new Class[0]);
        Object e = method.invoke(target);
        return e;
    }

    public static boolean isBlank(final CharSequence idFieldName) {
        int strLen;
        if (idFieldName == null || StringEnum.NULL_STRING.getStr().equals(idFieldName) || (strLen = idFieldName.length()) == 0) {
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
     * 多条件查询 的 条件组装 AND
     * @param bean 多条件查询
     * @param <T> 多条件查询
     * @return JPAUtilExpandCriteria
     */
    public static <T> JPAUtilExpandCriteria<T> getSelectBean(T bean ) {
        JPAUtilExpandCriteria<T> jpaSelect = new JPAUtilExpandCriteria<T>();
        Field[] fields = ReflectUtil.getFields(bean.getClass());
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            if("serialVersionUID".equals(fieldName)){
                continue;
            }
            Object fieldValue = ReflectUtil.getFieldValue(bean, fields[i]);
            jpaSelect.add(Restrictions.eq(fieldName, fieldValue, true));
        }
        return jpaSelect;
    }



    /**
     * 多条件查询 的 条件组装 AND
     * @param bean 多条件查询
     * @param <T> 多条件查询
     * @param isFieldOr 指定条件为 or 的字段名
     * @return JPAUtilExpandCriteria
     */
    public static <T> JPAUtilExpandCriteria<T> getSelectBean(T bean, List<String> isFieldOr) {
        JPAUtilExpandCriteria<T> jpaSelect = new JPAUtilExpandCriteria<T>();
        Field[] fields = ReflectUtil.getFields(bean.getClass());
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            if("serialVersionUID".equals(fieldName)){
                continue;
            }
            Object fieldValue = ReflectUtil.getFieldValue(bean, fields[i]);
            if(isFieldOr!=null && isFieldOr.contains(fieldName)){
                jpaSelect.add(Restrictions.eq(fieldName, fieldValue, true));
            }else {
                jpaSelect.or(Restrictions.eq(fieldName, fieldValue, true));
            }
        }
        return jpaSelect;
    }

}

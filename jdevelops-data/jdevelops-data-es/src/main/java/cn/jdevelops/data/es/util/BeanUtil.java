package cn.jdevelops.data.es.util;


import co.elastic.clients.elasticsearch._types.mapping.Property;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;

/**
 * @author tan
 */
public class BeanUtil {

    /**
     * 功能 : 只复制source对象的非空属性到target对象上
     */
    public static void copyNoNullProperties(Object source, Object target) throws BeansException {
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        // 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
                        if (value != null) {
                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                    } catch (Throwable ex) {
                        throw new FatalBeanException("Could not copy properties from source to target", ex);
                    }
                }
            }
        }
    }


    /**
     * 反射用的 获取Class的字段
     *
     * @param clazz Class
     * @return Fields
     */
    public static Field[] getAllField(Class clazz) {
        Field[] array;
        for (array = null; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] field = clazz.getDeclaredFields();
            array = ArrayUtils.addAll(array, field);
        }
        return array;
    }


    /**
     * 验证 dsl 是否相同 （目前值判断到了key是否相同，里面的小元素没有验证）
     *
     * @param creatEsIndexDsl 创建的dsl元数据
     * @param properties      查出来的 dsl信息
     * @return true: 一样。false 不一样
     */
    public static boolean compareDSL(JSONObject creatEsIndexDsl, Map<String, Property> properties) {

        // 大小不一致直接返回
        if (properties.size() != creatEsIndexDsl.size()) {
            return false;
        }
        try {
            Iterator<Map.Entry<String, Object>> iterator = creatEsIndexDsl.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> e = iterator.next();
                String key = e.getKey();
                if(!properties.containsKey(key)){
                    return false;
                }
            }
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }


        return true;
    }
}

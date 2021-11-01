package com.detabes.map.core.bean;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * bean
 *
 * @author tn
 * @version 1
 * @date 2021/2/2 13:17
 */
public class BeanUtil {
    /**
     * BeanMerge，对象属性合并
     *
     * @param target 最终对象
     */
    public static <M> void merge(M target, M destination) throws Exception {
        //获取目标bean
        BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());
        // 遍历所有属性
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            // 如果是可写属性
            if (descriptor.getWriteMethod() != null) {
                Object defaultValue = descriptor.getReadMethod().invoke(destination);
                //可以使用StringUtil.isNotEmpty(defaultValue)来判断
                if (defaultValue != null && !"".equals(defaultValue)) {
                    //用非空的defaultValue值覆盖到target去
                    descriptor.getWriteMethod().invoke(target, defaultValue);
                }
            }
        }
    }


    /**
     * 合并对象
     * 以destination对象为主
     *
     * @param origin      对象1
     * @param destination 对象2
     * @param <T>
     * @return 把 origin 塞到 destination
     */
    public static <T> void mergeObject(T origin, T destination) {
        if (origin == null || destination == null) {
            return;
        }
        if (!origin.getClass().equals(destination.getClass())) {
            return;
        }

        Field[] fields = destination.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                fields[i].setAccessible(true);
                Object valueD = fields[i].get(origin);
                Object valueO = fields[i].get(destination);
                if (null == valueO) {
                    fields[i].set(destination, valueD);
                }
                fields[i].setAccessible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

package cn.tannn.jdevelops.result.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * bean
 *
 * @author tn
 * @version 1
 * @date 2021/2/2 13:17
 */
public class BeanUtil {


    private static final Logger LOG = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * 功能 : 只复制source对象的非空属性到target对象上
     */
    public static void mergeNotNull(Object source, Object target) throws BeansException {
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
     * 以destination对象为主[ 把 origin 塞到 destination]
     *
     * @param origin      对象1
     * @param destination 对象2
     *
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
                LOG.error("合并对象失败", e);
            }
        }
    }
}

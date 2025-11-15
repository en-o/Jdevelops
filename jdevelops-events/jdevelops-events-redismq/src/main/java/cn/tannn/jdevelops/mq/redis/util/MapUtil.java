package cn.tannn.jdevelops.mq.redis.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * map工具类
 *
 * @author tn
 * @date 2020/4/9 15:12
 */
public class MapUtil {

    private static final Logger LOG = LoggerFactory.getLogger(MapUtil.class);
    /**
     * Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
     */
    public static Map<String, Object> transBean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>(16);
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!"class".equals(key)) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (null != value && !"".equals(value)) {
                        map.put(key, value);
                    }
                }

            }
        } catch (Exception e) {
            LOG.error("利用Introspector和PropertyDescriptor 将Bean --> Map失败", e);
        }
        return map;

    }

}

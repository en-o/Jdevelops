package cn.jdevelops.encryption.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author tn
 * @version 1
 * @date 2020/12/15 13:48
 */
public class RemarkUtil {
    public static String str(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        } else {
            return null == charset ? new String(data) : new String(data, charset);
        }
    }

    public static byte[] bytes(CharSequence str, Charset charset) {
        if (str == null) {
            return null;
        } else {
            return null == charset ? str.toString().getBytes() : str.toString().getBytes(charset);
        }
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 包装 {@link System#arraycopy(Object, int, Object, int, int)}<br>
     * 数组复制，缘数组和目标数组都是从位置0开始复制
     *
     * @param src 源数组
     * @param dest 目标数组
     * @param length 拷贝数组长度
     * @return 目标数组
     * @since 3.0.6
     */
    public static Object copy(Object src, Object dest, int length) {
        //noinspection SuspiciousSystemArraycopy
        System.arraycopy(src, 0, dest, 0, length);
        return dest;
    }

    /**
     * map 转 str
     * @param map map
     * @return String
     */
    public static String map2Str(Map<String, Object> map) {
        ArrayList<Map.Entry<String, Object>> list = new ArrayList<>(map.entrySet());

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> mapping : list) {
            sb.append(mapping.getKey()).append("=").append(mapping.getValue()).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * <pre>
     *     Bean -- Map 1: 利用Introspector和PropertyDescriptor 将Bean -- Map
     * </pre>
     * @param obj 对象
     * @return map
     */
    public static Map<String, Object> transBean2Map(Object obj) {
        if (obj == null) {
            return Collections.emptyMap();
        }
        Map<String,Object> map = new LinkedHashMap<>();
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
                    if(null !=value && !"".equals(value)) {
                        map.put(key, value);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }

}

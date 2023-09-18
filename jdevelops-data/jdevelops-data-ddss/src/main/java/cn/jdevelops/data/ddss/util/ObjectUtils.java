package cn.jdevelops.data.ddss.util;

/**
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/15 15:01
 */
public class ObjectUtils {


    /**判断一个对象是否是基本类型或基本类型的封装类型*/
    public static boolean isPrimitive(Object obj) {
        try {
            return ((Class<?>)obj.getClass().getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }
}

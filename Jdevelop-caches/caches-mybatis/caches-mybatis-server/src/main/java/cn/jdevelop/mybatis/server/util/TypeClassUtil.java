package cn.jdevelop.mybatis.server.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author lmz
 * @date 2021/3/9  13:55
 */
public class TypeClassUtil {

    public static Class<?> getClass(Class<?> clazz,int index) {
        Type type = clazz.getGenericSuperclass();
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        return types[index].getClass();
    }
}

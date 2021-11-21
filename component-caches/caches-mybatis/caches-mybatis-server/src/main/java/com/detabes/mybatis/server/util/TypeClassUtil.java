package com.detabes.mybatis.server.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author lmz
 * @projectName detabes-component
 * @packageName com.detabes.mybatis.server.util
 * @company Peter
 * @date 2021/3/9  13:55
 * @description
 */
public class TypeClassUtil {

    public static Class<?> getClass(Class<?> clazz,int index) {
        Type type = clazz.getGenericSuperclass();
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        return types[index].getClass();
    }
}

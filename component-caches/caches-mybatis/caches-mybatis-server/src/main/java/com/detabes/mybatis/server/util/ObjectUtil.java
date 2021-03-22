package com.detabes.mybatis.server.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * description: java反射工具类，获取对象的字段名称和属性
 *
 * @author lmz
 * @company Peter
 * @date 2020/8/25  17:12
 * @expection
 * @return
 */
public class ObjectUtil {
    /**
     * description: 过滤不需要属性
     *
     * @param field field
     * @return java.lang.Boolean
     * @author lmz
     * @company Peter
     * @date 2020/8/25  17:12
     * @expection
     */
    private static Boolean needFilterField(Field field) {
        // 过滤静态属性
        if (Modifier.isStatic(field.getModifiers())) {
            return true;
        }
        // 过滤transient 关键字修饰的属性
        if (Modifier.isTransient(field.getModifiers())) {
            return true;
        }
        return false;
    }

    /**
     * description: 利用Java反射根据类的名称获取属性信息和父类的属性信息
     *
     * @param obj obj
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author lmz
     * @company Peter
     * @date 2020/8/25  17:12
     * @expection
     */
    public static Map<String, Object> getFieldList(Object obj) throws IllegalAccessException {
        Map<String, Object> fieldMap = new LinkedHashMap<>();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (needFilterField(field)) {
                continue;
            }
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value!=null){
                fieldMap.put(field.getName(), value);
            }

        }
        getParentField(clazz, fieldMap, obj);
        return fieldMap;
    }

    /**
     * description: 递归所有父类属性 和值
     *
     * @param clazz    clazz 当前class
     * @param fieldMap fieldMap 类的属性值集合
     * @param obj      obj 当前传递的类
     * @return void
     * @author lmz
     * @company Peter
     * @date 2020/8/25  17:10
     * @expection
     */
    private static void getParentField(Class<?> clazz, Map<String, Object> fieldMap, Object obj) throws IllegalAccessException {
        //获取父类
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            Field[] superFields = superClazz.getDeclaredFields();
            for (Field field : superFields) {
                if (needFilterField(field)) {
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value!=null){
                    fieldMap.put(field.getName(), value);
                }
            }
            getParentField(superClazz, fieldMap, obj);
        }
    }

    /**
     * description: 根据类获取属性信息和父类的属性信息
     *
     * @param obj obj
     * @return java.util.Map<java.lang.String, java.lang.reflect.Method>
     * @author lmz
     * @company Peter
     * @date 2020/8/25  17:13
     * @expection
     */
    public static Map<String, Method> getMethodMap(Object obj) {
        Map<String, Method> methodMap = new LinkedHashMap<>();
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            methodMap.put(method.getName(), method);
        }
        getParentMethod(clazz, methodMap);
        return methodMap;
    }

    /**
     * description: 递归所有父类方法
     *
     * @param clazz     clazz
     * @param methodMap methodMap
     * @return void
     * @author lmz
     * @company Peter
     * @date 2020/8/25  17:14
     * @expection
     */
    private static void getParentMethod(Class<?> clazz, Map<String, Method> methodMap) {
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            Method[] superMethods = superClazz.getMethods();
            for (Method field : superMethods) {
                methodMap.put(field.getName(), field);
            }
            getParentMethod(superClazz, methodMap);
        }
    }

    /**
     * 根据属性名获取属性值
     *
     * @param obj obj
     * @param fieldName fieldName
     * @return
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        try {
            Map<String, Method> methodMap = getMethodMap(obj);
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = methodMap.get(getter);
            Object value = null;
            if (method != null) {
                value = method.invoke(obj, new Object[]{});
            }
            return value;
        } catch (Exception e) {
            return null;
        }
    }

}
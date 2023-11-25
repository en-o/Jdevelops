package cn.jdevelops.data.jap.util;


import cn.jdevelops.data.jap.exception.JpaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射工具
 *
 * @author web
 */
public class ReflectUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectUtils.class);

    public static final String TABLE_NAME = "tableName";
    public static final String CLASS_NAME = "className";
    public static final String FIELD_NAME = "fieldNames";
    public static final String COLUMN_NAME = "column";

    /**
     * * 获取属性名数组
     *
     * @param clazz clazz
     * @return String[]
     */
    public static String[] getPropertiesName(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        String[] fieldNameStrings = new String[fields.length];
        for (int i = 0; i < fieldNameStrings.length; i++) {
            fieldNameStrings[i] = fields[i].getName();
        }

        return fieldNameStrings;
    }

    /**
     * 通过反射设置属性的值
     *
     * @param fieldName      属性名
     * @param fieldValue     属性值
     * @param object         实体类对象
     * @param parameterTypes 设置属性值的类型  String.class
     */
    public static void setPropertyValue(String fieldName, Object fieldValue, Object object, Class<?>... parameterTypes) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                //字段名称
                String name = field.getName();
                if (name.equals(fieldName)) {
                    field.setAccessible(true);
                    String methname = name.substring(0, 1).toUpperCase() + name.substring(1);
                    Method m = object.getClass().getMethod("set" + methname, parameterTypes);
                    m.invoke(object, fieldValue);
                }
            }
        } catch (Exception e) {
            LOG.error("通过反射设置属性的值失败", e);
        }
    }

    /**
     * 根据主键名称获取实体类主键属性值
     *
     * @param clazz     实体类对象
     * @param fieldName 属性名
     * @return Object
     */
    public static Object getPropertyValue(Object clazz, String fieldName) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = clazz.getClass().getMethod(getter, new Class[]{});
            return method.invoke(clazz, new Object[]{});
        } catch (Exception e) {
            LOG.error("根据主键名称获取实体类主键属性值", e);
            return null;
        }
    }

    /**
     * 通过反射将 source不为空的值赋值给target
     *
     * @param source 实体类对象
     * @param target 实体类对象
     */
    public static void copyProperties(Object source, Object target) throws Exception {
        Field[] field = source.getClass().getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
            String name = field[i].getName();
            if ("serialVersionUID".equals(name)) {
                continue;
            }
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            Method m1 = source.getClass().getMethod("get" + name);
            Object value = m1.invoke(source);
            if (value != null) {
                Field f = field[i];
                //设置对象的访问权限，保证对private的属性的访问
                f.setAccessible(true);
                f.set(target, value);
            }
        }
    }

    /**
     * 获取实体类主键
     *
     * @param clazz clazz
     * @return Field
     */
    public static Field getJpaIdField(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Field item = null;
        for (Field field : fields) {
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                field.setAccessible(true);
                item = field;
                break;
            }
        }
        if (item == null) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                item = getJpaIdField(superclass);
            }
        }

        return item;
    }

    /**
     * 获取实体类 @Column 的其中一个属性名称
     *
     * @param clazz clazz
     * @return Map
     */
    public static Map<String, String> getJpaColumns(Class<?> clazz) {
        Map<String, String> map = new ConcurrentHashMap<>(50);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column declaredAnnotation = field.getDeclaredAnnotation(Column.class);
                String column = declaredAnnotation.name();
                map.put(FIELD_NAME, field.getName());
                map.put(COLUMN_NAME, column);
            }
        }

        return map;
    }

    /**
     * 通过获取类上的@Table注解获取表名称
     *
     * @param clazz clazz
     * @return Map
     */
    public static Map<String, String> getJpaTableName(Class<?> clazz) {
        Map<String, String> map = new ConcurrentHashMap<>(50);
        Table annotation = clazz.getAnnotation(Table.class);
        String name = annotation.name();
        String className = clazz.getSimpleName();
        map.put(TABLE_NAME, name);
        map.put(CLASS_NAME, className);

        return map;
    }

    /**
     * 根据字段名称获取对象的属性
     *
     * @param fieldName fieldName
     * @param target    目标
     * @return Object
     */
    public static Object getFieldValueByName(String fieldName, Object target) {
        try {
            Object value = null;
            Class tempClass = target.getClass();
            // 死循环获取所有 自己和继承
            while (tempClass != null) {
                try {
                    Field field = tempClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    value = field.get(target);
                    break;
                } catch (Exception e) {
                    //得到父类,然后赋给自己
                    tempClass = tempClass.getSuperclass();
                }
            }
            if (value == null) {
                throw new JpaException("获取字段的值失败");
            }
            return value;
        } catch (Exception e) {
            throw new JpaException("获取字段的值失败", e);
        }
    }


}

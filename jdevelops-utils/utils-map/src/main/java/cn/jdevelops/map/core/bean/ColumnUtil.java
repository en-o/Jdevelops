package cn.jdevelops.map.core.bean;


import cn.jdevelops.string.StringFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *  我照着spring 的 Sort 写了一个但是觉得不是很好，后面就看看 Function 接口是不是可以做这件事，就看到了这个
 * Java8通过Function函数获取字段名称(获取实体类的字段名称)
 * <a href="https://www.cnblogs.com/IT-study/p/15351980.html">参考</a>
 * @author jx
 */
public class ColumnUtil {
    /**
     * 字段名注解, 声明此注解后会直接获取注解值，不会对其进行人任何操作
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TableField {
        String value() default "";
    }

    /**
     * 默认配置
     */
    static Boolean defaultToLine = false;

    /**
     * 获取实体类的字段名称(实体声明的字段名称)
     */
    public static <T> String getFieldName(ColumnSFunction<T, ?> fn) {
        return getFieldName(fn, defaultToLine);
    }


    /**
     * 获取实体类的字段名称
     * @param toLine  是否转驼峰 true:驼峰 。 false：正常bean字段
     */
    public static <T> String getFieldName(ColumnSFunction<T, ?> fn, Boolean toLine) {
        SerializedLambda serializedLambda = getSerializedLambda(fn);

        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        Field field;
        Class<?> aClass = null;
        try {
            aClass = Class.forName(serializedLambda.getImplClass().replace("/", "."));
            field = aClass.getDeclaredField(fieldName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }catch (NoSuchFieldException ne) {
            field = superclass(aClass,fieldName);
        }
        // 从field取出字段名，可以根据实际情况调整
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField != null && tableField.value().length() > 0) {
            return tableField.value();
        } else {
            if(Boolean.TRUE.equals(toLine)){
                return StringFormat.toLine(fieldName);
            }else {
                return fieldName;
            }
        }

    }

    /**
     * 获取实体类的字段名称
     */
    private static <T> SerializedLambda getSerializedLambda(ColumnSFunction<T, ?> fn) {
        // 从function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 从序列化方法取出序列化的lambda信息
        boolean isAccessible = writeReplaceMethod.isAccessible();
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);
        return serializedLambda;
    }



    private static Field superclass(Class<?> aClass,String fieldName){
        try {
            return aClass.getSuperclass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return superclass(aClass,fieldName);
        }
    }
}

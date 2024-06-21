package cn.tannn.jdevelops.jdectemplate.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 类方法工具
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/4/1 下午10:37
 */
public class MethodUtils {

    /**
     * 获取自定注解的方法
     *
     * @param aClass Class
     * @return Field
     */
    public static List<Method> findAnnotatedMethod(Class<?> aClass, Class<? extends Annotation> annotation) {
        ArrayList<Method> result = new ArrayList<>();
        while (aClass != null) {
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(annotation)) {
                    result.add(method);
                }
            }
            aClass = aClass.getSuperclass();
        }
        return result;
    }
}

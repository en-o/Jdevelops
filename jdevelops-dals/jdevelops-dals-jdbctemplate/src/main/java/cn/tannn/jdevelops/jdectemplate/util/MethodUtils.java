package cn.tannn.jdevelops.jdectemplate.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
     * 生成方法签名
     *
     * @param method Method
     * @return 方法签名 方法名@参数数量_参数类型1_参数类型2 [lo@2_java.lang.String_java.lang.Integer
     */
    public static String methodSign(Method method) {
        // 方法名
        StringBuilder signature = new StringBuilder(method.getName());
        // 参数个数
        signature.append("@").append(method.getParameterCount());

        Arrays.stream(method.getParameterTypes()).forEach(parameter -> {
            signature.append("_").append(parameter.getCanonicalName());
        });
        return signature.toString();
    }



    /**
     * 检查 method 是不是 Object 的内置方法
     * @param methodName 方法名
     * @return boolean [true表示存在]
     */
    public static boolean checkLocalMethod(final String methodName) {
        return "toString".equals(methodName) ||
                "hashCode".equals(methodName) ||
                "notifyAll".equals(methodName) ||
                "equals".equals(methodName) ||
                "wait".equals(methodName) ||
                "getClass".equals(methodName) ||
                "notify".equals(methodName);
    }

    /**
     * 检查 method 是不是 Object 的内置方法
     * @param methodSign 方法签名
     * @return boolean [true表示存在]
     */
    public static boolean checkLocalMethodSign(final String methodSign) {
        return checkLocalMethod(methodSign.substring(0,methodSign.indexOf("@")));
    }


    /**
     * 检查 method 是不是 Object 的内置方法 - 被重写过后会拦截不到
     * @param method 方法名
     * @return boolean [true表示存在]
     */
    public static boolean checkLocalMethod(final Method method) {
        return method.getDeclaringClass().equals(Object.class);
    }


    /**
     * 获取自定注解的属性字段
     *
     * @param aClass Class
     * @return Field
     */
    public static List<Field> findAnnotatedField(Class<?> aClass, Class<? extends Annotation> annotation) {
        ArrayList<Field> result = new ArrayList<>();
        while (aClass != null) {
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(annotation)) {
                    result.add(field);
                }
            }
            aClass = aClass.getSuperclass();
        }
        return result;
    }
}

package cn.jdevelops.interceptor.util;


import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

/**
 * handler
 * @author tan
 */
public class HandlerUtil {

    /**
     * 获取类上的注解
     * @param handler HandlerMethod
     * @param annotationClass 注解
     * @return 注解
     * @param <A> 注解
     */
    public static <A extends Annotation> Optional<A> classAnnotation(Object handler, Class<A> annotationClass) {
        Objects.requireNonNull(annotationClass);
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            return Optional.ofNullable(method.getMethod().getDeclaringClass().getAnnotation(annotationClass));
        }
        return Optional.empty();
    }




    /**
     * 获取方法上的注解
     * @param handler HandlerMethod
     * @param annotationClass 注解
     * @return 注解
     * @param <A> 注解
     */
    public static  <A extends Annotation> Optional<A> methodAnnotation(Object handler, Class<A> annotationClass) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            return Optional.ofNullable(method.getMethodAnnotation(annotationClass));
        }
        return Optional.empty();
    }

}

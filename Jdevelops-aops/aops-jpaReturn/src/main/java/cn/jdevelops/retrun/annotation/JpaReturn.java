package cn.jdevelops.retrun.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 修改方法的返回值 下划线转驼峰
 * @author tn
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JpaReturn {
     /**
      * 返回值类型 可以不写
      * @return Class
      */
     Class reBean() default Object.class;
}
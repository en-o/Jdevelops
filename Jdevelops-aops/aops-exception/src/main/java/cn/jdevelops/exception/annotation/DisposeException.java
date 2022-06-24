package cn.jdevelops.exception.annotation;

import java.lang.annotation.*;

/**
 * 错误处理
 * messages["你错了","我错了"]
 * codes[500,500]
 * exceptions[youerr.class,meerr.class]
 * @author tnnn
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisposeException {



    /**
     * 错误消息
     * @return String
     */
    String[] messages() default {""};


    /**
     * 错误code  -跟上面的错误消息要对应，要不然错误code指定不了
     * @return String
     */
    int[] codes() default {500};

    /**
     * 错误类型  -跟上面的错误消息要对应，要不然错误消息指定不了
     * @return Class
     */
    Class[] exceptions() default {Exception.class};
}

package cn.jdevelops.authentication.jwt.annotation;

import java.lang.annotation.*;

/**
 * 不刷新token
 * redis 用的其他的用了也没用，
 * @author tnnn
 * @version V1.0
 * @date 2022-07-24 03:34
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface NotRefreshToken {
}

package cn.tannn.jdevelops.renewpwd.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动密码续命
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/2 下午8:21
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import({RenewpwdRegister.class})
public @interface EnableRenewpwd {
    /**
     * 是否启用配置中心
     * @return 默认启用
     */
    boolean enable() default true;

}

package cn.jdevelop.sms.mail.annotation;

import cn.jdevelop.sms.mail.config.MailConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lmz
 * @date 2021/3/29  9:27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(MailConfiguration.class)
public @interface Mail {
}

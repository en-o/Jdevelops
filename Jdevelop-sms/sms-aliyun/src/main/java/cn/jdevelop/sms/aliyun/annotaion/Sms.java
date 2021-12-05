package cn.jdevelop.sms.aliyun.annotaion;


import cn.jdevelop.sms.aliyun.config.SmsConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lmz
 * @date 2021/3/11  11:06
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(SmsConfiguration.class)
public @interface Sms {
}

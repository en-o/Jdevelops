package com.detabes.sms.aliyun.annotaion;


import com.detabes.sms.aliyun.config.SmsConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lmz
 * @projectName smart-appointment
 * @packageName com.detabes.sms.annotaion
 * @company Peter
 * @date 2021/3/11  11:06
 * @description
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(SmsConfiguration.class)
public @interface Sms {
}

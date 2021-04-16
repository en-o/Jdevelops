package com.detabes.sms.mail.config;

import com.detabes.sms.mail.service.MailService;

import com.detabes.sms.mail.service.impl.MailServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;


import javax.annotation.Resource;

/**
 * description: 邮件配置
 *
 * @author lmz
 * @company Peter
 * @date 2021/3/29  9:26
 * @expection
 * @return
 */
@Configuration
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class MailConfiguration {
    @Resource
    private  JavaMailSender mailSender;
    @Resource
    private  MailProperties mailProperties;

    @Bean
    public MailService mailService() {
        return new MailServiceImpl(mailSender, mailProperties);
    }
}

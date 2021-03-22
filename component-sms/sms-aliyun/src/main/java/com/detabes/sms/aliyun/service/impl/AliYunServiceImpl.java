package com.detabes.sms.aliyun.service.impl;


import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.detabes.sms.aliyun.config.SmsConfig;
import com.detabes.sms.aliyun.service.AliYunService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

/**
 * @author lmz
 * @projectName smart-appointment
 * @packageName com.detabes.sms.service.impl
 * @company Peter
 * @date 2021/3/11  11:41
 * @description
 */
@Service
@Log4j2
public class AliYunServiceImpl implements AliYunService {
    private final Client client;

    public AliYunServiceImpl(Client client) {
        this.client = client;
    }

    @Override
    public boolean sendSms(SmsConfig smsConfig) {
        try {
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(smsConfig.getPhoneNumbers())
                    .setSignName(smsConfig.getSignName())
                    .setTemplateCode(smsConfig.getTemplateCode())
                    .setTemplateParam(smsConfig.getTemplateParam());
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            SendSmsResponseBody body = sendSmsResponse.getBody();
            log.info("短信发送测试{}：", body.getMessage());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getRandCode(int digits) {
        StringBuilder sBuilder = new StringBuilder();
        Random rd = new Random(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        for (int i = 0; i < digits; ++i) {
            sBuilder.append(rd.nextInt(9));
        }
        return sBuilder.toString();
    }
}

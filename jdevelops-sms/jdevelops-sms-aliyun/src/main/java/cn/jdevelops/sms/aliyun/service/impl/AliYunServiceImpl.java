package cn.jdevelops.sms.aliyun.service.impl;


import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import cn.jdevelops.sms.aliyun.config.SmsConfig;
import cn.jdevelops.sms.aliyun.service.AliYunService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * @author lmz
 * @date 2021/3/11  11:41
 */
@Service
public class AliYunServiceImpl implements AliYunService {

    private static final Logger LOG = LoggerFactory.getLogger(AliYunServiceImpl.class);
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
            LOG.info("短信发送测试{}：", body.getMessage());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getRandCode(int digits) {
        StringBuilder sBuilder = new StringBuilder();
        SecureRandom rd = new SecureRandom();
        for (int i = 0; i < digits; ++i) {
            sBuilder.append(rd.nextInt(9));
        }
        return sBuilder.toString();
    }
}

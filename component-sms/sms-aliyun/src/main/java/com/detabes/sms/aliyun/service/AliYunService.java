package com.detabes.sms.aliyun.service;


import com.detabes.sms.aliyun.config.SmsConfig;

/**
 * @author lmz
 * @projectName smart-appointment
 * @packageName com.detabes.sms.service
 * @company Peter
 * @date 2021/3/11  11:39
 * @description 阿里云短信服务接口
 */
public interface AliYunService {
    /**
     * description: 阿里云发送短消息
     *
     * @param smsConfig smsConfig
     * @return boolean
     * @author lmz
     * @company Peter
     * @date 2021/3/11  11:43
     * @expection
     */
    boolean sendSms(SmsConfig smsConfig);


    /**
     * description:获取验证码随机数
     *
     * @param digits 几位验证码数
     * @return java.lang.String
     * @author lmz
     * @company Peter
     * @date 2021/3/11  11:43
     * @expection
     */
    String getRandCode(int digits);
}

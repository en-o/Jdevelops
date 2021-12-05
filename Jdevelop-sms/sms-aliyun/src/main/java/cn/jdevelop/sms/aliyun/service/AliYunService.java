package cn.jdevelop.sms.aliyun.service;


import cn.jdevelop.sms.aliyun.config.SmsConfig;

/**
 * 阿里云短信服务接口
 * @author lmz
 * @date 2021/3/11  11:39
 */
public interface AliYunService {
    /**
     * description: 阿里云发送短消息
     *
     * @param smsConfig smsConfig
     * @return boolean
     * @author lmz
     * @date 2021/3/11  11:43
     */
    boolean sendSms(SmsConfig smsConfig);


    /**
     * description:获取验证码随机数
     *
     * @param digits 几位验证码数
     * @return java.lang.String
     * @author lmz
     * @date 2021/3/11  11:43
     */
    String getRandCode(int digits);
}

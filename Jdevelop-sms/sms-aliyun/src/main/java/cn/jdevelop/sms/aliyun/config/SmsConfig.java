package cn.jdevelop.sms.aliyun.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * @author lmz
 * @date 2021/3/11  10:27
 */
@Component
@ConfigurationProperties(prefix = "sms.aliyun", ignoreUnknownFields = false)
public class SmsConfig {
    /**
     * accessKeyId
     */
    @NotNull
    private String accessKeyId;
    /**
     * accessKeySecret
     */
    @NotNull
    private String accessKeySecret;
    /**
     * 短信签名
     */
    @NotNull
    private String signName;
    /**
     * 短信模板
     */
    @NotNull
    private String templateCode;
    /**
     * 短信验证码
     */
    private String templateParam;
    /**
     * 手机号码
     */
    private String phoneNumbers;
    /**
     * 上行短信扩展码
     */
    private String smsUpExtendCode;
    /**
     * 外部流水扩展字段
     */
    private String outId;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getSmsUpExtendCode() {
        return smsUpExtendCode;
    }

    public void setSmsUpExtendCode(String smsUpExtendCode) {
        this.smsUpExtendCode = smsUpExtendCode;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }
}

package cn.jdevelops.sms.aliyun.config;


import cn.jdevelops.sms.aliyun.service.AliYunService;
import cn.jdevelops.sms.aliyun.service.impl.AliYunServiceImpl;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lmz
 * @date 2021/3/11  10:36
 */
@Configuration
@EnableConfigurationProperties(SmsConfig.class)
public class SmsConfiguration {
    private final SmsConfig smsConfig;

    public SmsConfiguration(SmsConfig smsConfig) {
        this.smsConfig = smsConfig;
    }

    @Bean
    public Client getClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(smsConfig.getAccessKeyId())
                .setAccessKeySecret(smsConfig.getAccessKeySecret());
        config.setEndpoint("dysmsapi.aliyuncs.com");
        return new Client(config);
    }

    @Bean
    public AliYunService aliYunSmsService(Client client) {
        return new AliYunServiceImpl(client);
    }
}

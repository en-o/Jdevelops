package cn.jdevelop.message.properties.wx;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Configuration
@ConfigurationProperties(prefix = "wechat.miniapp")
@Getter
@Setter
public class WxMaProperties {
    /**
     * 设置微信小程序的appid
     */
    private String appid;

    /**
     * 设置微信小程序的Secret
     */
    private String secret;

    /**
     * 消息模板uuid
     */
    private String templateId;

    /**
     * 消息格式，XML或者JSON（默认）
     */
    private String msgDataFormat;

    /**
     * "developer", "开发版"
     * "trial", "体验版"
     * "formal", "正式版" (默认)
     */
    private String miniProgramState;

}

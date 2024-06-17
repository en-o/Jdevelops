package cn.tannn.jdevelops.events.websocket.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

/**
 * socket配置
 *
 * @author tn
 * @date 2020-07-08 12:33
 */

@ConfigurationProperties(prefix = "jdevelops.websocket")
@ConditionalOnMissingBean(WebSocketConfig.class)
@Component
public class WebSocketConfig {

    /**
     * 缓存选择
     * <p> true: 本地缓存 (默认)
     * <p> falas:  redis 缓存 (redis 无法存储非序列化数据,websocket的session无法序列化,所以此参数废弃)
     */
    private boolean enable = true;

    /**
     * 多端登录
     * <p> true: 允许 (默认)
     * <p> false: 不允许
     */
    private boolean multipart = true;


    /**
     * true: 下线之前的来连接,(默认)
     * <p> false: 下线当前连接
     * <p> multipart = false 时使用,其余无效
     */
    private boolean onClose = true;


    /**
     * 是否开启 VERIFY_PATH_NO 路径
     * <p>  默认开启。[默认true, falase会禁用  /socket/n/]
     */
    private boolean verifyPathNo = true;

    /**
     * websocket连接认证【request.getParameter("tokenName")】
     * <p>  默认 token
     */
    private String tokenName;

    /**
     * websocket连接认证的验证密钥
     * <p>  默认 123456
     */
    private String tokenSecret ;


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }


    public boolean isMultipart() {
        return multipart;
    }

    public void setMultipart(boolean multipart) {
        this.multipart = multipart;
    }

    public boolean isOnClose() {
        return onClose;
    }

    public void setOnClose(boolean onClose) {
        this.onClose = onClose;
    }

    public boolean isVerifyPathNo() {
        return verifyPathNo;
    }

    public void setVerifyPathNo(boolean verifyPathNo) {
        this.verifyPathNo = verifyPathNo;
    }

    public String getTokenName() {
        if(tokenName == null){
            return "token";
        }
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenSecret() {
        if(tokenSecret == null){
            return "123456";
        }
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WebSocketConfig.class.getSimpleName() + "[", "]")
                .add("enable=" + enable)
                .add("multipart=" + multipart)
                .add("onClose=" + onClose)
                .add("verifyPathNo=" + verifyPathNo)
                .add("tokenName='" + tokenName + "'")
                .add("tokenSecret='" + tokenSecret + "'")
                .toString();
    }
}

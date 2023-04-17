package cn.jdevelops.webs.websocket.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 *   socket配置
 * @author tn
 * @date  2020-07-08 12:33
 */

@ConfigurationProperties(prefix = "jdevelops.websocket")
@ConditionalOnMissingBean(WebSocketConfig.class)
@Component
public class WebSocketConfig {

    /**
     * 缓存选择
     * true: 本地缓存 (默认)
     * falas:  redis 缓存 (redis 无法存储非序列化数据,websocket的session无法序列化,所以此参数废弃)
     */
    private boolean enable = true;

    /**
     * 多端登录
     * true: 允许 (默认)
     * false: 不允许
     */
    private boolean multipart = true;


    /**
     *  true: 下线之前的来连接,(默认)
     *  false: 下线当前连接
     *  multipart = false 时使用,其余无效
     */
    private boolean onClose = true;




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

    @Override
    public String toString() {
        return "WebSocketConfig{" +
                "enable=" + enable +
                ", multipart=" + multipart +
                ", onClose=" + onClose +
                '}';
    }
}

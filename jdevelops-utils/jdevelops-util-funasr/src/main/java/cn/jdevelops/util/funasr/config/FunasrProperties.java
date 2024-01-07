package cn.jdevelops.util.funasr.config;

import cn.jdevelops.util.funasr.domain.FunasrMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 基础配置
 * 默认不适用ssl
 * @author tnnn
 * @version V1.0
 * @date 2024-01-07 14:54
 */
@ConfigurationProperties(prefix = "jdevelops.funasr")
public class FunasrProperties {


    /**
     * websocket 连接地址 [默认127.0.0.1]
     *
     */
    String host;

    /**
     * websocket 连接端口 [10095]
     */
    Integer port;

    /**
     * 选择录音模式
     * @link <a href="https://www.yuque.com/tanning/baavra/yairag7df0dawg6a#m2DTr">...</a>
     */
    FunasrMode funasrMode;


    public String getHost() {
        if(null == host){
            return "127.0.0.1";
        }
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        if(null == port){
            return 10095;
        }
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public FunasrMode getFunasrMode() {
        return funasrMode;
    }

    public void setFunasrMode(FunasrMode funasrMode) {
        this.funasrMode = funasrMode;
    }

    @Override
    public String toString() {
        return "FunasrProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", funasrMode=" + funasrMode +
                '}';
    }
}

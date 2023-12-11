package cn.jdevelops.data.es.config;

import org.apache.http.client.config.RequestConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ES 基础配置
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/25 11:39
 */
@AutoConfiguration
@ConfigurationProperties(prefix = "jdevelops.elasticsearch")
public class ElasticProperties {

    /**
     * 扫描[实体类包]包路径[包含子路径] e.g com.text [他会扫描这个路径下所有的类包括子路径的类]
     * <p>
     *     用户实体生成Mapping的 实体类扫描
     * </p>
     */
    String basePackage;


    /**
     * 连接请求超时/毫秒 （-1 默认）
     * @see RequestConfig#connectionRequestTimeout
     */
    Integer connectionRequestTimeout;
    /**
     * 连接超时/毫秒 （-1 默认）
     * @see RequestConfig#connectTimeout
     */
     Integer connectTimeout;

    /**
     * 连接请求超时/毫秒 （-1 默认）
     * @see RequestConfig#connectionRequestTimeout
     */
    Integer socketTimeout;


    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }


    public Integer getConnectionRequestTimeout() {
        if(connectionRequestTimeout == null){
            return -1;
        }
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public Integer getConnectTimeout() {
        if(connectTimeout == null){
            return 5000;
        }
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getSocketTimeout() {
        if(socketTimeout == null){
           return 60000;
        }
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    @Override
    public String toString() {
        return "ElasticProperties{" +
                "basePackage='" + basePackage + '\'' +
                ", connectionRequestTimeout=" + connectionRequestTimeout +
                ", connectTimeout=" + connectTimeout +
                ", socketTimeout=" + socketTimeout +
                '}';
    }
}

package cn.jdevelops.data.es.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ES基础配置
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/25 11:39
 */
@AutoConfiguration
@ConfigurationProperties(prefix = "jdevelops.elasticsearch")
public class ElasticProperties {
    /**
     * 扫描包路径
     */
    String basePackage;


    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public String toString() {
        return "ElasticProperties{" +
                "basePackage='" + basePackage + '\'' +
                '}';
    }
}

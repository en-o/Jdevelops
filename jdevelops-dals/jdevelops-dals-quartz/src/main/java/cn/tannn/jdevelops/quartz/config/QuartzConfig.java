package cn.tannn.jdevelops.quartz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置文件
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/7/26 下午2:16
 */
@ConfigurationProperties(prefix = "jdevelops.quartz")
public class QuartzConfig {

    /**
     * 扫描[实体类包]包路径[包含子路径] e.g com.text [他会扫描这个路径下所有的类包括子路径的类]
     * <p> 扫描被标记{@link cn.tannn.jdevelops.quartz.Job}的类
     */
    String basePackage;

    public String getBasePackage() {
        if(basePackage == null || basePackage.trim().isEmpty()){
            return "cn.tannn";
        }
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public String toString() {
        return "JdbcTemplateConfig{" +
                "basePackage='" + basePackage + '\'' +
                '}';
    }
}

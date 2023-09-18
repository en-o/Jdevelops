package cn.jdevelops.data.ddss.config.properties;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 多数据源属性（从applition.yml读取数据源配置）
 *
 * @author tan
 */
@ConfigurationProperties(prefix = "jdevelops.dynamic")
public class DynamicDataSourceProperties {
    /**
     *  数据库加密盐(16位) <br/>
     *  如果不满足16或者则会使用默认
     */
    String salt;

    public String getSalt() {
        if(salt == null || 16 != salt.length() ){
            return "salt1231212qadqw";
        }
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}


package cn.tannn.jdevelops.jdectemplate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 一些元信息配置
 *
 * @author <a href="https://t.tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/6/24 下午6:02
 */
@ConfigurationProperties(prefix = "jdevelops.jdbc")
public class JdbcTemplateConfig {

    /**
     * 扫描[实体类包]包路径[包含子路径] e.g com.text [他会扫描这个路径下所有的类包括子路径的类]
     * <p> 扫描被标记{@link cn.tannn.jdevelops.annotations.jdbctemplate.JdbcTemplate}的类
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

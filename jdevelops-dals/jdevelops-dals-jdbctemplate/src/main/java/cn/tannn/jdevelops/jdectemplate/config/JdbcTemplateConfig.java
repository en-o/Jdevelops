package cn.tannn.jdevelops.jdectemplate.config;

import cn.tannn.jdevelops.annotations.jdbctemplate.proxysql.JdbcTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

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
     * 扫描包路径[包含子路径] e.g com.text [他会扫描这个路径下所有的类包括子路径的类]
     * <p> 扫描被标记{@link JdbcTemplate}的字段
     * <p> 建议扫描直接项目根包
     */
    String basePackage;

    @NestedConfigurationProperty
    XmlMapperProperties xmlmapper = new XmlMapperProperties();

    public String getBasePackage() {
        if(basePackage == null || basePackage.trim().isEmpty()){
            return "cn.tannn";
        }
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public XmlMapperProperties getXmlmapper() {
        return xmlmapper;
    }

    public void setXmlmapper(XmlMapperProperties xmlmapper) {
        this.xmlmapper = xmlmapper;
    }

    @Override
    public String toString() {
        return "JdbcTemplateConfig{" +
                "basePackage='" + basePackage + '\'' +
                ", xmlmapper=" + xmlmapper +
                '}';
    }
}

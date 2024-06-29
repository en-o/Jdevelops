package cn.tannn.jdevelops.autoschema.scan;

import cn.tannn.jdevelops.autoschema.LocalDataSourceLoader;
import cn.tannn.jdevelops.autoschema.properties.DataBaseProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 默认不启用 （都启用以注解为准
 * <p> 1. 可以使用注解方式启用{@link EnableAutoSchema}
 * <p> 2. 通过 jdevelops.autoschema.enabled=true 启用
 *
 * <p>  在上面启动的情况下关闭 自动建库： jdevelops.database.enabled=false
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/18 下午2:42
 */
@ConditionalOnProperty(name = "jdevelops.autoschema.enabled", havingValue = "true")
public class AutoSchemaConfiguration {

    /**
     * 建库
     */
    @Bean
    @ConditionalOnMissingBean(value = DataBaseProperties.class)
    public DataBaseProperties dataBaseProperties() {
        return new DataBaseProperties();
    }


    /**
     * 建库
     */
    @Bean
    @ConditionalOnMissingBean(value = LocalDataSourceLoader.class)
    public LocalDataSourceLoader localDataSourceLoader() {
        return new LocalDataSourceLoader();
    }
}

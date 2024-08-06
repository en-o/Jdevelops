package cn.tannn.jdevelops.pf4j;

import cn.tannn.jdevelops.pf4j.service.DefPluginService;
import cn.tannn.jdevelops.pf4j.service.PluginService;
import org.pf4j.AbstractPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.spring.SpringPluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 *
 * @see https://www.yuque.com/tanning/mbquef/gqed6ormkahepucp
 * @see https://blog.csdn.net/weixin_33347188/article/details/129880387
 */
@Configuration
@EnableConfigurationProperties(Pf4j3Properties.class)
@ConditionalOnProperty(prefix = Pf4j3Properties.PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
public class Pf4j3AutoConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(Pf4j3AutoConfiguration.class);
    @Bean
    @ConditionalOnMissingBean
    public PluginManager springPluginManager(Pf4j3Properties pf4j3Properties) {
        LOG.info("=======pluginManager start ============");

        LOG.info("=======pf4j3Properties  ============ {}", pf4j3Properties.toString());
        // 设置运行模式
        System.setProperty(AbstractPluginManager.MODE_PROPERTY_NAME, pf4j3Properties.getRuntimeMode().toString());

        // 默认会加载  jar 同路径下的plugins目录
        File file = new File(pf4j3Properties.getPath());
        SpringPluginManager pluginManager = new SpringPluginManager(file.toPath());
        //  SpringPluginManager 内部有个init
        LOG.info("=======pluginManager end ============");
        return pluginManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public PluginService pluginService(PluginManager springPluginManager) {
        return new DefPluginService(springPluginManager);
    }
}

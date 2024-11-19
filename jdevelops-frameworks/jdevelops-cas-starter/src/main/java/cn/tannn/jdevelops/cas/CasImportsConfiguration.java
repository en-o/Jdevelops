package cn.tannn.jdevelops.cas;

import cn.tannn.jdevelops.cas.config.CasConfig;
import cn.tannn.jdevelops.cas.core.CasService;
import cn.tannn.jdevelops.cas.core.CasServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * spring
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/7/4 下午1:56
 */
public class CasImportsConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "casConfig")
    public CasConfig casConfig() {
        return new CasConfig();
    }

    @Bean
    @ConditionalOnMissingBean(name = "casService")
    public CasService casService(CasConfig casConfig) {
        return new CasServiceImpl(casConfig);
    }



}

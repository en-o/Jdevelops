package cn.jdevelops.delay.core.scan;

import cn.jdevelops.delay.core.factory.DelayFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@Configuration
public class EnableAutoScanDelayCore {

    /**
     * 延迟方法的调用工厂
     * @return DelayFactory
     */
    @ConditionalOnMissingBean(name = "delayFactory")
    @Bean
    public DelayFactory delayFactory(){
        return new DelayFactory();
    }


}

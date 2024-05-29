package cn.tannn.jdevelops.delays.core.scan;

import cn.tannn.jdevelops.delays.core.factory.DelayFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
public class EnableAutoScanDelayCore {

    /**
     * 延迟方法的调用工厂
     * @return DelayFactory
     */
    @ConditionalOnMissingBean(DelayFactory.class)
    @Bean
    public DelayFactory delayFactory(){
        return new DelayFactory();
    }


}

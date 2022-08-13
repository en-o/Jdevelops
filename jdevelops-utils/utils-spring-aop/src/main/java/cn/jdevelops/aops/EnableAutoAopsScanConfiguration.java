package cn.jdevelops.aops;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
public class EnableAutoAopsScanConfiguration {

    @ConditionalOnMissingBean(ContextUtil.class)
    @Bean
    public ContextUtil contextUtil(){
        return new ContextUtil();
    }


}

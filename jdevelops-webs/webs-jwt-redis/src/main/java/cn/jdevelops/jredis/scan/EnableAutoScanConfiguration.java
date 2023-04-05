package cn.jdevelops.jredis.scan;

import cn.jdevelops.jredis.service.RedisService;
import cn.jdevelops.jredis.service.impl.RedisServiceImpl;
import cn.jdevelops.util.jwt.util.JwtContextUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
public class EnableAutoScanConfiguration {

    @ConditionalOnMissingBean(name = "redisService")
    @Bean
    public RedisService redisService(){
        return new RedisServiceImpl();
    }


    @ConditionalOnMissingBean(name = "contextUtil")
    @Bean
    public JwtContextUtil contextUtil(){
        return new JwtContextUtil();
    }

}

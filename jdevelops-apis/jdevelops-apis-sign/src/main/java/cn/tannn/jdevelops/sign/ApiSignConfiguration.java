package cn.tannn.jdevelops.sign;

import cn.tannn.jdevelops.sign.config.ApiSignConfig;
import org.springframework.context.annotation.Bean;

/**
 * api sign Configuration
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/3 下午4:23
 */
public class ApiSignConfiguration {

    @Bean
    public ApiSignConfig apiSignConfig(){
        return new ApiSignConfig();
    }


    @Bean
    public SignAppInterceptor signAppInterceptor(ApiSignConfig apiSignConfig){
        return new SignAppInterceptor(apiSignConfig);
    }
}

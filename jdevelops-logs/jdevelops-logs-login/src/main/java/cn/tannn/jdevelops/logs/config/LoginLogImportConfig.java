package cn.tannn.jdevelops.logs.config;

import cn.tannn.jdevelops.logs.aspect.LoginLogAspect;
import cn.tannn.jdevelops.logs.service.DefLoginLogSave;
import cn.tannn.jdevelops.logs.service.LoginLogSave;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/12/25 11:45
 */
public class LoginLogImportConfig {

    /**
     *  loginLogSave
     */
    @Bean
    @ConditionalOnMissingBean(LoginLogSave.class)
    public LoginLogSave loginLogSave(){
        return new DefLoginLogSave();
    }

    @Bean
    @ConditionalOnBean(LoginLogSave.class)
    public LoginLogAspect auditLogAspect(LoginLogSave loginLogSave){
        return new LoginLogAspect(loginLogSave);
    }

}

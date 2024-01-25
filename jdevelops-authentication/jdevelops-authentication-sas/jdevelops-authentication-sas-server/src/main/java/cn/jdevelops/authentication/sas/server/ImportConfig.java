package cn.jdevelops.authentication.sas.server;

import cn.jdevelops.authentication.sas.server.core.config.AuthorizationServerConfig;
import cn.jdevelops.authentication.sas.server.core.config.DefaultSecurityConfig;
import cn.jdevelops.authentication.sas.server.core.config.SasProperties;
import cn.jdevelops.authentication.sas.server.core.controller.AuthorizationConsentController;
import cn.jdevelops.authentication.sas.server.core.controller.ServerController;
import cn.jdevelops.authentication.sas.server.oauth.model.oidc.CustomOidcUserInfoService;
import cn.jdevelops.authentication.sas.server.user.service.AuthenticationService;
import cn.jdevelops.authentication.sas.server.user.service.JUserDetailsService;
import cn.jdevelops.authentication.sas.server.user.service.impl.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * 导入
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/1/25 10:01
 */
@AutoConfiguration
public class ImportConfig {


    @Bean
    @ConditionalOnMissingBean(SasProperties.class)
    public SasProperties sasProperties(){
        return new SasProperties();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationService.class)
    public AuthenticationService authenticationService(){
        return new AuthenticationService(){};
    }

    @Bean
    @ConditionalOnMissingBean(JUserDetailsService.class)
    public JUserDetailsService jUserDetailsService(AuthenticationService authenticationService){
        return new UserDetailsServiceImpl(authenticationService);
    }

    @Bean
    @ConditionalOnMissingBean(CustomOidcUserInfoService.class)
    public CustomOidcUserInfoService customOidcUserInfoService(AuthenticationService authenticationService){
        return new CustomOidcUserInfoService(authenticationService);
    }

    @Bean
    @ConditionalOnMissingBean(DefaultSecurityConfig.class)
    public DefaultSecurityConfig defaultSecurityConfig(SasProperties sasProperties){
        return new DefaultSecurityConfig(sasProperties);
    }


    @Bean
    @ConditionalOnMissingBean(DefaultSecurityConfig.class)
    public AuthorizationServerConfig authorizationServerConfig(JUserDetailsService jUserDetailsService,
                                                               UserDetailsService userDetailsService,
                                                               CustomOidcUserInfoService customOidcUserInfoService){
        return new AuthorizationServerConfig(
                jUserDetailsService,
                userDetailsService,
                customOidcUserInfoService
        );
    }


    @Bean
    @ConditionalOnMissingBean(DefaultSecurityConfig.class)
    public AuthorizationConsentController authorizationConsentController(RegisteredClientRepository registeredClientRepository,
                                                                         OAuth2AuthorizationConsentService authorizationConsentService){
        return new AuthorizationConsentController(
                registeredClientRepository,
                authorizationConsentService
        );
    }


    @Bean
    @ConditionalOnMissingBean(DefaultSecurityConfig.class)
    public ServerController serverController(RegisteredClientRepository registeredClientRepository,
                                             PasswordEncoder passwordEncoder,
                                             SasProperties sasProperties){
        return new ServerController(
                registeredClientRepository, passwordEncoder, sasProperties
        );
    }

}

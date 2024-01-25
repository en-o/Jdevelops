/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.jdevelops.sboot.authentication.sas.server.core.config;

import cn.jdevelops.util.authorization.error.UnAccessDeniedHandler;
import cn.jdevelops.util.authorization.error.UnAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * @author Joe Grandja
 */
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class DefaultSecurityConfig {

    private  final SasProperties sasProperties;

    public DefaultSecurityConfig(SasProperties sasProperties) {
        this.sasProperties = sasProperties;
    }


    /**
     * 配置Spring Security相关的东西  <br/>
     * Spring Security 过滤链配置（此处是纯Spring Security相关配置）
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // 禁止csrf 要不然 post 403 {@link https://blog.csdn.net/Mr_FenKuan/article/details/121718258}
                .csrf().disable()
                // 设置所有请求都需要认证，未认证的请求都被重定向到login页面进行登录
                .authorizeHttpRequests((authorize) -> authorize
                        // 放行静态资源
                        .mvcMatchers(sasProperties.getRequests().mvcMatchersToArr()).permitAll()
                        // 放行接口
                        .antMatchers(sasProperties.getRequests().antMatchersToArr()).permitAll()
                        // 拦截其余所有
                        .anyRequest().authenticated()
                ) // 自定义登录界面
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/page/login")

                );

        // 异常处理器
        http.oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(new UnAuthenticationEntryPoint("/page/login"))
                        .accessDeniedHandler(new UnAccessDeniedHandler())
        );


        return http.build();
    }


    /**
     * 配置密码解析器，使用BCrypt的方式对密码进行加密和验证
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

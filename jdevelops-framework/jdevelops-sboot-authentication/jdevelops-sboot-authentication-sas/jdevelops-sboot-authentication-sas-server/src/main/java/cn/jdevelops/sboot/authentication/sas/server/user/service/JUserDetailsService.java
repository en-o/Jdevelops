package cn.jdevelops.sboot.authentication.sas.server.user.service;


import cn.jdevelops.sboot.authentication.sas.server.user.entity.AuthenticationAccount;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/1/8 16:57
 */
public interface JUserDetailsService {

    /**
     * 查询当前鉴权的短信验证码
     * @param phoneNumber 根据手机号查询
     * @return 验证码
     */
    String findSmsCode(String phoneNumber);


    /**
     * loadUserByUsername 的变体
     * @param phoneNumber 根据手机号
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    UserDetails loadUserDetailsByPhoneNumber(String phoneNumber) throws UsernameNotFoundException;

    /**
     * 查询用户详情
     * @param username 登录名
     * @return AuthenticationAccount
     */
    Optional<AuthenticationAccount> findUserInfo(String username);

}

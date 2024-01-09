package cn.jdevelops.sboot.authentication.sas.server.user.service.impl;

import cn.jdevelops.sboot.authentication.sas.server.user.entity.AuthenticationAccount;
import cn.jdevelops.sboot.authentication.sas.server.user.service.AuthenticationService;
import cn.jdevelops.sboot.authentication.sas.server.user.service.JUserDetailsService;
import cn.jdevelops.util.authorization.error.CustomAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * UserDetailsService 的自定义实现。
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/28 10:34
 */
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService,JUserDetailsService {

    private final AuthenticationService authenticationService;

    public UserDetailsServiceImpl(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     *  加载用户基础信息，用于鉴权
     * @param username 登录名
     * @return UserDetails
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthenticationAccount account = authenticationService
                .findUser(username)
                .orElseThrow(() -> new CustomAuthenticationException("账户密码错误，请重试！"));
        // 用户角色
        List<String> rolesList = account.getRoles();
        String[] roles = rolesList.toArray(new String[0]);
        return User.builder()
                .password(account.getPassword())
                .username(account.getLoginName())
                .authorities(roles)
                .build();
    }

    @Override
    public String findSmsCode(String phoneNumber) {
        return authenticationService.findSmsCode(phoneNumber);
    }

    @Override
    public UserDetails loadUserDetailsByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
        AuthenticationAccount account = authenticationService
                .findUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomAuthenticationException("账户不存在，请注册或者绑定手机号！"));
        // 用户角色
        List<String> rolesList = account.getRoles();
        String[] roles = rolesList.toArray(new String[0]);
        return User.builder()
                .password(account.getPassword())
                .username(account.getLoginName())
                .authorities(roles)
                .build();
    }
}

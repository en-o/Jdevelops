package cn.jdevelops.sboot.authentication.sas.server.user.service;

import cn.jdevelops.sboot.authentication.sas.server.user.dao.AuthenticationAccountDao;
import cn.jdevelops.sboot.authentication.sas.server.user.entity.AuthenticationAccount;
import cn.jdevelops.util.authorization.error.CustomAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * 用户
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/28 10:34
 */
@Slf4j
public class AuthenticationAccountService implements UserDetailsService {

    private final AuthenticationAccountDao authenticationAccountDao;

    public AuthenticationAccountService(AuthenticationAccountDao authenticationAccountDao) {
        this.authenticationAccountDao = authenticationAccountDao;
    }

    /**
     * 设置用户信息，校验用户名、密码 <br/>
     * 授权平台的登录认证操作，是脱离客户端（第三方平台）的使用的
     * @param username 登录名
     * @return UserDetails
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthenticationAccount account = authenticationAccountDao
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

}

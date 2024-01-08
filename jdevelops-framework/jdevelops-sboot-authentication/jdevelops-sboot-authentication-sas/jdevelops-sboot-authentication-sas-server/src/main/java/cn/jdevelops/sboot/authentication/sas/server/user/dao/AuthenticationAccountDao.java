package cn.jdevelops.sboot.authentication.sas.server.user.dao;

import cn.jdevelops.sboot.authentication.sas.server.user.entity.AuthenticationAccount;

import java.util.Optional;

/**
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/1/8 15:52
 */
public interface AuthenticationAccountDao {

    /**
     * 查询用户
     * @param loginName 登录名
     * @return AuthenticationAccount
     */
    default Optional<AuthenticationAccount> findUser(String loginName) {
        return Optional.of(new AuthenticationAccount());
    }
}

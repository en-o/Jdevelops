package cn.jdevelops.sboot.authentication.sas.server.user.service;

import cn.jdevelops.sboot.authentication.sas.server.user.entity.AuthenticationAccount;

import java.util.Optional;

/**
 * 鉴权用到的基础接口，需要业务自己实现
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/1/8 15:52
 */
public interface AuthenticationService {

    /**
     * 查询用户
     * @param loginName 登录名
     * @return AuthenticationAccount
     */
    default Optional<AuthenticationAccount> findUser(String loginName) {
        return Optional.of(new AuthenticationAccount());
    }

    /**
     * 查询用户
     * @param phoneNumber 手机号
     * @return AuthenticationAccount
     */
    default Optional<AuthenticationAccount> findUserByPhoneNumber(String phoneNumber) {
        return Optional.of(new AuthenticationAccount());
    }

    /**
     * 查询当前鉴权的短信验证码
     * @param phoneNumber 根据手机号查询
     * @return 验证码
     */
    default String findSmsCode(String phoneNumber) {
        return "8888";
    }

}

package cn.jdevelops.jredis.entity;

import cn.jdevelops.jredis.entity.base.BasicsAccount;

import java.util.List;

/**
 * 用户信息 存redis的
 * @author tomsun28
 * @date 16:20 2019-05-19
 */
public class RedisAccount<T> extends BasicsAccount {

    /** 用户其他数据 **/
    private T userInfo;


    public RedisAccount() {
    }

    public RedisAccount(T userInfo) {
        this.userInfo = userInfo;
    }

    public RedisAccount(String userCode,
                        String password,
                        String salt,
                        boolean disabledAccount,
                        boolean excessiveAttempts,
                        List<String> ownRoles) {
        super(userCode, password, salt, disabledAccount, excessiveAttempts, ownRoles);
    }

    public RedisAccount(String userCode,
                        String password,
                        String salt,
                        boolean disabledAccount,
                        boolean excessiveAttempts,
                        List<String> ownRoles, T userInfo) {
        super(userCode, password, salt, disabledAccount, excessiveAttempts, ownRoles);
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "RedisAccount{" +
                "userInfo=" + userInfo +
                '}';
    }

    public T getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(T userInfo) {
        this.userInfo = userInfo;
    }
}

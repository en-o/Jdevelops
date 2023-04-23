package cn.jdevelops.sboot.authentication.jredis.entity;


import cn.jdevelops.sboot.authentication.jredis.entity.base.BasicsAccount;


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

    /**
     *
     * @param userCode 用户唯一编码
     * @param password 用户密码
     * @param salt 加密的盐
     * @param disabledAccount 是否被禁用
     * @param excessiveAttempts 是否被锁定
     * @param ownRoleJsons 用户角色(json, 有什么就存什么把，id,名称等json)
     */
    public RedisAccount(String userCode,
                        String password,
                        String salt,
                        boolean disabledAccount,
                        boolean excessiveAttempts,
                        String ownRoleJsons) {
        super(userCode, password, salt, disabledAccount, excessiveAttempts, ownRoleJsons);
    }

    /**
     *
     * @param userCode 用户唯一编码
     * @param password 用户密码
     * @param salt 加密的盐
     * @param disabledAccount 是否被禁用
     * @param excessiveAttempts 是否被锁定
     * @param ownRoleJsons 用户角色(json, 有什么就存什么把，id,名称等json)
     * @param userInfo  userInfo
     */
    public RedisAccount(String userCode,
                        String password,
                        String salt,
                        boolean disabledAccount,
                        boolean excessiveAttempts,
                        String ownRoleJsons, T userInfo) {
        super(userCode, password, salt, disabledAccount, excessiveAttempts, ownRoleJsons);
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

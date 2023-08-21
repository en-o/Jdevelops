package cn.jdevelops.sboot.authentication.jredis.entity.base;


import java.util.List;

/**
 * 基础 用户信息 存redis的
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-02-09 16:36
 */
public class BasicsAccount {


    /** 用户唯一编码(一般都用用户的登录名 **/
    private String userCode;

    /** 用户密码 **/
    private String password;

    /** 加密的盐 **/
    private String salt;

    /** 是否被禁用 **/
    private boolean disabledAccount;

    /** 是否被锁定 **/
    private boolean excessiveAttempts;
    /**
     * 用户角色 为空则默认不验证角色权限
     */
    private  List<String> roles;

    /**
     * 用户权限 为空则默认不验证角色权限
     */
    private  List<String> permissions;

    public BasicsAccount() {
    }

    public BasicsAccount(String userCode,
                         String password, String salt,
                         boolean disabledAccount,
                         boolean excessiveAttempts,
                         List<String> roles,
                         List<String> permissions) {
        this.userCode = userCode;
        this.password = password;
        this.salt = salt;
        this.disabledAccount = disabledAccount;
        this.excessiveAttempts = excessiveAttempts;
        this.roles = roles;
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "BasicsAccount{" +
                "userCode='" + userCode + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", disabledAccount=" + disabledAccount +
                ", excessiveAttempts=" + excessiveAttempts +
                ", roles=" + roles +
                ", permissions=" + permissions +
                '}';
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isDisabledAccount() {
        return disabledAccount;
    }

    public void setDisabledAccount(boolean disabledAccount) {
        this.disabledAccount = disabledAccount;
    }

    public boolean isExcessiveAttempts() {
        return excessiveAttempts;
    }

    public void setExcessiveAttempts(boolean excessiveAttempts) {
        this.excessiveAttempts = excessiveAttempts;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}

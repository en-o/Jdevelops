package cn.tannn.jdevelops.renewpwd.properties;

/**
 * 用户修改密码超级账户
 * <p> 用于在密码续命时，提供一个超级账户来执行，注意这个账户不允许修改和过期
 * <p> pgsql 或者基于pgsql的数据库才需要这个 </p>
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/19 09:42
 */
public class RootAccess {
    /**
     * 超级账户名
     */
    private String username;

    /**
     * 超级账户密码
     */
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RootAccess{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

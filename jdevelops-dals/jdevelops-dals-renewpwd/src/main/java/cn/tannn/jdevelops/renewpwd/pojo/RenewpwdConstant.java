package cn.tannn.jdevelops.renewpwd.pojo;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/15 16:13
 */
public interface RenewpwdConstant {

    /**
     * 配置中心配置的密码续命开关
     */
    String CONFIG_KEY = "jdevelops.renewpwd.enabled";
    /**
     * mysql密码过期查询语句
     */
    String MYSQL_PASSWORD_EXPIRED_SQL = "SELECT password_expired FROM mysql.user WHERE user = ?";

    // 当前使用的密码
    String DATASOURCE_PASSWORD_KEY = "spring.datasource.password";
    String DEFAULT_PASSWORD = "123456";
}

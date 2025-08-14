package cn.tannn.jdevelops.renewpwd.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 密码池 - 只在两个密码之间切换
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/11 14:54
 */
@ConfigurationProperties(prefix = "jdevelops.renewpwd")
public class PasswordPool {
    /**
     * 备用密码,DatabasePwdEnvironmentPostProcessor#postProcessBeanFactory
     *
     *  <p>备用密码用于在主密码失效或需要更换时使用</p>
     *  <p>注意：此密码应当与主密码不同，且在使用时需要确保安全性</p>
     *  <p>如果空，这使用datasource中的密码进行处理所有事情</p>
     *  <p>如果不为空，主密码登录失败就会使用这个秘密。并且如果是密码过期会进行交替设置，主过期用备份，备份过期用主</p>
     *  <p> 如果需要加密请使用：ENC(你的密码)</p>
     */
    private String backupPassword;

    /**
     * 是否启用密码虚续命，以配置文件为准-这里只是为空编写配置的时候有提示，具体在RenewpwdRegister#isRefreshEnabled
     * <p>这个可以控制注解中的enabled，这不代表不写注解，注解必须写</p>
     */
    private Boolean enabled;


    public String getBackupPassword() {
        return backupPassword;
    }

    public void setBackupPassword(String backupPassword) {
        this.backupPassword = backupPassword;
    }

    public Boolean getEnabled() {
        return enabled != null && enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "PasswordPool{" +
                "backupPassword='" + backupPassword + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}

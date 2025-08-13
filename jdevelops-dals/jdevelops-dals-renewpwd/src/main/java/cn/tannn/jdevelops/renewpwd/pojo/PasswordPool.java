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
     * 备用密码
     * <p>备用密码用于在主密码失效或需要更换时使用</p>
     * <p>注意：此密码应当与主密码不同，且在使用时需要确保安全性</p>
     * <p>如果备用密码为空，则表示不做密码处理</p>
     * <p> 如果需要加密请使用：ENC(你的密码)</p>
     */
    private String backupPassword;


    /**
     * 是否启用密码虚续命，以配置文件为准
     */
    private String enabled;


    public String getBackupPassword() {
        return backupPassword;
    }

    public void setBackupPassword(String backupPassword) {
        this.backupPassword = backupPassword;
    }


    /**
     * 检查密码池是否有效
     *
     * @return true 如果密码和备用密码都不为空
     */
    public boolean isValid() {
        return  backupPassword != null && !backupPassword.isEmpty();
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "PasswordPool{" +
                "backupPassword='" + backupPassword + '\'' +
                ", enabled='" + enabled + '\'' +
                '}';
    }
}

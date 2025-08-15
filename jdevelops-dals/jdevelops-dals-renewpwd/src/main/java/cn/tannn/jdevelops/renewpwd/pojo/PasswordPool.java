package cn.tannn.jdevelops.renewpwd.pojo;

import cn.tannn.jdevelops.renewpwd.util.AESUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 密码池 - 只在两个密码之间切换
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/11 14:54
 */
@Component("renewpwdPasswordPool")
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
     * 主密码 - 只在两个密码之间切换，这里的作用就是防止spring.datasource.password被污染导致不知道当前运行密码是哪一个了
     * <p>作为基准密码，用于判断当前 spring.datasource.password 是否被污染
     * <p>在密码轮换时，通过对比 currentPassword 和 masterPassword 来决定下一个密码是什么
     * <p>不应该随着密码更新而改变，保持稳定的基准值
     * <p>如果不配置，默认使用数据源中的密码
     * <p>主密码登录失败就会使用备用密码进行登录
     * <p>如果主密码过期，备用密码会被设置
     */
    private String masterPassword;

    /**
     * 是否启用密码虚续命，以配置文件为准-这里只是为空编写配置的时候有提示，具体在RenewpwdRegister#isRefreshEnabled
     * <p>这个可以控制注解中的enabled，这不代表不写注解，注解必须写</p>
     */
    private Boolean enabled;

    /**
     * 密码加密密钥 - 可选
     * <p>如果你的密码是加密的，请在这里配置解密密钥</p>
     * <p>如果不配置，默认使用 AESUtil.KEY 作为解密密钥</p>
     * <p>注意：确保密钥的安全性，不要将其暴露在公共代码库中</p>
     */
    private String pwdEncryptKey;


    public String getBackupPassword() {
        return backupPassword;
    }

    public String getBackupPasswordDecrypt() {
        return AESUtil.decryptPassword(backupPassword, pwdEncryptKey);
    }

    public void setBackupPassword(String backupPassword) {
        this.backupPassword = backupPassword;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public String getMasterPasswordDecrypt() {
        return AESUtil.decryptPassword(masterPassword, pwdEncryptKey);
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }


    public Boolean getEnabled() {
        return enabled != null && enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getPwdEncryptKey() {
        return pwdEncryptKey;
    }

    public void setPwdEncryptKey(String pwdEncryptKey) {
        this.pwdEncryptKey = pwdEncryptKey;
    }


    @Override
    public String toString() {
        return "PasswordPool{" +
                "backupPassword='" + backupPassword + '\'' +
                ", masterPassword='" + masterPassword + '\'' +
                ", enabled=" + enabled +
                ", pwdEncryptKey='" + pwdEncryptKey + '\'' +
                '}';
    }
}

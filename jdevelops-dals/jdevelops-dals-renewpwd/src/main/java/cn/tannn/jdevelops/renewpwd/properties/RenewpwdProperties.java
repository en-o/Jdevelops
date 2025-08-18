package cn.tannn.jdevelops.renewpwd.properties;

import cn.tannn.jdevelops.renewpwd.pojo.RenewpwdConstant;
import cn.tannn.jdevelops.renewpwd.util.AESUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * 密码配置 - 只在两个密码之间切换
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/11 14:54
 */
@Component("renewpwdProperties")
@ConfigurationProperties(prefix = "jdevelops.renewpwd")
public class RenewpwdProperties {
    /**
     * 备用密码,DatabasePwdEnvironmentPostProcessor#postProcessBeanFactory
     *
     *  <p> 注意：此密码应当与主密码不同，且在使用时需要确保安全性</p>
     *  <p> 如果不配置，默认使用数据源中的密码
     *  <p> 如果需要加密请使用：ENC(你的密码)</p>
     */
    private String backupPassword;

    /**
     * 主密码
     * <p>作为基准密码 跟数据源中的密码要保持一直（可以不配置
     * <p> 跟backupPassword进行轮换（取反）
     * <p> 如果不配置，默认使用数据源中的密码
     * <p> DefaultRenewPwdRefresh#determineNewPasswordForExpiredCurrent
     * <p> 如果需要加密请使用：ENC(你的密码)</p>
     */
    private String masterPassword;

    /**
     * 是否启用密码虚续命，以配置文件为准-这里只是为空编写配置的时候有提示，具体在RenewpwdRegister#isRefreshEnabled
     * <p>这个可以控制注解中的enabled，这不代表不写注解，注解必须写</p>
     * <p>{@link RenewpwdConstant#CONFIG_KEY}</p>
     */
    private Boolean enabled;

    /**
     * 密码加密密钥 - 可选
     * <p>如果你的密码是加密的，请在这里配置解密密钥</p>
     * <p>如果不配置，默认使用 AESUtil.KEY 作为解密密钥</p>
     * <p>注意：确保密钥的安全性，不要将其暴露在公共代码库中</p>
     */
    private String pwdEncryptKey;

    /**
     * SQL异常处理配置属性类
     */
    @NestedConfigurationProperty
    private SQLExceptionHandlingProperties exception;


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
        return enabled == null || enabled;
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


    public SQLExceptionHandlingProperties getException() {
        return exception==null? new SQLExceptionHandlingProperties() : exception;
    }

    public void setException(SQLExceptionHandlingProperties exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "RenewpwdProperties{" +
                "backupPassword='" + backupPassword + '\'' +
                ", masterPassword='" + masterPassword + '\'' +
                ", enabled=" + enabled +
                ", pwdEncryptKey='" + pwdEncryptKey + '\'' +
                ", exception=" + exception +
                '}';
    }
}

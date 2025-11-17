package cn.tannn.jdevelops.renewpwd.properties;

import cn.tannn.jdevelops.renewpwd.annotation.RenewpwdRegister;
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
     * 是否启用密码虚续命，以配置文件为准-这里只是为空编写配置的时候有提示，具体在{@link RenewpwdRegister#registerBeanDefinitions}
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

    /**
     * 用户修改密码超级账户
     * <p> 用于在密码续命时，提供一个超级账户来执行，注意这个账户不允许修改和过期
     * <p> pgsql 或者基于pgsql的数据库才需要这个 </p>
     */
    @NestedConfigurationProperty
    private RootAccess root;

    /**
     * 重置密码过期天数
     * <p>如果不配置，默认使用 30 天</p>
     * <p>mysql修改密码会重置天数（所以这里设置了我也不用）,但是pgsql和kingbase8需要自己手动重新设置（必须设置）</p>
     */
    private Integer resetExpiryDay = 30;


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

    public Integer getResetExpiryDay() {
        return resetExpiryDay;
    }

    public void setResetExpiryDay(Integer resetExpiryDay) {
        this.resetExpiryDay = resetExpiryDay;
    }

    public RootAccess getRoot() {
        return root;
    }

    public void setRoot(RootAccess root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return "RenewpwdProperties{" +
                "backupPassword='" + backupPassword + '\'' +
                ", masterPassword='" + masterPassword + '\'' +
                ", enabled=" + enabled +
                ", pwdEncryptKey='" + pwdEncryptKey + '\'' +
                ", exception=" + exception +
                ", root=" + root +
                ", resetExpiryDay=" + resetExpiryDay +
                '}';
    }
}

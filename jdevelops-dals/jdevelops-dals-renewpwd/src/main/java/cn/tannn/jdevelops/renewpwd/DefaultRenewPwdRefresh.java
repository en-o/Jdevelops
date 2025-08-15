package cn.tannn.jdevelops.renewpwd;

import cn.tannn.jdevelops.renewpwd.pojo.PasswordPool;
import cn.tannn.jdevelops.renewpwd.proerty.RenewPasswordService;
import cn.tannn.jdevelops.renewpwd.util.AESUtil;
import cn.tannn.jdevelops.renewpwd.util.PwdRefreshUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.List;

import static cn.tannn.jdevelops.renewpwd.util.PwdRefreshUtil.updateUserPassword;

/**
 * 默认密码续命处理实现
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/2 下午8:49
 */
public class DefaultRenewPwdRefresh implements RenewPwdRefresh {

    private final Environment environment;
    private final ApplicationContext applicationContext;
    private final ConfigurationPropertiesRebinder rebinder;
    private final RefreshScope refreshScope;
    @Autowired(required = false)
    private RenewPasswordService renewPasswordService;

    private static final Logger log = LoggerFactory.getLogger(DefaultRenewPwdRefresh.class);

    public DefaultRenewPwdRefresh(Environment environment
            , ApplicationContext applicationContext
            , ConfigurationPropertiesRebinder rebinder, RefreshScope refreshScope) {
        this.environment = environment;
        this.applicationContext = applicationContext;
        this.rebinder = rebinder;
        this.refreshScope = refreshScope;
    }

    @Override
    public void fixPassword() {
        if (renewPasswordService == null) {
            log.error("renewPasswordService is null");
            return;
        }
        // 获取当前环境中的数据源配置
        ConfigurableEnvironment ENV = (ConfigurableEnvironment) environment;
        // 获取当前环境中的数据源配置
        PasswordPool passwordPool = applicationContext.getBean(PasswordPool.class);
        // 这个跟下面的逻辑不一样，
        // 1. 调用这个一定是当前密码过期，所以我这里需要获取当前密码
        String currentPassword = ENV.getProperty("spring.datasource.password","123456");
        currentPassword = AESUtil.decryptPassword(currentPassword, passwordPool.getPwdEncryptKey());

        String backPassword = passwordPool.getBackupPasswordDecrypt();
        String masterPassword = passwordPool.getMasterPassword();

        // 防止spring.datasource.password被污染，即密码被改过
        String newPassword = currentPassword.equals(masterPassword) ? backPassword : masterPassword;
        // 更新庄户密码
        if (!updateUserPassword(ENV
                , currentPassword
                , backPassword)) {
            log.warn("[renewpwd] 数据源配置验证失败，跳过刷新");
            return;
        }
        renewPasswordService.initialize(newPassword);
        log.info("[renewpwd] 开始刷新数据源配置");
        // 1. 重新绑定所有 @ConfigurationProperties
        rebinder.rebind();
        log.info("[renewpwd] @ConfigurationProperties 重新绑定完成");
        refreshScope.refresh("dataSource");
        log.info("[renewpwd] 配置刷新完成");
    }

    @Override
    public void fixPassword(String newPassword) {
        fixPassword(newPassword,  List.of("dataSource"));
    }


    @Override
    public void fixPassword(String newPassword, List<String> beanNames) {
        if (renewPasswordService == null) {
            log.error("renewPasswordService is null");
            return;
        }
        // 如果是数据源相关配置变更，需要先验证连接
        if (!validateDatasourceBeforeRefresh(newPassword)) {
            log.warn("[renewpwd] 数据源配置验证失败，跳过刷新");
            return;
        }
        renewPasswordService.initialize(newPassword);
        log.info("[renewpwd] 开始刷新数据源配置");
        // 1. 重新绑定所有 @ConfigurationProperties
        rebinder.rebind();
        log.info("[renewpwd] @ConfigurationProperties 重新绑定完成");
        beanNames.forEach(refreshScope::refresh);
        log.info("[renewpwd] 配置刷新完成");
    }




    /**
     * 在刷新数据源Bean之前验证数据库连接
     *
     * @param newPassword  新密码
     * @return 验证是否通过
     */
    private boolean validateDatasourceBeforeRefresh(String newPassword) {
        try {
            // 获取当前环境中的数据源配置
            ConfigurableEnvironment ENV = (ConfigurableEnvironment) environment;
            // 既然是重置，当前密码是过期的也要用当前给定的密码进行重置，所以两个写一样
            boolean isValid = PwdRefreshUtil.validateDatasourceConfig(ENV, newPassword, newPassword);

            if (isValid) {
                log.info("[renewpwd] 数据源配置验证成功");
            } else {
                log.error("[renewpwd] 数据源配置验证失败");
            }
            return isValid;
        } catch (Exception e) {
            log.error("[renewpwd] 验证数据源配置时发生异常: {}", e.getMessage());
            return false;
        }
    }
}

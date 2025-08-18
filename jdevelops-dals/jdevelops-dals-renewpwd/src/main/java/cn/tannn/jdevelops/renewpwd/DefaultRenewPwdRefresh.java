package cn.tannn.jdevelops.renewpwd;

import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
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
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 默认密码续命处理实现
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/2 下午8:49
 */
public class DefaultRenewPwdRefresh implements RenewPwdRefresh {

    private static final Logger log = LoggerFactory.getLogger(DefaultRenewPwdRefresh.class);
    private static final String DEFAULT_PASSWORD = "123456";
    // 默认刷新的bean name
    private static final String DATASOURCE_BEAN_NAME = "dataSource";
    // 当前使用的密码
    private static final String DATASOURCE_PASSWORD_KEY = "spring.datasource.password";

    private final Environment environment;
    private final ApplicationContext applicationContext;
    private final ConfigurationPropertiesRebinder rebinder;
    private final RefreshScope refreshScope;

    @Autowired(required = false)
    private RenewPasswordService renewPasswordService;

    public DefaultRenewPwdRefresh(Environment environment,
                                  ApplicationContext applicationContext,
                                  ConfigurationPropertiesRebinder rebinder,
                                  RefreshScope refreshScope) {
        this.environment = environment;
        this.applicationContext = applicationContext;
        this.rebinder = rebinder;
        this.refreshScope = refreshScope;
    }

    @Override
    public void fixPassword() {
        if (validateService()) {
            return;
        }

        String newPassword = determineNewPasswordForExpiredCurrent();
        if (newPassword == null) {
            log.warn("[renewpwd] 无法确定新密码，跳过刷新");
            return;
        }

        executePasswordRefresh(newPassword, List.of(DATASOURCE_BEAN_NAME));
    }

    @Override
    public void fixPassword(String newPassword) {
        fixPassword(newPassword, List.of(DATASOURCE_BEAN_NAME));
    }

    @Override
    public void fixPassword(String newPassword, List<String> beanNames) {
        if (validateService() || Objects.isNull(newPassword)) {
            return;
        }

        if (CollectionUtils.isEmpty(beanNames)) {
            log.warn("[renewpwd] beanNames为空，跳过刷新");
            return;
        }

        // 验证数据源配置
        if (!validateDatasourceConfig(newPassword)) {
            log.warn("[renewpwd] 数据源配置验证失败，跳过刷新");
            return;
        }

        executePasswordRefresh(newPassword, beanNames);
    }

    /**
     * 验证服务是否可用
     * @return true 如果服务不可用，false 如果服务可用
     */
    private boolean validateService() {
        if (null == renewPasswordService) {
            log.error("[renewpwd] renewPasswordService is null");
            return true;
        }
        return false;
    }

    /**
     * 为过期的当前密码确定新密码
     * 当前密码过期时的密码切换逻辑
     */
    private String determineNewPasswordForExpiredCurrent() {
        try {
            ConfigurableEnvironment env = getConfigurableEnvironment();
            RenewpwdProperties renewpwdProperties = applicationContext.getBean(RenewpwdProperties.class);

            // 获取当前密码并尝试解密
            String currentPassword = env.getProperty(DATASOURCE_PASSWORD_KEY, DEFAULT_PASSWORD);
            currentPassword = AESUtil.decryptPassword(currentPassword, renewpwdProperties.getPwdEncryptKey());

            // 获取备用密码和主密码
            String backPassword = renewpwdProperties.getBackupPasswordDecrypt();
            String masterPassword = renewpwdProperties.getMasterPassword();

            // 当前密码如果等于主密码，则使用备用密码作为新密码，否则使用主密码
            String newPassword = currentPassword.equals(masterPassword) ? backPassword : masterPassword;

            // 验证当前密码和备用密码的有效性
            if (!PwdRefreshUtil.updateUserPassword(env, currentPassword, newPassword)) {
                log.error("[renewpwd] 用户密码更新验证失败");
                return null;
            }

            return newPassword;
        } catch (Exception e) {
            log.error("[renewpwd] 确定新密码时发生异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 验证数据源配置
     */
    private boolean validateDatasourceConfig(String newPassword) {
        try {
            ConfigurableEnvironment env = getConfigurableEnvironment();
            // 既然是重置，当前密码是过期的也要用当前给定的密码进行重置，所以两个写一样
            boolean isValid = PwdRefreshUtil.validateDatasourceConfig(env, newPassword, newPassword);

            if (isValid) {
                log.info("[renewpwd] 数据源配置验证成功");
            } else {
                log.error("[renewpwd] 数据源配置验证失败");
            }
            return isValid;
        } catch (Exception e) {
            log.error("[renewpwd] 验证数据源配置时发生异常: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 执行密码刷新操作 - 服务环境
     */
    private void executePasswordRefresh(String newPassword, List<String> beanNames) {
        try {
            // 初始化新密码
            renewPasswordService.initialize(newPassword);
            log.info("[renewpwd] 开始刷新数据源配置，新密码已初始化");

            // 重新绑定所有 @ConfigurationProperties
            rebinder.rebind();
            log.info("[renewpwd] @ConfigurationProperties 重新绑定完成");

            // 刷新指定的Bean
            beanNames.forEach(beanName -> {
                try {
                    refreshScope.refresh(beanName);
                    log.info("[renewpwd] Bean [{}] 刷新完成", beanName);
                } catch (Exception e) {
                    log.error("[renewpwd] Bean [{}] 刷新失败: {}", beanName, e.getMessage(), e);
                }
            });

            log.info("[renewpwd] 配置刷新完成");
        } catch (Exception e) {
            log.error("[renewpwd] 执行密码刷新时发生异常: {}", e.getMessage(), e);
            throw new RuntimeException("密码刷新失败", e);
        }
    }

    /**
     * 获取可配置环境对象
     */
    private ConfigurableEnvironment getConfigurableEnvironment() {
        return (ConfigurableEnvironment) environment;
    }
}

package cn.tannn.jdevelops.renewpwd;

import cn.tannn.jdevelops.renewpwd.jdbc.ExecuteJdbcSql;
import cn.tannn.jdevelops.renewpwd.jdbc.MySqlJdbc;
import cn.tannn.jdevelops.renewpwd.pojo.DbType;
import cn.tannn.jdevelops.renewpwd.proerty.RenewPasswordService;
import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import cn.tannn.jdevelops.renewpwd.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static cn.tannn.jdevelops.renewpwd.pojo.RenewpwdConstant.DATASOURCE_PASSWORD_KEY;
import static cn.tannn.jdevelops.renewpwd.pojo.RenewpwdConstant.DEFAULT_PASSWORD;

/**
 * 默认密码续命处理实现
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/2 下午8:49
 */
public class DefaultRenewPwdRefresh implements RenewPwdRefresh {

    private static final Logger log = LoggerFactory.getLogger(DefaultRenewPwdRefresh.class);

    // 默认刷新的bean name
    private static final String DATASOURCE_BEAN_NAME = "dataSource";


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
        // 更新密码
        String newPassword = determineNewPasswordForExpiredCurrent(DbType.MYSQL);
        if (newPassword == null) {
            log.warn("[renewpwd] 无法确定新密码，跳过刷新");
            return;
        }

        // 刷新spring上下文
        executePasswordRefresh(newPassword, List.of(DATASOURCE_BEAN_NAME));
    }

    @Override
    public void fixPassword(DbType dbType) {
        if (validateService()) {
            return;
        }
        // 更新密码
        String newPassword = determineNewPasswordForExpiredCurrent(dbType);
        if (newPassword == null) {
            log.warn("[renewpwd] 无法确定新密码，跳过刷新");
            return;
        }

        // 刷新spring上下文
        executePasswordRefresh(newPassword, List.of(DATASOURCE_BEAN_NAME));
    }

    @Override
    public void updatePassword() {
        if (validateService()) {
            return;
        }
        // 更新密码
        String newPassword = forceFixPassword(DbType.MYSQL);
        if (newPassword == null) {
            log.warn("[renewpwd] 无法确定新密码，跳过刷新");
            return;
        }
        // 刷新spring上下文
        executePasswordRefresh(newPassword, List.of(DATASOURCE_BEAN_NAME));
    }

    @Override
    public void updatePassword(DbType dbType) {
        if (validateService()) {
            return;
        }
        // 更新密码
        String newPassword = forceFixPassword(dbType);
        if (newPassword == null) {
            log.warn("[renewpwd] 无法确定新密码，跳过刷新");
            return;
        }
        // 刷新spring上下文
        executePasswordRefresh(newPassword, List.of(DATASOURCE_BEAN_NAME));
    }


    /**
     * 验证服务是否可用
     *
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
     * <p>当前密码过期时的密码切换逻辑</p>
     * <p>1. 当前密码过期则切换成备用密码</p>
     * <p>2. 备用密码为空则使用当前密码重置</p>
     *
     * @return 当前连接用的密码
     */
    private String determineNewPasswordForExpiredCurrent(DbType dbType) {
        try {
            ConfigurableEnvironment env = getConfigurableEnvironment();
            RenewpwdProperties renewpwdProperties = applicationContext.getBean(RenewpwdProperties.class);
            return ExecuteJdbcSql.updateUserPassword(env, renewpwdProperties, dbType);
        } catch (Exception e) {
            log.error("[renewpwd] 确定新密码时发生异常: {}", e.getMessage(), e);
            return null;
        }
    }


    /**
     * 暴力修改。
     * <p>如果当前连接的密码错误，则尝试备用密码休息修复</p>
     * <p>如果备用密码为空或者更当前密码一直则表示没有设置备份密码则不管了</p>
     *
     * @return 当前连接用的密码
     */
    private String forceFixPassword(DbType dbType) {
        try {
            ConfigurableEnvironment env = getConfigurableEnvironment();
            RenewpwdProperties renewpwdProperties = applicationContext.getBean(RenewpwdProperties.class);

            // 获取当前密码并尝试解密
            String currentPassword = env.getProperty(DATASOURCE_PASSWORD_KEY, DEFAULT_PASSWORD);
            currentPassword = AESUtil.decryptPassword(currentPassword, renewpwdProperties.getPwdEncryptKey());

            // 获取备用密码和主密码
            String backPassword = renewpwdProperties.getBackupPasswordDecrypt();
            String masterPassword = renewpwdProperties.getMasterPasswordDecrypt();
            String newPassword;
            if(dbType.equals(DbType.MYSQL)){
                // 当前密码如果等于主密码，则使用备用密码作为新密码，否则使用主密码
                newPassword = currentPassword.equals(masterPassword) ? backPassword : masterPassword;
                if (currentPassword.equals(newPassword)) {
                    log.error("[renewpwd] 当前密码与备用密码一致，且连接已断开，无法更新密码，交由业务方自行处理。");
                    return null;
                }
                // 验证当前密码和备用密码的有效性
                if (!ExecuteJdbcSql.updateUserPasswordForce(env, newPassword)) {
                    log.error("[renewpwd] 用户密码更新验证失败");
                    return null;
                }
            } else {
                log.error("[renewpwd] 不支持过期密码更新的数据库类型: {}", dbType);
                return null;
            }


            return newPassword;
        } catch (Exception e) {
            log.error("[renewpwd] 确定新密码时发生异常: {}", e.getMessage(), e);
            return null;
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
                CompletableFuture.runAsync(() -> {
                    try {
                        refreshScope.refresh(beanName);
                        log.info("[renewpwd] Bean [{}] 刷新完成", beanName);
                    } catch (Exception e) {
                        log.error("[renewpwd] Bean [{}] 刷新失败: {}", beanName, e.getMessage(), e);
                    }
                });
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

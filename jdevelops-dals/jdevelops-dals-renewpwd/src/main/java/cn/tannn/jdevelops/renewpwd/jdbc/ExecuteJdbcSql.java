package cn.tannn.jdevelops.renewpwd.jdbc;

import cn.tannn.jdevelops.renewpwd.pojo.DbType;
import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import cn.tannn.jdevelops.renewpwd.properties.RootAccess;
import cn.tannn.jdevelops.renewpwd.util.AESUtil;
import cn.tannn.jdevelops.renewpwd.util.PasswordUpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * sql 操作 - 重构版本
 * 使用封装的组件来简化代码结构
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/10 01:33
 */
public class ExecuteJdbcSql {

    private static PasswordUpdateListener passwordUpdateListener;
    private static final Logger log = LoggerFactory.getLogger(ExecuteJdbcSql.class);

    public static void setPasswordUpdateListener(PasswordUpdateListener listener) {
        passwordUpdateListener = listener;
    }

    /**
     * 验证数据源配置是否有效
     *
     * @param environment     ConfigurableEnvironment
     * @param currentPassword 当前密码
     * @param backupPassword  密码已过期 (1862) 错误时，将数据库密码修改为备份密码
     * @return true 当前密码有效或密码已成功更新，false 如果配置无效或验证失败
     */
    public static boolean validateDatasourceConfig(ConfigurableEnvironment environment,
                                                   String currentPassword,
                                                   String backupPassword,
                                                   RenewpwdProperties config) {
        return DatasourceConfigValidator.validateDatasourceConfiguration(
                environment, currentPassword, backupPassword, config);
    }

    /**
     * 更新用户密码
     *
     * @param environment           ConfigurableEnvironment
     * @param renewpwdProperties    配置文件
     * @param dbType                {@link DbType}
     * @param connectionPassword    连接用的密码 [明文]（只针对mysql有效，pgsql是通过配置中的root拿到）
     * @param newPassword           需要更新的密码 [明文]
     * @param consistencyComparison true判断当前密码跟修改的密码是否一致，一致则不更新，false不判断
     * @return 新密码 ，null=更新失败
     */
    public static String handlePasswordUpdate(ConfigurableEnvironment environment,
                                              RenewpwdProperties renewpwdProperties,
                                              DbType dbType,
                                              String connectionPassword,
                                              String newPassword,
                                              boolean consistencyComparison) {
        try {
            String url = environment.getProperty("spring.datasource.url");
            String driverClassName = environment.getProperty("spring.datasource.driver-class-name");
            log.error("[renewpwd] 密码已过期必须更改密码才能登录, 尝试更新密码");

            // 如果备份密码为空，则使用当前密码
            newPassword = newPassword.isEmpty() ? connectionPassword : newPassword;

            String username;
            String actualConnectionPassword;

            // 处理不同数据库
            if (dbType.equals(DbType.MYSQL)) {
                username = environment.getProperty("spring.datasource.username");
                actualConnectionPassword = connectionPassword;

                if (consistencyComparison && connectionPassword.equals(newPassword)) {
                    log.error("[renewpwd] 当前密码与备用密码一致，且连接已断开，无法更新密码，交由业务方自行处理。");
                    return null;
                }
            } else if (dbType.equals(DbType.POSTGRE_SQL) || dbType.equals(DbType.KINGBASE8)) {
                // 对于pgsql和kingbase8，使用root账户来更新密码
                PasswordUpdateContext context = preparePostgreSQLContext(renewpwdProperties, environment);
                if (context == null) {
                    return null;
                }
                username = context.getRootUsername();
                actualConnectionPassword = context.getRootPassword();
            } else {
                log.error("[renewpwd] 不支持过期密码更新的数据库类型: {}", dbType);
                return null;
            }

            // 执行密码更新
            String targetUsername = environment.getProperty("spring.datasource.username");
            boolean updateSuccess = PasswordUpdateExecutor.updateAndValidatePassword(
                    url, username, actualConnectionPassword, targetUsername, newPassword,
                    driverClassName, renewpwdProperties.getResetExpiryDay(), dbType);

            if (!updateSuccess) {
                log.error("[renewpwd] 用户密码更新验证失败");
                return null;
            }

            // 项目启动的时候用的通知，其他地方通知了也没用到
            notifyPasswordUpdate(newPassword);
            return newPassword;

        } catch (Exception e) {
            log.error("[renewpwd] 更新密码 - 验证数据源配置时发生异常", e);
            return null;
        }
    }


    // ================= 私有辅助方法 =================

    /**
     * PostgreSQL上下文信息
     */
    private static class PasswordUpdateContext {
        private final String rootUsername;
        private final String rootPassword;

        public PasswordUpdateContext(String rootUsername, String rootPassword) {
            this.rootUsername = rootUsername;
            this.rootPassword = rootPassword;
        }

        public String getRootUsername() {
            return rootUsername;
        }

        public String getRootPassword() {
            return rootPassword;
        }
    }

    /**
     * 准备PostgreSQL/KingBase8的上下文信息
     */
    private static PasswordUpdateContext preparePostgreSQLContext(RenewpwdProperties renewpwdProperties,
                                                                  ConfigurableEnvironment environment) {
        RootAccess root = renewpwdProperties.getRoot();
        if (root == null) {
            log.error("[renewpwd] pgsql/KINGBASE8 必须配置root超级账户");
            return null;
        }

        String rootUsername = root.getUsername();
        String rootPassword = root.getPassword();

        // 尝试解密密码
        rootPassword = AESUtil.decryptPassword(rootPassword, renewpwdProperties.getPwdEncryptKey());

        if (rootUsername == null || rootUsername.isEmpty() ||
                rootPassword == null || rootPassword.isEmpty()) {
            log.error("[renewpwd] pgsql/KINGBASE8 root账户密码不能为空");
            return null;
        }

        return new PasswordUpdateContext(rootUsername, rootPassword);
    }

    /**
     * 通知密码更新
     */
    private static void notifyPasswordUpdate(String newPassword) {
        if (passwordUpdateListener != null) {
            passwordUpdateListener.onPasswordUpdated(newPassword);
        }
    }
}

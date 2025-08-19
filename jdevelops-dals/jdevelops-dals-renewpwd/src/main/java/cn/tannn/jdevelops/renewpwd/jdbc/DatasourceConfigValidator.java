package cn.tannn.jdevelops.renewpwd.jdbc;

import cn.tannn.jdevelops.renewpwd.exception.SQLExceptionHandlingHelper;
import cn.tannn.jdevelops.renewpwd.pojo.DbType;
import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源配置验证器
 * 负责验证数据源配置的有效性和连接测试
 */
public class DatasourceConfigValidator {

    private static final Logger log = LoggerFactory.getLogger(DatasourceConfigValidator.class);

    /**
     * 数据源配置信息
     */
    public static class DatasourceConfig {
        private final String url;
        private final String username;
        private final String driverClassName;

        public DatasourceConfig(String url, String username, String driverClassName) {
            this.url = url;
            this.username = username;
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public String getUsername() {
            return username;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public boolean isValid() {
            return url != null && username != null && driverClassName != null;
        }
    }

    private DatasourceConfigValidator() {
    }

    /**
     * 从环境配置中提取数据源配置
     *
     * @param environment Spring环境配置
     * @return 数据源配置对象
     */
    public static DatasourceConfig extractDatasourceConfig(ConfigurableEnvironment environment) {
        String url = environment.getProperty("spring.datasource.url");
        String username = environment.getProperty("spring.datasource.username");
        String driverClassName = environment.getProperty("spring.datasource.driver-class-name");

        log.debug("[renewpwd] 提取数据源配置: url={}, username={}", url, username);

        return new DatasourceConfig(url, username, driverClassName);
    }

    /**
     * 验证数据源配置的完整性
     *
     * @param config          数据源配置
     * @param currentPassword 当前密码
     * @return true 如果配置完整
     */
    public static boolean validateConfigCompleteness(DatasourceConfig config, String currentPassword) {
        if (!config.isValid() || currentPassword == null) {
            log.warn("[renewpwd] 数据源配置不完整: url={}, username={}, password={}, driver={}",
                    config.getUrl(), config.getUsername(),
                    currentPassword != null ? "***" : "null", config.getDriverClassName());
            return false;
        }
        return true;
    }

    /**
     * 测试数据库连接
     *
     * @param config   数据源配置
     * @param password 连接密码
     * @return true 如果连接成功
     * @throws SQLException 如果连接过程中发生SQL异常
     */
    public static boolean testConnection(DatasourceConfig config, String password) throws SQLException {
        Connection connection = null;
        try {
            log.info("[renewpwd] 正在验证数据库连接: url={}, username={}", config.getUrl(), config.getUsername());
            DbType dbType = DbType.getDbType(config.getDriverClassName());
            connection = DatabaseConnectionManager.createConnection(
                    config.getUrl(),
                    config.getUsername(),
                    password,
                    config.getDriverClassName(),
                    dbType
                    );

            // 测试连接有效性
            if (DatabaseConnectionManager.isConnectionValid(connection, 3, dbType)) {
                log.info("[renewpwd] 数据库连接验证成功");
                return true;
            } else {
                log.warn("[renewpwd] 数据库连接无效");
                return false;
            }

        } catch (SQLException e) {
            log.debug("[renewpwd] 数据库连接测试失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[renewpwd] 数据库连接验证异常", e);
            return false;
        } finally {
            DatabaseConnectionManager.closeConnection(connection);
        }
    }

    /**
     * 完整的数据源配置验证流程
     * 包括配置完整性检查和连接测试
     *
     * @param environment     Spring环境配置
     * @param currentPassword 当前密码
     * @param backupPassword  备份密码
     * @param config          配置属性
     * @return true 如果验证成功或密码已成功更新
     */
    public static boolean validateDatasourceConfiguration(ConfigurableEnvironment environment,
                                                          String currentPassword,
                                                          String backupPassword,
                                                          RenewpwdProperties config) {
        try {
            // 如果备份密码为空，则使用当前密码
            backupPassword = backupPassword.isEmpty() ? currentPassword : backupPassword;

            // 提取数据源配置
            DatasourceConfig datasourceConfig = extractDatasourceConfig(environment);

            // 验证配置完整性
            if (!validateConfigCompleteness(datasourceConfig, currentPassword)) {
                return false;
            }

            // 测试数据库连接
            return testConnection(datasourceConfig, currentPassword);

        } catch (SQLException sqlException) {
            // 如果是SQL异常，交由异常处理器处理
            log.warn("[renewpwd] 数据库连接测试失败，等待错误处理: {}", sqlException.getMessage());
            return SQLExceptionHandlingHelper
                    .handleDataSourceException(environment, config, currentPassword, backupPassword,
                            extractDatasourceConfig(environment).getDriverClassName(),
                            sqlException, "项目启动时验证数据源配置");
        } catch (Exception e) {
            log.error("[renewpwd] 验证数据源配置时发生异常", e);
            return false;
        }
    }
}

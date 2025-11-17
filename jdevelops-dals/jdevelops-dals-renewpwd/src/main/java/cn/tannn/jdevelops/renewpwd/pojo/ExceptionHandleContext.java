package cn.tannn.jdevelops.renewpwd.pojo;

import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import org.springframework.core.env.ConfigurableEnvironment;

import java.sql.SQLException;

/**
 * 异常处理上下文
 */
public class ExceptionHandleContext {
    private final ConfigurableEnvironment environment;
    private final RenewpwdProperties config;
    private final String connectionPassword;
    private final String newPassword;
    private final String driverClassName;
    private final String connectionUrl;
    private final SQLException exception;
    private final String operation;

    public ExceptionHandleContext(ConfigurableEnvironment environment, RenewpwdProperties config,
                                  String connectionPassword,
                                  String newPassword,
                                  String driverClassName,
                                  String connectionUrl,
                                  SQLException exception, String operation) {
        this.environment = environment;
        this.config = config;
        this.connectionPassword = connectionPassword;
        this.newPassword = newPassword;
        this.driverClassName = driverClassName;
        this.connectionUrl = connectionUrl;
        this.exception = exception;
        this.operation = operation;
    }

    // Getters
    public ConfigurableEnvironment getEnvironment() {
        return environment;
    }

    public RenewpwdProperties getConfig() {
        return config;
    }

    public String getConnectionPassword() {
        return connectionPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public SQLException getException() {
        return exception;
    }

    public String getOperation() {
        return operation;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public boolean hasEnvironmentAndConfig() {
        return environment != null && config != null;
    }
}

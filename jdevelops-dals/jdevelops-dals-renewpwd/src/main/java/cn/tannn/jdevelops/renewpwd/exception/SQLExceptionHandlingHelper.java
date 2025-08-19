package cn.tannn.jdevelops.renewpwd.exception;

import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import cn.tannn.jdevelops.renewpwd.util.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import java.sql.SQLException;

/**
 * 对数据库连接异常进行处理的代理类
 */
public class SQLExceptionHandlingHelper {

    private static final Logger log = LoggerFactory.getLogger(SQLExceptionHandlingHelper.class);

    private SQLExceptionHandlingHelper() {
    }

    /* ================= 异常处理入口 ================= */

    /**
     * 处理数据源异常 - 基于代理对象
     *
     * @param proxy 数据源代理对象
     * @param e SQL异常
     * @param operation 操作名称
     */
    public static void handleDataSourceException(SQLExceptionHandlingDataSourceProxy proxy,
                                                 SQLException e, String operation) {
        // 确保是最深层的SQLException
        SQLException exception = DatabaseUtils.findDeepestSQLException(e);

        // 记录异常日志
        SQLExceptionLogger.logException(proxy, "DATASOURCE", exception, operation, null, 0);

        // 分类处理异常
        SQLExceptionClassifier.classifyAndHandle(proxy.getDriverClassName(), exception, operation);

        // 发送告警
        if (proxy.getConfig().getException().isAlertEnabled()) {
            AlertManager.sendAlert(proxy, "DATASOURCE", exception, operation, null, 0, "数据源连接异常");
        }
    }

    /**
     * 处理数据源异常 - 基于配置对象
     *
     * @param environment 环境配置
     * @param config 配置属性
     * @param connectionPassword 连接密码
     * @param newPassword 新密码
     * @param driverClassName 驱动类名
     * @param e SQL异常
     * @param operation 操作名称
     * @return true 如果处理成功
     */
    public static boolean handleDataSourceException(ConfigurableEnvironment environment,
                                                    RenewpwdProperties config,
                                                    String connectionPassword,
                                                    String newPassword,
                                                    String driverClassName,
                                                    SQLException e, String operation) {
        // 确保是最深层的SQLException
        SQLException exception = DatabaseUtils.findDeepestSQLException(e);

        // 记录异常日志
        SQLExceptionLogger.logException(config, "DATASOURCE", exception, operation, null, 0);

        // 分类处理异常
        boolean handle = SQLExceptionClassifier.classifyAndHandle(environment, config,
                connectionPassword, newPassword, driverClassName, exception, operation);

        // 发送告警
        if (config.getException().isAlertEnabled()) {
            AlertManager.sendAlert("DATASOURCE", exception, operation, null, 0, "数据源连接异常");
        }

        return handle;
    }

}

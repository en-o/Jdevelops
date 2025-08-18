package cn.tannn.jdevelops.renewpwd.exception;

import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SQLExceptionHandlingDataSourceProxy
 * 数据源代理类，通过拦截数据库操作实现全局异常处理和性能监控。
 * 支持配置化的异常处理策略和统计信息收集。
 */
public class SQLExceptionHandlingDataSourceProxy extends DelegatingDataSource {

    private static final Logger log = LoggerFactory.getLogger(SQLExceptionHandlingDataSourceProxy.class);

    private final DataSource targetDataSource;
    private final RenewpwdProperties config;

    // 告警频率控制
    final Map<String, Long> lastAlertTime = new ConcurrentHashMap<>();

    // 统计信息
    final AtomicLong totalOperations = new AtomicLong(0);
    final AtomicLong totalExceptions = new AtomicLong(0);
    final AtomicLong totalSlowQueries = new AtomicLong(0);

    public SQLExceptionHandlingDataSourceProxy(DataSource targetDataSource,
                                               RenewpwdProperties config) {
        super(targetDataSource);
        this.targetDataSource = targetDataSource;
        this.config = config;
        log.info("SQL异常处理代理初始化完成 - enable: {}, 配置: {}",config.getEnabled(), config.getException());
    }

    /**
     * 获取当前数据源的唯一标识
     * 用于日志记录和异常处理
     */
    @Override
    public Connection getConnection() throws SQLException {
        try {
            Connection connection = targetDataSource.getConnection();
            log.debug("获取数据库连接成功: {}", connection.getClass().getSimpleName());
            // 拦截所有数据库操作
            return ConnectionProxyFactory.createProxy(this, connection);
        } catch (SQLException e) {
            SQLExceptionHandlingHelper.handleDataSourceException(this, e, "获取数据库连接");
            throw e;
        }
    }

    /**
     * 获取带认证的数据库连接
     * 通过代理模式拦截认证过程中的异常
     *
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return 代理的数据库连接
     * @throws SQLException 如果获取连接失败
     */
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        try {
            Connection connection = targetDataSource.getConnection(username, password);
            log.debug("获取数据库连接成功(带认证): {}", connection.getClass().getSimpleName());
            // 拦截所有数据库操作
            return ConnectionProxyFactory.createProxy(this, connection);
        } catch (SQLException e) {
            SQLExceptionHandlingHelper.handleDataSourceException(this, e, "获取数据库连接(带认证)");
            throw e;
        }
    }

    /* ===== 统计接口保持原样 ===== */
    public String getStatistics() {
        return SQLExceptionHandlingHelper.buildStatistics(this);
    }

    public void resetStatistics() {
        SQLExceptionHandlingHelper.resetStatistics(this);
    }

    /* ===== 允许包内访问的 getters ===== */
    RenewpwdProperties getConfig() {
        return config;
    }
}

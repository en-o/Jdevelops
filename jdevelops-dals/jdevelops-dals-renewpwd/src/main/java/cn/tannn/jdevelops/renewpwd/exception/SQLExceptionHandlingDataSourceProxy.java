package cn.tannn.jdevelops.renewpwd.exception;

import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import cn.tannn.jdevelops.renewpwd.properties.SQLExceptionHandlingProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SQL异常处理数据源代理
 * 通过代理模式拦截所有数据库操作，实现真正的全局异常处理
 * 集成配置化的异常处理策略
 */
public class SQLExceptionHandlingDataSourceProxy extends DelegatingDataSource {

    private static final Logger log = LoggerFactory.getLogger(SQLExceptionHandlingDataSourceProxy.class);

    private final DataSource targetDataSource;
    private final RenewpwdProperties config;

    // 告警频率控制
    private final Map<String, Long> lastAlertTime = new ConcurrentHashMap<>();

    // 统计信息
    private final AtomicLong totalOperations = new AtomicLong(0);
    private final AtomicLong totalExceptions = new AtomicLong(0);
    private final AtomicLong totalSlowQueries = new AtomicLong(0);

    public SQLExceptionHandlingDataSourceProxy(DataSource targetDataSource,
                                               RenewpwdProperties config) {
        super(targetDataSource);
        this.targetDataSource = targetDataSource;
        this.config = config;

        log.info("SQL异常处理代理初始化完成 - enable: {}, 配置: {}",config.getEnabled(), config.getException());
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            Connection connection = targetDataSource.getConnection();
            log.debug("获取数据库连接成功: {}", connection.getClass().getSimpleName());
            return createConnectionProxy(connection);
        } catch (SQLException e) {
            handleDataSourceException(e, "获取数据库连接");
            throw e;
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        try {
            Connection connection = targetDataSource.getConnection(username, password);
            log.debug("获取数据库连接成功(带认证): {}", connection.getClass().getSimpleName());
            return createConnectionProxy(connection);
        } catch (SQLException e) {
            handleDataSourceException(e, "获取数据库连接(带认证)");
            throw e;
        }
    }

    /**
     * 创建Connection代理
     */
    private Connection createConnectionProxy(Connection connection) {
        return (Connection) Proxy.newProxyInstance(
                connection.getClass().getClassLoader(),
                new Class[]{Connection.class},
                new ConnectionInvocationHandler(connection)
        );
    }

    /**
     * Connection调用处理器
     */
    private class ConnectionInvocationHandler implements InvocationHandler {
        private final Connection targetConnection;

        public ConnectionInvocationHandler(Connection targetConnection) {
            this.targetConnection = targetConnection;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            long startTime = System.currentTimeMillis();

            try {
                Object result = method.invoke(targetConnection, args);

                // 对于创建Statement的方法，返回Statement代理
                if (result instanceof PreparedStatement) {
                    return createPreparedStatementProxy((PreparedStatement) result, args);
                } else if (result instanceof CallableStatement) {
                    return createCallableStatementProxy((CallableStatement) result, args);
                } else if (result instanceof Statement) {
                    return createStatementProxy((Statement) result);
                }

                return result;

            } catch (Exception e) {
                long duration = System.currentTimeMillis() - startTime;

                Throwable cause = e.getCause();
                if (cause instanceof SQLException) {
                    handleConnectionException((SQLException) cause, methodName, args, duration);
                }

                throw e;
            }
        }
    }

    /**
     * 创建PreparedStatement代理
     */
    private PreparedStatement createPreparedStatementProxy(PreparedStatement stmt, Object[] args) {
        return (PreparedStatement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                new Class[]{PreparedStatement.class},
                new PreparedStatementInvocationHandler(stmt, args)
        );
    }

    /**
     * 创建CallableStatement代理
     */
    private CallableStatement createCallableStatementProxy(CallableStatement stmt, Object[] args) {
        return (CallableStatement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                new Class[]{CallableStatement.class},
                new CallableStatementInvocationHandler(stmt, args)
        );
    }

    /**
     * 创建Statement代理
     */
    private Statement createStatementProxy(Statement stmt) {
        return (Statement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                new Class[]{Statement.class},
                new StatementInvocationHandler(stmt)
        );
    }

    /**
     * PreparedStatement调用处理器
     */
    private class PreparedStatementInvocationHandler implements InvocationHandler {
        private final PreparedStatement targetStatement;
        private final Object[] sqlArgs;

        public PreparedStatementInvocationHandler(PreparedStatement targetStatement, Object[] sqlArgs) {
            this.targetStatement = targetStatement;
            this.sqlArgs = sqlArgs;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            long startTime = System.currentTimeMillis();

            try {
                Object result = method.invoke(targetStatement, args);

                if (isExecuteMethod(methodName)) {
                    long duration = System.currentTimeMillis() - startTime;
                    totalOperations.incrementAndGet();

                    // 记录成功的SQL执行
                    logSuccessfulExecution(methodName, sqlArgs, duration);

                    // 慢查询检测
                    if (duration > config.getException().getSlowQueryThreshold()) {
                        totalSlowQueries.incrementAndGet();
                        handleSlowQuery(methodName, sqlArgs, duration);
                    }
                }

                return result;

            } catch (Exception e) {
                long duration = System.currentTimeMillis() - startTime;
                totalExceptions.incrementAndGet();

                Throwable cause = e.getCause();
                if (cause instanceof SQLException) {
                    handleStatementException((SQLException) cause, methodName, sqlArgs, duration);
                }

                throw e;
            }
        }
    }

    /**
     * CallableStatement调用处理器
     */
    private class CallableStatementInvocationHandler implements InvocationHandler {
        private final CallableStatement targetStatement;
        private final Object[] sqlArgs;

        public CallableStatementInvocationHandler(CallableStatement targetStatement, Object[] sqlArgs) {
            this.targetStatement = targetStatement;
            this.sqlArgs = sqlArgs;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            long startTime = System.currentTimeMillis();

            try {
                Object result = method.invoke(targetStatement, args);

                if (isExecuteMethod(methodName)) {
                    long duration = System.currentTimeMillis() - startTime;
                    totalOperations.incrementAndGet();
                    logSuccessfulExecution("存储过程:" + methodName, sqlArgs, duration);

                    if (duration > config.getException().getSlowQueryThreshold()) {
                        totalSlowQueries.incrementAndGet();
                        handleSlowQuery("存储过程:" + methodName, sqlArgs, duration);
                    }
                }

                return result;

            } catch (Exception e) {
                long duration = System.currentTimeMillis() - startTime;
                totalExceptions.incrementAndGet();

                Throwable cause = e.getCause();
                if (cause instanceof SQLException) {
                    handleStatementException((SQLException) cause, "存储过程:" + methodName, sqlArgs, duration);
                }

                throw e;
            }
        }
    }

    /**
     * Statement调用处理器
     */
    private class StatementInvocationHandler implements InvocationHandler {
        private final Statement targetStatement;

        public StatementInvocationHandler(Statement targetStatement) {
            this.targetStatement = targetStatement;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            long startTime = System.currentTimeMillis();

            try {
                Object result = method.invoke(targetStatement, args);

                if (isExecuteMethod(methodName)) {
                    long duration = System.currentTimeMillis() - startTime;
                    totalOperations.incrementAndGet();
                    logSuccessfulExecution(methodName, args, duration);

                    if (duration > config.getException().getSlowQueryThreshold()) {
                        totalSlowQueries.incrementAndGet();
                        handleSlowQuery(methodName, args, duration);
                    }
                }

                return result;

            } catch (Exception e) {
                long duration = System.currentTimeMillis() - startTime;
                totalExceptions.incrementAndGet();

                Throwable cause = e.getCause();
                if (cause instanceof SQLException) {
                    handleStatementException((SQLException) cause, methodName, args, duration);
                }

                throw e;
            }
        }
    }

    /**
     * 判断是否为SQL执行方法
     */
    private boolean isExecuteMethod(String methodName) {
        return methodName.startsWith("execute") ||
                methodName.equals("executeQuery") ||
                methodName.equals("executeUpdate") ||
                methodName.equals("executeBatch") ||
                methodName.equals("executeLargeBatch");
    }

    /**
     * 记录成功的SQL执行
     */
    private void logSuccessfulExecution(String operation, Object[] params, long duration) {
        if (config.getException().isLogSuccessfulOperations()) {
            String paramStr = config.getException().isLogParameters() && params != null ?
                    Arrays.toString(params) : "参数已隐藏";

            switch (config.getException().getLogLevel().toUpperCase()) {
                case "DEBUG":
                    log.debug("SQL执行成功 - 操作: {}, 耗时: {}ms, 参数: {}", operation, duration, paramStr);
                    break;
                case "INFO":
                    log.info("SQL执行成功 - 操作: {}, 耗时: {}ms", operation, duration);
                    break;
                default:
                    log.trace("SQL执行成功 - 操作: {}, 耗时: {}ms, 参数: {}", operation, duration, paramStr);
            }
        }
    }

    /**
     * 处理慢查询
     */
    private void handleSlowQuery(String operation, Object[] params, long duration) {
        String paramStr = config.getException().isLogParameters() && params != null ?
                Arrays.toString(params) : "参数已隐藏";

        log.warn("慢查询检测 - 操作: {}, 耗时: {}ms, 阈值: {}ms, 参数: {}",
                operation, duration, config.getException().getSlowQueryThreshold(), paramStr);

        if (config.getException().isAlertEnabled()) {
            sendAlert("SLOW_QUERY", null, operation, params, duration,
                    "慢查询检测: 耗时" + duration + "ms，超过阈值" + config.getException().getSlowQueryThreshold() + "ms");
        }
    }

    /**
     * 处理数据源级别异常
     */
    private void handleDataSourceException(SQLException e, String operation) {
        logException("DATASOURCE", e, operation, null, 0);

        if (config.getException().isAlertEnabled()) {
            sendAlert("DATASOURCE", e, operation, null, 0, "数据源连接异常");
        }
    }

    /**
     * 处理连接级别异常
     */
    private void handleConnectionException(SQLException e, String operation, Object[] params, long duration) {
        logException("CONNECTION", e, operation, params, duration);

        // 分类处理
        classifyAndHandle(e, operation);

        if (config.getException().isAlertEnabled()) {
            sendAlert("CONNECTION", e, operation, params, duration, "连接操作异常");
        }
    }

    /**
     * 处理SQL执行异常
     */
    private void handleStatementException(SQLException e, String operation, Object[] params, long duration) {
        logException("SQL_EXECUTION", e, operation, params, duration);

        // 分类处理
        classifyAndHandle(e, operation);

        if (config.getException().isAlertEnabled()) {
            sendAlert("SQL_EXECUTION", e, operation, params, duration, "SQL执行异常");
        }
    }

    /**
     * 记录异常日志
     */
    private void logException(String level, SQLException e, String operation, Object[] params, long duration) {
        String paramStr = config.getException().isLogParameters() && params != null ?
                Arrays.toString(params) : "参数已隐藏";

        switch (config.getException().getLogLevel().toUpperCase()) {
            case "ERROR":
                log.error("=== [{}] SQL异常处理 ===", level);
                log.error("操作: {}", operation);
                log.error("执行时间: {}ms", duration);
                log.error("SQL状态码: {}", e.getSQLState());
                log.error("错误码: {}", e.getErrorCode());
                log.error("错误信息: {}", e.getMessage());
                log.error("参数: {}", paramStr);
                break;
            case "WARN":
                log.warn("[{}] SQL异常 - 操作: {}, 状态码: {}, 错误码: {}, 信息: {}",
                        level, operation, e.getSQLState(), e.getErrorCode(), e.getMessage());
                break;
            default:
                log.debug("[{}] SQL异常详情 - 操作: {}, 耗时: {}ms, 状态码: {}, 错误码: {}, 参数: {}",
                        level, operation, duration, e.getSQLState(), e.getErrorCode(), paramStr);
        }
    }

    /**
     * 根据SQLException类型进行分类处理
     */
    private void classifyAndHandle(SQLException e, String operation) {
        String sqlState = e.getSQLState();
        if (sqlState == null) return;

        // 检查是否为关键异常
        boolean isCritical = Arrays.asList(config.getException().getCriticalSqlStates()).contains(sqlState);

        switch (sqlState) {
            case "40001": // 死锁
            case "40P01": // PostgreSQL死锁
                handleDeadlock(e, operation, isCritical);
                break;
            case "08001": // 连接失败
            case "08006": // 连接异常
            case "08S01": // MySQL连接异常
                handleConnectionIssue(e, operation, isCritical);
                break;
            case "23000": // 完整性约束违反
            case "23505": // 唯一约束违反 (PostgreSQL)
            case "23001": // 限制违反
                handleConstraintViolation(e, operation, isCritical);
                break;
            case "42000": // 语法错误
            case "42601": // 语法错误 (PostgreSQL)
                handleSyntaxError(e, operation, isCritical);
                break;
            case "28000": // 无效的授权规范
                handleAuthorizationError(e, operation, isCritical);
                break;
            case "HY000": // MySQL通用错误
                handleMySQLGenericError(e, operation, isCritical);
                break;
            default:
                handleGenericSQLException(e, operation, isCritical);
        }
    }

    private void handleDeadlock(SQLException e, String operation, boolean isCritical) {
        log.warn("死锁检测 - 操作: {}, 错误码: {}, 关键级别: {}", operation, e.getErrorCode(), isCritical);
    }

    private void handleConnectionIssue(SQLException e, String operation, boolean isCritical) {
        log.warn("连接问题 - 操作: {}, 错误码: {}, 关键级别: {}", operation, e.getErrorCode(), isCritical);
    }

    private void handleConstraintViolation(SQLException e, String operation, boolean isCritical) {
        log.warn("约束违反 - 操作: {}, 错误码: {}, 关键级别: {}", operation, e.getErrorCode(), isCritical);
    }

    private void handleSyntaxError(SQLException e, String operation, boolean isCritical) {
        log.error("SQL语法错误 - 操作: {}, 错误码: {}, 关键级别: {}", operation, e.getErrorCode(), isCritical);
    }

    private void handleAuthorizationError(SQLException e, String operation, boolean isCritical) {
        log.error("权限错误 - 操作: {}, 错误码: {}, 关键级别: {}", operation, e.getErrorCode(), isCritical);
    }

    private void handleMySQLGenericError(SQLException e, String operation, boolean isCritical) {
        log.error("MySQL通用错误 - 操作: {}, 错误码: {}, 关键级别: {}", operation, e.getErrorCode(), isCritical);
    }

    private void handleGenericSQLException(SQLException e, String operation, boolean isCritical) {
        log.error("未分类SQL异常 - 操作: {}, 错误码: {}, 关键级别: {}", operation, e.getErrorCode(), isCritical);
    }

    /**
     * 发送告警（带频率控制）
     */
    private void sendAlert(String level, SQLException e, String operation,
                           Object[] params, long duration, String description) {
        String alertKey = level + "_" + operation;
        long currentTime = System.currentTimeMillis();

        // 检查告警频率控制
        Long lastTime = lastAlertTime.get(alertKey);
        if (lastTime != null &&
                (currentTime - lastTime) < (config.getException().getAlertIntervalSeconds() * 1000)) {
            log.debug("告警被频率控制限制: {}", alertKey);
            return;
        }

        lastAlertTime.put(alertKey, currentTime);

        String alertMsg;
        if (e != null) {
            alertMsg = String.format(
                    "[%s] %s - 操作: %s, 状态码: %s, 错误码: %d, 耗时: %dms, 错误: %s",
                    level, description, operation, e.getSQLState(), e.getErrorCode(), duration, e.getMessage()
            );
        } else {
            alertMsg = String.format(
                    "[%s] %s - 操作: %s, 耗时: %dms",
                    level, description, operation, duration
            );
        }

        log.warn("告警发送: {}", alertMsg);

        // 这里可以集成实际的告警系统
        // alertService.sendAlert(alertMsg);
        // 或者发送到消息队列、邮件、短信等
    }

    /**
     * 获取统计信息
     */
    public String getStatistics() {
        return String.format(
                "SQL执行统计 - 总操作数: %d, 异常数: %d, 慢查询数: %d, 异常率: %.2f%%, 慢查询率: %.2f%%",
                totalOperations.get(),
                totalExceptions.get(),
                totalSlowQueries.get(),
                totalOperations.get() > 0 ? (totalExceptions.get() * 100.0 / totalOperations.get()) : 0,
                totalOperations.get() > 0 ? (totalSlowQueries.get() * 100.0 / totalOperations.get()) : 0
        );
    }

    /**
     * 重置统计信息
     */
    public void resetStatistics() {
        totalOperations.set(0);
        totalExceptions.set(0);
        totalSlowQueries.set(0);
        lastAlertTime.clear();
        log.info("SQL执行统计信息已重置");
    }
}

package cn.tannn.jdevelops.renewpwd.properties;


/**
 * SQL异常处理配置属性类
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/11 14:54
 */
public class SQLExceptionHandlingProperties {

    /**
     * 是否启用告警功能
     * 默认: true
     */
    private boolean alertEnabled = true;

    /**
     * 日志记录级别 (ERROR/WARN/DEBUG/INFO)
     * 默认: ERROR
     */
    private String logLevel = "ERROR";

    /**
     * 是否记录SQL执行成功的日志
     * 开发环境建议开启，生产环境建议关闭
     * 默认: false
     */
    private boolean logSuccessfulOperations = false;

    /**
     * 是否记录SQL参数
     * 生产环境建议关闭以避免敏感信息泄露
     * 默认: true
     */
    private boolean logParameters = true;

    /**
     * 慢查询阈值（毫秒）
     * 超过此阈值的SQL会被记录为慢查询
     * 默认: 3000ms (3秒)
     */
    private long slowQueryThreshold = 3000;

    /**
     * 死锁重试次数
     * 检测到死锁时的自动重试次数
     * 默认: 3
     */
    private int deadlockRetryCount = 3;

    /**
     * 告警发送间隔（秒）
     * 相同类型告警的最小发送间隔，防止告警轰炸
     * 默认: 300秒 (5分钟)
     */
    private int alertIntervalSeconds = 300;

    /**
     * 需要特别关注的SQL状态码
     * 这些状态码会被标记为关键异常
     * 默认包含: 死锁、连接异常、权限错误等
     */
    private String[] criticalSqlStates = {
            "40001",  // 死锁 (通用)
            "40P01",  // PostgreSQL死锁
            "08001",  // 连接失败
            "08006",  // 连接异常
            "08S01",  // MySQL连接异常
            "28000"   // 无效的授权规范
    };

    /**
     * 连接超时时间（毫秒）
     * 获取数据库连接的超时时间
     * 默认: 30000ms (30秒)
     */
    private long connectionTimeout = 30000;

    /**
     * 查询超时时间（毫秒）
     * SQL查询执行的超时时间
     * 默认: 60000ms (60秒)
     */
    private long queryTimeout = 60000;

    /**
     * 是否启用连接泄露检测
     * 默认: true
     */
    private boolean enableConnectionLeakDetection = true;

    /**
     * 连接泄露检测阈值（毫秒）
     * 连接被占用超过此时间会被认为可能泄露
     * 默认: 60000ms (60秒)
     */
    private long connectionLeakDetectionThreshold = 60000;

    /**
     * 是否启用SQL注入检测
     * 简单的SQL注入模式检测
     * 默认: true
     */
    private boolean enableSqlInjectionDetection = true;

    /**
     * 危险SQL关键字列表
     * 用于SQL注入检测
     */
    private String[] dangerousSqlKeywords = {
            "drop table", "delete from", "truncate", "alter table",
            "create table", "insert into", "update set", "grant", "revoke",
            "exec", "execute", "sp_", "xp_", "union select", "script", "javascript"
    };

    /**
     * 是否启用批量操作监控
     * 监控批量操作的性能和异常
     * 默认: true
     */
    private boolean enableBatchMonitoring = true;

    /**
     * 批量操作大小阈值
     * 超过此大小的批量操作会被特别关注
     * 默认: 1000
     */
    private int batchSizeThreshold = 1000;

    /**
     * 是否启用事务监控
     * 监控事务的开启、提交、回滚等操作
     * 默认: true
     */
    private boolean enableTransactionMonitoring = true;

    /**
     * 长事务阈值（毫秒）
     * 超过此时间的事务会被记录为长事务
     * 默认: 30000ms (30秒)
     */
    private long longTransactionThreshold = 30000;

    /**
     * 是否启用数据库连接池监控
     * 监控连接池的状态和性能
     * 默认: true
     */
    private boolean enableConnectionPoolMonitoring = true;

    /**
     * 连接池使用率告警阈值（百分比）
     * 连接池使用率超过此阈值会发送告警
     * 默认: 80%
     */
    private int connectionPoolUsageThreshold = 80;

    /**
     * 异常统计窗口大小（秒）
     * 统计异常频率的时间窗口
     * 默认: 60秒
     */
    private int exceptionStatisticsWindow = 60;

    /**
     * 异常频率告警阈值（次/分钟）
     * 在统计窗口内异常次数超过此阈值会发送告警
     * 默认: 10次/分钟
     */
    private int exceptionFrequencyThreshold = 10;


    public boolean isAlertEnabled() {
        return alertEnabled;
    }

    public void setAlertEnabled(boolean alertEnabled) {
        this.alertEnabled = alertEnabled;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public boolean isLogSuccessfulOperations() {
        return logSuccessfulOperations;
    }

    public void setLogSuccessfulOperations(boolean logSuccessfulOperations) {
        this.logSuccessfulOperations = logSuccessfulOperations;
    }

    public boolean isLogParameters() {
        return logParameters;
    }

    public void setLogParameters(boolean logParameters) {
        this.logParameters = logParameters;
    }

    public long getSlowQueryThreshold() {
        return slowQueryThreshold;
    }

    public void setSlowQueryThreshold(long slowQueryThreshold) {
        this.slowQueryThreshold = slowQueryThreshold;
    }

    public int getDeadlockRetryCount() {
        return deadlockRetryCount;
    }

    public void setDeadlockRetryCount(int deadlockRetryCount) {
        this.deadlockRetryCount = deadlockRetryCount;
    }

    public int getAlertIntervalSeconds() {
        return alertIntervalSeconds;
    }

    public void setAlertIntervalSeconds(int alertIntervalSeconds) {
        this.alertIntervalSeconds = alertIntervalSeconds;
    }

    public String[] getCriticalSqlStates() {
        return criticalSqlStates;
    }

    public void setCriticalSqlStates(String[] criticalSqlStates) {
        this.criticalSqlStates = criticalSqlStates;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public long getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(long queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public boolean isEnableConnectionLeakDetection() {
        return enableConnectionLeakDetection;
    }

    public void setEnableConnectionLeakDetection(boolean enableConnectionLeakDetection) {
        this.enableConnectionLeakDetection = enableConnectionLeakDetection;
    }

    public long getConnectionLeakDetectionThreshold() {
        return connectionLeakDetectionThreshold;
    }

    public void setConnectionLeakDetectionThreshold(long connectionLeakDetectionThreshold) {
        this.connectionLeakDetectionThreshold = connectionLeakDetectionThreshold;
    }

    public boolean isEnableSqlInjectionDetection() {
        return enableSqlInjectionDetection;
    }

    public void setEnableSqlInjectionDetection(boolean enableSqlInjectionDetection) {
        this.enableSqlInjectionDetection = enableSqlInjectionDetection;
    }

    public String[] getDangerousSqlKeywords() {
        return dangerousSqlKeywords;
    }

    public void setDangerousSqlKeywords(String[] dangerousSqlKeywords) {
        this.dangerousSqlKeywords = dangerousSqlKeywords;
    }

    public boolean isEnableBatchMonitoring() {
        return enableBatchMonitoring;
    }

    public void setEnableBatchMonitoring(boolean enableBatchMonitoring) {
        this.enableBatchMonitoring = enableBatchMonitoring;
    }

    public int getBatchSizeThreshold() {
        return batchSizeThreshold;
    }

    public void setBatchSizeThreshold(int batchSizeThreshold) {
        this.batchSizeThreshold = batchSizeThreshold;
    }

    public boolean isEnableTransactionMonitoring() {
        return enableTransactionMonitoring;
    }

    public void setEnableTransactionMonitoring(boolean enableTransactionMonitoring) {
        this.enableTransactionMonitoring = enableTransactionMonitoring;
    }

    public long getLongTransactionThreshold() {
        return longTransactionThreshold;
    }

    public void setLongTransactionThreshold(long longTransactionThreshold) {
        this.longTransactionThreshold = longTransactionThreshold;
    }

    public boolean isEnableConnectionPoolMonitoring() {
        return enableConnectionPoolMonitoring;
    }

    public void setEnableConnectionPoolMonitoring(boolean enableConnectionPoolMonitoring) {
        this.enableConnectionPoolMonitoring = enableConnectionPoolMonitoring;
    }

    public int getConnectionPoolUsageThreshold() {
        return connectionPoolUsageThreshold;
    }

    public void setConnectionPoolUsageThreshold(int connectionPoolUsageThreshold) {
        this.connectionPoolUsageThreshold = connectionPoolUsageThreshold;
    }

    public int getExceptionStatisticsWindow() {
        return exceptionStatisticsWindow;
    }

    public void setExceptionStatisticsWindow(int exceptionStatisticsWindow) {
        this.exceptionStatisticsWindow = exceptionStatisticsWindow;
    }

    public int getExceptionFrequencyThreshold() {
        return exceptionFrequencyThreshold;
    }

    public void setExceptionFrequencyThreshold(int exceptionFrequencyThreshold) {
        this.exceptionFrequencyThreshold = exceptionFrequencyThreshold;
    }

    @Override
    public String toString() {
        return "SQLExceptionHandlingProperties{" +
                "alertEnabled=" + alertEnabled +
                ", logLevel='" + logLevel + '\'' +
                ", logSuccessfulOperations=" + logSuccessfulOperations +
                ", logParameters=" + logParameters +
                ", slowQueryThreshold=" + slowQueryThreshold +
                ", deadlockRetryCount=" + deadlockRetryCount +
                ", alertIntervalSeconds=" + alertIntervalSeconds +
                ", connectionTimeout=" + connectionTimeout +
                ", queryTimeout=" + queryTimeout +
                ", enableConnectionLeakDetection=" + enableConnectionLeakDetection +
                ", enableSqlInjectionDetection=" + enableSqlInjectionDetection +
                ", enableBatchMonitoring=" + enableBatchMonitoring +
                ", enableTransactionMonitoring=" + enableTransactionMonitoring +
                ", enableConnectionPoolMonitoring=" + enableConnectionPoolMonitoring +
                '}';
    }
}

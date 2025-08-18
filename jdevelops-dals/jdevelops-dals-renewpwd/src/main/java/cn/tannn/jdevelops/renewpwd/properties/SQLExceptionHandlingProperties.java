package cn.tannn.jdevelops.renewpwd.properties;


import java.util.Arrays;

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
     * 告警发送间隔（秒）
     * 相同类型告警的最小发送间隔，防止告警轰炸
     * 默认: 300秒 (5分钟)
     */
    private int alertIntervalSeconds = 300;


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


    public int getAlertIntervalSeconds() {
        return alertIntervalSeconds;
    }

    public void setAlertIntervalSeconds(int alertIntervalSeconds) {
        this.alertIntervalSeconds = alertIntervalSeconds;
    }

    @Override
    public String toString() {
        return "SQLExceptionHandlingProperties{" +
                "alertEnabled=" + alertEnabled +
                ", logLevel='" + logLevel + '\'' +
                ", logSuccessfulOperations=" + logSuccessfulOperations +
                ", logParameters=" + logParameters +
                ", alertIntervalSeconds=" + alertIntervalSeconds +
                '}';
    }
}

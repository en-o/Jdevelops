package cn.tannn.jdevelops.p6spy;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 * 配置元信息
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/26 上午10:18
 */
@ConfigurationProperties(prefix = "jdevelops.p6spy")
public class P6spyConfig {

    /**
     * 指定应该被P6Spy代理的JDBC驱动程序列表,内置了4个
     */
    List<String> drivers;

    /**
     * 自定义日志打印类路径设置
     * <p> 默认 cn.tannn.jdevelops.p6spy.DefP6SpyLoggerFormat
     */
    String logMessageFormatter;

    /**
     * 多少时间算慢查询 /s
     */
    Integer outagedetectioninterval;

    /**
     * 是否自动刷新 默认 TRUE
     */
    Boolean autoflush;

    /**
     * 是否开启慢SQL记录  默认 TRUE
     */
    Boolean outagedetection;

    /**
     * https://p6spy.readthedocs.io/en/latest/configandusage.html?highlight=autoflush#modulelist
     * <p> 默认 "com.p6spy.engine.spy.P6SpyFactory,com.p6spy.engine.logging.P6LogFactory,com.p6spy.engine.outage.P6OutageFactory"</p>
     */
    String modulelist;


    public List<String> getDrivers() {
        if (drivers == null || drivers.isEmpty()) {
            return Arrays.asList("com.mysql.cj.jdbc.Driver"
                    , "com.microsoft.sqlserver.jdbc.SQLServerDriver"
                    , "com.kingbase8.Driver"
                    , "org.h2.Driver"
                    , "org.postgresql.Driver");
        }
        return drivers;
    }

    public void setDrivers(List<String> drivers) {
        this.drivers = drivers;
    }

    public String getLogMessageFormatter() {
        return logMessageFormatter == null ? "cn.tannn.jdevelops.p6spy.DefP6SpyLoggerFormat" : logMessageFormatter;
    }

    public void setLogMessageFormatter(String logMessageFormatter) {
        this.logMessageFormatter = logMessageFormatter;
    }

    public Integer getOutagedetectioninterval() {
        return outagedetectioninterval==null?20:outagedetectioninterval;
    }

    public void setOutagedetectioninterval(Integer outagedetectioninterval) {
        this.outagedetectioninterval = outagedetectioninterval;
    }

    public String outagedetectionintervalStr() {
        return getOutagedetectioninterval().toString();
    }

    public Boolean getAutoflush() {
        return autoflush == null || autoflush;
    }

    public void setAutoflush(Boolean autoflush) {
        this.autoflush = autoflush;
    }

    public String getModulelist() {
        return modulelist==null?"com.p6spy.engine.spy.P6SpyFactory,com.p6spy.engine.logging.P6LogFactory,com.p6spy.engine.outage.P6OutageFactory":modulelist;
    }

    public void setModulelist(String modulelist) {
        this.modulelist = modulelist;
    }

    public Boolean getOutagedetection() {
        return outagedetection == null || outagedetection;
    }

    public void setOutagedetection(Boolean outagedetection) {
        this.outagedetection = outagedetection;
    }
}

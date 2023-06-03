package cn.jdevelops.logs.logback.rolling;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;

/**
 * APILOG 日志定制的RollingFileAppender类
 * <p>
 *     <appender name="APILOG" class="cn.jdevelops.logs.logback.rolling.CustomApiLogRollingFileAppenderInput">
 * </p>
 * @author tnnn
 * @date 2023-06-04 01:54:12
 */
public class CustomApiLogRollingFileAppenderInput extends RollingFileAppender<ILoggingEvent> {
    private final static String LOGGER_NAME = "APILOG";
    /**
     * 非 APILOG 的不输出，作用于  apilog 日志文件只保存 接口调用相关的内容
     * @param event ILoggingEvent
     */
    @Override
    protected void append(ILoggingEvent event) {
        if(event.getLoggerName().equals(LOGGER_NAME)){
            super.append(event);
        }
    }
}

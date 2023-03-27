package cn.jdevelops.api.log.entity;


/**
 * 日志输出
 * @author tan
 */
public class LoggerEntity {

    /**
     * 请求IP
     */
    String ip;

    /**
     * 请求URL
     */
    String url;

    /**
     * 请求参数
     */
    String params;

    /**
     * 调用时间（毫秒）
     */
    Long callTime;

    public LoggerEntity(String ip, String url, String params, Long callTime) {
        this.ip = ip;
        this.url = url;
        this.params = params;
        this.callTime = callTime;
    }

    @Override
    public String toString() {
        return "LoggerEntity{" +
                "request IP='" + ip + '\'' +
                ", request url='" + url + '\'' +
                ", request params='" + params + '\'' +
                ", request time (millis cond)=" + callTime +
                '}';
    }
}

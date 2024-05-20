package cn.tannn.jdevelops.apis.log.module;


/**
 * 日志打印
 * @author tan
 */
public class LoggerPrint {

    /**
     * 请求IP
     */
    String ip;

    /**
     * 请求URL
     */
    String url;


    /**
     * The HTTP request methods to map to, narrowing the primary mapping:
     * GET, POST, HEAD, OPTIONS, PUT, PATCH, DELETE, TRACE.
     */
    String method;


    /**
     * 请求参数
     */
    String params;

    /**
     * 调用时间（毫秒）
     */
    Long callTime;

    public LoggerPrint(String ip, String url, String method, String params, Long callTime) {
        this.ip = ip;
        this.url = url;
        this.method = method;
        this.params = params;
        this.callTime = callTime;
    }

    @Override
    public String toString() {
        return "LoggerEntity{" +
                "ip='" + ip + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                ", callTime=" + callTime +
                '}';
    }

    public String ltoString(){
        return  "\n"+
                "request IP='" + ip + '\''  +"\n"+
                "request url='" + url + '\'' +"\n" +
                "request method='" + method + '\'' +"\n" +
                "request params='" + params + '\'' +"\n" +
                "request time (millisecond)=" + callTime +"\n" ;

    }
}

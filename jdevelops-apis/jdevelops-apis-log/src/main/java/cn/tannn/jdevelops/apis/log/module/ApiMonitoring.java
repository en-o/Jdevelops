package cn.tannn.jdevelops.apis.log.module;


/**
 *  api调用监控 （我方接口）
 * @author tn
 * @version 1
 * @date 2020/6/1 17:47
 */

public class ApiMonitoring {

    /**
     * 接口地址
     */
    private String apiUrl;

    /** 接口名 */
    private String chineseApi;

    /**
     *  调用异常和成功类型（1:正常日志；2：错误日志）
     */
    private Integer callType;

    /**
     * 类型
     */
    private  Integer logType;

    /** 接口返回的调用状态 /true/false */
    private boolean status;

    /** 入参 */
    private String inParams;

    /** 出参 */
    private String outParams;

    /**调用时间 毫秒(currentTimeMillis) */
    private Long callTime;

    /**
     * 请求IP
     */
    private String  poxyIp;


    /**
     * 描述
     */
    private String description;

    /**
     *  操作的方式
     */
    private String method;

    /**
     * 解析表达式后获取的参数
     */
    private String expression;

    public ApiMonitoring() {
    }

    public ApiMonitoring(String apiUrl,
                         String chineseApi,
                         Integer callType,
                         Integer logType,
                         boolean status,
                         String inParams,
                         String outParams,
                         Long callTime,
                         String poxyIp,
                         String description,
                         String method,
                         String expression) {
        this.apiUrl = apiUrl;
        this.chineseApi = chineseApi;
        this.callType = callType;
        this.logType = logType;
        this.status = status;
        this.inParams = inParams;
        this.outParams = outParams;
        this.callTime = callTime;
        this.poxyIp = poxyIp;
        this.description = description;
        this.method = method;
        this.expression = expression;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getChineseApi() {
        return chineseApi;
    }

    public void setChineseApi(String chineseApi) {
        this.chineseApi = chineseApi;
    }

    public Integer getCallType() {
        return callType;
    }

    public void setCallType(Integer callType) {
        this.callType = callType;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getInParams() {
        return inParams;
    }

    public void setInParams(String inParams) {
        this.inParams = inParams;
    }

    public String getOutParams() {
        return outParams;
    }

    public void setOutParams(String outParams) {
        this.outParams = outParams;
    }

    public Long getCallTime() {
        return callTime;
    }

    public void setCallTime(Long callTime) {
        this.callTime = callTime;
    }

    public String getPoxyIp() {
        return poxyIp;
    }

    public void setPoxyIp(String poxyIp) {
        this.poxyIp = poxyIp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    @Override
    public String toString() {
        return "ApiMonitoring{" +
                "apiUrl='" + apiUrl + '\'' +
                ", chineseApi='" + chineseApi + '\'' +
                ", callType=" + callType +
                ", logType=" + logType +
                ", status=" + status +
                ", inParams='" + inParams + '\'' +
                ", outParams='" + outParams + '\'' +
                ", callTime=" + callTime +
                ", poxyIp='" + poxyIp + '\'' +
                ", description='" + description + '\'' +
                ", method='" + method + '\'' +
                ", expression='" + expression + '\'' +
                '}';
    }
}

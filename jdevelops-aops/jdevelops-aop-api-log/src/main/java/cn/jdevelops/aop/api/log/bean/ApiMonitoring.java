package cn.jdevelops.aop.api.log.bean;


/**
 *  api调用监控 （我方接口）
 * @author tn
 * @version 1
 * @date 2020/6/1 17:47
 */

public class ApiMonitoring {

    /** 接口地址 */
    private String apiName;

    /** 接口名 */
    private String chineseApi;

    /** 调用状态 /true/false */
    private String status;

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
     * 描述 可以使用表达式取值（#{入参bean.taskName}）
     */
    private String description;

    public ApiMonitoring() {
    }

    public ApiMonitoring(String apiName,
                         String chineseApi,
                         String status,
                         String inParams,
                         String outParams,
                         Long callTime,
                         String poxyIp,
                         String description) {
        this.apiName = apiName;
        this.chineseApi = chineseApi;
        this.status = status;
        this.inParams = inParams;
        this.outParams = outParams;
        this.callTime = callTime;
        this.poxyIp = poxyIp;
        this.description = description;
    }

    @Override
    public String toString() {
        return "ApiMonitoring{" +
                "apiName='" + apiName + '\'' +
                ", chineseApi='" + chineseApi + '\'' +
                ", status='" + status + '\'' +
                ", inParams='" + inParams + '\'' +
                ", outParams='" + outParams + '\'' +
                ", callTime=" + callTime +
                ", poxyIp='" + poxyIp + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getChineseApi() {
        return chineseApi;
    }

    public void setChineseApi(String chineseApi) {
        this.chineseApi = chineseApi;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
}

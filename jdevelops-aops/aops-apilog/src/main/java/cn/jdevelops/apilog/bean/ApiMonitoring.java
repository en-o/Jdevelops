package cn.jdevelops.apilog.bean;


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

    /** 调用方key */
    private String apiKey;

    /** 调用状态 /true/false */
    private String status;

    /** 入参 */
    private String inParams;

    /** 出参 */
    private String outParams;

    /**调用时间*/
    private String callTime;

    /**
     * 请求IP
     */
    private String  poxyIp;

    public ApiMonitoring() {
    }

    public ApiMonitoring(String apiName, String chineseApi, String apiKey, String status, String inParams, String outParams, String callTime, String poxyIp) {
        this.apiName = apiName;
        this.chineseApi = chineseApi;
        this.apiKey = apiKey;
        this.status = status;
        this.inParams = inParams;
        this.outParams = outParams;
        this.callTime = callTime;
        this.poxyIp = poxyIp;
    }

    @Override
    public String toString() {
        return "ApiMonitoring{" +
                "apiName='" + apiName + '\'' +
                ", chineseApi='" + chineseApi + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", status='" + status + '\'' +
                ", inParams='" + inParams + '\'' +
                ", outParams='" + outParams + '\'' +
                ", callTime='" + callTime + '\'' +
                ", poxyIp='" + poxyIp + '\'' +
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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
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

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getPoxyIp() {
        return poxyIp;
    }

    public void setPoxyIp(String poxyIp) {
        this.poxyIp = poxyIp;
    }
}

package cn.jdevelops.apilog.bean;

import lombok.*;

/**
 *  api调用监控 （我方接口）
 * @author tn
 * @version 1
 * @date 2020/6/1 17:47
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

}

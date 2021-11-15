package com.detabes.apilog.bean;

import lombok.*;

/**
 * @author tn
 * @version 1
 * @ClassName ApiMonitoringBean
 * @description api调用监控 （我方接口）
 * @date 2020/6/1 17:47
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiMonitoring {

    /** 接口名 */
    private String apiName;

    /** 调用方key */
    private String apiKey;

    /** 调用状态 /true/false */
    private String status;

    /** 入参 */
    private String inParams;

    /** 出参 */
    private String outParams;

    /**接口名*/
    private String callTime;

    /**
     * 请求IP
     */
    private String  poxyIp;

}

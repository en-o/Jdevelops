package com.detabes.apilog.server;


import com.detabes.apilog.bean.ApiMonitoring;

/**
 * @author tn
 * @version 1
 * @ClassName ApiLog
 * @description 保存接口日志，需要时重写里面的保存方法即可实现数据自定义存入
 * @date 2020/6/15 16:09
 */
public interface ApiLogSave {
    /**
     * 接口日志输出
     * @param apilog
     * @return
     */
    ApiMonitoring saveLog(ApiMonitoring apilog);

}

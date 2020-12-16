package com.detabes.apilog.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.detabes.apilog.bean.ApiMonitoring;
import com.detabes.apilog.server.ApiLogSave;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tn
 * @version 1
 * @ClassName ApiLogSaveImpl
 * @description 保存日志
 * @date 2020/6/16 20:50
 */
@Slf4j
public class ApiLogSaveImpl implements ApiLogSave {

    @Override
    public ApiMonitoring saveLog(ApiMonitoring apilog) {
        log.info("api接口调用信息默认输出控制台:"+ JSONObject.toJSONString(apilog));
        return apilog;
    }
}

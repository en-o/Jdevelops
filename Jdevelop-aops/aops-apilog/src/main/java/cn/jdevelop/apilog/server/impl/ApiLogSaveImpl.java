package cn.jdevelop.apilog.server.impl;

import cn.jdevelop.apilog.bean.ApiMonitoring;
import cn.jdevelop.apilog.server.ApiLogSave;
import lombok.extern.slf4j.Slf4j;

/**
 * 保存日志
 * @author tn
 * @version 1
 * @date 2020/6/16 20:50
 */
@Slf4j
public class ApiLogSaveImpl implements ApiLogSave {

    @Override
    public ApiMonitoring saveLog(ApiMonitoring apilog) {
        log.info("api接口调用信息默认输出控制台:"+ apilog.toString());
        return apilog;
    }
}

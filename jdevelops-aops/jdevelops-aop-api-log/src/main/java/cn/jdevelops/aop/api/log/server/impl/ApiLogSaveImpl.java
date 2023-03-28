package cn.jdevelops.aop.api.log.server.impl;

import cn.jdevelops.aop.api.log.server.ApiLogSave;
import cn.jdevelops.aop.api.log.bean.ApiMonitoring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 保存日志
 * @author tn
 * @version 1
 * @date 2020/6/16 20:50
 */
public class ApiLogSaveImpl implements ApiLogSave {
    private static final Logger LOG = LoggerFactory.getLogger(ApiLogSaveImpl.class);
    @Override
    public void saveLog(ApiMonitoring apilog) {
        LOG.info("api接口调用信息默认输出控制台:{}",apilog.toString());
    }
}

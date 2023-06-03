package cn.jdevelops.aop.api.log.server;

import cn.jdevelops.aop.api.log.bean.ApiMonitoring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.scheduling.annotation.Async;

/**
 * 保存日志
 * @author tn
 * @version 1
 * @date 2020/6/16 20:50
 */
@ConditionalOnMissingBean(ApiLogSave.class)
@AutoConfiguration
public class DefApiLogSave implements ApiLogSave {

    private final static String LOGGER_NAME = "APILOG";

    private static final Logger LOG = LoggerFactory.getLogger(LOGGER_NAME);

    @Override
    @Async("apiLogAsyncTaskExecutor")
    public void saveLog(ApiMonitoring apilog) {
        LOG.info("api接口调用信息默认输出控制台:{}",apilog.toString());
    }
}

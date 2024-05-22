package cn.tannn.jdevelops.apis.log;

import cn.tannn.jdevelops.apis.log.module.ApiMonitoring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.scheduling.annotation.Async;

import static cn.tannn.jdevelops.apis.log.constants.ApiLogConstants.LOGGER_NAME;

/**
 * 保存日志
 * @author tn
 * @version 1
 * @date 2020/6/16 20:50
 */
@ConditionalOnMissingBean(ApiLogSave.class)
@AutoConfiguration
public class DefApiLogSave implements ApiLogSave {

    private static final Logger LOG = LoggerFactory.getLogger(LOGGER_NAME);

    @Override
    @Async("apiLogAsyncTaskExecutor")
    public void saveLog(ApiMonitoring apilog) {
        LOG.info("api接口调用信息默认输出控制台:{}",apilog.toString());
    }
}

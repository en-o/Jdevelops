package cn.tannn.jdevelops.logs.service;

import cn.tannn.jdevelops.logs.model.LoginLogRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;


/**
 * 保存，需要时重写里面的保存方法即可实现数据自定义存入
 * 保存
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/5/20 下午3:01
 */
@ConditionalOnMissingBean(LoginLogSave.class)
@AutoConfiguration
public class DefLoginLogSave implements LoginLogSave {

    private static final Logger LOG = LoggerFactory.getLogger(DefLoginLogSave.class);

    @Override
    public void saveLog(LoginLogRecord audit) {
        LOG.info("{}审计日志默认输出:{}", System.currentTimeMillis(), audit.toString());
    }
}

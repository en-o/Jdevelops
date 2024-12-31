package cn.tannn.jdevelops.log.audit.service;

import cn.hutool.core.date.DateTime;
import cn.tannn.jdevelops.log.audit.AuditContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.scheduling.annotation.Async;

/**
 * 保存，需要时重写里面的保存方法即可实现数据自定义存入
 * 保存
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/5/20 下午3:01
 */
@ConditionalOnMissingBean(AuditSave.class)
@AutoConfiguration
public class DefAuditSave implements AuditSave {

    private static final Logger LOG = LoggerFactory.getLogger(DefAuditSave.class);

    @Override
    @Async("auditLogAsyncTaskExecutor")
    public void saveLog(AuditContext audit) {
        LOG.info("{}审计日志默认输出:{}", DateTime.now(), audit.toString());
    }
}

package cn.tannn.jdevelops.log.audit.config;

import cn.tannn.jdevelops.log.audit.AuditLogAspect;
import cn.tannn.jdevelops.log.audit.service.AuditSave;
import cn.tannn.jdevelops.log.audit.service.DefAuditSave;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/12/25 11:45
 */
@Import(AuditLogAsyncConfiguration.class)
public class ImportConfig {

    /**
     *  DefAuditSave
     */
    @Bean
    @ConditionalOnMissingBean(AuditSave.class)
    public AuditSave auditSave(){
        return new DefAuditSave();
    }

    @Bean
    @ConditionalOnBean(AuditSave.class)
    public AuditLogAspect auditLogAspect(AuditSave auditSave){
        return new AuditLogAspect(auditSave);
    }

}

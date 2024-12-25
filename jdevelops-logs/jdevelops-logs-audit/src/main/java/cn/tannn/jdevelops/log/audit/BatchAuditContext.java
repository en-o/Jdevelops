package cn.tannn.jdevelops.log.audit;

import cn.tannn.jdevelops.log.audit.annotations.AuditLog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Slf4j
@NoArgsConstructor
@Accessors(chain = true)
public class BatchAuditContext {
    private final List<AuditContext> contexts = new ArrayList<>();
    private String auditType;
    private String operationType;
    private String description;


    public BatchAuditContext(String auditType, String operationType, String description) {
        this.auditType = auditType;
        this.operationType = operationType;
        this.description = description;
    }

    public BatchAuditContext(AuditLog auditLog) {
        this.auditType = auditLog.auditType();
        this.operationType = auditLog.operationType();
        this.description = auditLog.description();
    }

    public void addContext(AuditContext context) {
        // 确保每个Context都有基础信息
        context.setAuditType(this.auditType);
        context.setOperationalType(this.operationType);
        context.setDescription(this.description);
        contexts.add(context);
    }
}

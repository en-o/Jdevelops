package cn.tannn.jdevelops.log.audit;

import cn.tannn.jdevelops.log.audit.annotations.AuditLog;
import cn.tannn.jdevelops.log.audit.constant.OperationalAuditType;
import cn.tannn.jdevelops.log.audit.constant.OperationalType;


import java.util.ArrayList;
import java.util.List;


public class BatchAuditContext {
    private final List<AuditContext> contexts = new ArrayList<>();
    private String auditType;
    private String operationType;
    private String description;

    public BatchAuditContext() {
    }

    /**
     * @param auditType     审计类型 {@link OperationalAuditType}
     * @param operationType 操作类型 {@link OperationalType}
     * @param description   备注
     */
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

    public List<AuditContext> getContexts() {
        return contexts;
    }


    public String getAuditType() {
        return auditType;
    }

    /**
     * @param auditType 审计类型 {@link OperationalAuditType}
     */
    public BatchAuditContext setAuditType(String auditType) {
        this.auditType = auditType;
        return this;
    }

    public String getOperationType() {
        return operationType;
    }

    /**
     * @param operationType 操作类型 {@link OperationalType}
     */
    public BatchAuditContext setOperationType(String operationType) {
        this.operationType = operationType;
        return this;
    }

    public String getDescription() {
        return description;
    }

    /**
     * @param description 备注
     */
    public BatchAuditContext setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return "BatchAuditContext{" +
                "contexts=" + contexts +
                ", auditType='" + auditType + '\'' +
                ", operationType='" + operationType + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

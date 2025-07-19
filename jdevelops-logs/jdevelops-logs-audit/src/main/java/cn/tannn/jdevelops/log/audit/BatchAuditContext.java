package cn.tannn.jdevelops.log.audit;

import cn.tannn.jdevelops.log.audit.annotations.AuditLog;
import cn.tannn.jdevelops.log.audit.constant.OperationalAuditType;
import cn.tannn.jdevelops.log.audit.constant.OperationalType;

import java.util.ArrayList;
import java.util.List;

/**
 * 多条数据保存
 */
public class BatchAuditContext {
    private final List<AuditContext> contexts = new ArrayList<>();
    private String auditType;
    private String operationType;
    private String description;
    private Integer customType;

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
        this.customType = auditLog.customType();
    }

    public void addContext(AuditContext context) {
        // 确保每个Context都有基础信息
        if(context.getAuditType() == null) {
            context.setAuditType(this.auditType);
        }
        if(context.getOperationalType() == null) {
            context.setOperationalType(this.operationType);
        }
        if(context.getDescription() == null) {
            context.setDescription(this.description);
        }
        if(context.getCustomType() == null) {
            context.setCustomType(this.customType);
        }
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

    public Integer getCustomType() {
        return customType;
    }

    public BatchAuditContext setCustomType(Integer customType) {
        this.customType = customType;
        return this;
    }

    @Override
    public String toString() {
        return "BatchAuditContext{" +
                "contexts=" + contexts +
                ", auditType='" + auditType + '\'' +
                ", operationType='" + operationType + '\'' +
                ", description='" + description + '\'' +
                ", customType=" + customType +
                '}';
    }
}

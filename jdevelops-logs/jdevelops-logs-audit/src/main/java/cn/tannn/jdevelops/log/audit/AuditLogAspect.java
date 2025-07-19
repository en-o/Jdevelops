package cn.tannn.jdevelops.log.audit;

import cn.tannn.jdevelops.log.audit.annotations.AuditLog;
import cn.tannn.jdevelops.log.audit.service.AuditSave;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 审计日志切面
 * 处理带有 @AuditLog 注解的方法的审计日志记录
 */
@Aspect
public class AuditLogAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditLogAspect.class);

    private final AuditSave auditSave;

    public AuditLogAspect(AuditSave auditSave) {
        this.auditSave = auditSave;
    }

    /**
     * 环绕通知处理审计日志
     */
    @Around("@annotation(auditLog)")
    public Object around(ProceedingJoinPoint point, AuditLog auditLog) throws Throwable {
        boolean isBatch = auditLog.batch();
        try {
            // 初始化审计上下文
            initializeAuditContext(auditLog, isBatch);
            // 执行目标方法
            Object result = point.proceed();
            // 保存审计日志
            saveAuditLogs(isBatch);
            return result;
        } catch (Throwable e) {
            String eMessage = e.getMessage();
            log.error("审计日志处理失败: {}", eMessage, e);

            // 标记操作失败
            markOperationAsFailed(isBatch,eMessage);

            // 尝试保存失败的审计日志
            tryToSaveFailedAuditLogs(isBatch);

            throw e;
        } finally {
            // 清理上下文
            clearAuditContext(isBatch);
        }
    }

    /**
     * 初始化审计上下文
     */
    private void initializeAuditContext(AuditLog auditLog, boolean isBatch) {
        try {
            if (isBatch) {
                BatchAuditContext batchContext = new BatchAuditContext(auditLog);
                AuditContextHolder.setBatchContext(batchContext);
                log.debug("批量审计上下文初始化完成");
            } else {
                AuditContext context = createSingleAuditContext(auditLog);
                AuditContextHolder.setContext(context);
                log.debug("单个审计上下文初始化完成");
            }
        } catch (Exception e) {
            log.error("初始化审计上下文失败", e);
            throw new RuntimeException("Failed to initialize audit context", e);
        }
    }

    /**
     * 创建单个审计上下文
     */
    private AuditContext createSingleAuditContext(AuditLog auditLog) {
        return new AuditContext()
                .setAuditType(auditLog.auditType())
                .setOperationalType(auditLog.operationType())
                .setCustomType(auditLog.customType())
                .setDescription(auditLog.description());
    }

    /**
     * 保存审计日志
     */
    private void saveAuditLogs(boolean isBatch) {
        try {
            if (isBatch) {
                saveBatchAuditLogs();
            } else {
                saveSingleAuditLog();
            }
        } catch (Exception e) {
            log.error("保存审计日志失败", e);
            // 不重新抛出异常，避免影响业务流程
        }
    }

    /**
     * 保存批量审计日志
     */
    private void saveBatchAuditLogs() {
        BatchAuditContext batchContext = AuditContextHolder.getBatchContext();
        if (!batchContext.getContexts().isEmpty()) {
            batchContext.getContexts().forEach(context -> {
                try {
                    auditSave.saveLog(context);
                } catch (Exception e) {
                    log.error("保存单个批量审计日志失败: {}", context, e);
                }
            });
            log.debug("批量审计日志保存完成，共 {} 条", batchContext.getContexts().size());
        }
    }

    /**
     * 保存单个审计日志
     */
    private void saveSingleAuditLog() {
        AuditContext context = AuditContextHolder.getContext();
        if (context != null) {
            auditSave.saveLog(context);
            log.debug("单个审计日志保存完成");
        } else {
            log.warn("审计上下文为空，无法保存审计日志");
        }
    }

    /**
     * 标记操作为失败状态
     */
    private void markOperationAsFailed(boolean isBatch, String eMessage) {
        try {
            if (isBatch) {
                BatchAuditContext batchContext = AuditContextHolder.getBatchContext();
                batchContext.getContexts().forEach(ctx -> {
                    ctx.setStatus(false);
                    ctx.setFailMessage(eMessage);
                });
            } else {
                AuditContext context = AuditContextHolder.getContext();
                if (context != null) {
                    context.setStatus(false);
                    context.setFailMessage(eMessage);
                }
            }
        } catch (Exception e) {
            log.error("标记审计状态失败", e);
        }
    }

    /**
     * 尝试保存失败的审计日志
     */
    private void tryToSaveFailedAuditLogs(boolean isBatch) {
        try {
            saveAuditLogs(isBatch);
        } catch (Exception e) {
            log.error("保存失败审计日志时出错", e);
        }
    }

    /**
     * 清理审计上下文
     */
    private void clearAuditContext(boolean isBatch) {
        try {
            if (isBatch) {
                AuditContextHolder.clearBatch();
            } else {
                AuditContextHolder.clear();
            }
            log.debug("审计上下文清理完成");
        } catch (Exception e) {
            log.error("清理审计上下文失败", e);
        }
    }
}

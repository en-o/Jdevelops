package cn.tannn.jdevelops.log.audit;

import cn.tannn.jdevelops.log.audit.annotations.AuditLog;
import cn.tannn.jdevelops.log.audit.service.AuditSave;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class AuditLogAspect {

    private final  AuditSave apiLogSave;

    private static final Logger log = LoggerFactory.getLogger(AuditLogAspect.class);

    public AuditLogAspect(AuditSave apiLogSave) {
        this.apiLogSave = apiLogSave;
    }

    @Around("@annotation(auditLog)")
    public Object around(ProceedingJoinPoint point, AuditLog auditLog) throws Throwable {
        boolean isBatch = auditLog.batch();
        AuditContext context = null;
        BatchAuditContext batchContext = null;
        try {
            // 初始化审计上下文
            if (isBatch) {
                 batchContext = new BatchAuditContext(auditLog);
                AuditContextHolder.setBatchContext(batchContext);
            } else {
                // 单个操作初始化
                context = new AuditContext()
                        .setAuditType(auditLog.auditType())
                        .setOperationalType(auditLog.operationType())
                        .setCustomType(auditLog.customType())
                        .setDescription(auditLog.description());
                AuditContextHolder.setContext(context);
            }
            return point.proceed();
        } catch (Throwable e) {
            log.error("初始化审计上下文失败");
            // 设置失败状态
            if (isBatch && batchContext != null) {
                batchContext.getContexts().forEach(ctx -> ctx.setStatus(false));
            } else if (context != null) {
                context.setStatus(false);
            }
            throw e;
        }finally {
            if (!isBatch) {
                AuditContextHolder.clear();
            }else {
                AuditContextHolder.clearBatch();
            }
        }
    }

    @AfterReturning(pointcut = "@annotation(auditLog)", returning = "result")
    public void afterReturning(AuditLog auditLog, Object result) {
        try {
            if (auditLog.batch()) {
                // 处理批量审计日志
                BatchAuditContext batchContext = AuditContextHolder.getBatchContext();
                batchContext.getContexts().forEach(apiLogSave::saveLog);
            }else {
                // 处理单个审计日志
                AuditContext context = AuditContextHolder.getContext();
                if (context != null) {
                    apiLogSave.saveLog(context);
                }
            }
        } catch (Exception e) {
            log.error("审计日志记录失败", e);
        } finally {
            if (!auditLog.batch()) {
                AuditContextHolder.clear();
            }else {
                AuditContextHolder.clearBatch();
            }
        }
    }
}

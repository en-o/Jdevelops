package cn.tannn.jdevelops.log.audit;

import cn.tannn.jdevelops.log.audit.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 基于 RequestContextHolder 的实现
 */
public class AuditContextHolder {


    private static final Logger log = LoggerFactory.getLogger(AuditContextHolder.class);

    private static final String AUDIT_CONTEXT_ATTRIBUTE = "AUDIT_CONTEXT";
    private static final String BATCH_AUDIT_CONTEXT_ATTRIBUTE = "BATCH_AUDIT_CONTEXT";

    /**
     * 设置审计上下文
     */
    public static void setContext(AuditContext context) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.error("No thread-bound request found: " +
                    "Are you referring to request attributes outside of an actual web request, " +
                    "or processing a request outside of the originally receiving thread?");
        }else {
            attributes.setAttribute(AUDIT_CONTEXT_ATTRIBUTE, context, RequestAttributes.SCOPE_REQUEST);
        }

    }

    /**
     * 获取审计上下文
     */
    public static AuditContext getContext() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No thread-bound request found");
        }

        AuditContext context = (AuditContext) attributes.getAttribute(
                AUDIT_CONTEXT_ATTRIBUTE,
                RequestAttributes.SCOPE_REQUEST
        );

        // IP设置
        try {
            if (context != null && attributes instanceof ServletRequestAttributes servletAttributes) {
                context.setAccessIp(IpUtil.getPoxyIpEnhance(servletAttributes.getRequest()));
            }
        }catch (Exception e){
            log.error("Failed to get context from request attributes", e);
        }

        if (context == null) {
            log.error("No AuditContext found in current request");
        }

        return context;
    }

    /**
     * 清理审计上下文
     */
    public static void clear() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            attributes.removeAttribute(AUDIT_CONTEXT_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        }
    }


    /**
     * 设置批量审计上下文
     */
    public static void setBatchContext(BatchAuditContext context) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.error("No thread-bound request found: " +
                    "Are you referring to request attributes outside of an actual web request, " +
                    "or processing a request outside of the originally receiving thread?");
        }else {
            attributes.setAttribute(BATCH_AUDIT_CONTEXT_ATTRIBUTE, context, RequestAttributes.SCOPE_REQUEST);
        }

    }

    /**
     * 获取批量审计上下文
     */
    public static BatchAuditContext getBatchContext() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No thread-bound request found");
        }

        BatchAuditContext batchContext = (BatchAuditContext) attributes.getAttribute(
                BATCH_AUDIT_CONTEXT_ATTRIBUTE,
                RequestAttributes.SCOPE_REQUEST
        );

        if (batchContext == null) {
            batchContext = new BatchAuditContext();
            attributes.setAttribute(
                    BATCH_AUDIT_CONTEXT_ATTRIBUTE,
                    batchContext,
                    RequestAttributes.SCOPE_REQUEST
            );
        }
        return batchContext;
    }

    /**
     * 清理批量审计上下文
     */
    public static void clearBatch() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            attributes.removeAttribute(BATCH_AUDIT_CONTEXT_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        }
    }


}

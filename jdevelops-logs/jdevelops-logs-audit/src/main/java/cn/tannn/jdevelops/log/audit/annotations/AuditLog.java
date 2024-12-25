package cn.tannn.jdevelops.log.audit.annotations;

import com.sunway.rdpi.logs.constant.AuditLogDescription;
import com.sunway.rdpi.logs.constant.OperationalAuditType;
import com.sunway.rdpi.logs.constant.OperationalType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {

    /**
     * Dictionary#102 {@link OperationalAuditType}
     * @return 审计类型
     */
    String auditType();

    /**
     * Dictionary#103 {@link OperationalType}
     * @return 操作类型 - 兜底参数，如果在方法内设置了，以方法内的为准
     */
    String operationType();

    /**
     * 是否批量
     */
    boolean batch() default false;


    /**
     * 参考 {@link AuditLogDescription}
     * @return 描述
     */
    String description() default "";
}

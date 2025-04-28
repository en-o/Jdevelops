package cn.tannn.jdevelops.logs.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class LoginContextHolder {
    private static final Logger log = LoggerFactory.getLogger(LoginContextHolder.class);

    private static final ThreadLocal<LoginContext> contextHolder = new ThreadLocal<>();

    public static void setContext(LoginContext context) {
        Assert.notNull(context, "LoginContext must not be null");
        contextHolder.set(context);
    }

    public static LoginContext getContext() {
        LoginContext context = contextHolder.get();
        if (context == null) {
            log.error("No AuditContext found in current request");
        }
        return context;
    }

    public static void clear() {
        contextHolder.remove();
    }

    public static boolean hasContext() {
        return contextHolder.get() != null;
    }
}

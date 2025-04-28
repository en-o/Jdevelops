package cn.tannn.jdevelops.logs.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


public class LoginContextHolder {
    private static final Logger log = LoggerFactory.getLogger(LoginContextHolder.class);

    private static final String LOGIN_CONTEXT_ATTRIBUTE = "LOGIN_CONTEXT";



    public static void setContext(LoginContext context) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.error("No thread-bound request found: " +
                    "Are you referring to request attributes outside of an actual web request, " +
                    "or processing a request outside of the originally receiving thread?");
        }else {
            attributes.setAttribute(LOGIN_CONTEXT_ATTRIBUTE, context, RequestAttributes.SCOPE_REQUEST);
        }
    }

    public static LoginContext getContext() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No thread-bound request found");
        }
        LoginContext context = (LoginContext) attributes.getAttribute(
                LOGIN_CONTEXT_ATTRIBUTE,
                RequestAttributes.SCOPE_REQUEST
        );
        if (context == null) {
            log.error("No AuditContext found in current request");
        }
        return context;
    }

    public static void clear() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            attributes.removeAttribute(LOGIN_CONTEXT_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        }
    }

    public static void initContext() {
        String s = RequestIdGenerator.generateRequestId("");
        LoginContextHolder.setContext(new LoginContext(s));
    }
}

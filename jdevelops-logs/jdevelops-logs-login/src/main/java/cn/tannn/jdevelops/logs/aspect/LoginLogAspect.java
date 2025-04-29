package cn.tannn.jdevelops.logs.aspect;

import cn.tannn.jdevelops.logs.LoginLog;
import cn.tannn.jdevelops.logs.context.LoginContext;
import cn.tannn.jdevelops.logs.context.LoginContextHolder;
import cn.tannn.jdevelops.logs.model.InputParams;
import cn.tannn.jdevelops.logs.model.LoginLogRecord;
import cn.tannn.jdevelops.logs.service.LoginLogSave;
import cn.tannn.jdevelops.uitls.aop.reflect.AopReasolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 登录日志记录
 *
 * @author tn
 * @date 2025-04-09 16:09:39
 */
@Aspect
public class LoginLogAspect {

    private static final Logger log = LoggerFactory.getLogger(LoginLogAspect.class);
    private final LoginLogSave loginLogSave;
    private static final String DEFAULT_ERROR_MESSAGE = "System error occurred";
    private static final String NORMAL_STATUS = "normal";

    public LoginLogAspect(LoginLogSave loginLogSave) {
        this.loginLogSave = loginLogSave;
    }

    @Pointcut("@annotation(cn.tannn.jdevelops.logs.LoginLog)")
    public void loginLogPointcut() {
        // Pointcut definition
    }

    @Around("loginLogPointcut()")
    public Object aroundLoginLog(ProceedingJoinPoint point) throws Throwable {
        try {
            LoginContextHolder.initContext();
            return point.proceed();
        } catch (Throwable e) {
            log.error("Login log context initialization failed", e);
            throw e;
        } finally {
            LoginContextHolder.clear();
        }
    }

    @AfterThrowing(value = "loginLogPointcut()", throwing = "ex")
    public void handleLoginException(JoinPoint jp, Exception ex) {
        try {
            LoginLogRecord logRecord = createBaseLogRecord(jp);
            logRecord.setStatus(0);
            logRecord.setDescription(Optional.ofNullable(ex.getMessage())
                    .filter(StringUtils::hasText)
                    .orElse(DEFAULT_ERROR_MESSAGE));
            logRecord.setLoginContext(LoginContextHolder.getContext());
            // Asynchronously save the log
            saveLogAsync(logRecord);
        } catch (Exception e) {
            log.error("Failed to process login exception log", e);
        } finally {
            LoginContextHolder.clear();
        }
    }

    @AfterReturning(value = "loginLogPointcut()", returning = "result")
    public void handleSuccessfulLogin(JoinPoint joinPoint, Object result) {
        try {
            LoginLogRecord logRecord = createBaseLogRecord(joinPoint);
            logRecord.setStatus(1);
            logRecord.setDescription(NORMAL_STATUS);

            // Handle expression evaluation
            handleExpression(joinPoint, logRecord);

            // Asynchronously save the log
            saveLogAsync(logRecord);
        } catch (Exception e) {
            log.error("Failed to process successful login log", e);
        }
    }

    private LoginLogRecord createBaseLogRecord(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LoginLog loginLog = method.getAnnotation(LoginLog.class);

        LoginLogRecord logRecord = new LoginLogRecord(loginLog);
        setRequestInfo(logRecord);
        LoginContext loginContext = LoginContextHolder.getContext();
        logRecord.setLoginContext(loginContext);
        InputParams loginInfo = extractLoginInfo(joinPoint, loginLog.loginNameKey());
        loginContext.setLoginName(loginInfo.getLoginName());
        loginContext.setPlatform(loginInfo.getPlatform());

        return logRecord;
    }

    private void setRequestInfo(LoginLogRecord logRecord) {
        Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(attrs -> (ServletRequestAttributes) attrs)
                .map(ServletRequestAttributes::getRequest)
                .ifPresent(logRecord::setRequest);
    }

    private void handleExpression(JoinPoint joinPoint, LoginLogRecord logRecord) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            LoginLog loginLog = signature.getMethod().getAnnotation(LoginLog.class);

            Optional.ofNullable(AopReasolver.newInstance().resolver(joinPoint, loginLog.expression()))
                    .map(Object::toString)
                    .ifPresent(logRecord::setExpression);
        } catch (Exception e) {
            log.warn("Expression evaluation failed", e);
            logRecord.setExpression("");
        }
    }

    private void saveLogAsync(LoginLogRecord logRecord) {
        CompletableFuture.runAsync(() -> {
            try {
                loginLogSave.saveLog(logRecord);
            } catch (Exception e) {
                log.error("Async log saving failed", e);
            }
        });
    }

    @NonNull
    private static InputParams extractLoginInfo(JoinPoint joinPoint, String key) {
        try {
            Map<String, Object> params = new HashMap<>();
            Arrays.stream(joinPoint.getArgs())
                    .filter(arg -> !(arg instanceof HttpServletRequest || arg instanceof HttpServletResponse))
                    .filter(Objects::nonNull)
                    .forEach(arg -> extractParameters(arg, params));

            return new InputParams(params.get(key), params.get("platform"));
        } catch (Exception e) {
            log.error("Failed to extract login info", e);
            return new InputParams();
        }
    }

    private static void extractParameters(Object obj, Map<String, Object> params) {
        if (obj instanceof Map<?, ?> map) {
            map.forEach((key, value) -> params.put(key.toString(), value));
            return;
        }

        Arrays.stream(obj.getClass().getDeclaredFields())
                .forEach(field -> extractFieldValue(field, obj, params));
    }

    private static void extractFieldValue(Field field, Object obj, Map<String, Object> params) {
        try {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value != null) {
                params.put(field.getName(), value);
            }
        } catch (IllegalAccessException e) {
            log.warn("Cannot access field: {}", field.getName(), e);
        }
    }
}

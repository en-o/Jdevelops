package cn.tannn.jdevelops.logs.aspect;


import cn.tannn.jdevelops.logs.LoginLog;
import cn.tannn.jdevelops.logs.model.LoginLogRecord;
import cn.tannn.jdevelops.logs.service.LoginLogSave;
import cn.tannn.jdevelops.uitls.aop.reflect.AopReasolver;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 登录日志记录
 *
 * @author tn
 * @date 2025-04-09 16:09:39
 */

@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@Aspect
public class LoginLogAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LoginLogAspect.class);

    private final LoginLogSave loginLogSave;

    /**
     * 表达式 异常时用
     */
    String expressionError = "";

    public LoginLogAspect(LoginLogSave loginLogSave) {
        this.loginLogSave = loginLogSave;
    }


    /**
     * 定义切点 @Pointcut
     * 在注解的位置切入代码
     */
    @Pointcut("@annotation(cn.tannn.jdevelops.logs.LoginLog)")
    public void loginLog() {
    }


    /**
     * 异常通知
     */
    @AfterThrowing(value = "loginLog()", throwing = "ex")
    public void doAfterThrowing(JoinPoint jp, Exception ex) {

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) jp.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        /*key*/
        LoginLog myLog = method.getAnnotation(LoginLog.class);

        //保存日志
        LoginLogRecord apiLog = new LoginLogRecord(myLog);

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            apiLog.setRequest( requestAttributes.getRequest());
        }
        apiLog.setStatus(0);
        apiLog.setTokenPlatform("");
        apiLog.setLoginName(getLoginName(jp, myLog.loginNameKey()));
        apiLog.setDescription(ex.getMessage());
        // save
        loginLogSave.saveLog(apiLog);
    }

    /**
     * 返回通知
     */
    @AfterReturning(value = "loginLog()", returning = "rvt")
    public void loginLogSave(JoinPoint joinPoint, Object rvt) {
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        LoginLog myLog = method.getAnnotation(LoginLog.class);
        //保存日志
        LoginLogRecord apiLog = new LoginLogRecord(myLog);

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            apiLog.setRequest( requestAttributes.getRequest());
        }
        apiLog.setStatus(1);
        apiLog.setTokenPlatform("");
        apiLog.setDescription("正常");
        apiLog.setLoginName(getLoginName(joinPoint, myLog.loginNameKey()));
        Object expression = AopReasolver.newInstance().resolver(joinPoint, myLog.expression());
        if (null != expression) {
            expressionError = expression.toString();
            apiLog.setExpression(expressionError);
        } else {
            apiLog.setExpression("");
        }
        loginLogSave.saveLog(apiLog);
    }


    public static String getLoginName(JoinPoint joinPoint, String key) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof String vals) {
                // 处理String类型参数
                return vals;
            } else if (arg != null && !arg.getClass().isPrimitive()) {
                // 处理Bean对象
                try {
                    Field field = arg.getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    Object value = field.get(arg);
                    return value.toString();
                } catch (Exception e) {
                    // 处理异常
                    LOG.error("登录名获取失败");
                }
            }
        }
        return null;
    }

}

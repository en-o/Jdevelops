package cn.tannn.jdevelops.logs.aspect;


import cn.tannn.jdevelops.logs.LoginLog;
import cn.tannn.jdevelops.logs.model.InputParams;
import cn.tannn.jdevelops.logs.model.LoginLogRecord;
import cn.tannn.jdevelops.logs.service.LoginLogSave;
import cn.tannn.jdevelops.uitls.aop.reflect.AopReasolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.Arrays;
import java.util.List;

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
        apiLog.setLoginName(getLoginName(jp, myLog.loginNameKey()).getLoginName());
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
        // todo 续期的注释没有做，
        apiLog.setDescription("正常");
        apiLog.setLoginName(getLoginName(joinPoint, myLog.loginNameKey()).getLoginName());
        Object expression = AopReasolver.newInstance().resolver(joinPoint, myLog.expression());
        if (null != expression) {
            expressionError = expression.toString();
            apiLog.setExpression(expressionError);
        } else {
            apiLog.setExpression("");
        }
        loginLogSave.saveLog(apiLog);
    }


    public static InputParams getLoginName(JoinPoint joinPoint, String key) {
        Object[] args = joinPoint.getArgs();
        // 过滤参数
        List<Object> argObjects = Arrays.stream(args).filter(s -> !(s instanceof HttpServletRequest)
                && !(s instanceof HttpServletResponse)).toList();
        InputParams inputParams = new InputParams();

        argObjects.forEach(arg -> {
            if (arg instanceof String vals) {
                // 处理String类型参数
                inputParams.setLoginName(vals);
            } else if (arg != null && !arg.getClass().isPrimitive()) {
                // 处理Bean对象
                try {
                    Field field = arg.getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    inputParams.setLoginName(field.get(arg).toString());
                    Field field2 = arg.getClass().getDeclaredField("platform");
                    field2.setAccessible(true);
                    inputParams.setPlatform(field2.get(arg).toString());
                } catch (Exception e) {
                    // 处理异常
                    LOG.error("登录名获取失败");
                }
            }
        });
        return inputParams;
    }


}

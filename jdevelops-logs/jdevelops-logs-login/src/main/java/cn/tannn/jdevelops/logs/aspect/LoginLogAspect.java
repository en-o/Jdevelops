package cn.tannn.jdevelops.logs.aspect;


import cn.tannn.jdevelops.logs.LoginLog;
import cn.tannn.jdevelops.logs.context.LoginContextHolder;
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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        try {
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
            InputParams loginName = getLoginName(jp, myLog.loginNameKey());
            apiLog.setLoginName(loginName.getLoginName());
            apiLog.setDescription(ex.getMessage());
            apiLog.setPlatform(loginName.getPlatform());
            // save
            loginLogSave.saveLog(apiLog);
        }catch (Exception e){
            LOG.error("LoginLogAspect error: {}", e.getMessage());
        }finally {
            LoginContextHolder.clear();
        }
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
        apiLog.setDescription("正常");
        InputParams loginName = getLoginName(joinPoint, myLog.loginNameKey());
        apiLog.setLoginName(loginName.getLoginName());
        apiLog.setPlatform(loginName.getPlatform());
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
        // 根据不同的类型提取参数
        Map<String, Object> extractedParams = new HashMap<>();
        // 2. Handle method arguments (including bean parameters)
        Object[] args = joinPoint.getArgs();
        // 过滤参数
        List<Object> argObjects = Arrays.stream(args).filter(s -> !(s instanceof HttpServletRequest)
                && !(s instanceof HttpServletResponse)).toList();
        for (Object arg : argObjects) {
            if (arg != null) {
                extractParamsFromObject(arg, extractedParams);
            }
        }
        return new InputParams(extractedParams.get(key),extractedParams.get("platform"));
    }


    private  static void extractParamsFromObject(Object obj, Map<String, Object> params) {
        try {
            // Handle Map parameters
            if (obj instanceof Map maps) {
                params.putAll(maps);
                return;
            }
            // Handle Bean parameters
            Arrays.stream(obj.getClass().getDeclaredFields())
                    .forEach(field -> {
                        try {
                            field.setAccessible(true);
                            Object value = field.get(obj);
                            if (value != null) {
                                params.put(field.getName(), value);
                            }
                        } catch (IllegalAccessException e) {
                            LOG.error("Error accessing field: {}", field.getName(), e);
                        }
                    });
        } catch (Exception e) {
            // Handle exception appropriately
            LOG.error("Error accessing field: {}", obj, e);
        }
    }


}

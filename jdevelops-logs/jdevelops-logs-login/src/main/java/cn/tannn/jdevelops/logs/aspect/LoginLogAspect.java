package cn.tannn.jdevelops.logs.aspect;


import cn.tannn.ip2region.sdk.IpUtil;
import cn.tannn.jdevelops.logs.LoginLog;
import cn.tannn.jdevelops.logs.model.LoginLogRecord;
import cn.tannn.jdevelops.uitls.aop.JsonUtils;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    /**
     * 表达式 异常时用
     */
    String expressionError = "";


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
        apiLog.setToken("");
        apiLog.setDescription(ex.getMessage());
        // save
    }

    /**
     * 返回通知
     */
    @AfterReturning(value = "loginLog()", returning = "rvt")
    public void saveSysLog(JoinPoint joinPoint, Object rvt) {

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
        apiLog.setStatus(0);
        apiLog.setTokenPlatform("");
        apiLog.setToken("");
        apiLog.setDescription("正常");



        if (myLog == null || !myLog.enable()) {
            return;
        }

        Object expression = AopReasolver.newInstance().resolver(joinPoint, myLog.expression());
        if (null != expression) {
            expressionError = expression.toString();
            apiLog.setExpression(expressionError);
        } else {
            apiLog.setExpression("");
        }
        apiLog.setDescription(myLog.description());
        apiLog.setChineseApi(myLog.chineseApi());
        apiLog.setLogType(myLog.type());


        /* outParams and  status  */
        if (Objects.nonNull(rvt)) {
            try {
                apiLog.setStatus(true);
                if (rvt instanceof String || rvt instanceof Integer) {
                    apiLog.setStatus(true);
                } else if (rvt instanceof List) {
                    apiLog.setStatus(true);
                } else {
                    Boolean status = (Boolean) JsonUtils.objectToMap(rvt).getOrDefault("success", false);
                    apiLog.setStatus(status);
                }
            } catch (Exception e) {
                LOG.warn("api find status error : {}", e.getMessage());
                apiLog.setStatus(false);
            }
            try {
                if (myLog.logResultData()) {
                    apiLog.setOutParams(JsonUtils.toJson(rvt));
                } else {
                    apiLog.setOutParams("");
                }
            } catch (Exception e) {
                LOG.error("", e);
                LOG.warn("api find out params  resolver error : {}", e.getMessage());
                apiLog.setOutParams("");
            }
        } else {
            apiLog.setStatus(true);
            apiLog.setOutParams("");
        }



        /*inParams    输入 */
        apiLog.setPoxyIp(IpUtil.httpRequestIp(request));
    }


}

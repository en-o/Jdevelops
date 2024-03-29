package cn.jdevelops.aop.api.log.aspect;

import cn.jdevelops.aop.api.log.annotation.ApiLog;
import cn.jdevelops.aop.api.log.bean.ApiMonitoring;
import cn.jdevelops.aop.api.log.server.ApiLogSave;
import cn.jdevelops.util.aops.AopReasolver;
import cn.jdevelops.util.aops.IpUtil;
import cn.jdevelops.util.aops.JsonUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 接口日志保存
 *
 * @author tn
 * @date 2020/6/1 21:04
 */

@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@Aspect
@Component
public class ApiLogAspectSave {

    private static final Logger LOG = LoggerFactory.getLogger("APILOG");

    @Autowired
    private ApiLogSave apiLogSave;

    /**
     * 表达式 异常时用
     */
    String expressionError = "";


    /**
     * 定义切点 @Pointcut
     * 在注解的位置切入代码
     */
    @Pointcut("@annotation(cn.jdevelops.aop.api.log.annotation.ApiLog)")
    public void apiLog() {
    }


    /**
     * 异常通知
     */
    @AfterThrowing(value = "apiLog()", throwing = "ex")
    public void doAfterThrowing(JoinPoint jp, Exception ex) {
        //保存日志
        ApiMonitoring apiLog = new ApiMonitoring();
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) jp.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        /*key*/
        ApiLog myLog = method.getAnnotation(ApiLog.class);
        if (myLog != null) {
            apiLog.setDescription(myLog.expression());
            apiLog.setChineseApi(myLog.chineseApi());
            apiLog.setLogType(myLog.type().getType());
        }
        /*接口名*/
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String requestUri = (request).getRequestURI();
        apiLog.setApiUrl(requestUri);
        apiLog.setCallType(2);
        apiLog.setMethod(request.getMethod());
        /* outParams and  status  */
        apiLog.setStatus(false);
        apiLog.setOutParams("接口调用出错");
        /* callTime 调用时间  */
        apiLog.setCallTime(System.currentTimeMillis());
        /* callTime 调用时间  */
        /*inParams    输入 */
        apiLog.setInParams("");
        apiLog.setExpression(expressionError);
        /*inParams    输入 */
        apiLog.setPoxyIp(IpUtil.getPoxyIp(request));
        apiLogSave.saveLog(apiLog);
    }

    /**
     * 返回通知
     */
    @AfterReturning(value = "apiLog()", returning = "rvt")
    public void saveSysLog(JoinPoint joinPoint, Object rvt) {
        //保存日志
        ApiMonitoring apiLog = new ApiMonitoring();

        /*接口名*/
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String requestUri = (request).getRequestURI();
        apiLog.setApiUrl(requestUri);
        apiLog.setMethod(request.getMethod());
        apiLog.setCallType(1);


        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        /*key*/
        ApiLog myLog = method.getAnnotation(ApiLog.class);
        if(myLog == null || !myLog.enable()){
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
        apiLog.setLogType(myLog.type().getType());


        /* outParams and  status  */
        if (Objects.nonNull(rvt)) {
            try {
                if (myLog.logResultData()) {
                    if (rvt instanceof String || rvt instanceof Integer) {
                        apiLog.setStatus(true);
                    } else if (rvt instanceof List) {
                        apiLog.setStatus(true);
                    } else {
                        Map<String, Object> beanToMap = beanToMap(rvt);
                        if (beanToMap.get("code").equals(200)) {
                            apiLog.setStatus(true);
                        } else {
                            apiLog.setStatus(true);
                        }
                    }
                    apiLog.setOutParams(JsonUtils.toJson(rvt));
                } else {
                    apiLog.setStatus(true);
                    apiLog.setOutParams("");
                }

            } catch (Exception e) {
                LOG.error("解析结果失败", e);
                apiLog.setStatus(false);
                apiLog.setOutParams("");
            }
        } else {
            apiLog.setStatus(true);
            apiLog.setOutParams("");
        }


        /* callTime 调用时间  */
        apiLog.setCallTime(System.currentTimeMillis());
        /* callTime 调用时间  */


        /*inParams    输入 */

        if ( myLog.logArgs()) {
            //请求的参数
            Object[] args = joinPoint.getArgs();
            //将参数所在的数组转换成json
            try {
                List<Object> argObjects = Arrays.stream(args).filter(s -> !(s instanceof HttpServletRequest)
                        && !(s instanceof HttpServletResponse)).collect(Collectors.toList());
                String params = JsonUtils.toJson(argObjects);
                apiLog.setInParams(params.contains("null") ? params.replaceAll("null", "") : params);
            } catch (Exception e) {
                LOG.error("解析入参失败", e);
                apiLog.setInParams("");
            }
        }
        /*inParams    输入 */
        apiLog.setPoxyIp(IpUtil.getPoxyIp(request));
        apiLogSave.saveLog(apiLog);
    }


    /**
     * bean转化为map
     *
     * @param bean bean
     * @return Map
     */
    private static Map<String, Object> beanToMap(Object bean) {
        if (bean == null) {
            return Collections.emptyMap();
        } else {
            HashMap<String, Object> hashMap = new HashMap<>(50);

            try {
                Class<?> c = bean.getClass();
                Method[] methods = c.getMethods();

                for (Method method : methods) {
                    String name = method.getName();
                    String key = "";
                    if (name.startsWith("get")) {
                        key = name.substring(3);
                    }

                    if (!key.isEmpty() && Character.isUpperCase(key.charAt(0)) && method.getParameterTypes().length == 0) {
                        if (key.length() == 1) {
                            key = key.toLowerCase();
                        } else if (!Character.isUpperCase(key.charAt(1))) {
                            key = key.substring(0, 1).toLowerCase() + key.substring(1);
                        }

                        if (!"class".equalsIgnoreCase(key)) {
                            Object value = method.invoke(bean);
                            if (value != null) {
                                hashMap.put(key, value);
                            }
                        }
                    }
                }
            } catch (Throwable var9) {
                var9.printStackTrace();
            }

            return hashMap;
        }
    }


}

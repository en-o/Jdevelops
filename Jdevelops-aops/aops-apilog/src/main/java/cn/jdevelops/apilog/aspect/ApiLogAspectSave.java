package cn.jdevelops.apilog.aspect;

import cn.jdevelops.apilog.util.AopReasolver;
import cn.jdevelops.apilog.util.IpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.jdevelops.apilog.annotation.ApiLog;
import cn.jdevelops.apilog.bean.ApiMonitoring;
import cn.jdevelops.apilog.server.ApiLogSave;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 接口日志保存
 * @author tn
 * @date  2020/6/1 21:04
 */

@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@Aspect
@Component
@Slf4j
public class ApiLogAspectSave {

   final static String DEFAULT_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private ApiLogSave apiLogSave;

    /**
     * appkey 异常时用
     */
    String appKeyError = "";


    /**
     * 定义切点 @Pointcut
     * 在注解的位置切入代码
     */
    @Pointcut("@annotation(cn.jdevelops.apilog.annotation.ApiLog)")
    public void apiLog() {
    }

    /**
     * 返回通知
     */
    @AfterReturning(value="apiLog()",returning="rvt")
    public void saveSysLog(JoinPoint joinPoint, Object rvt) {
        //保存日志
        ApiMonitoring apiLog = new ApiMonitoring();

        /*接口名*/
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String requestUri = (request).getRequestURI();
        apiLog.setApiName(requestUri);

        /* outParams and  status  */
        if (Objects.nonNull(rvt)) {
            try {
                if (rvt instanceof String || rvt instanceof Integer) {
                    apiLog.setStatus("true");
                } else if (rvt instanceof List) {
                    apiLog.setStatus("true");
                } else {
                    Map<String, Object> beanToMap = beanToMap(rvt);
                    apiLog.setStatus(beanToMap.get("success") + "");
                }
                apiLog.setOutParams(JSONObject.toJSONString(rvt));

            } catch (Exception e) {
                apiLog.setStatus("false");
                apiLog.setOutParams("");
            }
        }

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        /*key*/
        ApiLog myLog = method.getAnnotation(ApiLog.class);
        if (myLog != null) {
            Object apiKey = AopReasolver.newInstance().resolver(joinPoint, myLog.apiKey());
            appKeyError = Objects.nonNull(rvt) ? apiKey + "" : "";
            apiLog.setApiKey(Objects.nonNull(rvt) ? apiKey + "" : "");
        }

        /* callTime 调用时间  */
        apiLog.setCallTime(DateTime.now().toString(DEFAULT_FORMAT_DATETIME));
        /* callTime 调用时间  */


        /*inParams    输入 */
        //请求的参数
        Object[] args = joinPoint.getArgs();
        //将参数所在的数组转换成json
        try {
            String params = JSON.toJSONString(args);
            apiLog.setInParams(params.contains("null") ? params.replaceAll("null", "") : params);
        }catch (Exception e){
            apiLog.setInParams(null);
        }
        /*inParams    输入 */
        apiLog.setPoxyIp(IpUtil.getPoxyIp(request));
        apiLogSave.saveLog(apiLog);
    }






    /**
     * bean转化为map
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

                    if (key.length() > 0 && Character.isUpperCase(key.charAt(0)) && method.getParameterTypes().length == 0) {
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

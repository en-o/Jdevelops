package com.detabes.apilog.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.detabes.apilog.annotation.ApiLog;
import com.detabes.apilog.bean.ApiMonitoring;
import com.detabes.apilog.server.ApiLogSave;
import com.detabes.enums.string.StringEnum;
import com.detabes.spring.core.aop.AopReasolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tn
 * @date  2020/6/1 21:04
 * @description   接口日志
 */

@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@Aspect
@Component
@Slf4j
public class ApiLogAspectSave {


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
    @Pointcut("@annotation(com.detabes.apilog.annotation.ApiLog)")
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
        String url = request.getRequestURL().toString();
        apiLog.setApiName(requestUri);

        /* outParams and  status  */
        if (ObjectUtil.isNotEmpty(rvt)) {
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
            appKeyError = ObjectUtil.isNotEmpty(rvt) ? apiKey + "" : "";
            apiLog.setApiKey(ObjectUtil.isNotEmpty(rvt) ? apiKey + "" : "");
        }

        /* callTime 调用时间  */
        apiLog.setCallTime(DateUtil.now());
        /* callTime 调用时间  */

        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();

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
        /**inParams    输入 */

        apiLogSave.saveLog(apiLog);
    }
    /**
     * 异常通知
     */
    @AfterThrowing("apiLog()")
    public void doAfterThrowing(){

    }





    /**
     * 实体转Map
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    private   Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> params = new HashMap<String, Object>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!"class".equals(name)) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
            if(obj!=null&&params.containsKey(StringEnum.EMPTY_STRING.getStr()) ){
                params = (Map<String, Object>) obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

}

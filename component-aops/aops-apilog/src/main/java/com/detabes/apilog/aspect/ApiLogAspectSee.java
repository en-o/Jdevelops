package com.detabes.apilog.aspect;

import com.detabes.spring.core.aop.ParamsDis;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tn
 * @version 1
 * @ClassName ApiLogAspectSee
 * @description 接口记录
 * @date 2020/6/16 10:03
 */
@Component
@Aspect
public class ApiLogAspectSee {

    @Pointcut("execution(* com.*..controller..*(..))")
    public void executeService() {
    }

    @Around("executeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = getRequest();
        return ParamsDis.aopDis(request, pjp);
    }


    private static HttpServletRequest getRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        return sra.getRequest();
    }
}

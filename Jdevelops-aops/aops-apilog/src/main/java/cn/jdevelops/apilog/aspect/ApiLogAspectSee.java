package cn.jdevelops.apilog.aspect;

import cn.jdevelops.spring.core.aop.ParamsDis;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 接口记录
 * @author tn
 * @version 1
 * @date 2020/6/16 10:03
 */
@Component
@Aspect
public class ApiLogAspectSee {


    @Around("execution(* *.*..controller..*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = getRequest();
        if(Objects.isNull(request)){
            return pjp.proceed();
        }
        return ParamsDis.aopDis(request, pjp);
    }


    private static HttpServletRequest getRequest() {
        try {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            assert sra != null;
            return sra.getRequest();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

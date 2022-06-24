package cn.jdevelops.exception.aspect;

import cn.jdevelops.exception.annotation.DisposeException;
import cn.jdevelops.exception.exception.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 异常处理
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-06-24 13:17
 */
@Aspect
@Component
@Lazy(false)
public class ExceptionAspect {

    private static final int  DEF_CODE = 500;

    @Pointcut("@annotation(cn.jdevelops.exception.annotation.DisposeException)")
    public void disposeException() {
    }

    /**
     * 异常通知
     */
    @AfterThrowing(value = "disposeException()",throwing = "ex")
    public void doAfterThrowing(JoinPoint jp,Exception ex){
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) jp.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        DisposeException disposeException = method.getAnnotation(DisposeException.class);
        String[] messages = disposeException.messages();
        Class[] exceptions = disposeException.exceptions();
        int[] codes = disposeException.codes();
        for (int i = 0; i < exceptions.length; i++) {
            Class exception = exceptions[i];
            if(exception.isInstance(ex)){
                String message = ex.getMessage();
                int code = DEF_CODE;
                try {
                    message = messages[i];
                }catch (Exception ignored){}
                try {
                    code = codes[i];
                }catch (Exception ignored){}

                throw new BusinessException(code,message);
            }
        }
    }


}

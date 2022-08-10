package cn.jdevelops.exception.aspect;

import cn.jdevelops.exception.AopException;
import cn.jdevelops.exception.annotation.DisposeException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 异常处理
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-06-24 13:17
 */
@Aspect
@Component
public class ExceptionAspect {

    private static final int DEF_CODE = 500;

    private static Throwable oneEx;

    /**
     * <p>
     *     within 对象级别
     *     annotation 方法级别
     * </p>
     *
     */
    @Pointcut("@within(cn.jdevelops.exception.annotation.DisposeException) || @annotation(cn.jdevelops.exception.annotation.DisposeException)")
    public void disposeException() {
    }

    /**
     * 异常通知
     */
    @AfterThrowing(value = "disposeException()", throwing = "ex")
    public void doAfterThrowing(JoinPoint jp, Exception ex) {
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) jp.getSignature();


        //获取切入点所在的方法
        Method method = signature.getMethod();
        DisposeException disposeException = method.getAnnotation(DisposeException.class);
        if(Objects.isNull(disposeException)){
            disposeException = jp.getTarget().getClass().getAnnotation(DisposeException.class);
        }
        String[] messages = disposeException.messages();
        Class[] exceptions = disposeException.exceptions();
        int[] codes = disposeException.codes();
        oneEx = ex;
        for (int i = 0; i < exceptions.length; i++) {
            search(exceptions, codes, ex, messages, i);
        }
        throw new AopException(DEF_CODE, oneEx.getMessage());
    }

    /**
     *
     * @param exceptions 待处理的异常
     * @param codes 待处理的异常code
     * @param ex 抛出异常的ex
     * @param messages 待处理的异常message
     * @param index Class[]下标
     */
    protected void search(Class[] exceptions, int[] codes, Throwable ex, String[] messages, int index) {
        int code = DEF_CODE;
        String eName;
        try {
            eName = ex.getClass().getName();
        } catch (Exception e) {
           return;
        }
        Class exception = exceptions[index];
        String pName = exception.getName();
        if (eName.equalsIgnoreCase(pName)) {
            String message = ex.getMessage();
            try {
                message = messages[index];
            } catch (Exception ignored) {
            }
            try {
                code = codes[index];
            } catch (Exception ignored) {
            }

            throw new AopException(code, message);
        } else {
            search(exceptions, codes, ex.getCause(), messages, index);
        }
    }

}

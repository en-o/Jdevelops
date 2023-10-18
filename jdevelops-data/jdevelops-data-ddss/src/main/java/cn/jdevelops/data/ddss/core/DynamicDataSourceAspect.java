package cn.jdevelops.data.ddss.core;

import cn.jdevelops.annotation.ddss.DbName;
import cn.jdevelops.annotation.ddss.DbNamed;
import cn.jdevelops.annotation.ddss.DyDS;
import cn.jdevelops.data.ddss.context.DynamicContextHolder;
import cn.jdevelops.data.ddss.exception.DynamicDataSourceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 多数据源，切面处理类
 * @author tan
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DynamicDataSourceAspect {
    protected Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);


    /**
     * @annotation(匹配有指定注解的方法,注解作用在方法上面) <br/>
     * @within(拦截包中任意方法，不包含子包中的方法)
     */
    @Pointcut("@annotation(cn.jdevelops.annotation.ddss.DyDS) " +
            "|| @within(cn.jdevelops.annotation.ddss.DyDS)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //获取数据源名
        String dbName = getDbName(point);
        if (dbName != null) {
            //根据dbName动态设置数据源
            DynamicDataSource.setDataSource(dbName);
        }
        try {
            // todo 这里进行源切换时，可以考虑在缓存中加入有效源和无效源，这样就可以直接判断失效源，从而不在依靠try处理失效源
            return point.proceed();
        } catch (CannotGetJdbcConnectionException e) {
            throw DynamicDataSourceException.specialMessage("数据源====《" + dbName + "》 可能已被移除无法在对其进行操作");
        } finally {
            DynamicContextHolder.poll();
            logger.debug("clean datasource");
        }
    }


    /**
     * 根据@DbName注解获取数据源的dbName
     *
     * @param point ProceedingJoinPoint
     * @return dbName
     */
    private String getDbName(ProceedingJoinPoint point) throws IllegalAccessException {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class targetClass = point.getTarget().getClass();
        Method method = signature.getMethod();
        String dbName = getDbNameByDyDS(method, targetClass);
        if(Objects.equals(dbName, "")){
            Object[] args = point.getArgs();
            dbName = getDbNameByDbName(args, method);
        }
        return dbName;
    }


    /**
     * 从  @DyDS 中获取数据源名[直接获取写死的数据源名]
     *
     * @param method Method
     * @param targetClass 当前被拦截对象的类信息
     * @return 数据源名
     */
    public String getDbNameByDyDS(Method method, Class targetClass) {
        DyDS targetDsDhoose = (DyDS) targetClass.getAnnotation(DyDS.class);
        DyDS methodDsDhoose = method.getAnnotation(DyDS.class);
        String value = "";
        if (targetDsDhoose != null || methodDsDhoose != null) {
            if (methodDsDhoose != null) {
                value = methodDsDhoose.value();
            } else {
                value = targetDsDhoose.value();
            }
        }
        return value;
    }


    /**
     * 从  @DbName 中获取数据源名[从参数中获取 数据源名]
     *
     * @param args   参数
     * @param method Method
     * @return 数据源名
     * @throws IllegalAccessException 数据源名获取异常
     */
    public String getDbNameByDbName(Object[] args, Method method) throws IllegalAccessException {

        String value = "";
        //参数注解，1维是参数，2维是注解
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Object param = args[i];
            Annotation[] paramAnn = annotations[i];
            //参数为空，直接下一个参数
            if (param == null || paramAnn.length == 0) {
                continue;
            }
            for (Annotation pAnnotation : paramAnn) {
                if (pAnnotation.annotationType().equals(DbName.class)) {
                    value = param.toString();
                    break;
                } else if (pAnnotation.annotationType().equals(DbNamed.class)) {
                    Field[] fields = param.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        boolean hasSecureField = field.isAnnotationPresent(DbName.class);
                        if (hasSecureField) {
                            field.setAccessible(true);
                            value = (String) field.get(param);
                        }
                    }

                }
            }
        }
        return value;
    }

}

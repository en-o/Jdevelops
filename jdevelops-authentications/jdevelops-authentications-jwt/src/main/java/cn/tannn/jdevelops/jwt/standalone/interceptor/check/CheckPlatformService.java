package cn.tannn.jdevelops.jwt.standalone.interceptor.check;


import cn.tannn.jdevelops.annotations.web.authentication.ApiPlatform;
import cn.tannn.jdevelops.annotations.web.constant.PlatformConstant;
import cn.tannn.jdevelops.jwt.standalone.exception.PermissionsException;
import cn.tannn.jdevelops.utils.jwt.core.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

import static cn.tannn.jdevelops.utils.jwt.exception.TokenCode.UNAUTHENTICATED_PLATFORM;


/**
 *  PlatformS 检查
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/26 下午2:47
 */
public class CheckPlatformService {

    private static final Logger logger_global = LoggerFactory.getLogger(CheckPlatformService.class);

    private final String token;
    private final Method method;
    private final Class<?> controllerClass;

    public CheckPlatformService(String token, Method method, Class<?> controllerClass) {
        this.token = token;
        this.method = method;
        this.controllerClass = controllerClass;
    }

    /**
     * 检查接口的使用范围是否能跟token中的返回对应上
     */
    public void checkApiPlatform() {
        List<PlatformConstant> platformConstants = JwtService.getPlatformConstantExpires(token);
        if (method.isAnnotationPresent(ApiPlatform.class)) {
            if (!jwtListExistAnnotationMethod(platformConstants, method)) {
                throw new PermissionsException(UNAUTHENTICATED_PLATFORM);
            }
        } else if (controllerClass.isAnnotationPresent(ApiPlatform.class)
                   && (!jwtListExistAnnotationMethodClasses(platformConstants, controllerClass))) {
            throw new PermissionsException(UNAUTHENTICATED_PLATFORM);

        }

    }


    /**
     * 检查方法
     */
    private boolean jwtListExistAnnotationMethod(List<PlatformConstant> platformConstants,
                                                 Method method) {
        ApiPlatform annotation = method.getAnnotation(ApiPlatform.class);
        for (PlatformConstant annotationPlatform : annotation.platform()) {
            if (annotationPlatform.contains(platformConstants)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查类
     */
    private boolean jwtListExistAnnotationMethodClasses(List<PlatformConstant> platformConstants,
                                                        Class<?> controllerClass) {
        ApiPlatform annotation = controllerClass.getAnnotation(ApiPlatform.class);
        for (PlatformConstant annotationPlatform : annotation.platform()) {
            if (annotationPlatform.contains(platformConstants)) {
                return true;
            }
        }
        return false;
    }
}

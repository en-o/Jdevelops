package cn.tannn.jdevelops.jwt.standalone.interceptor.check;


import cn.tannn.jdevelops.annotations.web.authentication.ApiPlatform;
import cn.tannn.jdevelops.jwt.standalone.exception.PermissionsException;
import cn.tannn.jdevelops.utils.jwt.core.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static cn.tannn.jdevelops.utils.jwt.exception.TokenCode.UNAUTHENTICATED_PLATFORM;


/**
 *  PlatformS 检查
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/26 下午2:47
 */
public class CheckPlatformService {

    private static final Logger logger_global = LoggerFactory.getLogger(CheckPlatformService.class);
    public static AntPathMatcher antPathMatcher = new AntPathMatcher();
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
     * @param servletPath 当前接口路径
     */
    public void checkApiPlatform(String servletPath) {
        logger_global.debug("Checking API Platform servletPath {}", servletPath);
        List<String> platformConstants = JwtService.getPlatformConstantExpires(token);
        if (method.isAnnotationPresent(ApiPlatform.class)) {
            if (!jwtListExistAnnotationMethod(platformConstants, method)) {
                throw new PermissionsException(UNAUTHENTICATED_PLATFORM);
            }
        } else if (controllerClass.isAnnotationPresent(ApiPlatform.class)){
            ApiPlatform annotation = controllerClass.getAnnotation(ApiPlatform.class);
            // 过滤 掉不需要检查的接口
            if(Arrays.stream(annotation.filter()).noneMatch(e -> antPathMatcher.match(e, servletPath))){
                if(!jwtListExistAnnotationMethodClasses(annotation.platform(), platformConstants)){
                    throw new PermissionsException(UNAUTHENTICATED_PLATFORM);
                }
            }else{
                logger_global.debug("permit through Platform servletPath {}", servletPath);
            }
        }

    }


    /**
     * 检查方法
     */
    private boolean jwtListExistAnnotationMethod(List<String> platformConstants,
                                                 Method method) {
        ApiPlatform annotation = method.getAnnotation(ApiPlatform.class);
        for (String annotationPlatform : annotation.platform()) {
            if (platformConstants.contains(annotationPlatform)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查类、
     * @param platform 接口支持的平台
     * @param platformConstants 当前用户拥有的平台
     * @return true 当前接口支持的平台在用户拥有的平台中
     */
    private boolean jwtListExistAnnotationMethodClasses(String[] platform,
                                                        List<String> platformConstants) {
        for (String annotationPlatform : platform) {
            if (platformConstants.contains(annotationPlatform)) {
                return true;
            }
        }
        return false;
    }
}

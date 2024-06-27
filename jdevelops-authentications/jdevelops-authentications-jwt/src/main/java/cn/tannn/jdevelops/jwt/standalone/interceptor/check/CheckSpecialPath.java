package cn.tannn.jdevelops.jwt.standalone.interceptor.check;

import cn.tannn.jdevelops.jwt.standalone.service.CheckTokenInterceptor;
import cn.tannn.jdevelops.jwt.standalone.util.CookieUtil;
import cn.tannn.jdevelops.utils.jwt.config.JwtConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static cn.tannn.jdevelops.jwt.standalone.interceptor.JwtWebApiInterceptor.extractedErrorResponse;

/**
 * 路径处理
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/26 下午3:23
 */
public class CheckSpecialPath {

    private static final Logger logger_global = LoggerFactory.getLogger(CheckSpecialPath.class);

    private final ResourceHttpRequestHandler resourceHandler;
    private final String servletPath;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final JwtConfig jwtConfig;
    private final CheckTokenInterceptor checkTokenInterceptor;

    public CheckSpecialPath(ResourceHttpRequestHandler resourceHandler
            , String servletPath
            , HttpServletRequest request
            , HttpServletResponse response
            , JwtConfig jwtConfig, CheckTokenInterceptor checkTokenInterceptor) {
        this.resourceHandler = resourceHandler;
        this.servletPath = servletPath;
        this.request = request;
        this.response = response;
        this.jwtConfig = jwtConfig;
        this.checkTokenInterceptor = checkTokenInterceptor;
    }

    /**
     * 检查 local oss 文件访问的权限
     *
     * @return 1: 放行, 0 拦截, null 不做处理
     */
    public Integer checkOssToken() {

        try {
            ApplicationContext applicationContext = resourceHandler
                    .getApplicationContext();
            if (applicationContext == null) {
                return null;
            }
            Environment environment = applicationContext.getEnvironment();
            String localOssResourceUpDir = environment.getProperty("jdevelops.oss.local.upload-dir", "");

            String localOssResourceContextPath = environment.getProperty("jdevelops.oss.local.context-path", "");

            // local oss
            if (servletPath.startsWith(localOssResourceContextPath)) {
                if (resourceHandler.toString().equals("ResourceHttpRequestHandler [URL [file:" + localOssResourceUpDir + "/]]")) {
                    Cookie cookie = CookieUtil.findCookie(
                            jwtConfig.getOss().getOssLocalJwtKey(), request.getCookies()
                    ).orElse(null);
                    if (cookie == null) {
                        extractedErrorResponse(response);
                        return 0;
                    } else {
                        if (checkTokenInterceptor.checkToken(cookie.getValue())) {
                            return 1;
                        } else {
                            extractedErrorResponse(response);
                            return 0;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger_global.error("  ===> checkOssToken error {}", e.getMessage());
        }
        return null;
    }
}

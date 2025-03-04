package cn.tannn.jdevelops.apis.log;


import cn.tannn.jdevelops.apis.log.annotation.ApiLog;
import cn.tannn.jdevelops.apis.log.config.LogConfig;
import cn.tannn.jdevelops.apis.log.constants.ApiLogConstants;
import cn.tannn.jdevelops.apis.log.module.LoggerPrint;
import cn.tannn.jdevelops.apis.log.util.IpUtil;
import cn.tannn.jdevelops.webs.interceptor.ApiBeforeInterceptor;
import cn.tannn.jdevelops.webs.interceptor.util.HandlerUtil;
import cn.tannn.jdevelops.webs.interceptor.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;

import java.util.Objects;
import java.util.Optional;

/**
 * 全局打印接口日志
 *
 * @author tan
 */
@Order(1)
public class GlobalApiLogPrint implements ApiBeforeInterceptor {

    private final LogConfig logConfig;

    /**
     * logback-spring.xml中定义 appender api-log 完成自定api文件输出
     */
    private static final Logger logger = LoggerFactory.getLogger(ApiLogConstants.LOGGER_NAME);

    public GlobalApiLogPrint(LogConfig logConfig) {
        this.logConfig = logConfig;
    }

    @Override
    public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求参数
        LoggerPrint loggerPrint = null;
        try {
            // 不拦截这个页面
            if (request.getRequestURI().contains(ApiLogConstants.ERROR_PAGE)) {
                return true;
            }
            try {
                Optional<ApiLog> apiLog = HandlerUtil.methodAnnotation(handler, ApiLog.class);
                if (apiLog.isPresent() && apiLog.get().enable() && apiLog.get().consolEenable()) {
                    return true;
                }
            } catch (Exception e) {
                logger.error("ApiLog注解查询失败 {}", e.getMessage());
            }
            String requestParams = RequestUtil.requestParams(request);
            if (!IpUtil.printParams(logConfig.getInterceptApis(), request.getRequestURI())) {
                requestParams = RequestUtil.requestParams(request);
            }
            loggerPrint = new LoggerPrint(IpUtil.httpRequestIp(request),
                    request.getRequestURL().toString(), request.getMethod(), requestParams, System.currentTimeMillis());
        } catch (Exception e) {
            logger.error("接口日志记录失败", e);
        }
        logger.info(Objects.isNull(loggerPrint) ? "" : loggerPrint.ltoString());
        return true;
    }
}

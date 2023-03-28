package cn.jdevelops.api.log;


import cn.jdevelops.api.log.entity.LoggerEntity;
import cn.jdevelops.http.core.IpUtil;
import cn.jdevelops.interceptor.api.ApiBeforeInterceptor;
import cn.jdevelops.interceptor.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 接口日志
 *
 * @author tan
 */
@AutoConfiguration
@ConditionalOnProperty(
        value="jdevelops.api.log.enabled",
        havingValue = "true",
        matchIfMissing = true)
@Order(1)
public class ApiLogInterceptor implements ApiBeforeInterceptor {
    private final static String ERROR_PAGE = "/error";

    /**
     * logback-spring.xml中定义 appender api-log 完成自定api文件输出
     */
    private static final Logger logger = LoggerFactory.getLogger("api-log");

    @Override
    public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求参数
        LoggerEntity loggerEntity = null;
        try {
            // 不拦截这个页面
            if(request.getRequestURI().contains(ERROR_PAGE)){
                return true;
            }
            String requestParams = RequestUtil.requestParams(request);
            loggerEntity = new LoggerEntity(IpUtil.getPoxyIpEnhance(request),
                    request.getRequestURL().toString(), requestParams, System.currentTimeMillis());
        }catch (Exception e){
            logger.error("接口日志记录失败", e);
        }

        logger.info(Objects.isNull(loggerEntity)? "" : loggerEntity.toString());
        return true;
    }
}

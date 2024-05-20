package cn.tannn.jdevelops.apis.log;


import cn.tannn.jdevelops.apis.log.constants.ApiLogConstants;
import cn.tannn.jdevelops.apis.log.module.LoggerEntity;
import cn.tannn.jdevelops.apis.log.util.IpUtil;
import cn.tannn.jdevelops.webs.interceptor.ApiBeforeInterceptor;
import cn.tannn.jdevelops.webs.interceptor.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 全局打印接口日志
 *
 * @author tan
 */
@ConditionalOnProperty(
        value="jdevelops.api.log.enabled",
        havingValue = "true",
        matchIfMissing = true)
@Order(1)
public class GlobalApiLogInterceptor implements ApiBeforeInterceptor {

    /**
     * logback-spring.xml中定义 appender api-log 完成自定api文件输出
     */
    private static final Logger logger = LoggerFactory.getLogger(ApiLogConstants.LOGGER_NAME);

    @Override
    public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求参数
        LoggerEntity loggerEntity = null;
        try {
            // 不拦截这个页面
            if(request.getRequestURI().contains(ApiLogConstants.ERROR_PAGE)){
                return true;
            }
            String requestParams = RequestUtil.requestParams(request);
            loggerEntity = new LoggerEntity(IpUtil.httpRequestIp(request),
                    request.getRequestURL().toString(),request.getMethod(), requestParams, System.currentTimeMillis());
        }catch (Exception e){
            logger.error("接口日志记录失败", e);
        }
        logger.info(Objects.isNull(loggerEntity)? "" : loggerEntity.ltoString());
        return true;
    }
}

package cn.jdevelops.api.log.server;

import cn.jdevelops.api.log.annotation.ApiLog;
import cn.jdevelops.api.log.entity.ApiMonitoring;
import cn.jdevelops.http.core.IpUtil;
import cn.jdevelops.interceptor.util.HandlerUtil;
import cn.jdevelops.interceptor.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 日子存储默认类
 *
 * @author tn
 * @version 1
 * @date 2020/6/16 20:50
 */
@ConditionalOnMissingBean(name = {"apiLogSave", "defaultApiLogSave"})
@Service
public class DefaultApiLogSave implements ApiLogSave {
    private static final Logger LOG = LoggerFactory.getLogger("api-log");


    @Override
    @Async("apiLogAsyncTaskExecutor")
    public void saveLog(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            Optional<ApiLog> apiLog = HandlerUtil.methodAnnotation(handler, ApiLog.class);
            apiLog.ifPresent(api -> {
                        ApiMonitoring apiMonitoring = new ApiMonitoring();
                        apiMonitoring.setChineseApi(apiLog.get().chineseApi());
                        apiMonitoring.setDescription(apiLog.get().description());
                        apiMonitoring.setInParams(RequestUtil.requestParams(request));
                        apiMonitoring.setPoxyIp(IpUtil.getPoxyIpEnhance(request));
                        apiMonitoring.setCallTime(System.currentTimeMillis());
                        apiMonitoring.setApiUrl(request.getRequestURL().toString());
                        apiMonitoring.setStatus("");
                        apiMonitoring.setOutParams("");
                        LOG.info("api接口调用信息默认输出控制台:{}", apiMonitoring);
                    }
            );
        }catch (Exception e){
            LOG.error("api接口调用信息默认输出控制台:{}", e);
        }
    }
}

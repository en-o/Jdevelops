package cn.tannn.jdevelops.events.websocket.service.impl;

import cn.tannn.jdevelops.events.websocket.config.WebSocketConfig;
import cn.tannn.jdevelops.events.websocket.core.CommonConstant;
import cn.tannn.jdevelops.events.websocket.service.VerifyService;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


/**
 * websocket连接认证
 * @author tan
 */

public class VerifyServiceImpl implements VerifyService{

    private final WebSocketConfig webSocketConfig;


    public VerifyServiceImpl(WebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
    }

    @Override
    public boolean verifyLogin(HttpServletRequest request) {

        String servletPath = request.getServletPath();
        // 如果开启了verify-path-no: true 配置，这里就必须些
        if(servletPath.contains(CommonConstant.VERIFY_PATH_NO)){
            //  不用登录直接连接
            return true;
        }
        String token = request.getParameter(webSocketConfig.getTokenName());
        if(Objects.isNull(token)){
            token = request.getHeader(webSocketConfig.getTokenName());
        }
        return webSocketConfig.getTokenSecret().equals(token);
    }


}

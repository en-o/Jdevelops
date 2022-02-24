package cn.jdevelops.websocket.core.config;

import cn.jdevelops.jwt.util.JwtUtil;
import cn.jdevelops.websocket.core.constant.CommonConstant;
import cn.jdevelops.websocket.core.util.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Objects;

/**
 * 鉴权  默认所有接口鉴权
 * @author https://blog.csdn.net/lovo1/article/details/103852900
 * */
@Slf4j
@Component
public class ServerConfigurator extends ServerEndpointConfig.Configurator {

    /**
     * token鉴权认证
     */
    @Override
    public boolean checkOrigin(String originHeaderValue) {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String servletPath = request.getServletPath();
        if (SocketUtil.banConnection(servletPath)){
            return false;
        }
        if(servletPath.contains(CommonConstant.VERIFY_PATH_NO)){
            //  不用登录连接
            return true;
        }
        String token = request.getParameter("token");
        if(Objects.isNull(token)){
            token = request.getHeader("token");
        }
        return JwtUtil.verity(token);
    }
 

 
}

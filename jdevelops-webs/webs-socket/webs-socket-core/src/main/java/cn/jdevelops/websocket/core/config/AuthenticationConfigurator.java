package cn.jdevelops.websocket.core.config;

import cn.jdevelops.jwt.constant.JwtConstant;
import cn.jdevelops.jwt.util.JwtUtil;
import cn.jdevelops.websocket.core.constant.CommonConstant;
import cn.jdevelops.websocket.core.util.SocketUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Objects;

import static cn.jdevelops.websocket.core.cache.LocalCache.sessionPools;

/**
 * 鉴权  默认所有接口鉴权和 注入 (https://www.zhihu.com/question/509998275)
 * @author https://blog.csdn.net/lovo1/article/details/103852900
 * */
@Component
public class AuthenticationConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {


    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AuthenticationConfigurator.applicationContext = applicationContext;
    }


    @Override
    public <T> T getEndpointInstance(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * token鉴权认证
     * 用的是控制请求地址的方法来鉴权控制
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
        String token = request.getParameter(JwtConstant.TOKEN);
        if(Objects.isNull(token)){
            token = request.getHeader(JwtConstant.TOKEN);
        }
        boolean verity = JwtUtil.verity(token);
        // 这一步好像没什么用，记不得以前写来是为什么的了
        if(!verity) {
            String topic = request.getServletPath();
            String substring = topic.substring(topic.lastIndexOf("/")+1);
            sessionPools.remove(substring);
        }
        return verity;
    }



}

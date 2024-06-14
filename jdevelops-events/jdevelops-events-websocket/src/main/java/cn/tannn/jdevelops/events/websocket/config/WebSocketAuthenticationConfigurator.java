package cn.tannn.jdevelops.events.websocket.config;

import cn.tannn.jdevelops.events.websocket.service.VerifyService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.ServerEndpointConfig;


/**
 * 鉴权  默认所有接口鉴权和 注入 (https://www.zhihu.com/question/509998275)
 *
 * @author https://blog.csdn.net/lovo1/article/details/103852900
 */
public class WebSocketAuthenticationConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {


    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WebSocketAuthenticationConfigurator.applicationContext = applicationContext;
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

        VerifyService verifyService = applicationContext.getBean(VerifyService.class);
        WebSocketConfig webSocketConfig = applicationContext.getBean(WebSocketConfig.class);
        if (verifyService.verifyPath(servletPath, webSocketConfig.isVerifyPathNo())) {
            return verifyService.verifyLogin(request);
        }
        return false;
    }

}

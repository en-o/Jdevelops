package cn.jdevelops.webs.websocket.scan;

import cn.jdevelops.webs.websocket.config.AuthenticationConfigurator;
import cn.jdevelops.webs.websocket.config.WebSocketConfig;
import cn.jdevelops.webs.websocket.service.CacheService;
import cn.jdevelops.webs.websocket.service.WebSocketServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
public class EnableAutoScanConfiguration {

    /**
     * 配置文件
     */
    @ConditionalOnMissingBean(WebSocketConfig.class)
    @Bean
    public WebSocketConfig webSocketConfig(){
        return new WebSocketConfig();
    }



    /**
     * 缓存服务
     */
    @ConditionalOnMissingBean(CacheService.class)
    @Bean
    public CacheService cacheService(WebSocketConfig webSocketConfig){
        return new CacheService(webSocketConfig);
    }


    /**
     * webSocker服务端
     */
    @ConditionalOnMissingBean(WebSocketServer.class)
    @Bean
    public WebSocketServer webSocketServer(CacheService cacheService){
        return  new WebSocketServer(cacheService);
    }


    /**
     *   往 spring 容器中注入ServerEndpointExporter实例
     */
    @ConditionalOnMissingBean(ServerEndpointExporter.class)
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }


    /**
     *   鉴权
     */
    @ConditionalOnMissingBean(AuthenticationConfigurator.class)
    @Bean
    public AuthenticationConfigurator serverConfigurator(){
        return new AuthenticationConfigurator();
    }


}

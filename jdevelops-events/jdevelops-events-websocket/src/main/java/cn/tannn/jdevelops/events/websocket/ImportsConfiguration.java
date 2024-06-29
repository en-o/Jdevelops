package cn.tannn.jdevelops.events.websocket;

import cn.tannn.jdevelops.events.websocket.config.WebSocketAuthenticationConfigurator;
import cn.tannn.jdevelops.events.websocket.config.WebSocketConfig;
import cn.tannn.jdevelops.events.websocket.core.WebSocketServer;
import cn.tannn.jdevelops.events.websocket.service.VerifyService;
import cn.tannn.jdevelops.events.websocket.service.WebSocketCacheService;
import cn.tannn.jdevelops.events.websocket.service.impl.VerifyServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * spring注入设置
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/14 下午2:19
 */
@Configuration
public class ImportsConfiguration {

    /**
     * 配置元信息
     */
    @Bean
    public WebSocketConfig webSocketConfig() {
        return new WebSocketConfig();
    }


    /**
     * 缓存
     */
    @Bean
    public WebSocketCacheService webSocketCacheService(WebSocketConfig webSocketConfig) {
        return new WebSocketCacheService(webSocketConfig);
    }

    /**
     * webSocket服务端
     */
    @Bean
    public WebSocketServer webSocketServer(WebSocketCacheService cacheService) {
        return new WebSocketServer(cacheService);
    }

    /**
     * 鉴权配置
     */
    @Bean
    @ConditionalOnMissingBean(name = "webSocketAuthenticationConfigurator")
    public WebSocketAuthenticationConfigurator webSocketAuthenticationConfigurator() {
        return new WebSocketAuthenticationConfigurator();
    }

    /**
     * 鉴权服务
     */
    @Bean
    @ConditionalOnMissingBean(VerifyService.class)
    public VerifyService verifyService(WebSocketConfig webSocketConfig) {
        return new VerifyServiceImpl(webSocketConfig);
    }

    /**
     * 服务器端点导出器
     */
    @Bean
    @ConditionalOnMissingBean(ServerEndpointExporter.class)
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

package com.detabes.websocket.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 *   往 spring 容器中注入ServerEndpointExporter实例
 *
 * @ClassName WebSocketConfig
 * @description  socket配置
 * @author tn
 * @date  2020-07-08 12:33
 */
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}

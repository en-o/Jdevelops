package cn.jdevelops.websocket.core.scan;

import cn.jdevelops.websocket.core.config.ServerConfigurator;
import cn.jdevelops.websocket.core.config.WebSocketConfig;
import cn.jdevelops.websocket.core.service.WebSocketServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({WebSocketConfig.class, WebSocketServer.class, ServerConfigurator.class})
public class EnableAutoScanConfiguration {
}

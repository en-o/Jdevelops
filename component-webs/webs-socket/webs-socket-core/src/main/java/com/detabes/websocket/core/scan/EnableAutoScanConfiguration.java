package com.detabes.websocket.core.scan;

import com.detabes.websocket.core.config.WebSocketConfig;
import com.detabes.websocket.core.service.WebSocketServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;

/**
 * @author tn
 * @ClassName EnableScan
 * @description 自动扫描
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({WebSocketConfig.class, WebSocketServer.class})
public class EnableAutoScanConfiguration {
}

package cn.jdevelops.util.funasr;

import cn.jdevelops.util.funasr.config.FunasrProperties;
import cn.jdevelops.util.funasr.message.FunasrMessage;
import cn.jdevelops.util.funasr.websocket.FunasrWebSocketClientEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.TargetDataLine;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

/**
 * funasr 客户端
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-07 15:08
 */
public class WebSocketClient {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketClient.class);
    public final FunasrProperties funasrProperties;
    public final FunasrMessage funasrMessage;

    public WebSocketClient(FunasrProperties funasrProperties, FunasrMessage funasrMessage) {
        this.funasrProperties = funasrProperties;
        this.funasrMessage = funasrMessage;
    }

    /**
     * @param audio   音频流
     * @param line  TargetDataLine
     */
    public void startFunasr(byte[] audio, TargetDataLine line) {

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            String uri = String.format("ws://%s:%d/", funasrProperties.getHost(), funasrProperties.getPort());
            Session session = container.connectToServer(FunasrWebSocketClientEndpoint.class, URI.create(uri));
            // 发送数据
            funasrMessage.sendSpeech("username", session, audio, line);
        } catch (Exception ex) {
            LOG.error("funasr连接失败", ex);
        }

    }

}

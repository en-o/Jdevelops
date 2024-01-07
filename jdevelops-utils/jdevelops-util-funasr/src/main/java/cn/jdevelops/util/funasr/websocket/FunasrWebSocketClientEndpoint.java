package cn.jdevelops.util.funasr.websocket;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.websocket.*;
import java.io.IOException;

/**
 * 实现 WebSocket 会话的处理
 */
@ClientEndpoint
public class FunasrWebSocketClientEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(FunasrWebSocketClientEndpoint.class);

    /**
     * 接收消息
     * @param message message
     * @param session session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject jsonObject = new JSONObject();
        JSONParser jsonParser = new JSONParser();
        try {
            jsonObject = (JSONObject) jsonParser.parse(message);
            if(jsonObject.containsKey("timestamp")){
                logger.info("timestamp: " + jsonObject.get("timestamp"));
            }
            logger.info("返回的:"+jsonObject);
        } catch (ParseException e) {
            logger.error("jsonParser.parse失败", e);
        }
//        if (iseof && mode.equals(FunasrMode.OFFLINE)
//                && !jsonObject.containsKey("is_final")) {
//            onClose(session);
//        }
//
//        if (iseof && mode.equals(FunasrMode.OFFLINE)
//                && jsonObject.containsKey("is_final")
//                && jsonObject.get("is_final").equals("false")) {
//            onClose(session);
//        }
    }




    @OnClose
    public void onClose(Session session) {
        // 关闭连接
        try {
            session.close();
        } catch (IOException e) {
            logger.error("session.close() error", e);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        // 错误处理
    }
}

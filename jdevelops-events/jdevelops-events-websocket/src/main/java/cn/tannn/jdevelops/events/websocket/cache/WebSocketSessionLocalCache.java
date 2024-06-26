package cn.tannn.jdevelops.events.websocket.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-12-10 22:22
 */
public class WebSocketSessionLocalCache {

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象
     * websocket session
     */
    public static Map<String, SessionInfo> ROBUST_SESSION_POOLS = new ConcurrentHashMap<>();

}

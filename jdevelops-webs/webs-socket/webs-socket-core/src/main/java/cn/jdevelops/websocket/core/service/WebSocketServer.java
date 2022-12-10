package cn.jdevelops.websocket.core.service;

import cn.jdevelops.websocket.core.config.AuthenticationConfigurator;
import cn.jdevelops.websocket.core.constant.CommonConstant;
import cn.jdevelops.websocket.core.util.SocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * webSocker服务端
 * 包含接收消息，推送消息等接口
 * /socket/y/topic
 * /socket/n/topic
 *
 * @author tn
 * @date 2020-07-08 12:36
 */
@Component
@ServerEndpoint(value = "/socket/{ver}/{name}", configurator = AuthenticationConfigurator.class)
public class WebSocketServer {


    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);


    public  final CacheService cacheService;


    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static AtomicInteger online = new AtomicInteger();

    public WebSocketServer(CacheService cacheService) {
        this.cacheService = cacheService;
    }


    /**
     * 连接建立成功调用
     *
     * @param session  客户端与socket建立的会话
     * @param userName 客户端的userName
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "name") String userName, @PathParam(value = "ver") String verify) {
        if (!CommonConstant.OK_PATH.contains(verify)) {
            logger.error("第二路径不合法，第二路径只能为：y,n");
            return;
        }
        List<Session> sessionsArray = new ArrayList<>();
        //获取session
        List<Session> sessions = cacheService.loadSession(userName);
        //存在session
        if (sessions != null) {
            sessionsArray.addAll(sessions);
        }
        sessionsArray.add(session);
        cacheService.saveSession(userName, sessionsArray);
        addOnlineCount();
        logger.info("{}加入webSocket！当前人数为{}", userName, online);
        try {
            sendMessage(session, "欢迎" + userName + "加入连接！");
        } catch (IOException e) {
            logger.error("发送欢迎消息失败", e);
        }
    }

    /**
     * 关闭连接时调用
     *
     * @param userName 关闭连接的客户端的姓名
     * @param session  session
     */
    @OnClose
    public void onClose(@PathParam(value = "name") String userName, Session session) {

        //获取session
        List<Session> sessions = cacheService.loadSession(userName);
        if (session == null) {
            cacheService.removeSession(userName);
        } else {
            sessions.remove(session);
            // 重新保存用户信息
            cacheService.saveSession(userName, sessions);
        }
        subOnlineCount();
        logger.info("{}断开webSocket连接！当前人数为{}", userName, online);
    }

    /**
     * 收到客户端消息时触发（群发）
     *
     * @param message 消息
     */
    @OnMessage
    public void onMessage(String message) {
        for (List<Session> session : cacheService.loadSession()) {
            try {
                session.forEach(ss -> {
                    try {
                        sendMessage(ss, message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                logger.error("群发消息失败", e);
            }
        }
    }


    /**
     * 发生错误时候
     *
     * @param throwable throwable
     */
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * 给指定用户发送消息
     *
     * @param userName 用户名
     * @param message  消息
     */
    public void sendInfo(String userName, String message) {
        List<Session> sessions = cacheService.loadSession(userName);
        if (sessions != null) {
            try {
                sessions.forEach(session -> {
                    try {
                        sendMessage(session, message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                logger.error("指定用户发送消息失败", e);
            }
        }
    }


    /**
     * eg:  name:324
     * name:432
     * 传入 name:   就可以给上面的那个一起发数据（前提这来两个在线）
     * 给指定用户(模糊用户)发送消息
     *
     * @param userName 用户名
     * @param message  消息
     */
    public void sendInfoByLikeKey(String userName, String message) {
        Map<String, List<Session>> stringListMap = SocketUtil.parseMapForFilter(
                cacheService.loadSessionForPools(),
                userName);
        List<Session> session = new ArrayList<>();
        stringListMap.forEach((key, value) -> session.addAll(value));
        try {
            session.forEach(se -> {
                try {
                    sendMessage(se, message);
                } catch (IOException e) {
                    logger.error("发送消息失败", e);
                }
            });
        } catch (Exception e) {
            logger.error("发送消息失败", e);
        }
    }

    /**
     * 发送消息方法
     *
     * @param session 客户端与socket建立的会话
     * @param message 消息
     * @throws IOException 抛出异常
     */
    public void sendMessage(Session session, String message) throws IOException {
        if (session != null) {
            //异步
            session.getAsyncRemote().sendText(message);
        }
    }

    public static void addOnlineCount() {
        online.incrementAndGet();
    }

    public static void subOnlineCount() {
        online.decrementAndGet();
    }



}

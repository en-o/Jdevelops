package cn.tannn.jdevelops.events.websocket.service;

import cn.tannn.jdevelops.events.websocket.cache.SessionInfo;
import cn.tannn.jdevelops.events.websocket.config.WebSocketConfig;
import cn.tannn.jdevelops.events.websocket.exception.WebSocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.util.*;

import static cn.tannn.jdevelops.events.websocket.cache.WebSocketSessionLocalCache.ROBUST_SESSION_POOLS;


/**
 * 缓存服务
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-12-10 22:25
 */
public class WebSocketCacheService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketCacheService.class);

    public final WebSocketConfig webSocketConfig;

    public WebSocketCacheService(WebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
    }

    /**
     * 保存用户连接信息
     * ps: 会根据配置判断是否能多端登录
     *
     * @param userName key
     * @param session  Session
     * @return 返回需要下线的session
     */
    public Session saveSession(final String userName, final String verify, final Session session) {
        Session resultSession = null;
        // 等待保存的 集合
        List<Session> sessionsArray = new ArrayList<>();
        //获取存储了的session(已连接用户)
        List<Session> sessions = loadSession(userName);

        if (webSocketConfig.isMultipart()) {
            //允许多端时,保存用户的所有session
            if (sessions != null) {
                sessionsArray.addAll(sessions);
            }
            sessionsArray.add(session);
            saveSession(userName, verify, sessionsArray);
        } else {
            // 不允许多端时, 保证必须有一个
            if (sessions == null || sessions.isEmpty()) {
                sessionsArray.add(session);
                saveSession(userName, verify, sessionsArray);
            }
            // 下线之前的
            if (webSocketConfig.isOnClose()) {
                // 不知道前面的是谁统统清空后在添加
                removeSession(userName);
                sessionsArray.add(session);
                saveSession(userName, verify, sessionsArray);
                // 终止以前的连接
                resultSession = (sessions == null || sessions.isEmpty() ? null : sessions.get(0));
            } else {
                if (Objects.nonNull(sessions) && !sessions.isEmpty()) {
                    // 后来的不允许连接
                    resultSession = session;
                }
            }

        }
        return resultSession;
    }


    /**
     * 保存用户连接信息
     * ps 不做任何其他判断只会进行保存动作
     *
     * @param userName      key
     * @param sessionsArray Session Array
     */
    public void saveSession(final String userName, final String verify, final List<Session> sessionsArray) {
        Map<String, SessionInfo> robustSessionPools = ROBUST_SESSION_POOLS;
        if(!robustSessionPools.isEmpty()){
            SessionInfo sessionInfo = ROBUST_SESSION_POOLS.get(userName);
            if (sessionInfo != null
                    && sessionInfo.getSessions() != null
                    && !sessionInfo.getSessions().isEmpty()
                    && !sessionInfo.getPath().equalsIgnoreCase(verify)) {
                throw new WebSocketException("同一个用户不允许同时存在于 Y/N 连接");
            }
        }
        ROBUST_SESSION_POOLS.put(userName, new SessionInfo(sessionsArray, verify));
    }

    /**
     * 加载用户连接信息
     *
     * @param userName key
     * @return Session of List
     */
    public List<Session> loadSession(final String userName) {
        //获取session
        SessionInfo sessionInfo = ROBUST_SESSION_POOLS.get(userName);
        if(sessionInfo != null){
            return sessionInfo.getSessions();
        }else {
            return new ArrayList<>();
        }
    }


    /**
     * 加载所有用户连接信息
     *
     * @return List<Session> of Collection
     */
    public List<Session> loadSession() {
        //获取session
        Collection<SessionInfo> sessionInfos = ROBUST_SESSION_POOLS.values();
        List<Session> result = new ArrayList<>();
        for (SessionInfo sessionInfo : sessionInfos) {
            List<Session> sessions = sessionInfo.getSessions();
            if(sessions != null && !sessions.isEmpty()){
                result.addAll(sessions);
            }
        }
        return result;
    }


    /**
     * 加载整个 缓存池map
     *
     * @return Map(name,session)
     */
    public Map<String, List<Session>> loadSessionForPools() {
        Map<String, List<Session>> result = new HashMap<>();
        ROBUST_SESSION_POOLS.forEach((k,v) -> {
            result.put(k,v.getSessions());
        });
        return result;
    }


    /**
     * 删除用户连接信息
     *
     * @param userName key
     */
    public void removeSession(final String userName) {
        ROBUST_SESSION_POOLS.remove(userName);
    }


    /**
     * 删除用户连接信息
     * ps: 有session ,删除指定session值, 没有session 直接删除key的所有值
     *
     * @param userName key
     * @param session  Session
     */
    public void removeSession(final String userName, final String verify, final Session session) {
        if (session == null) {
            removeSession(userName);
        } else {
            //这个用户的所有session
            List<Session> sessions = loadSession(userName);
            // 删除这个session,后重新保存
            sessions.remove(session);
            saveSession(userName, verify, sessions);
        }
    }


}

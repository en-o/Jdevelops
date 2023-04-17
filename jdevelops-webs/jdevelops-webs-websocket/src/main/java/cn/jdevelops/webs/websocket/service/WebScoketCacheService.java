package cn.jdevelops.webs.websocket.service;

import cn.jdevelops.webs.websocket.config.WebSocketConfig;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.*;

import static cn.jdevelops.webs.websocket.cache.LocalCache.sessionPools;


/**
 * 缓存服务
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-12-10 22:25
 */
@Service
public class WebScoketCacheService {


    public final WebSocketConfig webSocketConfig;

    public WebScoketCacheService(WebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
    }

    /**
     * 保存用户连接信息
     * ps: 会根据配置判断是否能多端登录
     * @param userName key
     * @param session  Session
     * @return 返回需要下先的session
     */
    public Session saveSession( final String userName,final Session session){
        Session resultSession = null;
        // 等待保存的 集合
        List<Session> sessionsArray = new ArrayList<>();
        //获取存储了的session(已连接用户)
        List<Session> sessions = loadSession(userName);

        if(webSocketConfig.isMultipart()){
            //允许多端时,保存用户的所有session
            if (sessions != null) {
                sessionsArray.addAll(sessions);
            }
            sessionsArray.add(session);
            saveSession(userName, sessionsArray);
        }else {
            // 不允许多端时, 保证必须有一个
            if(sessions == null || sessions.isEmpty() ){
                sessionsArray.add(session);
                saveSession(userName, sessionsArray);
            }
            // 下线之前的
            if(webSocketConfig.isOnClose()){
                // 不知道前面的是谁统统清空后在添加
                removeSession(userName);
                sessionsArray.add(session);
                saveSession(userName, sessionsArray);
                // 终止以前的连接
                resultSession = (sessions == null || sessions.isEmpty() ? null:sessions.get(0));
            }else {
                if(Objects.nonNull(sessions) && !sessions.isEmpty() ){
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
     * @param userName key
     * @param sessionsArray  Session Array
     */
    public void saveSession( final String userName,final List<Session> sessionsArray){
        sessionPools.put(userName, sessionsArray);
    }

    /**
     * 加载用户连接信息
     * @param userName key
     * @return Session of List
     */
    public List<Session> loadSession(final String userName){
      //获取session
      return sessionPools.get(userName);
    }


    /**
     * 加载所有用户连接信息
     * @return List<Session> of Collection
     */
    public Collection<List<Session>>  loadSession(){
        //获取session
       return sessionPools.values();
    }


    /**
     * 加载整个 缓存池map
     * @return Map
     */
    public Map<String, List<Session>> loadSessionForPools(){
        return sessionPools;
    }


    /**
     * 删除用户连接信息
     * @param userName key
     */
    public void removeSession(final String userName){
        sessionPools.remove(userName);
    }


    /**
     * 删除用户连接信息
     * ps: 有session ,删除指定session值, 没有session 直接删除key的所有值
     * @param userName key
     * @param session  Session
     */
    public void removeSession(final String userName,final Session session){
        if (session == null) {
            removeSession(userName);
        } else {
            //这个用户的所有session
            List<Session> sessions = loadSession(userName);
            // 删除这个session,后重新保存
            sessions.remove(session);
            saveSession(userName, sessions);
        }
    }


}

package cn.jdevelops.websocket.core.service;

import cn.jdevelops.websocket.core.config.WebSocketConfig;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.jdevelops.websocket.core.cache.LocalCache.sessionPools;

/**
 * 缓存服务
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-12-10 22:25
 */
@Service
public class CacheService {


    public final WebSocketConfig webSocketConfig;

    public CacheService(WebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
    }

    /**
     * 保存用户连接信息
     * @param userName key
     * @param sessionsArray  session array
     */
    public void saveSession( final String userName,final List<Session> sessionsArray){
        if(webSocketConfig.isMultipart()){
            sessionPools.put(userName, sessionsArray);
        }else {
            // 不允许多端时,先情况key下的数据在进行添加
            sessionPools.remove(userName);
            sessionPools.put(userName, sessionsArray);
        }

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


}

package cn.tannn.jdevelops.events.websocket.cache;

import jakarta.websocket.Session;

import java.util.List;

/**
 * session元信息
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-12-31 16:58
 */
public class SessionInfo {

    List<Session> sessions;

    /**
     * ServerEndpoint(value = "/socket/{ver}/{name}" 中的{ver}
     */
    String path;

    public SessionInfo(List<Session> sessions, String path) {
        if(!sessions.isEmpty()){
            this.sessions = sessions;
            this.path = path;
        }
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "SessionInfo{" +
                "sessions=" + sessions +
                ", path='" + path + '\'' +
                '}';
    }
}

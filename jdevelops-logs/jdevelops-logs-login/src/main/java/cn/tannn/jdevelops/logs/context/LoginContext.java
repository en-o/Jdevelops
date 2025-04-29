package cn.tannn.jdevelops.logs.context;


import java.util.List;
import java.util.Map;

/**
 * 登录上下文
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/4/28 14:06
 */
public class LoginContext {
    private String requestId;
    private String userId;
    private String userNo;
    private String name;

    /**
     * 登录平台 (默认冲token里取,自己处理（request里)
     */
    private String platform;

    /**
     * 当前登录名
     */
    private String loginName;

    private Map<String, Object> map;


    public LoginContext(String requestId) {
        this.requestId = requestId;
    }

    public LoginContext(String requestId
            , String userId
            , String userNo
            , String name
            , String platform
            , String loginName
            , Map<String, Object> map) {
        this.requestId = requestId;
        this.userId = userId;
        this.userNo = userNo;
        this.name = name;
        this.loginName = loginName;
        this.platform = platform;
        this.map = map;
    }

    public String getUserId() {
        return userId;
    }

    public LoginContext setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserNo() {
        return userNo;
    }

    public LoginContext setUserNo(String userNo) {
        this.userNo = userNo;
        return this;
    }

    public String getName() {
        return name;
    }

    public LoginContext setName(String name) {
        this.name = name;
        return this;
    }

    public String getLoginName() {
        return loginName;
    }

    public LoginContext setLoginName(String loginName) {
        this.loginName = loginName;
        return this;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public LoginContext setMap(Map<String, Object> map) {
        this.map = map;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }


    public void writeTokenPlatform(List<String> platform) {
        if (platform != null && !platform.isEmpty()) {
            this.platform = String.join(",", platform);
        }
    }

    @Override
    public String toString() {
        return "LoginContext{" +
                "requestId='" + requestId + '\'' +
                ", userId='" + userId + '\'' +
                ", userNo='" + userNo + '\'' +
                ", name='" + name + '\'' +
                ", platform='" + platform + '\'' +
                ", loginName='" + loginName + '\'' +
                ", map=" + map +
                '}';
    }
}

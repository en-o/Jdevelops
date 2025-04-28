package cn.tannn.jdevelops.logs.context;


import java.util.Map;

/**
 * 登录上下文
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/4/28 14:06
 */
public class LoginContext {
    private String userId;
    private String userNo;
    private String name;
    private String loginName;
    private Map<String,Object> map;


    public LoginContext() {
    }

    public LoginContext(String userId, String userNo, String name, String loginName, Map<String, Object> map) {
        this.userId = userId;
        this.userNo = userNo;
        this.name = name;
        this.loginName = loginName;
        this.map = map;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "LoginContext{" +
                "userId='" + userId + '\'' +
                ", userNo='" + userNo + '\'' +
                ", name='" + name + '\'' +
                ", loginName='" + loginName + '\'' +
                ", map=" + map +
                '}';
    }
}

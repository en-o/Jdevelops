package cn.jdevelops.util.jwt.entity;



import java.util.Map;

/**
 * 用户登录签名办法token的数据
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-28 15:14
 */
public class SignEntity{

    /**
     * 必填： jwt 主题 （唯一凭证(一般是登录名）
     */
    String subject;

    /**
     * 登录名
     */
    String loginName;


    /**
     * 用户ID
     */
    String userId;


    /**
     * 用户名
     */
    String userName;

    /**
     * 其他信息数据 value 如果时map list的话会变成json
     */
    Map<String, Object> map;


    /**
     *
     * @param subject 必填： jwt 主题 （唯一凭证(一般是登录名）
     * @param loginName 登录名
     * @param userId 用户ID
     * @param userName 用户名
     * @param map 其他信息数据 value 如果时map list的话会变成json
     */
    public SignEntity(String subject,
                      String loginName,
                      String userId,
                      String userName,
                      Map<String, Object> map) {
        this.subject = subject;
        this.loginName = loginName;
        this.userId = userId;
        this.userName = userName;
        this.map = map;
    }

    public SignEntity(String subject,
                      String loginName,
                      String userId,
                      String userName) {
        this.subject = subject;
        this.loginName = loginName;
        this.userId = userId;
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "SignEntity{" +
                "subject='" + subject + '\'' +
                ", loginName='" + loginName + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", map=" + map +
                '}';
    }
}

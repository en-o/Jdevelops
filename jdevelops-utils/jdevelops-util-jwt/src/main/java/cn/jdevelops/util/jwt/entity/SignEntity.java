package cn.jdevelops.util.jwt.entity;



/**
 * 用户登录签名办法token的数据
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-28 15:14
 */
public class SignEntity<T>{

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
     * 其他信息数据最终会变成{map: jsonObject}
     */
    T map;


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
                      T map) {
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

    public  T getMap() {
        return map;
    }

    public void setMap(T map) {
        this.map = map;
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

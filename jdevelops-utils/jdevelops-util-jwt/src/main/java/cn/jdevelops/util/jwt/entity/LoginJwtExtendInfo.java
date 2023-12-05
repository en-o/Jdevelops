package cn.jdevelops.util.jwt.entity;

/**
 * jwt扩展信息 [生成的token信息附带的map信息]
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/11/3 11:14
 */
public class LoginJwtExtendInfo<T> {

    /**
     * 登录名
     */
    String loginName;


    /**
     * 用户ID
     */
    String userId;

    /**
     * 用户No
     */
    String userNo;
    /**
     * 用户名
     */
    String userName;

    /**
     * 手机号
     */
    String phone;


    /**
     * 其他信息数据最终会变成{map: jsonObject} 如果时map list的话会变成json
     */
    T map;

    public LoginJwtExtendInfo() {
    }

    public LoginJwtExtendInfo(String loginName,
                              String userId,
                              String userNo,
                              String userName,
                              String phone) {
        this.loginName = loginName;
        this.userId = userId;
        this.userNo = userNo;
        this.userName = userName;
        this.phone = phone;
    }
    public LoginJwtExtendInfo(String loginName,
                              String userId,
                              String userNo,
                              String userName,
                              String phone,
                              T map) {
        this.loginName = loginName;
        this.userId = userId;
        this.userNo = userNo;
        this.userName = userName;
        this.phone = phone;
        this.map = map;
    }

    public LoginJwtExtendInfo(String loginName,
                              String userNo,
                              String userName,
                              T map) {
        this.loginName = loginName;
        this.userNo = userNo;
        this.userName = userName;
        this.map = map;
    }

    public LoginJwtExtendInfo(String loginName,
                              String userNo,
                              String userName) {
        this.loginName = loginName;
        this.userNo = userNo;
        this.userName = userName;
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

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public T getMap() {
        return map;
    }

    public void setMap(T map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "LoginJwtExtendInfo{" +
                "loginName='" + loginName + '\'' +
                ", userId='" + userId + '\'' +
                ", userNo='" + userNo + '\'' +
                ", userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", map=" + map +
                '}';
    }
}

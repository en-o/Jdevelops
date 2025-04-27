package cn.tannn.jdevelops.logs.enums;


/**
 * 登录类型
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/8 12:57
 */

public enum LoginType {
    /**
     * 利用端账号密码登录
     */
    WEB_ACCOUNT_PASSWORD("利用端账号密码"),

    /**
     * 利用端IP登录
     */
    WEB_ACCOUNT_IP("利用端账号IP"),

    /**
     * 管理端账号密码登录
     */
    ADMIN_ACCOUNT_PASSWORD("管理端账号密码"),

    /**
     * 微信小程序
     */
    WECHAT_APPLET("微信小程序");


    private final String description;


    LoginType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

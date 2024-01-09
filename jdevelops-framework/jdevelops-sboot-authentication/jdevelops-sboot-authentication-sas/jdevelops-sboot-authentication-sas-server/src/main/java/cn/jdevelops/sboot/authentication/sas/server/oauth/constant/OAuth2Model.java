package cn.jdevelops.sboot.authentication.sas.server.oauth.constant;

/**
 * 验证模式(grant_type)
 *  - 账号密码登录验证
 *  - 手机号登录验证
 * @author tan
 */
public class OAuth2Model {

    /**
     * 密码模式（自定义）
     */
    public static final String GRANT_TYPE_PASSWORD = "authorization_password";


    /**
     * 短信验证码模式（自定义）
     */
    public static final String GRANT_TYPE_MOBILE = "authorization_mobile";


}

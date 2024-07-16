package cn.tannn.jdevelops.utils.jwt.module;


import cn.tannn.jdevelops.utils.jwt.constant.JwtConstant;

import java.util.Objects;

/**
 * jwtbean中的cookie相关对象
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-02-10 11:46
 */
public class JCookie {

    /**
     *  是否开启从cookie中获取token(顺序为： Header -> Parameter -> Cookies)
     *  默认false
     */
    private Boolean cookie;

    /**
     * cookies key名
     * 默认 token
     */
    private String cookieKey;

    public JCookie() {
    }

    public JCookie(Boolean cookie, String cookieKey) {
        this.cookie = cookie;
        this.cookieKey = cookieKey;
    }

    public Boolean getCookie() {
        if(Objects.isNull(cookie)){
            return false;
        }
        return cookie;
    }

    public void setCookie(Boolean cookie) {
        this.cookie = cookie;
    }

    public String getCookieKey() {
        if(Objects.isNull(cookieKey)){
            return JwtConstant.TOKEN;
        }
        return cookieKey;
    }

    public void setCookieKey(String cookieKey) {
        this.cookieKey = cookieKey;
    }

    @Override
    public String toString() {
        return "JCookie{" +
                "cookie=" + cookie +
                ", cookieKey='" + cookieKey + '\'' +
                '}';
    }


}

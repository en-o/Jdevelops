package cn.jdevelops.jwtweb.util;

import javax.servlet.http.Cookie;

/**
 * cookie
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-02-10 09:52
 */
public class CookieUtil {

    /***
     *查找指定名称的 Cookie 对象
     * @param name cookie键值
     * @param cookies HttpServletRequest.getCookies())
     * @return  Cookie
     *  */
    public static Cookie findCookie(String name , Cookie[] cookies){
        if (name == null || cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }



}

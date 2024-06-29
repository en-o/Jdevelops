package cn.tannn.jdevelops.utils.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Cookie
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-10-18 09:01
 */
public class CookieUtil {

    /**
     * 获取cookies
     * @return  Cookie[]
     *  */
    public static Cookie[] cookies(HttpServletRequest request){
        return request.getCookies();
    }


    /**
     * 查找指定名称的 Cookie 对象
     * @param name cookie键值
     * @param request HttpServletRequest
     * @return  Cookie
     *  */
    public static Cookie findCookie(String name , HttpServletRequest request){
        Cookie[] cookies = cookies(request);
        return findCookie(name,cookies);
    }

    /**
     * 查找指定名称的 Cookie 对象
     * @param name cookie键值
     * @param cookies HttpServletRequest.getCookies())
     * @return  Cookie
     *  */
    public static Cookie findCookie(String name , Cookie[] cookies){
        if (name == null || cookies == null || cookies.length == 0) {
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

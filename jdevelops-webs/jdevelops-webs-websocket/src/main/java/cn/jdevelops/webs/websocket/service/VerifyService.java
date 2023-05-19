package cn.jdevelops.webs.websocket.service;


import cn.jdevelops.webs.websocket.util.SocketUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴权
 * @author tan
 */
public interface VerifyService {


    /**
     * 验证是否登录，且登录是否合法
     * @param request HttpServletRequest
     * @return  boolean ture:验证通过
     */
    boolean verifyLogin(HttpServletRequest request);

    /**
     * 验证路径是否合法
     * @param servletPath  servletRequestAttributes.getRequest().getServletPath();
     * @return boolean ture:合法
     */
    default boolean verifyPath(String servletPath ){
        return !SocketUtil.banConnection(servletPath);
    }
}

package cn.tannn.jdevelops.events.websocket.service;



import cn.tannn.jdevelops.events.websocket.util.SocketUtil;
import jakarta.servlet.http.HttpServletRequest;


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
     * @param verifyPathNo  false 关闭 VERIFY_PATH_NO 前缀【即所有只允许y存在】
     * @return boolean ture:合法
     */
    default boolean verifyPath(String servletPath , boolean verifyPathNo){
        return !SocketUtil.banConnection(servletPath, verifyPathNo);
    }
}

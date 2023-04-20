package cn.jdevelops.sboot.authentication.jwt.server;

import cn.jdevelops.sboot.authentication.jwt.util.JwtWebUtil;
import cn.jdevelops.util.jwt.core.JwtService;
import cn.jdevelops.util.jwt.entity.SignEntity;
import cn.jdevelops.util.jwt.exception.JwtException;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录工具
 * @author tan
 */
public interface LoginService  {


      Logger logger = LoggerFactory.getLogger(LoginService.class);

    /**
     * 登录
     * @param subject 用户唯一凭证(一般是登录名
     * @return 签名
     */
     default String login(String subject) {
        return login(new SignEntity(subject));
    }

    /**
     * 登录
     *  - 信息保存到redis中
     * @param subject 用户唯一凭证(一般是登录名
     * @return 签名
     */
    default String login(SignEntity subject) {
        try {
            return JwtService.generateToken(subject);
        } catch (JoseException e) {
            throw new JwtException("登录异常，请重新登录", e);
        }
    }

    /**
     * 是否登录
     *
     * @param request HttpServletRequest
     * @return true 登录中
     */
    default boolean isLogin(HttpServletRequest request) {
        return isLogin(request, false);
    }

    /**
     * 是否登录
     *
     * @param request HttpServletRequest
     * @param cookie  true 去cookie参数
     * @return true 登录中
     */
    default boolean isLogin(HttpServletRequest request, Boolean cookie) {
        try {
            String token = JwtWebUtil.getToken(request, cookie);
            return JwtService.validateTokenByBoolean(token);
        } catch (Exception e) {
            logger.warn("登录失效", e);
        }
        return false;
    }


    /**
     * 退出登录
     * @param request HttpServletRequest
     */
    default void loginOut(HttpServletRequest request) {
    }


}

package cn.tannn.jdevelops.jwt.standalone.service.impl;

import cn.tannn.jdevelops.jwt.standalone.pojo.TokenSign;
import cn.tannn.jdevelops.jwt.standalone.service.LoginService;
import cn.tannn.jdevelops.jwt.standalone.util.JwtWebUtil;
import cn.tannn.jdevelops.utils.jwt.core.JwtService;
import cn.tannn.jdevelops.utils.jwt.exception.LoginException;
import cn.tannn.jdevelops.utils.jwt.module.SignEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录工具
 *
 * @author tan
 */
public class DefLoginService implements LoginService {

    Logger logger = LoggerFactory.getLogger(DefLoginService.class);


    @Override
    public <T, S extends SignEntity<T>> TokenSign login(S subject) {
        try {
            return new TokenSign(JwtService.generateToken(subject));
        } catch (JoseException e) {
            throw new LoginException("登录异常，请重新登录", e);
        }
    }

    @Override
    public boolean isLogin(HttpServletRequest request) {
        return isLogin(request, false);
    }

    @Override
    public boolean isLogin(String subject) {
        logger.warn("jwt登录方式无法使用此方法进行登录验证");
        return false;
    }

    @Override
    public boolean isLogin(HttpServletRequest request, Boolean cookie) {
        try {
            String token = JwtWebUtil.getToken(request, cookie);
            return JwtService.validateTokenByBoolean(token);
        } catch (Exception e) {
            logger.warn("登录失效", e);
        }
        return false;
    }
}

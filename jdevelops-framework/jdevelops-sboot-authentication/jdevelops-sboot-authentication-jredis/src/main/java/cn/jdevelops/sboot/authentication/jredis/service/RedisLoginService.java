package cn.jdevelops.sboot.authentication.jredis.service;

import cn.jdevelops.sboot.authentication.jredis.entity.only.StorageUserTokenEntity;
import cn.jdevelops.sboot.authentication.jredis.entity.sign.RedisSignEntity;
import cn.jdevelops.sboot.authentication.jwt.server.LoginService;
import cn.jdevelops.sboot.authentication.jwt.util.JwtWebUtil;
import cn.jdevelops.util.jwt.core.JwtService;
import cn.jdevelops.util.jwt.entity.SignEntity;
import cn.jdevelops.util.jwt.exception.LoginException;
import cn.jdevelops.util.jwt.util.JwtContextUtil;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;


/**
 * 登录工具
 * @author tan
 */
public class RedisLoginService implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(RedisLoginService.class);


    @Override
    public String login(String subject) {
        return login(new RedisSignEntity(subject));
    }

    @Override
    public String login(SignEntity subject) {
        RedisSignEntity redisSign = new RedisSignEntity(subject);
        return login(redisSign);
    }

    /**
     * 常用
     * @param subject RedisSignEntity
     * @return 签名
     */
    public String login(RedisSignEntity subject) {
        JwtRedisService jwtRedisService = JwtContextUtil.getBean(JwtRedisService.class);
        // 生成token
        try {
            String sign = JwtService.generateToken(subject);
            StorageUserTokenEntity build = StorageUserTokenEntity.builder()
                    .userCode(subject.getSubject())
                    .alwaysOnline(subject.getAlwaysOnline())
                    .token(sign)
                    .build();
            jwtRedisService.storageUserToken(build);
            return sign;
        } catch (JoseException e) {
            throw new LoginException("登录异常，请重新登录", e);
        }
    }

    @Override
    public boolean isLogin(HttpServletRequest request) {
        return isLogin(request, false);
    }

    @Override
    public boolean isLogin(HttpServletRequest request, Boolean cookie) {
        JwtRedisService jwtRedisService = JwtContextUtil.getBean(JwtRedisService.class);
        try {
            String token = JwtWebUtil.getToken(request, cookie);
            jwtRedisService.loadUserTokenInfoByToken(token);
            return true;
        } catch (Exception e) {
            logger.warn("登录失效", e);
        }
        return false;
    }

    @Override
    public void loginOut(HttpServletRequest request) {
        JwtRedisService jwtRedisService = JwtContextUtil.getBean(JwtRedisService.class);
        try {
            String subject = JwtWebUtil.getTokenSubject(request);
            jwtRedisService.removeUserToken(subject);
        } catch (Exception e) {
            logger.warn("退出失败", e);
        }
    }
}

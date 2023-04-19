package cn.jdevelops.sboot.authentication.jredis.util;

import cn.jdevelops.sboot.authentication.jredis.entity.only.StorageUserTokenEntity;
import cn.jdevelops.sboot.authentication.jredis.entity.sign.RedisSignEntity;
import cn.jdevelops.sboot.authentication.jredis.service.JwtRedisService;
import cn.jdevelops.sboot.authentication.jwt.util.JwtWebUtil;
import cn.jdevelops.util.jwt.exception.JwtException;
import cn.jdevelops.util.jwt.util.JwtContextUtil;
import cn.jdevelops.util.jwt.core.JwtService;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;


/**
 * 登录工具
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-24 02:55
 */
public class LoginUtil {

    private static final Logger logger = LoggerFactory.getLogger(LoginUtil.class);




    /**
     * 登录
     *  - 默认有过期时间
     * @param subject 用户唯一凭证(一般是登录名
     * @return 签名
     */
    public static String login(String subject) {
        return login(new RedisSignEntity(subject));
    }

    /**
     * 登录
     *  - 信息保存到redis中
     * @param subject 用户唯一凭证(一般是登录名
     * @return 签名
     */
    public static String login(RedisSignEntity subject) {
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
            throw new JwtException("登录异常，请重新登录", e);
        }
    }


    /**
     * 是否登录
     *
     * @param request HttpServletRequest
     * @return true 登录中
     */
    public static boolean isLogin(HttpServletRequest request) {
        return isLogin(request, false);
    }

    /**
     * 是否登录
     *
     * @param request HttpServletRequest
     * @param cookie  true 去cookie参数
     * @return true 登录中
     */
    public static boolean isLogin(HttpServletRequest request, Boolean cookie) {
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

}

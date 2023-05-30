package cn.jdevelops.sboot.authentication.jredis.util;

import cn.jdevelops.sboot.authentication.jredis.entity.sign.RedisSignEntity;
import cn.jdevelops.sboot.authentication.jwt.util.JwtWebUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * RsJwtWebUtil
 * @author tan
 */
public class RsJwtWebUtil extends JwtWebUtil {

    /**
     * 解析token参数
     * @param request request
     * @return token
     */
    public static RedisSignEntity getTokenByRedisSignEntity(HttpServletRequest request) {
        return getTokenByBean(request,RedisSignEntity.class);
    }
}

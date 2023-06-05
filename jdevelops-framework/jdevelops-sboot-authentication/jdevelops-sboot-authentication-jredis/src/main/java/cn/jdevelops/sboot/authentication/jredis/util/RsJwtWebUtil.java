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
     *  获取token的参数并转化为登录时使用的实体对象
     * @param request request
     * @param ts RedisSignEntity中map对象calss
     * @return token
     */
    public static <T> RedisSignEntity<T> getTokenByRedisSignEntity(HttpServletRequest request, Class<T> ts) {
        return getTokenByBean(request,RedisSignEntity.class, ts);
    }
}

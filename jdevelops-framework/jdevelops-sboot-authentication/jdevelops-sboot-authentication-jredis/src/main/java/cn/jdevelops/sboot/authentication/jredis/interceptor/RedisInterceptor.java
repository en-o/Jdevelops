package cn.jdevelops.sboot.authentication.jredis.interceptor;

import cn.jdevelops.sboot.authentication.jredis.entity.only.StorageToken;
import cn.jdevelops.sboot.authentication.jredis.service.RedisToken;
import cn.jdevelops.sboot.authentication.jredis.service.RedisUserRole;
import cn.jdevelops.sboot.authentication.jredis.service.RedisUserState;
import cn.jdevelops.sboot.authentication.jwt.annotation.ApiPermission;
import cn.jdevelops.sboot.authentication.jwt.exception.ExpiredRedisException;
import cn.jdevelops.sboot.authentication.jwt.server.CheckTokenInterceptor;
import cn.jdevelops.spi.JoinSPI;
import cn.jdevelops.util.jwt.util.JwtContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Objects;


/**
 * redis 验证token
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-24 11:51
 */
@JoinSPI(cover = true)
public class RedisInterceptor implements CheckTokenInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(RedisInterceptor.class);

    @Override
    public boolean checkToken(String token) {
        RedisToken redisToken = JwtContextUtil.getBean(RedisToken.class);
        StorageToken storageToken = redisToken.verifyByToken(token);
        return Objects.nonNull(storageToken) && storageToken.getToken().equalsIgnoreCase(token);
    }

    @Override
    public void refreshToken(String token) {
       try {
           RedisToken redisToken = JwtContextUtil.getBean(RedisToken.class);
           redisToken.refreshByToken(token);
       }catch (Exception e){
           LOG.warn("token刷新失败:", e);
       }
    }

    @Override
    public void checkUserStatus(String token) throws ExpiredRedisException {
        RedisUserState redisUserState = JwtContextUtil.getBean(RedisUserState.class);
        redisUserState.verifyByToken(token);
    }

    @Override
    public void checkUserPermission(String token, Method method) {
        if (method.isAnnotationPresent(ApiPermission.class)) {
            ApiPermission annotation = method.getAnnotation(ApiPermission.class);
            RedisUserRole redisUserRole = JwtContextUtil.getBean(RedisUserRole.class);
            redisUserRole.verifyByToken(token,annotation);
        }
    }
}

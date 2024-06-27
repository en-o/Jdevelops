package cn.tannn.jdevelops.jwt.redis;

import cn.tannn.jdevelops.annotations.web.authentication.ApiPermission;
import cn.tannn.jdevelops.jwt.redis.entity.only.StorageToken;
import cn.tannn.jdevelops.jwt.redis.service.RedisToken;
import cn.tannn.jdevelops.jwt.redis.service.RedisUserRole;
import cn.tannn.jdevelops.jwt.redis.service.RedisUserState;
import cn.tannn.jdevelops.jwt.standalone.exception.ExpiredRedisException;
import cn.tannn.jdevelops.jwt.standalone.service.CheckTokenInterceptor;
import cn.tannn.jdevelops.spi.JoinSPI;
import cn.tannn.jdevelops.utils.jwt.util.JwtContextUtil;
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

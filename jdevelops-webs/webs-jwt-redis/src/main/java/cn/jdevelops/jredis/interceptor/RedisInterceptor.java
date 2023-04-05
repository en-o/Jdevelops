package cn.jdevelops.jredis.interceptor;

import cn.jdevelops.jredis.entity.only.StorageUserTokenEntity;
import cn.jdevelops.jwtweb.exception.ExpiredRedisException;
import cn.jdevelops.jredis.service.RedisService;
import cn.jdevelops.util.jwt.util.JwtContextUtil;
import cn.jdevelops.jwtweb.server.CheckTokenInterceptor;
import cn.jdevelops.spi.JoinSPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        RedisService redisService = JwtContextUtil.getBean(RedisService.class);
        StorageUserTokenEntity storageUserTokenEntity = redisService.verifyUserTokenByToken(token);
        return Objects.nonNull(storageUserTokenEntity) && storageUserTokenEntity.getToken().equalsIgnoreCase(token);
    }

    @Override
    public void refreshToken(String subject) {
       try {
           RedisService redisService = JwtContextUtil.getBean(RedisService.class);
           redisService.refreshUserToken(subject);
       }catch (Exception e){
           LOG.warn("token刷新失败:", e);
       }
    }

    @Override
    public void checkUserStatus(String subject) throws ExpiredRedisException {
        RedisService redisService = JwtContextUtil.getBean(RedisService.class);
        redisService.verifyUserStatus(subject);
    }
}

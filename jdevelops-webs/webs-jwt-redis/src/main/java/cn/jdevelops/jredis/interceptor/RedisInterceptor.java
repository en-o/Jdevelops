package cn.jdevelops.jredis.interceptor;

import cn.jdevelops.jredis.exception.ExpiredRedisException;
import cn.jdevelops.jredis.service.RedisService;
import cn.jdevelops.jwt.util.ContextUtil;
import cn.jdevelops.jwt.util.JwtUtil;
import cn.jdevelops.jwtweb.server.CheckTokenInterceptor;
import cn.jdevelops.spi.JoinSPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
        try {
            return JwtUtil.verity(token);
        }catch (Exception e){
            LOG.error("token验证失败:", e);
        }
        return false;
    }

    @Override
    public void refreshToken(String userCode) {
       try {
           RedisService redisService = ContextUtil.getBean(RedisService.class);
           redisService.refreshUserToken(userCode);
       }catch (Exception e){
           LOG.warn("token刷新失败:", e);
       }
    }

    @Override
    public void checkUserStatus(String userCode) throws ExpiredRedisException {
        RedisService redisService = ContextUtil.getBean(RedisService.class);
        redisService.verifyUserStatus(userCode);
    }
}

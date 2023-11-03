package cn.jdevelops.sboot.authentication.jredis.interceptor;

import cn.jdevelops.sboot.authentication.jredis.entity.only.StorageToken;
import cn.jdevelops.sboot.authentication.jredis.service.JwtRedisService;
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
        JwtRedisService jwtRedisService = JwtContextUtil.getBean(JwtRedisService.class);
        StorageToken storageToken = jwtRedisService.verifyUserTokenByToken(token);
        return Objects.nonNull(storageToken) && storageToken.getToken().equalsIgnoreCase(token);
    }

    @Override
    public void refreshToken(String subject) {
       try {
           JwtRedisService jwtRedisService = JwtContextUtil.getBean(JwtRedisService.class);
           jwtRedisService.refreshUserToken(subject);
       }catch (Exception e){
           LOG.warn("token刷新失败:", e);
       }
    }

    @Override
    public void checkUserStatus(String subject) throws ExpiredRedisException {
        JwtRedisService jwtRedisService = JwtContextUtil.getBean(JwtRedisService.class);
        jwtRedisService.verifyUserStatus(subject);
    }

    @Override
    public void checkUserPermission(String subject, Method method) throws Exception {
        if (method.isAnnotationPresent(ApiPermission.class)) {
            ApiPermission annotation = method.getAnnotation(ApiPermission.class);
            JwtRedisService jwtRedisService = JwtContextUtil.getBean(JwtRedisService.class);
            jwtRedisService.verifyUserPermission(subject,annotation);
        }
    }
}

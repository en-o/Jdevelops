package cn.jdevelops.jredis.interceptor;

import cn.jdevelops.jredis.service.RedisService;
import cn.jdevelops.jwt.util.ContextUtil;
import cn.jdevelops.jwt.util.JwtUtil;
import cn.jdevelops.jwtweb.server.CheckTokenInterceptor;
import cn.jdevelops.spi.JoinSPI;


/**
 * redis 验证token
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-24 11:51
 */
@JoinSPI(cover = true)
public class RedisInterceptor implements CheckTokenInterceptor {

    @Override
    public boolean checkToken(String token) {
        return JwtUtil.verity(token);
    }

    @Override
    public void refreshToken(String userCode) {
        RedisService redisService = ContextUtil.getBean(RedisService.class);
        redisService.refreshUserToken(userCode);
    }
}

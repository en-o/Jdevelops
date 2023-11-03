package cn.jdevelops.sboot.authentication.jredis.service;

import cn.jdevelops.api.exception.exception.TokenException;
import cn.jdevelops.sboot.authentication.jredis.entity.base.BasicsAccount;
import cn.jdevelops.sboot.authentication.jredis.entity.only.StorageUserTokenEntity;
import cn.jdevelops.sboot.authentication.jredis.entity.sign.RedisSignEntity;
import cn.jdevelops.sboot.authentication.jwt.exception.ExpiredRedisException;
import cn.jdevelops.sboot.authentication.jwt.server.LoginService;
import cn.jdevelops.sboot.authentication.jwt.util.JwtWebUtil;
import cn.jdevelops.util.jwt.core.JwtService;
import cn.jdevelops.util.jwt.entity.SignEntity;
import cn.jdevelops.util.jwt.exception.LoginException;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 登录工具
 *
 * @author tan
 */
@ConditionalOnMissingBean(LoginService.class)
public class RedisLoginService implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(RedisLoginService.class);

    @Resource
    private JwtRedisService jwtRedisService;


    /**
     * 登录
     * @param subject RedisSignEntity
     * @param <RB>    account 用户状态  @see CheckTokenInterceptor#checkUserStatus(String)
     * @return 签名
     */
    public <RB extends BasicsAccount, T> String login(RedisSignEntity<T> subject, RB account) {
        if (null != account) {
            jwtRedisService.storageUserStatus(account);
        }
        return login(subject);
    }

    /**
     * 登录
     * @param redisSubject RedisSignEntity
     * @param <RB>    account 用户状态  @see CheckTokenInterceptor#checkUserStatus(String)
     * @return 签名
     */
    public <RB extends BasicsAccount, T> String login(RedisSignEntity<T> redisSubject) {
        // 生成token
        try {
            // token
            String sign = JwtService.generateToken(redisSubject);
            // 预备登录信息给redis存储
            StorageUserTokenEntity build = StorageUserTokenEntity.builder()
                    .userCode(redisSubject.getSubject())
                    .alwaysOnline(redisSubject.getAlwaysOnline())
                    .token(sign)
                    .build();
            // 判断是否需要重复登录
            try {
                // 查询当前用户是否已经登录
                StorageUserTokenEntity loginUser = jwtRedisService.verifyUserTokenBySubject(redisSubject.getSubject());
                // 用户存在登录，判断是否需要重新登录
                if (!redisSubject.getOnlyOnline()) {
                    // 继续使用当前token
                    logger.warn("开始登录 - 登录判断 - 当前用户在线 - 继续使用当前token");
                    return loginUser.getToken();
                }
                // 重新登录
            } catch (ExpiredRedisException  | TokenException e) {
                logger.warn("开始登录 - 登录判断 - 当前用户不在线 - 执行登录流程");
            }catch (Exception e) {
                logger.warn("开始登录 - 登录判断 - 用户在线情况判断失败 - 执行登录流程");
            }
            // 存储 用户登录信息
            jwtRedisService.storageUserToken(build);
            // 返回token
            return sign;
        } catch (JoseException e) {
            throw new LoginException("登录异常，请重新登录", e);
        }
    }


    @Override
    public <T, S extends SignEntity<T>> String login(S subject) {
        RedisSignEntity<T> redisSign = new RedisSignEntity<>(subject);
        return login(redisSign);
    }

    @Override
    public boolean isLogin(HttpServletRequest request) {
        return isLogin(request, false);
    }

    @Override
    public boolean isLogin(String subject) {
        try {
            jwtRedisService.loadUserTokenInfoBySubject(subject);
            return true;
        } catch (Exception e) {
            logger.warn("登录失效", e);
        }
        return false;
    }

    @Override
    public boolean isLogin(HttpServletRequest request, Boolean cookie) {
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
        try {
            String subject = JwtWebUtil.getTokenSubject(request);
            jwtRedisService.removeUserToken(subject);
        } catch (Exception e) {
            logger.warn("退出失败", e);
        }
    }

    @Override
    public void loginOut(String subject) {
        try {
            jwtRedisService.removeUserToken(subject);
        } catch (Exception e) {
            logger.warn("退出失败", e);
        }
    }

    @Override
    public void loginOut(List<String> subject) {
        try {
            jwtRedisService.removeUserToken(subject);
        } catch (Exception e) {
            logger.warn("退出失败", e);
        }
    }
}

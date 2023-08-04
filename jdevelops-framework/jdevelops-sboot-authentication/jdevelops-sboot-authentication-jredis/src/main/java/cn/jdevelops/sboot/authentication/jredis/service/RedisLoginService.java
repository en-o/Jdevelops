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
     * 推荐使用
     * <p>
     * 用户主动登录时：
     * 1. 无登录 根据数据颁发token
     * 2. 登录过，根据 refresh 判断token是否维持原状
     *
     * @param refresh true token刷新, false token重复使用 (用户存在登录时 token时更新还是依旧)[必须要带上token才能实现]
     * @param <RB>    BasicsAccount 用户信息redis元数据@see CheckTokenInterceptor#checkUserStatus(String)
     * @param subject RedisSignEntity token元数据
     * @return 签名
     */
    public <RB extends BasicsAccount, T> String refreshLogin(boolean refresh,
                                                             RedisSignEntity<T> subject,
                                                             RB account) {

        if (null != account) {
            jwtRedisService.storageUserStatus(account);
        }

        // 判断是否需要重复登录
        try {
            // 查询当前用户是否已经登录
            StorageUserTokenEntity loginUser = jwtRedisService.verifyUserTokenBySubject(subject.getSubject());
            // 用户存在登录，判断是否需要重新登录
            if (refresh) {
                // 刷新登录
                return login(subject);
            }else {
                // 继续使用当前token
                return loginUser.getToken();
            }
        } catch (ExpiredRedisException  | TokenException e) {
            logger.warn("开始登录 - 登录判断 - 当前用户不在线 - 执行登录流程");
        }catch (Exception e) {
            logger.warn("开始登录 - 登录判断 - 用户在线情况判断失败 - 执行登录流程");
        }
        // 执行登录流程
        return login(subject);

    }


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
     * PS: 不会判断账户状态进行异常拦截 @see CheckTokenInterceptor#checkUserStatus(String)
     * @param subject RedisSignEntity
     * @return 签名
     */
    @Override
    public <T, S extends SignEntity<T>> String login(S subject) {
        // 生成token
        try {
            RedisSignEntity<T> redisSubject = (RedisSignEntity<T>) subject;
            String sign = JwtService.generateToken(redisSubject);
            StorageUserTokenEntity build = StorageUserTokenEntity.builder()
                    .userCode(redisSubject.getSubject())
                    .alwaysOnline(redisSubject.getAlwaysOnline())
                    .token(sign)
                    .build();
            jwtRedisService.storageUserToken(build);
            return sign;
        } catch (JoseException e) {
            throw new LoginException("登录异常，请重新登录", e);
        }
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
}

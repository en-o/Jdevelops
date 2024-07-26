package cn.tannn.jdevelops.jwt.redis.service;

import cn.tannn.jdevelops.annotations.web.constant.PlatformConstant;
import cn.tannn.jdevelops.exception.built.TokenException;
import cn.tannn.jdevelops.jwt.redis.entity.only.StorageToken;
import cn.tannn.jdevelops.jwt.redis.entity.sign.RedisSignEntity;
import cn.tannn.jdevelops.jwt.standalone.exception.ExpiredRedisException;
import cn.tannn.jdevelops.jwt.standalone.pojo.TokenSign;
import cn.tannn.jdevelops.jwt.standalone.service.LoginService;
import cn.tannn.jdevelops.jwt.standalone.util.JwtWebUtil;
import cn.tannn.jdevelops.utils.jwt.constant.SignState;
import cn.tannn.jdevelops.utils.jwt.core.JwtService;
import cn.tannn.jdevelops.utils.jwt.exception.LoginException;
import cn.tannn.jdevelops.utils.jwt.module.SignEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 登录工具
 *
 * @author tan
 */
public class RedisLoginService implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(RedisLoginService.class);

    @Resource
    private RedisToken redisToken;

    @Resource
    private RedisUserState redisUserState;

    @Resource
    private RedisUserRole redisUserRole;


    /**
     * 登录
     * @param  loginMeta 登录需要的元数据
     * @return 签名
     */
    public <T> TokenSign login(RedisSignEntity<T> loginMeta) {

        // 判断是否需要重复登录
        try {
            // 用户存在登录，判断是否需要重新登录
            if (Boolean.FALSE.equals(loginMeta.getOnlyOnline())) {
                // 查询当前用户是否已经登录
                StorageToken loginUser = redisToken.verify(loginMeta.getSubject());
                String token = loginUser.getToken();
                List<PlatformConstant> input = loginMeta.getPlatform();
                if(input != null){
                    List<PlatformConstant> local = JwtService.getPlatformConstantExpires(token);
                    if (!CollectionUtils.subtract(input, local).isEmpty()) {
                        throw new LoginException("存储的 platform 跟新登录的platform不一致");
                    }
                }
                // 继续使用当前token
                logger.warn("开始登录 - 登录判断 - 当前用户在线 - 继续使用当前token");
                return  new TokenSign(token, SignState.renewal);
            }
            // 重新登录
        } catch (ExpiredRedisException | TokenException e) {
            logger.warn("开始登录 - 登录判断 - 当前用户不在线 - 执行登录流程");
        }catch (LoginException e) {
            logger.warn("存储的 platform 跟新登录的platform不一致，");
        }catch (Exception e) {
            logger.warn("开始登录 - 登录判断 - 用户在线情况判断失败 - 执行登录流程");
        }

        // 登录 - 生成token
        try {
            // token
            String sign = JwtService.generateToken(loginMeta);
            // 预备登录信息给redis存储
            StorageToken build = new StorageToken(loginMeta, sign);
            // 存储 用户登录信息
            redisToken.storage(build);
            // todo 这个考虑提到前面去，每次触发都改动
            // state
            if (null != loginMeta.getUserState()) {
                redisUserState.storage(loginMeta.getUserState());
            }
            // role
            if (null != loginMeta.getUserRole()) {
                redisUserRole.storage(loginMeta.getUserRole());
            }
            // 返回token
            return new TokenSign(sign);
        } catch (JoseException e) {
            throw new LoginException("登录异常，请重新登录", e);
        }
    }


    @Override
    public <T, S extends SignEntity<T>> TokenSign login(S subject) {
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
            redisToken.load(subject);
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
            redisToken.loadByToken(token);
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
            redisToken.remove(subject);
            redisUserState.remove(subject);
            redisUserRole.remove(subject);
        } catch (Exception e) {
            logger.warn("退出失败", e);
        }
    }

    @Override
    public void loginOut(String subject) {
        try {
            redisToken.remove(subject);
            redisUserState.remove(subject);
            redisUserRole.remove(subject);
        } catch (Exception e) {
            logger.warn("退出失败", e);
        }
    }

    @Override
    public void loginOut(List<String> subject) {
        try {
            redisToken.remove(subject);
            redisUserState.remove(subject);
            redisUserRole.remove(subject);
        } catch (Exception e) {
            logger.warn("退出失败", e);
        }
    }
}

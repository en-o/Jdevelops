package cn.jdevelops.sboot.authentication.jredis.service;

import cn.jdevelops.api.exception.exception.TokenException;
import cn.jdevelops.sboot.authentication.jredis.entity.base.BasicsAccount;
import cn.jdevelops.sboot.authentication.jredis.entity.only.StorageUserTokenEntity;
import cn.jdevelops.sboot.authentication.jredis.entity.sign.RedisSignEntity;
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
     *
     * 用户主动登录时：
     *      1. 无登录 根据数据颁发token
     *      2. 登录过，根据 refresh 判断token是否维持原状
     * @param request  HttpServletRequest
     * @param refresh true token刷新, false token重复使用 (用户存在登录时 token时更新还是依旧)[必须要带上token才能实现]
     * @param <RB>  BasicsAccount 用户状态
     * @param subject RedisSignEntity
     * @return 签名
     */
    public  <RB extends BasicsAccount>  String refreshLogin(HttpServletRequest request,
                                                            boolean refresh,
                                                            RedisSignEntity subject,
                                                            RB account) {

        if(null != account){
            jwtRedisService.storageUserStatus(account);
        }
        // 请求头里的 token
        String token = null;
        try {
            token = JwtWebUtil.getToken(request);
        }catch (TokenException e){
           logger.warn("用户正在进行登录");
        }
        // 无token 重新登录
        if(null == token || token.length() == 0) {
            return login(subject);
        }else {
            // 验证是否登录过
            boolean login = isLogin(request);
            // 存在登录, 颁发新的 token
            if(login && refresh){
                return login(subject);
            }else if(login){
                // 继续使用当前token
                return token;
            }else {
                // 登录失效，重新登录
                return login(subject);
            }
        }
    }


    /**
     * 登录 主动记录用户状态
     *
     * @param subject RedisSignEntity
     * @param <RB>  account 用户状态
     * @return 签名
     */
    public  <RB extends BasicsAccount>  String login(RedisSignEntity subject, RB account) {
        if(null != account){
            jwtRedisService.storageUserStatus(account);
        }
        return login(subject);
    }

    /**
     * 登录 不主动记录用户状态
     *
     * @param subject RedisSignEntity
     * @return 签名
     */
    public String login(RedisSignEntity subject) {
        // 生成token
        try {
            String sign = JwtService.generateToken(subject);
            StorageUserTokenEntity build = StorageUserTokenEntity.builder()
                    .userCode(subject.getSubject())
                    .alwaysOnline(subject.getAlwaysOnline())
                    .token(sign)
                    .build();
            jwtRedisService.storageUserToken(build);
            return sign;
        } catch (JoseException e) {
            throw new LoginException("登录异常，请重新登录", e);
        }
    }


    /**
     * 普通登录 不建议使用
     * @param subject 用户唯一凭证(一般是登录名
     */
    @Override
    public String login(SignEntity subject) {
        RedisSignEntity redisSign = new RedisSignEntity(subject);
        return login(redisSign);
    }

    @Override
    public boolean isLogin(HttpServletRequest request) {
        return isLogin(request, false);
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
}

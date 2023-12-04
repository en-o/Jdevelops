package cn.jdevelops.sboot.authentication.jredis.service.impl;

import cn.jdevelops.api.exception.exception.TokenException;
import cn.jdevelops.sboot.authentication.jredis.constant.RedisJwtKey;
import cn.jdevelops.sboot.authentication.jredis.entity.base.BasicsAccount;
import cn.jdevelops.sboot.authentication.jredis.entity.only.StorageToken;
import cn.jdevelops.sboot.authentication.jredis.service.JwtRedisService;
import cn.jdevelops.sboot.authentication.jredis.util.ListUtil;
import cn.jdevelops.sboot.authentication.jredis.util.RedisUtil;
import cn.jdevelops.sboot.authentication.jwt.annotation.ApiPermission;
import cn.jdevelops.sboot.authentication.jwt.exception.DisabledAccountException;
import cn.jdevelops.sboot.authentication.jwt.exception.ExpiredRedisException;
import cn.jdevelops.sboot.authentication.jwt.exception.PermissionsException;
import cn.jdevelops.util.jwt.config.JwtConfig;
import cn.jdevelops.util.jwt.constant.JwtMessageConstant;
import cn.jdevelops.util.jwt.core.JwtService;
import cn.jdevelops.util.jwt.exception.LoginException;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.ObjectArrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static cn.jdevelops.api.result.emums.PermissionsExceptionCode.API_ROLE_AUTH_ERROR;
import static cn.jdevelops.api.result.emums.TokenExceptionCode.REDIS_EXPIRED_USER;
import static cn.jdevelops.api.result.emums.TokenExceptionCode.UNAUTHENTICATED;
import static cn.jdevelops.api.result.emums.UserException.BANNED_ACCOUNT;
import static cn.jdevelops.api.result.emums.UserException.EXCESSIVE_ATTEMPTS_ACCOUNT;


/**
 * redis
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-22 14:08
 */
@ConditionalOnMissingBean(JwtRedisService.class)
public class JwtJwtRedisServiceImpl implements JwtRedisService {

    private static final Logger LOG = LoggerFactory.getLogger(JwtJwtRedisServiceImpl.class);

    /**
     * reids
     */
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private JwtConfig jwtConfig;


    @Override
    public void verifyUserStatus(String subject) throws ExpiredRedisException {
        String userRedisFolder = RedisUtil.getRedisFolder(RedisJwtKey.REDIS_USER_INFO_FOLDER, subject);
        String redisUser;
        try {
            redisUser = (String) redisTemplate
                    .boundHashOps(userRedisFolder)
                    .get(subject);
        } catch (Exception e) {
            LOG.info("用户状态缓存失效");
            throw new LoginException(JwtMessageConstant.TOKEN_ERROR, e);
        }
        BasicsAccount basicsAccount = JSON.to(BasicsAccount.class, redisUser);
        if (!Objects.isNull(redisUser)) {
            if ((basicsAccount).isExcessiveAttempts()) {
                throw new DisabledAccountException(EXCESSIVE_ATTEMPTS_ACCOUNT);
            }
            if ((basicsAccount).isDisabledAccount()) {
                throw new DisabledAccountException(BANNED_ACCOUNT);
            }
        }
    }

    @Override
    public <RB extends BasicsAccount> void storageUserStatus(RB account) {
        String userRedisFolder = RedisUtil.getRedisFolder(RedisJwtKey.REDIS_USER_INFO_FOLDER, account.getSubject());
        // 处理由于是泛型对象导致其他地方继承后有问题，
        String accountJson = JSON.toJSONString(account);
        redisTemplate.boundHashOps(userRedisFolder).put(account.getSubject(), accountJson);
        // 永不过期
        redisTemplate.persist(userRedisFolder);
    }

    @Override
    public <RB extends BasicsAccount> RB loadUserStatus(String user, Class<RB> resultBean) {
        String userRedisFolder = RedisUtil.getRedisFolder(RedisJwtKey.REDIS_USER_INFO_FOLDER, user);

        String redisUser;
        try {
            redisUser = (String) redisTemplate
                    .boundHashOps(userRedisFolder)
                    .get(user);
        } catch (Exception e) {
            LOG.info("加载用户状态缓存失败");
            throw new TokenException(JwtMessageConstant.TOKEN_ERROR, e);
        }
        // 处理由于是泛型对象导致其他地方继承后有问题，
        RB basicsAccount = JSON.to(resultBean, redisUser);
        return Objects.isNull(redisUser) ? null : basicsAccount;
    }


    @Override
    public void verifyUserPermission(String subject, ApiPermission annotation) throws ExpiredRedisException {
        String userRedisFolder = RedisUtil.getRedisFolder(RedisJwtKey.REDIS_USER_INFO_FOLDER, subject);
        String redisUser;
        try {
            redisUser = (String) redisTemplate
                    .boundHashOps(userRedisFolder)
                    .get(subject);
        } catch (Exception e) {
            LOG.info("用户信息(权限判断)缓存失效");
            throw new LoginException(JwtMessageConstant.TOKEN_ERROR, e);
        }

        BasicsAccount basicsAccount = JSON.to(BasicsAccount.class, redisUser);
        if (!Objects.isNull(redisUser)) {
            String[] roles = annotation.roles();
            String[] permissions = annotation.permissions();
            if (roles != null && roles.length > 0 ) {
                if ( permissions != null && permissions.length > 0){
                    roles = new String[(roles.length + 1)+( permissions.length+1)];
                    roles = ObjectArrays.concat(roles, permissions, String.class);
                }
                if(!ListUtil.verifyList(basicsAccount.getRoles(), roles)){
                    // API_PERMISSION_AUTH_ERROR
                    throw new PermissionsException(API_ROLE_AUTH_ERROR);
                }
            }
        }
    }



}

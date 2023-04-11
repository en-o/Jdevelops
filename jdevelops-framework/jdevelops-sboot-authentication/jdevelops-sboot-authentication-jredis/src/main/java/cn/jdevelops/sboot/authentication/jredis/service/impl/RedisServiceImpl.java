package cn.jdevelops.sboot.authentication.jredis.service.impl;

import cn.jdevelops.api.exception.exception.BusinessException;
import cn.jdevelops.api.exception.exception.TokenException;
import cn.jdevelops.sboot.authentication.jredis.constant.RedisJwtKeyConstant;
import cn.jdevelops.sboot.authentication.jredis.entity.base.BasicsAccount;
import cn.jdevelops.sboot.authentication.jredis.entity.only.StorageUserTokenEntity;
import cn.jdevelops.sboot.authentication.jredis.entity.role.UserRole;
import cn.jdevelops.sboot.authentication.jredis.service.RedisService;
import cn.jdevelops.sboot.authentication.jredis.util.JwtRedisUtil;
import cn.jdevelops.sboot.authentication.jwt.exception.DisabledAccountException;
import cn.jdevelops.sboot.authentication.jwt.exception.ExpiredRedisException;
import cn.jdevelops.util.jwt.constant.JwtMessageConstant;
import cn.jdevelops.util.jwt.config.JwtConfig;
import cn.jdevelops.util.jwt.core.JwtService;
import cn.jdevelops.util.jwt.exception.JwtException;
import com.alibaba.fastjson.JSON;
import org.jose4j.jwt.MalformedClaimException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static cn.jdevelops.api.result.emums.UserExceptionEnum.*;


/**
 * redis
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-22 14:08
 */
@Service
@ConditionalOnMissingBean
public class RedisServiceImpl implements RedisService {

    private static final Logger LOG = LoggerFactory.getLogger(RedisServiceImpl.class);

    /**
     * reids
     */
    @Resource
    private  RedisTemplate<String, Object>  redisTemplate;

    @Resource
    private JwtConfig jwtConfig;



    @Override
    public void storageUserToken(StorageUserTokenEntity storageUserTokenEntity) {
        String loginRedisFolder = JwtRedisUtil.getRedisFolder(RedisJwtKeyConstant.REDIS_USER_LOGIN_FOLDER,
                storageUserTokenEntity.getUserCode());
        redisTemplate.boundHashOps(loginRedisFolder).put(storageUserTokenEntity.getUserCode(),
                storageUserTokenEntity);
        if(Boolean.TRUE.equals(storageUserTokenEntity.getAlwaysOnline())){
            // 永不过期
            redisTemplate.persist(loginRedisFolder);
        }else {
            // 设置过期时间（毫秒
            redisTemplate.expire(loginRedisFolder, jwtConfig.getLoginExpireTime(), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void refreshUserToken(String subject) {
        String loginRedisFolder = JwtRedisUtil.getRedisFolder(RedisJwtKeyConstant.REDIS_USER_LOGIN_FOLDER, subject);
        Object loginRedis = redisTemplate.boundHashOps(loginRedisFolder).get(subject);
        if (Objects.isNull(loginRedis)) {
            LOG.warn("{}用户未登录，不需要刷新", subject);
        }else {
            StorageUserTokenEntity tokenRedis = (StorageUserTokenEntity) loginRedis;
            if(Boolean.TRUE.equals(tokenRedis.getAlwaysOnline())){
                LOG.warn("{}用户是永久在线用户，不需要刷新", subject);
            }else {
                // 设置过期时间（毫秒
                redisTemplate.expire(loginRedisFolder, jwtConfig.getLoginExpireTime(), TimeUnit.MILLISECONDS);
            }
        }
    }

    @Override
    public void removeUserToken(String subject) {
        String redisFolder = JwtRedisUtil.getRedisFolder(RedisJwtKeyConstant.REDIS_USER_LOGIN_FOLDER, subject);
        redisTemplate.delete(redisFolder);
    }

    @Override
    public StorageUserTokenEntity verifyUserTokenByToken(String token) throws ExpiredRedisException {
        try {
            return verifyUserTokenBySubject(JwtService.getSubject(token));
        } catch (MalformedClaimException e) {
            throw new ExpiredRedisException(REDIS_NO_USER,e);
        } catch (JwtException e) {
            throw new ExpiredRedisException(REDIS_NO_USER,e);
        }
    }

    @Override
    public StorageUserTokenEntity verifyUserTokenBySubject(String subject) throws ExpiredRedisException {
        String loginRedisFolder = JwtRedisUtil.getRedisFolder(RedisJwtKeyConstant.REDIS_USER_LOGIN_FOLDER, subject);
        // redis 中比对 token 正确性
        StorageUserTokenEntity tokenRedis;
        Object loginRedis = redisTemplate.boundHashOps(loginRedisFolder).get(subject);
        if (Objects.isNull(loginRedis)) {
            throw new ExpiredRedisException(REDIS_EXPIRED_USER);
        }else {
            tokenRedis = (StorageUserTokenEntity) loginRedis;
        }
        return tokenRedis;
    }




    @Override
    public StorageUserTokenEntity loadUserTokenInfoByToken(String token) throws MalformedClaimException, JwtException {
        return loadUserTokenInfoBySubject(JwtService.getSubject(token));
    }

    @Override
    public StorageUserTokenEntity loadUserTokenInfoBySubject(String subject) {
        String loginRedisFolder = JwtRedisUtil.getRedisFolder(RedisJwtKeyConstant.REDIS_USER_LOGIN_FOLDER, subject);
        // redis 中比对 token 正确性
        Object loginRedis = redisTemplate.boundHashOps(loginRedisFolder).get(subject);
        if (Objects.isNull(loginRedis)) {
            throw new ExpiredRedisException(REDIS_EXPIRED_USER);
        }else {
            return (StorageUserTokenEntity) loginRedis;
        }
    }

    @Override
    public  <RB extends BasicsAccount> void verifyUserStatus(String subject) throws ExpiredRedisException{
        String userRedisFolder = JwtRedisUtil.getRedisFolder(RedisJwtKeyConstant.REDIS_USER_INFO_FOLDER, subject);
        String redisUser;
        try {
            redisUser = (String) redisTemplate
                    .boundHashOps(userRedisFolder)
                    .get(subject);
        }catch (Exception e){
            LOG.info("用户状态缓存失效");
            throw new BusinessException(JwtMessageConstant.TOKEN_ERROR,e);
        }

        Object basicsAccount = JSON.parse(redisUser);
        if(!Objects.isNull(redisUser) && basicsAccount instanceof BasicsAccount ){
            if (((RB) basicsAccount).isExcessiveAttempts()) {
                throw new DisabledAccountException(EXCESSIVE_ATTEMPTS_ACCOUNT);
            }
            if (((RB) basicsAccount).isDisabledAccount()) {
                throw new DisabledAccountException(BANNED_ACCOUNT);
            }
        }
    }

    @Override
    public <RB extends BasicsAccount> void storageUserStatus(RB account) {
        String userRedisFolder = JwtRedisUtil.getRedisFolder(RedisJwtKeyConstant.REDIS_USER_INFO_FOLDER, account.getUserCode());
        // 处理由于是泛型对象导致其他地方继承后有问题，
        String accountJson = JSON.toJSONString(account);
        redisTemplate.boundHashOps(userRedisFolder).put(account.getUserCode(), accountJson);
        // 永不过期
        redisTemplate.persist(userRedisFolder);
    }

    @Override
    public <RB extends BasicsAccount> RB loadUserStatus(String user) {
        String userRedisFolder = JwtRedisUtil.getRedisFolder(RedisJwtKeyConstant.REDIS_USER_INFO_FOLDER, user);

        String redisUser;
        try {
            redisUser = (String) redisTemplate
                    .boundHashOps(userRedisFolder)
                    .get(user);
        }catch (Exception e){
            LOG.info("加载用户状态缓存失败");
            throw new TokenException(JwtMessageConstant.TOKEN_ERROR,e);
        }
        // 处理由于是泛型对象导致其他地方继承后有问题，
        RB basicsAccount = (RB) JSON.parse(redisUser);
        return Objects.isNull(redisUser)?null: basicsAccount;
    }


    @Override
    public void storageUserRole(String user, List<String> roles) {
        // 用户角色存入 redis
        String roleRedisFolder = JwtRedisUtil.getRedisFolder(RedisJwtKeyConstant.REDIS_USER_ROLE_FOLDER, user);
        redisTemplate.boundHashOps(roleRedisFolder).put(user, roles);
        // 永不过期
        redisTemplate.persist(roleRedisFolder);
    }

    @Override
    public List<String> loadUserRole(String subject) {
        String roleRedisFolder = JwtRedisUtil.getRedisFolder(RedisJwtKeyConstant.REDIS_USER_ROLE_FOLDER, subject);
        Object roles = redisTemplate.boundHashOps(roleRedisFolder).get(subject);
        if (Objects.isNull(roles)) {
            return Collections.emptyList();
        } else {
            return (List<String>) roles;
        }
    }

    @Override
    public <T extends UserRole> void storageUserRoleInfo(String subject, List<T> roles) {
        // 用户角色存入 redis
        String roleRedisFolder = JwtRedisUtil.getRedisFolder(RedisJwtKeyConstant.REDIS_USER_ROLE_INFO_FOLDER, subject);
        redisTemplate.boundHashOps(roleRedisFolder).put(subject, roles);
        // 永不过期
        redisTemplate.persist(roleRedisFolder);
    }

    @Override
    public <T extends UserRole>  List<T> loadUserRoleInfo(String subject) {
        String roleRedisFolder = JwtRedisUtil.getRedisFolder(RedisJwtKeyConstant.REDIS_USER_ROLE_INFO_FOLDER, subject);
        Object roles = redisTemplate.boundHashOps(roleRedisFolder).get(subject);
        if (Objects.isNull(roles)) {
            return Collections.emptyList();
        } else {
            return (List<T>) roles;
        }
    }


}

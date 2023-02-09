package cn.jdevelops.jredis.service.impl;

import cn.jdevelops.jredis.constant.RedisKeyConstant;
import cn.jdevelops.jredis.entity.base.BasicsAccount;
import cn.jdevelops.jredis.entity.only.StorageUserTokenEntity;
import cn.jdevelops.jwtweb.exception.DisabledAccountException;
import cn.jdevelops.jwtweb.exception.ExpiredRedisException;
import cn.jdevelops.jredis.service.RedisService;
import cn.jdevelops.jredis.util.JwtRedisUtil;
import cn.jdevelops.jwt.bean.JwtBean;
import cn.jdevelops.jwt.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static cn.jdevelops.enums.result.UserExceptionEnum.*;


/**
 * redis
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-22 14:08
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static final Logger LOG = LoggerFactory.getLogger(RedisServiceImpl.class);

    /**
     * reids
     */
    @Resource
    private  RedisTemplate<String, Object>  redisTemplate;

    @Resource
    private JwtBean jwtBean;



    @Override
    public void storageUserToken(StorageUserTokenEntity storageUserTokenEntity) {
        String loginRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_LOGIN_FOLDER,
                storageUserTokenEntity.getUserCode());
        redisTemplate.boundHashOps(loginRedisFolder).put(storageUserTokenEntity.getUserCode(),
                storageUserTokenEntity);
        if(Boolean.TRUE.equals(storageUserTokenEntity.getAlwaysOnline())){
            // 永不过期
            redisTemplate.persist(loginRedisFolder);
        }else {
            // 设置过期时间（秒
            redisTemplate.expire(loginRedisFolder, jwtBean.getLoginExpireTime(), TimeUnit.SECONDS);
        }
    }

    @Override
    public void refreshUserToken(String subject) {
        String loginRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_LOGIN_FOLDER, subject);
        Object loginRedis = redisTemplate.boundHashOps(loginRedisFolder).get(subject);
        if (Objects.isNull(loginRedis)) {
            LOG.warn("{}用户未登录，不需要刷新", subject);
        }else {
            StorageUserTokenEntity tokenRedis = (StorageUserTokenEntity) loginRedis;
            if(Boolean.TRUE.equals(tokenRedis.getAlwaysOnline())){
                LOG.warn("{}用户是永久在线用户，不需要刷新", subject);
            }else {
                // 设置过期时间（秒
                redisTemplate.expire(loginRedisFolder, jwtBean.getLoginExpireTime(), TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public void removeUserToken(String subject) {
        String redisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_LOGIN_FOLDER, subject);
        redisTemplate.delete(redisFolder);
    }

    @Override
    public StorageUserTokenEntity verifyUserTokenByToken(String token) throws ExpiredRedisException {
        return verifyUserTokenBySubject(JwtUtil.getSubject(token));
    }

    @Override
    public StorageUserTokenEntity verifyUserTokenBySubject(String subject) throws ExpiredRedisException {
        String loginRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_LOGIN_FOLDER, subject);
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
    public StorageUserTokenEntity loadUserTokenInfoByToken(String token) {
        return loadUserTokenInfoBySubject(JwtUtil.getSubject(token));
    }

    @Override
    public StorageUserTokenEntity loadUserTokenInfoBySubject(String subject) {
        String loginRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_LOGIN_FOLDER, subject);
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
        String userRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_INFO_FOLDER, subject);
        Object redisUser = redisTemplate
                .boundHashOps(userRedisFolder)
                .get(subject);

        if(!Objects.isNull(redisUser) && redisUser instanceof BasicsAccount ){
            if (((RB) redisUser).isExcessiveAttempts()) {
                throw new DisabledAccountException(EXCESSIVE_ATTEMPTS_ACCOUNT);
            }
            if (((RB) redisUser).isDisabledAccount()) {
                throw new DisabledAccountException(BANNED_ACCOUNT);
            }
        }
    }

    @Override
    public <RB extends BasicsAccount> void storageUserStatus(RB account) {
        String userRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_INFO_FOLDER, account.getUserCode());
        redisTemplate.boundHashOps(userRedisFolder).put(account.getUserCode(), account);
        // 永不过期
        redisTemplate.persist(userRedisFolder);
    }

    @Override
    public <RB extends BasicsAccount> RB loadUserStatus(String user) {
        String userRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_INFO_FOLDER, user);
        Object redisUser = redisTemplate
                .boundHashOps(userRedisFolder)
                .get(user);
        return Objects.isNull(redisUser)?null: (RB) redisUser;
    }

    @Override
    public void storageUserRole(String user, List<String> roles) {
        // 用户角色存入 redis
        String roleRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_ROLE_FOLDER, user);
        redisTemplate.boundHashOps(roleRedisFolder).put(user, roles);
        // 永不过期
        redisTemplate.persist(roleRedisFolder);
    }

    @Override
    public List<String> loadUserRole(String subject) {
        String roleRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_ROLE_FOLDER, subject);
        Object roles = redisTemplate.boundHashOps(roleRedisFolder).get(subject);
        if (Objects.isNull(roles)) {
            return Collections.emptyList();
        } else {
            return (List<String>) roles;
        }
    }


}

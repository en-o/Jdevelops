package cn.jdevelops.jredis.service.impl;

import cn.jdevelops.jredis.constant.RedisKeyConstant;
import cn.jdevelops.jredis.entity.RedisAccount;
import cn.jdevelops.jredis.exception.ExpiredRedisException;
import cn.jdevelops.jredis.service.RedisService;
import cn.jdevelops.jredis.util.JwtRedisUtil;
import cn.jdevelops.jwt.bean.JwtBean;
import cn.jdevelops.jwt.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static cn.jdevelops.jredis.enums.RedisExceptionEnum.*;

/**
 * redis
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-22 14:08
 */
@Service
public class RedisServiceImpl implements RedisService {

    /**
     * reids
     */
    @Resource
    private  RedisTemplate<String, Object>  redisTemplate;

    @Resource
    private JwtBean jwtBean;



    @Override
    public void storageUserToken(String userCode, String token) {
        String loginRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_LOGIN_FOLDER, userCode);
        redisTemplate.boundHashOps(loginRedisFolder).put(userCode, token);
        // 设置过期时间（秒
        redisTemplate.expire(loginRedisFolder, jwtBean.getLoginExpireTime(), TimeUnit.SECONDS);
    }

    @Override
    public void refreshUserToken(String userCode) {
        String loginRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_LOGIN_FOLDER, userCode);
        // 设置过期时间（秒
        redisTemplate.expire(loginRedisFolder, jwtBean.getLoginExpireTime(), TimeUnit.SECONDS);
    }

    @Override
    public void removeUserToken(String userCode) {
        String redisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_LOGIN_FOLDER, userCode);
        redisTemplate.delete(redisFolder);
    }

    @Override
    public String verifyUserToken(String userCode) throws ExpiredRedisException {
        String loginRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_LOGIN_FOLDER, userCode);
        // redis 中比对 token 正确性
        String tokenRedis = (String) redisTemplate.boundHashOps(loginRedisFolder).get(userCode);
        if (StringUtils.isBlank(tokenRedis)) {
            throw new ExpiredRedisException(REDIS_EXPIRED_USER);
        }
        //验证token是否过期
        if(JwtUtil.verity(tokenRedis)){
            throw new ExpiredRedisException(REDIS_EXPIRED_USER);
        }

        return tokenRedis;
    }

    @Override
    public void verifyUserStatus(String userCode) throws ExpiredRedisException{
        String userRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_INFO_FOLDER, userCode);
        Object redisUser = redisTemplate
                .boundHashOps(userRedisFolder)
                .get(userCode);

        if(!Objects.isNull(redisUser) && redisUser instanceof RedisAccount ){
            if (((RedisAccount) redisUser).isDisabledAccount()) {
                throw new ExpiredRedisException(DISABLED_ACCOUNT);
            }
            if (((RedisAccount) redisUser).isExcessiveAttempts()) {
                throw new ExpiredRedisException(EXCESSIVE_ATTEMPTS_ACCOUNT);
            }
        }
    }

    @Override
    public <T> void storageUserStatus(RedisAccount<T> account) {
        String userRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_INFO_FOLDER, account.getUserCode());
        redisTemplate.boundHashOps(userRedisFolder).put(account.getUserCode(), account);
        // 永不过期
        redisTemplate.persist(userRedisFolder);
    }

    @Override
    public <T> RedisAccount<T> loadUserStatus(String user) {
        String userRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_INFO_FOLDER, user);
        Object redisUser = redisTemplate
                .boundHashOps(userRedisFolder)
                .get(user);
        return Objects.isNull(redisUser)?null:(RedisAccount)redisUser;
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
    public List<String> loadUserRole(String userCode) {
        String roleRedisFolder = JwtRedisUtil.getRedisFolder(RedisKeyConstant.REDIS_USER_ROLE_FOLDER, userCode);
        Object roles = redisTemplate.boundHashOps(roleRedisFolder).get(userCode);
        if (Objects.isNull(roles)) {
            return Collections.emptyList();
        } else {
            return (List<String>) roles;
        }
    }


}

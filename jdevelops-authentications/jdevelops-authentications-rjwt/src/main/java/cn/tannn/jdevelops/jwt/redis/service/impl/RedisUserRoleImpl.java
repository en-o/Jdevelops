package cn.tannn.jdevelops.jwt.redis.service.impl;

import cn.tannn.jdevelops.annotations.web.authentication.ApiPermission;
import cn.tannn.jdevelops.jwt.redis.constant.RedisJwtKey;
import cn.tannn.jdevelops.jwt.redis.entity.StorageUserRole;
import cn.tannn.jdevelops.jwt.redis.service.RedisUserRole;
import cn.tannn.jdevelops.jwt.redis.util.RedisUtil;
import cn.tannn.jdevelops.jwt.redis.util.UserRoleUtil;
import cn.tannn.jdevelops.jwt.standalone.exception.PermissionsException;
import cn.tannn.jdevelops.utils.jwt.config.JwtConfig;
import cn.tannn.jdevelops.utils.jwt.constant.JwtMessageConstant;
import cn.tannn.jdevelops.utils.jwt.core.JwtService;
import cn.tannn.jdevelops.utils.jwt.exception.LoginException;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static cn.tannn.jdevelops.jwt.standalone.exception.PermissionsCode.API_PERMISSION_AUTH_ERROR;
import static cn.tannn.jdevelops.jwt.standalone.exception.PermissionsCode.API_ROLE_AUTH_ERROR;


/**
 * 用户角色
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/12/5 9:47
 */
public class RedisUserRoleImpl implements RedisUserRole {

    private static final Logger LOG = LoggerFactory.getLogger(RedisUserRoleImpl.class);
    @Resource
    private JwtConfig jwtConfig;
    /**
     * reids
     */
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public void storage(StorageUserRole role) {
        String userRoleRedisFolder = RedisUtil.getRedisFolder(jwtConfig.getPrefix()
                , RedisJwtKey.REDIS_USER_ROLE_FOLDER, role.getSubject());
        // 处理由于是泛型对象导致其他地方继承后有问题，
        String accountJson = JSON.toJSONString(role);
        redisTemplate.boundHashOps(userRoleRedisFolder).put(role.getSubject(), accountJson);
        // 永不过期
        redisTemplate.persist(userRoleRedisFolder);
    }

    @Override
    public void refresh(StorageUserRole role) {
        storage(role);
    }

    @Override
    public StorageUserRole load(String subject) {
        String userRoleRedisFolder = RedisUtil.getRedisFolder(jwtConfig.getPrefix()
                ,RedisJwtKey.REDIS_USER_ROLE_FOLDER, subject);
        String redisRole;
        try {
            redisRole = (String) redisTemplate
                    .boundHashOps(userRoleRedisFolder)
                    .get(subject);
        } catch (Exception e) {
            LOG.info("加载用户角色缓存失败");
            throw new LoginException(JwtMessageConstant.TOKEN_ERROR, e);
        }
        if (Objects.isNull(redisRole)) {
            return null;
        } else {
            return JSON.to(StorageUserRole.class, redisRole);
        }
    }

    @Override
    public void verify(String subject, ApiPermission annotation) {
        StorageUserRole redisRole = load(subject);
        if (!Objects.isNull(redisRole)) {
            String[] roles = annotation.roles();
            String permissions = annotation.permissions();
            // 判断角色  -  注解里无值就不判断了
            if (roles != null && roles.length > 0
                    && (!UserRoleUtil.verifyRoles(redisRole.getRoles(), roles))) {
                throw new PermissionsException(API_ROLE_AUTH_ERROR);
            }
            // 判断权限(接口url)  -  注解里无值就不判断了
            if (permissions != null && !permissions.isEmpty()
                    && (!UserRoleUtil.verifyPermissions(redisRole.getPermissions(), permissions))) {
                throw new PermissionsException(API_PERMISSION_AUTH_ERROR);
            }
        }
    }

    @Override
    public void verifyByToken(String token, ApiPermission annotation) {
        String subjectExpires = JwtService.getSubjectExpires(token);
        verify(subjectExpires, annotation);
    }

    @Override
    public void remove(String subject) {
       try {
           String userRoleRedisFolder = RedisUtil.getRedisFolder(jwtConfig.getPrefix()
                   ,RedisJwtKey.REDIS_USER_ROLE_FOLDER, subject);
           redisTemplate.delete(userRoleRedisFolder);
       }catch (Exception e){
           LOG.error("删除"+subject+"userRole失败", e);
       }
    }

    @Override
    public void removeByToken(String token) {
        String subjectExpires = JwtService.getSubjectExpires(token);
        remove(subjectExpires);
    }

    @Override
    public void remove(List<String> subject) {
        try {
            Set<String> keys = new HashSet<>();
            for (String key : subject) {
                String userRoleRedisFolder = RedisUtil.getRedisFolder(jwtConfig.getPrefix()
                        ,RedisJwtKey.REDIS_USER_ROLE_FOLDER, key);
                keys.add(userRoleRedisFolder);
            }
            if (!keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            LOG.error("删除userRoles失败", e);
        }
    }
}

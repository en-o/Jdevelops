package cn.jdevelops.sboot.authentication.jredis.service.impl;

import cn.jdevelops.sboot.authentication.jredis.constant.RedisJwtKey;
import cn.jdevelops.sboot.authentication.jredis.entity.base.BasicsAccount;
import cn.jdevelops.sboot.authentication.jredis.service.JwtRedisService;
import cn.jdevelops.sboot.authentication.jredis.util.ListUtil;
import cn.jdevelops.sboot.authentication.jredis.util.RedisUtil;
import cn.jdevelops.sboot.authentication.jwt.annotation.ApiPermission;
import cn.jdevelops.sboot.authentication.jwt.exception.ExpiredRedisException;
import cn.jdevelops.sboot.authentication.jwt.exception.PermissionsException;
import cn.jdevelops.util.jwt.config.JwtConfig;
import cn.jdevelops.util.jwt.constant.JwtMessageConstant;
import cn.jdevelops.util.jwt.exception.LoginException;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.ObjectArrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Objects;

import static cn.jdevelops.api.result.emums.PermissionsExceptionCode.API_ROLE_AUTH_ERROR;


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

package cn.jdevelops.sboot.authentication.jredis.util;

import cn.jdevelops.sboot.authentication.jredis.entity.only.StorageUserTokenEntity;
import cn.jdevelops.sboot.authentication.jredis.service.RedisService;
import cn.jdevelops.util.jwt.entity.SignEntity;
import cn.jdevelops.util.jwt.exception.JwtException;
import cn.jdevelops.util.jwt.util.JwtContextUtil;
import cn.jdevelops.util.jwt.core.JwtService;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * token 给相关工具类
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-24 02:55
 */
public class JwtRedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtRedisUtil.class);

    /**
     * redis简单存储建立文件夹
     *
     * @param folderName 文件夹名
     * @param key        key
     * @return folderName:key
     */
    public static String getRedisFolder(String folderName, String key) {
        return folderName + ":" + key;
    }


    /**
     * 生成签名,并保存到redis中
     * - 默认有过期时间
     *
     * @param subject 用户唯一凭证(一般是登录名
     * @return 签名
     */
    public static String sign(String subject) {
        return sign(new SignEntity(subject));
    }

    /**
     * 生成签名,并保存到redis中
     *
     * @param subject 用户唯一凭证(一般是登录名
     * @return 签名
     */
    public static String sign(SignEntity subject) {
        RedisService redisService = JwtContextUtil.getBean(RedisService.class);
        // 生成token
        try {
            String sign = JwtService.generateToken(subject);
            StorageUserTokenEntity build = StorageUserTokenEntity.builder()
                    .userCode(subject.getSubject())
                    .alwaysOnline(subject.isAlwaysOnline())
                    .token(sign)
                    .build();
            redisService.storageUserToken(build);
            return sign;
        } catch (JoseException e) {
            throw new JwtException("登录异常，请重新登录",e);
        }
    }


    /**
     * token  验证token是否过期,
     * 如果未过期则刷新缓存中的token
     * @param token token
     * @return true:没过期,且刷新缓存
     */
    public static boolean verity(String token) {
        try {
            RedisService redisService = JwtContextUtil.getBean(RedisService.class);
            JwtClaims jwtClaims = JwtService.validateTokenByJwtClaims(token);
            redisService.refreshUserToken(jwtClaims.getSubject());
            return true;
        } catch (Exception e) {
            logger.error("token过期");
            return false;
        }
    }


}

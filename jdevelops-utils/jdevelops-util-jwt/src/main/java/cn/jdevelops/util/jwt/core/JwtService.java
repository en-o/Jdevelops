package cn.jdevelops.util.jwt.core;

import cn.jdevelops.api.result.emums.TokenExceptionCode;
import cn.jdevelops.util.jwt.config.JwtConfig;
import cn.jdevelops.util.jwt.entity.SignEntity;
import cn.jdevelops.util.jwt.exception.LoginException;
import cn.jdevelops.util.jwt.util.JwtContextUtil;
import com.alibaba.fastjson2.JSON;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

import static cn.jdevelops.util.jwt.constant.JwtMessageConstant.*;

/**
 *  jwt工具
 * @author tn
 * @version 1
 * @date 2020/6/19 11:52
 */

public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private static final String AUDIENCE = "jdevelops";
    private static final String LOGIN_NAME = "loginName";
    private static final String USER_ID = "userId";
    private static final String USER_NAME = "userName";
    private static final String SUBJECT = "subject";
    private static final String DATA_MAP = "map";


    static JwtConfig jwtConfig;
    static Key secret;


    static {
        try {
            jwtConfig = JwtContextUtil.getBean(JwtConfig.class);
        }catch (NullPointerException e){
            logger.warn("未配置jwt项目元数据，则开始使用默认配置");
        }
        if(Objects.isNull(jwtConfig)){
            jwtConfig = new JwtConfig(); // 默认
        }
        secret = new HmacKey(jwtConfig.getTokenSecret().getBytes(StandardCharsets.UTF_8));
    }



    /**
     *
     * 生成token
     * @param sign SignEntity
     * @return token
     * @param <D> SignEntity里的T
     * @param <T> SignEntity的子类
     * @throws JoseException JoseException
     *
     */
    public static <D,T extends SignEntity<D>> String generateToken(T  sign) throws JoseException {
        // Create the Claims, which will be the content of the JWT
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(jwtConfig.getIssuer()); //创建令牌并签名的人
        claims.setAudience(AUDIENCE); //令牌要发送给谁
        claims.setExpirationTimeMinutesInTheFuture(60 * jwtConfig.getExpireTime());  //   小时*60=分钟
        claims.setGeneratedJwtId(); // 令牌的唯一标识符
        claims.setIssuedAtToNow();  // 何时发行/创建令牌 (now)
//        claims.setNotBeforeMinutesInThePast(1); //令牌尚未生效的时间 (2 minutes ago)
        claims.setSubject(sign.getSubject()); // 主体/主体是标记的对象
        claims.setClaim(LOGIN_NAME, sign.getLoginName());
        claims.setClaim(USER_ID, sign.getUserId());
        claims.setClaim(USER_NAME, sign.getUserName());
        claims.setClaim(SUBJECT, sign.getSubject());
        if(null != sign.getMap()){
            // 判断是不是一个JAVA bean
            if(sign.getMap() instanceof String || sign.getMap() instanceof Integer){
                claims.setClaim(DATA_MAP, sign.getMap());
            }else {
                claims.setClaim(DATA_MAP, JSON.toJSONString(sign.getMap()));
            }

        }
        // 签名 JWT
        JsonWebSignature jws = getJsonWebSignature();
        // JWS的有效负载是JWT Claims的JSON内容
        jws.setPayload(claims.toJson());
        // 签署JWS并生成紧凑的序列化
        return jws.getCompactSerialization();
    }


    /**
     * 生成token
     * @param subject subject
     * @return token
     * @throws JoseException JoseException
     */
    @Deprecated
    public static String generateToken(String subject) throws JoseException {
        // 创建 JWT
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(jwtConfig.getIssuer());
        claims.setAudience(AUDIENCE);
        //   分钟
        claims.setExpirationTimeMinutesInTheFuture(60 * jwtConfig.getExpireTime());
        claims.setSubject(subject);
        // 签名 JWT
        JsonWebSignature jws = getJsonWebSignature();
        //在JWS对象上设置有效负载或签名内容
        jws.setPayload(claims.toJson());
        // 签署JWS并生成紧凑的序列化
        return jws.getCompactSerialization();
    }


    /**
     * 验证token
     * @param token token
     * @return false 无效token
     */
    public static boolean validateTokenByBoolean(String token)  {
        try {
            getJwtConsumer().processToClaims(token);
            return true;
        } catch (InvalidJwtException e) {
            try {
                if (e.hasExpired()){
                    logger.error("JWT expired at {}", e.getJwtContext().getJwtClaims().getExpirationTime());
                }
                if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID)){
                    logger.error("JWT had wrong audience: {}", e.getJwtContext().getJwtClaims().getAudience());
                }
            } catch (MalformedClaimException ex) {
                return false;
            }
        }
        return false;
    }



    /**
     * 验证token
     * @param token token
     * @return 有效返回则 JwtClaims
     * @throws MalformedClaimException InvalidJwtException
     * @throws LoginException LoginException
     */
    public static JwtClaims validateTokenByJwtClaims(String token) throws MalformedClaimException, LoginException {
        try {
            return getJwtConsumer().processToClaims(token);
        } catch (InvalidJwtException e) {
            if (e.hasExpired()){
                logger.error("JWT expired at {}", e.getJwtContext().getJwtClaims().getExpirationTime());
            }
            if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID)){
                logger.error("JWT had wrong audience: {}" , e.getJwtContext().getJwtClaims().getAudience());
            }
            throw new LoginException(TOKEN_ERROR, e);
        }
    }

    /**
     * 刷新token
     * @param token token
     * @return 新的token
     * @throws JoseException JoseException
     * @throws MalformedClaimException MalformedClaimException
     * @throws LoginException LoginException
     */
    public static String refreshToken(String token) throws JoseException, MalformedClaimException, LoginException {
        // 验证 JWT
        JwtClaims jwtClaims = validateTokenByJwtClaims(token);

        // 创建新的 JwtClaims 对象
        JwtClaims newClaims = new JwtClaims();
        newClaims.setIssuer(jwtConfig.getIssuer());
        newClaims.setAudience(AUDIENCE);
        newClaims.setSubject(jwtClaims.getSubject());
        newClaims.setExpirationTimeMinutesInTheFuture(60);

        // 从原 JWT 复制声明到新的 JwtClaims 对象中
        for (Map.Entry<String, Object> entry : jwtClaims.getClaimsMap().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            newClaims.setClaim(key, value);
        }

        // 签名新的 JWT
        JsonWebSignature jws = getJsonWebSignature();
        //在JWS对象上设置有效负载或签名内容
        jws.setPayload(newClaims.toJson());
        // 签署JWS并生成紧凑的序列化
        return jws.getCompactSerialization();
    }



    /**
     * 获得Token中的 Subject （非过期）
     *
     * @param token token
     * @return java.lang.String
     * @throws MalformedClaimException MalformedClaimException
     * @throws LoginException LoginException
     *
     */
    public static String getSubject(String token) throws MalformedClaimException, LoginException {
        // 验证 JWT
        JwtClaims jwtClaims = validateTokenByJwtClaims(token);
        return jwtClaims.getSubject();
    }


    /**
     * 获得Token中的 Subject（过期也解析）
     *
     * @param token token
     * @return java.lang.String
     * @throws LoginException LoginException
     */
    public static String getSubjectExpires(String token) throws LoginException {
        try {
            // 解析 JWT
            JwtClaims jwtClaims = parseJwt(token);
            return jwtClaims.getSubject();
        } catch (MalformedClaimException e) {
            throw new LoginException(TokenExceptionCode.TOKEN_ERROR,e);
        }
    }



    /**
     * 获得Token中的 LOGIN_NAME （过期也解析）
     *
     * @param token token
     * @return java.lang.String
     */
    public static String getLoginNameExpires(String token) {
        // 解析 JWT
        JwtClaims jwtClaims = parseJwt(token);
        return String.valueOf(jwtClaims.getClaimValue(LOGIN_NAME));
    }

    /**
     * 获得Token中的 USER_ID （过期也解析）
     *
     * @param token token
     * @return java.lang.String
     */
    public static String getUserIdExpires(String token) {
        // 解析 JWT
        JwtClaims jwtClaims = parseJwt(token);
        return String.valueOf(jwtClaims.getClaimValue(USER_ID));
    }


    /**
     * 获得Token中的 USER_NAME （过期也解析）
     *
     * @param token token
     * @return java.lang.String
     */
    public static String getUserNameExpires(String token) {
        // 解析 JWT
        JwtClaims jwtClaims = parseJwt(token);
        return String.valueOf(jwtClaims.getClaimValue(USER_NAME));
    }


    /**
     * 获取 jwt 的内容 - 过期令牌也能解析
     * @param token 获取token
     * @return JwtClaims
     */
    public static JwtClaims parseJwt(String token) {
        try {
            JwtConsumer jwtConsumer = getJwtConsumer();
            return jwtConsumer.processToClaims(token);
        }catch (InvalidJwtException e){
            return e.getJwtContext().getJwtClaims();
        }
    }



    /**
     *  A JWT is a JWS and/or a JWE with JSON claims as the payload.
     */
    private static JsonWebSignature getJsonWebSignature(){
        //创建一个新的JsonWebSignature
        JsonWebSignature jws = new JsonWebSignature();
        //在JWS上设置签名密钥
        jws.setKey(secret);
        //设置Key ID (kid)头
        jws.setKeyIdHeaderValue(UUID.randomUUID().toString());
        //在JWS上设置完整保护负载的签名算法
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        //放松secret长度要求
//        jws.setDoKeyValidation(false);
        return jws;
    }


    /**
     * 处理和验证
     * @return JwtConsumer
     */
    private static JwtConsumer getJwtConsumer(){
        // Use JwtConsumerBuilder to construct an appropriate JwtConsumer, which will
        // be used to validate and process the JWT.
        // The specific validation requirements for a JWT are context dependent, however,
        // it is typically advisable to require a (reasonable) expiration time, a trusted issuer, and
        // an audience that identifies your system as the intended recipient.
        // If the JWT is encrypted too, you need only provide a decryption key or
        // decryption key resolver to the builder.
        return new JwtConsumerBuilder()
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setAllowedClockSkewInSeconds(30) // 允许在验证基于时间的声明时留有一定的余地，以解释时钟偏差(秒)
                .setRequireSubject() // the JWT must have a subject claim
                .setExpectedIssuer(jwtConfig.getIssuer()) // whom the JWT needs to have been issued by
                .setExpectedAudience(AUDIENCE) // to whom the JWT is intended for
                .setVerificationKey(secret) // verify the signature with the public key
//                .setRelaxVerificationKeyValidation() // 绕过对解密密钥的严格检查。
                .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.HMAC_SHA256) // which is only RS256 here
                .build(); // create the JwtConsumer instance
    }

}

package cn.jdevelops.util.jwt.util;

import cn.jdevelops.util.jwt.bean.JwtBean;
import cn.jdevelops.util.jwt.entity.SignEntity;
import cn.jdevelops.util.jwt.entity.SignInit;
import cn.jdevelops.util.jwt.constant.JwtConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.awt.List;
import java.util.*;

import static cn.jdevelops.util.jwt.constant.JwtConstant.JWT_BEAN_STR;
import static cn.jdevelops.util.jwt.constant.JwtMessageConstant.*;

/**
 *  jwt工具
 * @author tn
 * @version 1
 * @date 2020/6/19 11:52
 */

public class JwtUtil {




    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);





    public static SignInit init(){
        JwtBean jwtBean = ContextUtil.getBean(JwtBean.class);
        return new SignInit(jwtBean);
    }
    public static SignInit init(long expireTime){
        JwtBean jwtBean = ContextUtil.getBean(JwtBean.class);
        return new SignInit(jwtBean, expireTime);
    }



    /**
     * 生成签名
     * 最建议使用的sign方法
     * @param signData SignEntity sign数据
     * @return token（签名）
     */
    public static String sign(SignEntity signData){

        SignInit init = init();
        // jwt
        JWTCreator.Builder builder = JWT.create();
        //jwt header
        builder.withHeader(init.getHeader());
        // 用户自定义字段
        builder.withClaim(JwtConstant.TOKEN_KEY, signData.getSubject());
        if (Objects.nonNull(signData.getMap())) {
            Iterator<String> iterator = signData.getMap().keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = signData.getMap().get(key);
                if(value instanceof Map || value instanceof List){
                    String mapJson = JSON.toJSONString(value);
                    builder.withClaim(key, mapJson);
                }else {
                    builder.withClaim(key, value+"");
                }
            }
        }
        // 签发时间
        builder.withIssuedAt(new Date());
        // 过期时间
        builder.withExpiresAt(init.getDate());
        // 发行人
        builder.withIssuer(signData.getIssuer());
        // 主题
        builder.withSubject(signData.getSubject());
        // 编号/版本
        builder.withJWTId(UUID.randomUUID().toString());
        // 生成token
        return builder.sign(init.getAlgorithm());
    }

    /**
     * 生成签名
     * subject= loginName
     * Claim.loginName = loginName
     * @param loginName   用户唯一凭证
     * @return 签名
     */
    public static String sign(String loginName){
        SignInit init = init();
        return JWT.create().withHeader(init.getHeader())
                .withClaim(JwtConstant.TOKEN_KEY,loginName)
                .withSubject(loginName)
                .withExpiresAt(init.getDate())
                .sign(init.getAlgorithm());
    }

    /**
     * 生成签名
     * subject= loginName
     * Claim.loginName = loginName
     * Claim.remark = remark
     * @param loginName  用户唯一凭证
     * @param remark 其余数据
     * @return 签名
     */
    public static String sign(String loginName, JSONObject remark){
        SignInit init = init();
        //附带username和userID生成签名
        return JWT.create().withHeader(init.getHeader())
                .withClaim(JwtConstant.TOKEN_KEY,loginName)
                .withSubject(loginName)
                .withClaim(JwtConstant.TOKEN_REMARK, remark==null?"":remark.toJSONString())
                .withExpiresAt(init.getDate())
                .sign(init.getAlgorithm());
    }



    /**
     * 生成签名 - 接口传过期时间
     * subject= loginName
     * Claim.loginName = loginName
     * Claim.remark = remark
     * @param loginName  用户唯一凭证
     * @param remark 其余数据
     * @param expireTime 过期时间/毫秒
     * @return 签名
     */
    public static String sign(String loginName, JSONObject remark, long expireTime){
        SignInit init = init(expireTime);
        //附带username和userID生成签名
        return JWT.create().withHeader(init.getHeader())
                .withClaim(JwtConstant.TOKEN_KEY,loginName)
                .withSubject(loginName)
                .withClaim(JwtConstant.TOKEN_REMARK,remark==null?"":remark.toJSONString())
                .withExpiresAt(init.getDate())
                .sign(init.getAlgorithm());
    }





    /**
     * token  验证token是否过期
     * @param token token
     * @return true:没过期
     */
    public static boolean verity(String token){
        try {
            verityForDecodedJWT(token);
            return true;
        }catch (Exception e){
            logger.error(TOKEN_EXPIRES);
            return false;
        }
    }

    /**
     * token  验证token是否过期
     * @param token token
     * @return DecodedJWT 报错则过期
     */
    public static DecodedJWT verityForDecodedJWT(String token){
        JwtBean jwtBean = ContextUtil.getBean(JwtBean.class);
        Algorithm algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    /**
     * token  验证 - 解析有效令牌
     * @param token 获取token
     * @return  Map
     */
    public static Map<String,Object> verityForMap(String token){
        try {
            JwtBean jwtBean = (JwtBean) ContextUtil.getBean(JWT_BEAN_STR);
            Algorithm algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            Map<String, Claim> claims = jwt.getClaims();
            return getClaims(claims);
        }catch (Exception e){
            throw new IllegalArgumentException(TOKEN_ERROR,e);
        }
    }

    /**
     * 获得Token中的 指定key的值
     *
     * @param token token
     * @param claim 获取token中的指定key的值 为空默认返回 subject
     * @return java.lang.String
     */
    public static String getClaim(String token, String claim) {
        DecodedJWT jwt = JWT.decode(token);
        // 只能输出String类型，如果是其他类型返回null
        if(claim==null){
            return jwt.getSubject();
        }else {
            return jwt.getClaim(claim).asString();
        }
    }


    /**
     * 获得Token中的 Subject
     *
     * @param token token
     * @return java.lang.String
     */
    public static String getSubject(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            if(Objects.isNull(jwt.getSubject())){
                throw new JWTDecodeException(TOKEN_ILLEGAL);
            }
            return jwt.getSubject();
        }catch (JWTDecodeException e){
            e.printStackTrace();
            throw new JWTDecodeException(TOKEN_ILLEGAL);
        }
    }


    /**
     * 获取 jwt 的内容 - 过期令牌也能解析
     * @param token 获取token
     * @return jwt内容
     */
    public static Map<String, Object> parseJwt(String token){
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        return getClaims(claims);
    }

    /**
     *  验证并解析
     * @param token 获取token
     * @return jwt内容
     */
    public static Map<String, Object> parseJwtVerify(String token){
        boolean verity = verity(token);
        if(!verity){
            throw new JWTVerificationException(TOKEN_EXPIRES);
        }
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        return getClaims(claims);
    }


    /**
     * 获取 jwt 的内容 - 过期令牌也能解析
     * @param token 获取token
     * @return jwt内容
     */
    public static Map<String, Claim> parseJwt2(String token){
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaims();
    }

    /**
     * 获取token
     * @param request 请求头
     * @return 返回token
     */
    public static String getToken(ServerHttpRequest request) {
        //从请求头获取token
        return Optional.ofNullable(request.getHeaders().get(JwtConstant.TOKEN))
                .map(t -> t.get(0))
                .orElse(null);

    }

    /**
     *  解析 jwt的 Map<String, Claim>  为 Map<String,Object>
     *     eg： 只要是为了解析 Claim
     * @param claims jwt.getClaims();
     * @return Map
     */
    public static Map<String,Object> getClaims( Map<String, Claim> claims){
        Iterator<String> iterator = claims.keySet().iterator();
        Map<String,Object> resMap = new HashMap<>(16);
        while(iterator.hasNext()){
            String key = iterator.next();
            Claim claim = claims.get(key);
            resMap.put(key,claim.as(Object.class));
        }
        return resMap;
    }


    /**
     *  解析 jwt的 Map<String, Claim>  为 Map<String,Object>
     * @param token  token
     * @return Map
     */
    public static  Map<String, Claim> getClaims(String token){
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaims();
    }

}

package cn.jdevelops.jwt.util;

import cn.jdevelops.enums.result.ResultCodeEnum;
import cn.jdevelops.jwt.bean.JwtBean;
import cn.jdevelops.jwt.constant.JwtConstant;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.*;

/**
 *  jwt工具
 * @author tn
 * @version 1
 * @date 2020/6/19 11:52
 */

public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private static final String JWT_BEAN_STR = "jwtBean";


    /**
     * 生成签名
     * @param loginName 登录名  用户唯一凭证
     * @return 签名
     */
    public static String sign(String loginName){
        JwtBean jwtBean = (JwtBean) ContextUtil.getBean(JWT_BEAN_STR);
        //过期时间
        Date date = new Date(System.currentTimeMillis() + jwtBean.getExpireTime());
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
        //设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return JWT.create().withHeader(header)
                .withClaim(JwtConstant.TOKEN_KEY,loginName)
                .withSubject(loginName)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 生成签名
     * @param loginName 登录名  用户唯一凭证
     * @param remark 其余数据
     * @return 签名
     */
    public static String sign(String loginName, JSONObject remark){
        JwtBean jwtBean = (JwtBean) ContextUtil.getBean(JWT_BEAN_STR);
        //过期时间
        Date date = new Date(System.currentTimeMillis() + jwtBean.getExpireTime());
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
        //设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //附带username和userID生成签名
        return JWT.create().withHeader(header)
                .withClaim(JwtConstant.TOKEN_KEY,loginName)
                .withSubject(loginName)
                .withClaim(JwtConstant.TOKEN_REMARK, remark==null?"":remark.toJSONString())
                .withExpiresAt(date)
                .sign(algorithm);
    }



    /**
     * 生成签名 - 接口传过期时间
     * @param loginName 登录名  用户唯一凭证
     * @param remark 其余数据
     * @param expireTime 过期时间/毫秒
     * @return 签名
     */
    public static String sign(String loginName, JSONObject remark, long expireTime){
        JwtBean jwtBean = (JwtBean) ContextUtil.getBean(JWT_BEAN_STR);
        //过期时间
        Date date = new Date(expireTime);
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
        //设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //附带username和userID生成签名
        return JWT.create().withHeader(header)
                .withClaim(JwtConstant.TOKEN_KEY,loginName)
                .withSubject(loginName)
                .withClaim(JwtConstant.TOKEN_REMARK,remark==null?"":remark.toJSONString())
                .withExpiresAt(date)
                .sign(algorithm);
    }


    /**
     * 生成签名
     * @param loginName 登录名  用户唯一凭证
     * @param map 其余数据
     * @return 签名
     */
    public static String sign(String loginName, Map<String, Object> map){
        JwtBean jwtBean = (JwtBean) ContextUtil.getBean(JWT_BEAN_STR);
        //过期时间
        Date date = new Date(System.currentTimeMillis() + jwtBean.getExpireTime());
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
        //设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        // jwt
        JWTCreator.Builder builder = JWT.create();

        //jwt header
        builder.withHeader(header);
        // 用户自定义字段
        builder.withClaim(JwtConstant.TOKEN_KEY,loginName);
        if(map!=null){
            Iterator<String> iterator = map.keySet().iterator();
            while(iterator.hasNext()){
                String key = iterator.next();
                builder.withClaim(key,map.get(key)+"");
            }
        }
        // 签发时间
        builder.withIssuedAt(new Date());
        // 过期时间
        builder.withExpiresAt(date);
        // 发行人
        builder.withIssuer("jdevelops");
        // 主题
        builder.withSubject(loginName);
        // 编号/版本
        builder.withJWTId(UUID.randomUUID().toString());
        // 生成token
        return builder.sign(algorithm);
    }


    /**
     * token  验证token是否过期
     * @param token token
     * @return true:没过期
     */
    public static boolean verity(String token){
        try {
            JwtBean jwtBean = (JwtBean) ContextUtil.getBean(JWT_BEAN_STR);
            Algorithm algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        }catch (Exception e){
            logger.error("token过期");
            return false;
        }

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
            throw new IllegalArgumentException(ResultCodeEnum.TOKEN_ERROR.getMessage(),e);
        }

    }

    /**
     * 获得Token中的 指定key的值
     *
     * @param token token
     * @param claim 获取token中的指定key的值  loginName or remark（是 json 需要解析）
     * @return java.lang.String
     */
    public static String getClaim(String token, String claim) {
        DecodedJWT jwt = JWT.decode(token);
        // 只能输出String类型，如果是其他类型返回null
        return jwt.getClaim(claim==null?JwtConstant.TOKEN_KEY:claim).asString();
    }


    /**
     * 获得Token中的 Subject
     *
     * @param token token
     * @return java.lang.String
     */
    public static String getSubject(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
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
            throw new JWTVerificationException("token过期");
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

}

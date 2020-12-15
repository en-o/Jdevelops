package com.detabes.jwt.util;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.detabes.jwt.bean.JwtBean;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.*;

/**
 *  jwt工具
 * @author tn
 * @version 1
 * @ClassName JwtUtil
 * @description jwt工具
 * @date 2020/6/19 11:52
 */
public class JwtUtil {

    /**
     * 生成签名
     * @param loginName 登录名  用户唯一凭证
     * @param remark 其余数据
     * @return 签名
     */
    public static String sign(String loginName, JSONObject remark){
        JwtBean jwtBean = (JwtBean) ContextUtil.getBean("jwtBean");
        //过期时间
        Date date = new Date(System.currentTimeMillis() + jwtBean.getExpireTime());
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
        //设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //附带username和userID生成签名
        return JWT.create().withHeader(header).withClaim("loginName",loginName)
                .withClaim("remark", remark==null?"":remark.toJSONString()).withExpiresAt(date).sign(algorithm);
    }




    /**
     * 生成签名
     * @param loginName 登录名  用户唯一凭证
     * @param map 其余数据
     * @return 签名
     */
    public static String sign(String loginName, Map<String, Object> map){
        JwtBean jwtBean = (JwtBean) ContextUtil.getBean("jwtBean");
        //过期时间
        Date date = new Date(System.currentTimeMillis() + jwtBean.getExpireTime());
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
        //设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //附带username和userID生成签名
        JWTCreator.Builder builder = JWT.create().withHeader(header);
        builder.withClaim("loginName",loginName);
        if(map!=null){
            Iterator<String> iterator = map.keySet().iterator();
            while(iterator.hasNext()){
                String key = iterator.next();
                builder.withClaim(key,map.get(key)+"");
            }
        }
        return builder.withExpiresAt(date).sign(algorithm);
    }


    /**
     * token  验证
     * @param token token
     * @return 真假
     */
    public static boolean verity(String token){

        JwtBean jwtBean = (JwtBean) ContextUtil.getBean("jwtBean");

        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    /**
     * token  验证 - 解析有效令牌
     * @param token 获取token
     * @return  Map
     */
    public static Map<String,Object> verityForMap(String token){

        JwtBean jwtBean = (JwtBean) ContextUtil.getBean("jwtBean");

        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            Map<String, Claim> claims = jwt.getClaims();
            return getClaims(claims);
        }catch (Exception e){
            return null;
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
        return jwt.getClaim(claim==null?"loginName":claim).asString();
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


//    /**
//     * 从 request 获取token
//     * @param request request
//     * @return token
//     */
//    public static String getToken(HttpServletRequest request) {
//        final String tokenName = "token";
//        String token = request.getHeader(tokenName);
//        if (StringUtils.isNotBlank(token)) {
//            return token;
//        }
//        token = request.getParameter(tokenName);
//        return token;
//    }

    /**
     * 获取token
     * @param request 请求头
     * @return 返回token
     */
    public static String getToken(ServerHttpRequest request) {
        final String tokenName = "token";
        //从请求头获取token
        return Optional.ofNullable(request.getHeaders().get(tokenName))
                .map(t -> t.get(0))
                .orElse(null);

    }

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

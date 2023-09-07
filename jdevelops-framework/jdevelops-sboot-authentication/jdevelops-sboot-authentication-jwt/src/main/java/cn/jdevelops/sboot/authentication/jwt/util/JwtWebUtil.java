package cn.jdevelops.sboot.authentication.jwt.util;

import cn.jdevelops.api.exception.exception.TokenException;
import cn.jdevelops.api.result.emums.TokenExceptionCode;
import cn.jdevelops.util.jwt.constant.JwtConstant;
import cn.jdevelops.util.jwt.core.JwtService;
import cn.jdevelops.util.jwt.entity.JCookie;
import cn.jdevelops.util.jwt.entity.SignEntity;
import cn.jdevelops.util.jwt.exception.LoginException;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static cn.jdevelops.util.jwt.constant.JwtMessageConstant.TOKEN_ERROR;

/**
 * @author tn
 * @version 1
 * @date 2020/12/15 18:22
 */
public class JwtWebUtil {
    /**
     * 从 request 获取token
     * 先从 header 中获取，无则从Parameter中获取
     *
     * @param request request
     * @return token
     */
    public static String getToken(HttpServletRequest request) {
        return getToken(request, new JCookie(false, JwtConstant.TOKEN));
    }


    /**
     * 从 request 获取token
     * 先从 header 中获取，无则从Parameter中获取,然后选择cookice
     *
     * @param request request
     * @param cookie  cookie true 去cookie参数
     * @return token
     */
    public static String getToken(HttpServletRequest request, Boolean cookie) {
        return getToken(request, new JCookie(cookie, JwtConstant.TOKEN));
    }

    /**
     * 从 request 获取token
     * 先从 header 中获取，无则从Parameter中获取
     *
     * @param request request
     * @param cookie  是否从cookie中获取（顺序为： Header -> Parameter -> Cookies
     * @return token
     */
    public static String getToken(HttpServletRequest request, JCookie cookie) {
        String token = request.getHeader(JwtConstant.TOKEN);
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        token = request.getParameter(JwtConstant.TOKEN);
        if (Boolean.TRUE.equals(cookie.getCookie())) {
            Optional<Cookie> findCookie = CookieUtil.findCookie(
                    cookie.getCookieKey(), request.getCookies()
            );
            token = Optional.ofNullable(token).orElse(findCookie.orElseThrow(
                    () -> new TokenException(TokenExceptionCode.UNAUTHENTICATED)
            ).getValue());
        }
        if (StringUtils.isBlank(token)) {
            throw new TokenException(TokenExceptionCode.UNAUTHENTICATED);
        }
        return token;
    }

    /**
     * 获取token中的Subject
     *
     * @param request request
     * @return String
     */
    public static String getTokenSubject(HttpServletRequest request) throws MalformedClaimException, LoginException {
        String token = JwtWebUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            throw new LoginException(TOKEN_ERROR);
        }
        return JwtService.getSubject(token);
    }

    /**
     * 获取token中的Subject（过期也能解析）
     *
     * @param request request
     * @return String
     */
    public static String getTokenSubjectExpires(HttpServletRequest request) throws LoginException {
        String token = JwtWebUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            throw new LoginException(TOKEN_ERROR);
        }
        return JwtService.getSubjectExpires(token);
    }

    /**
     * 获取token中的 Claim 参数 （过期也能解析）
     *
     * @param request  request
     * @param claimKey secret名
     * @return userCode
     */
    public static Object getTokenClaim(HttpServletRequest request, String claimKey) {
        String token = JwtWebUtil.getToken(request);
        return JwtService.parseJwt(token).getClaimsMap().get(claimKey);
    }


    /**
     * 获取token中的数据并 转化成 T类型
     *
     * @param request request
     * @param t       返回类型 （颁发时token存储的类型）
     * @param ts      t中的map对象class
     * @return t
     */
    public static <T, S> T getTokenByBean(HttpServletRequest request, Class<T> t, Class<S> ts) {
        String token = JwtWebUtil.getToken(request);
        return getTokenByBean(token, t, ts);
    }


    /**
     * 获取token中的 数据并 转化成 T类型
     *
     * @param token token
     * @param t     返回类型 （颁发时token存储的类型）
     * @param ts  t中map的对象
     * @return t
     */
    public static <T, S> T getTokenByBean(String token, Class<T> t, Class<S> ts) {
        JwtClaims jwtClaims = JwtService.parseJwt(token);
        String rawJson = jwtClaims.getRawJson();
        JSONObject jsonObject = JSON.parseObject(rawJson);
        if(ts != null){
            String mapValue = jsonObject.getString("map");
            if (null!=mapValue && mapValue.length() > 0) {
                if(JSON.isValid(mapValue)){
                    jsonObject.put("map", JSON.to(ts, mapValue));
                }else {
                    jsonObject.put("map", mapValue);
                }
            }
        }
        return JSON.to(t, jsonObject);
    }

    /**
     *  获取token的参数并转化为登录时使用的实体对象
     *
     * @param request request
     * @param ts      SignEntity中map的对象
     * @return token
     */
    public static <T,S> SignEntity<T> getTokenBySignEntity(HttpServletRequest request, Class<S> ts) {
        return getTokenByBean(request, SignEntity.class, ts);
    }

    /**
     *  获取token的参数并转化为登录时使用的实体对象 (不要map里的数据)
     * @param request request
     * @return token
     */
    public static <T> SignEntity<T> getTokenBySignEntity(HttpServletRequest request) {
        return getTokenByBean(request, SignEntity.class, null);
    }
}

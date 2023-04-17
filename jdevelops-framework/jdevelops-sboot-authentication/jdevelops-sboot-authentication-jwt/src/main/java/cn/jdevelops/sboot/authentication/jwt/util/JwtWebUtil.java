package cn.jdevelops.sboot.authentication.jwt.util;

import cn.jdevelops.api.exception.exception.TokenException;
import cn.jdevelops.api.result.emums.TokenExceptionCodeEnum;
import cn.jdevelops.util.jwt.constant.JwtConstant;
import cn.jdevelops.util.jwt.core.JwtService;
import cn.jdevelops.util.jwt.entity.JCookie;
import cn.jdevelops.util.jwt.exception.JwtException;
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
     * @param request request
     * @return token
     */
    public static String getToken(HttpServletRequest request) {
       return getToken(request,new JCookie(false, JwtConstant.TOKEN));
    }


    /**
     * 从 request 获取token
     * 先从 header 中获取，无则从Parameter中获取
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
        if(Boolean.TRUE.equals(cookie.getCookie())){
            Optional<Cookie> findCookie = CookieUtil.findCookie(
                    cookie.getCookieKey(), request.getCookies()
            );
            token = Optional.ofNullable(token).orElse(findCookie.orElseThrow(
                    () -> new TokenException(TokenExceptionCodeEnum.UNAUTHENTICATED)
            ).getValue());
        }
        if(StringUtils.isBlank(token)){
            throw new TokenException(TokenExceptionCodeEnum.UNAUTHENTICATED);
        }
        return token;
    }

    /**
     *  获取token中的Subject
     * @param request request
     * @return String
     */
    public static String getTokenSubject(HttpServletRequest request) throws MalformedClaimException, JwtException {
        String token = JwtWebUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            throw new JwtException(TOKEN_ERROR);
        }
        return JwtService.getSubject(token);
    }

    /**
     * 获取token中的 Claim 参数 （过期也能解析）
     * @param request request
     * @param claimKey secret名
     * @return userCode
     */
    public static Object getTokenClaim(HttpServletRequest request,String claimKey){
        String token = JwtWebUtil.getToken(request);
        return JwtService.parseJwt(token).getClaimsMap().get(claimKey);
    }


    /**
     * 获取token中的数据并 转化成 T类型
     * @param request request
     * @param t 返回类型 （颁发时token存储的类型）
     * @return t
     */
    public static <T> T getTokenUserInfoByRemark(HttpServletRequest request, Class<T> t ){
        String token = JwtWebUtil.getToken(request);
        JwtClaims jwtClaims = JwtService.parseJwt(token);
        String rawJson = jwtClaims.getRawJson();
        return GsonUtils.getGson().fromJson(rawJson, t);
    }


    /**
     * 获取token中的  remark 数据并 转化成 T类型
     * @param token token
     * @param t 返回类型 （颁发时token存储的类型）
     * @return t
     */
    public static  <T> T  getTokenUserInfoByRemark(String token, Class<T> t ){
        JwtClaims jwtClaims = JwtService.parseJwt(token);
        String rawJson = jwtClaims.getRawJson();
        return GsonUtils.getGson().fromJson(rawJson, t);
    }
}

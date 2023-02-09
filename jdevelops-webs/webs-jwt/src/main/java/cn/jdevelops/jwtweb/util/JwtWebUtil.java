package cn.jdevelops.jwtweb.util;

import cn.jdevelops.json.GsonUtils;
import cn.jdevelops.jwt.constant.JwtConstant;
import cn.jdevelops.jwt.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author tn
 * @version 1
 * @date 2020/12/15 18:22
 */
public class JwtWebUtil {
    /**
     * 从 request 获取token
     * @param request request
     * @return token
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(JwtConstant.TOKEN);
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        token = request.getParameter(JwtConstant.TOKEN);
        return token;
    }


    /**
     *  获取token中的Subject
     * @param request request
     * @return String
     */
    public static String getTokenSubject(HttpServletRequest request) {
        String token = JwtWebUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            return null;
        }
        try {
            return JwtUtil.getSubject(token);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取token中的 Claim 参数
     * @param request request
     * @param claimKey key名
     * @return userCode
     */
    public static String getTokenClaim(HttpServletRequest request,String claimKey){
        String token = JwtWebUtil.getToken(request);
        return JwtUtil.getClaim(token,claimKey);
    }


    /**
     * 获取token中的  remark 数据并 转化成 T类型
     * @param request request
     * @param t 返回类型 （颁发时token存储的类型）
     * @return t
     */
    public static <T> T getTokenUserInfoByRemark(HttpServletRequest request, Class<T> t ){
        String token = JwtWebUtil.getToken(request);
        Map<String, Object> loginNames = JwtUtil.parseJwt(token);
        String remark = loginNames.get(JwtConstant.TOKEN_REMARK).toString();
        return GsonUtils.getGson().fromJson(remark, t);
    }


    /**
     * 获取token中的  remark 数据并 转化成 T类型
     * @param token token
     * @param t 返回类型 （颁发时token存储的类型）
     * @return t
     */
    public static  <T> T  getTokenUserInfoByRemark(String token, Class<T> t ){
        Map<String, Object> loginNames = JwtUtil.parseJwt(token);
        String remark = loginNames.get(JwtConstant.TOKEN_REMARK).toString();
        return GsonUtils.getGson().fromJson(remark, t);
    }
}

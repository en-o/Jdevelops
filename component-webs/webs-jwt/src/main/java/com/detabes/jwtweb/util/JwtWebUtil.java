package com.detabes.jwtweb.util;

import com.detabes.json.GsonUtils;
import com.detabes.jwt.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author tn
 * @version 1
 * @ClassName JwtWebUtil
 * @description
 * @date 2020/12/15 18:22
 */
public class JwtWebUtil {
    /**
     * 从 request 获取token
     * @param request request
     * @return token
     */
    public static String getToken(HttpServletRequest request) {
        final String tokenName = "token";
        String token = request.getHeader(tokenName);
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        token = request.getParameter(tokenName);
        return token;
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
        String remark = loginNames.get("remark").toString();
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
        String remark = loginNames.get("remark").toString();
        return GsonUtils.getGson().fromJson(remark, t);
    }
}

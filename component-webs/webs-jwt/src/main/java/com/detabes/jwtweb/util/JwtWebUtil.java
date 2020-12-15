package com.detabes.jwtweb.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

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
}

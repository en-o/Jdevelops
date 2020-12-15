package com.detabes.jwtweb.server;


import com.detabes.jwt.util.JwtUtil;

/**
 * @author Tianms
 * @date 2020/4/19 11:18
 * @description 请各自的服务去实现该接口, 并注入到spring上下文中去
 */
public interface CheckTokenInterceptor {

    /**
     * token校验方法  默认不放行
     * @param token
     * @return
     */
    default boolean checkToken(String token) {
        return JwtUtil.verity(token);
    }
}

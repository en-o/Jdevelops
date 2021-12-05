package cn.jdevelop.jwtweb.server;


import cn.jdevelop.jwt.util.JwtUtil;

/**
 * 请各自的服务去实现该接口, 并注入到spring上下文中去
 * @author Tianms
 * @date 2020/4/19 11:18
 */
public interface CheckTokenInterceptor {

    /**
     * token校验方法  默认不放行
     * @param token token
     * @return boolean
     */
    default boolean checkToken(String token) {
        return JwtUtil.verity(token);
    }
}

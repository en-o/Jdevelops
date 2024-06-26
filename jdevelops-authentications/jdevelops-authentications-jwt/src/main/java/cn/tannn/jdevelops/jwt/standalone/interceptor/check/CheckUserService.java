package cn.tannn.jdevelops.jwt.standalone.interceptor.check;

import cn.tannn.jdevelops.jwt.standalone.service.CheckTokenInterceptor;

import java.lang.reflect.Method;

/**
 * 检查用户
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/26 下午3:14
 */
public class CheckUserService {


    private final CheckTokenInterceptor checkTokenInterceptor;

    public CheckUserService(CheckTokenInterceptor checkTokenInterceptor) {
        this.checkTokenInterceptor = checkTokenInterceptor;
    }

    /**
     * 检查token中的role是否跟接口的role匹配
     *
     * @param token token
     * @throws Exception 用户状态异常
     */
    public void checkUserPermission(String token, Method method) throws Exception {
        // 检查用户状态
        checkTokenInterceptor.checkUserPermission(token, method);
    }


    /**
     * 检查用户状态
     *
     * @param token token
     * @throws Exception 用户状态异常
     */
    public void checkUserStatus(String token) throws Exception {
        // 检查用户状态
        checkTokenInterceptor.checkUserStatus(token);
    }
}

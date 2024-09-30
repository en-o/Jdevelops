package cn.tannn.jdevelops.jwt.standalone.util;


import cn.tannn.jdevelops.utils.jwt.core.JwtService;
import cn.tannn.jdevelops.utils.jwt.module.LoginJwtExtendInfo;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户相关
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/7/8 下午12:35
 */
public class UserUtil {
    /**
     * 获得 LoginJwtExtendInfo
     *
     * @param request HttpServletRequest
     * @return LoginJwtExtendInfo
     */
    public static LoginJwtExtendInfo getUserInfo(HttpServletRequest request) {
        String token = JwtWebUtil.getToken(request);
        return JwtService.getLoginJwtExtendInfoExpires(token);
    }
}

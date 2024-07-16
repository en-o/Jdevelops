package cn.tannn.jdevelops.utils.jwt.constant;

/**
 * jwt错误消息
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-03-19 19:29
 */
public interface JwtMessageConstant {
    String TOKEN_ERROR = "登录失效，请重新登录";
    String TOKEN_ILLEGAL = "token是非法的";
    String TOKEN_EXPIRES = "token过期";
}

package cn.jdevelops.jwt.constant;

/**
 * jwt相关的常量
 *
 * @author tn
 * @date 2022-02-25 09:29
 */
public interface JwtConstant {

    /**
     * jwt鉴权key
     */
    String TOKEN = "token";

    /**
     * jwt密钥串中数据的主键
     */
    String TOKEN_KEY = "loginName";

    /**
     * jwt密钥串中数据的备注信息
     */
    String TOKEN_REMARK = "remark";

    /**
     *jwtBean类的 spring容器名
     */
    String JWT_BEAN_STR = "jwtBean";



}

package cn.jdevelops.sboot.authentication.jredis.constant;

/**
 * jwt登录的redis key
 * @author tnnn
 * @version V1.0
 * @date 2022-07-21 09:37
 */
public interface RedisJwtKeyConstant {

    /**
     * redis中 登录用户的文件夹名
     */
    String REDIS_USER_LOGIN_FOLDER = "login";

    /**
     * redis中 用户状态
     */
    String REDIS_USER_INFO_FOLDER = "user_status";

    /**
     * redis中 用户角色的文件夹名
     */
    String REDIS_USER_ROLE_FOLDER = "role";
    /**
     * redis中 用户角色的文件夹名
     */
    String REDIS_USER_ROLE_INFO_FOLDER = "role_info";


}

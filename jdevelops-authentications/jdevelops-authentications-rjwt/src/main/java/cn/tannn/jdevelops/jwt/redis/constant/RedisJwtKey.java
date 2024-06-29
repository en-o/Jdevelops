package cn.tannn.jdevelops.jwt.redis.constant;

/**
 * jwt登录的redis key
 * @author tnnn
 * @version V1.0
 * @date 2022-07-21 09:37
 */
public interface RedisJwtKey {

    /**
     * token 存储文件夹
     */
    String REDIS_USER_LOGIN_FOLDER = "login";

    /**
     * 用户状态 存储文件夹
     */
    String REDIS_USER_STATUS_FOLDER = "user_status";

    /**
     * 用户角色code集 存储文件夹 （list）
     */
    String REDIS_USER_ROLE_FOLDER = "user_roles";
    /**
     * 用户角色集 存储文件夹 （list）
     */
    String REDIS_USER_INFO_FOLDER = "user_info";


}

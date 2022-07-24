package cn.jdevelops.jredis.constant;

/**
 * 角色常量
 * @author tnnn
 * @version V1.0
 * @date 2022-07-21 09:37
 */
public interface RedisKeyConstant {

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
     * redis中 资源数据的文件夹名
     */
    String REDIS_RESOURCE_PATH_FOLDER = "resourcePath";


    /**
     * redis中 启用的资源数据（需要拦截
     */
    String REDIS_ENABLE_RESOURCE_FOLDER = "enable";


    /**
     * redis中 禁用的资源数据（不需要拦截
     */
    String REDIS_DISABLE_RESOURCE_FOLDER = "disable";

}

package cn.jdevelops.authentication.jredis.util;

/**
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/12/4 9:49
 */
public class RedisUtil {

    /**
     * redis简单存储建立文件夹
     *
     * @param folderName 文件夹名
     * @param key        key
     * @return folderName:key
     */
    public static String getRedisFolder(String folderName, String key) {
        return folderName + ":" + key;
    }
}

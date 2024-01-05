package cn.jdevelops.webs.websocket;

import java.util.Arrays;
import java.util.List;

/**
 * 公共常量
 *
 * @author tn
 * @date 2022-02-24 13:46
 */
public interface CommonConstant {

    /**
     * 连接路径 y
     */
    String PATH_Y = "y";

    /**
     * 连接路径 n
     */
    String PATH_N = "n";

    /**
     * 限制连接路径
     * y：需要验证token
     * n：不需要验证token
     */
    List<String> OK_PATH = Arrays.asList("y","n");


    /**
     * 不需要验证登录的socket请求
     */
    String VERIFY_PATH_NO = "/socket/n/";

    /**
     * 需要验证登录的socket请求
     */
    String VERIFY_PATH_YES = "/socket/y/";

}

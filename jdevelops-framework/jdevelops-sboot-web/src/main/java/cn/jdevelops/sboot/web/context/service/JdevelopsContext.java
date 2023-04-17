package cn.jdevelops.sboot.web.context.service;


import cn.jdevelops.sboot.web.entity.http.JdevelopsRequest;
import cn.jdevelops.sboot.web.entity.http.JdevelopsResponse;

/**
 * 上下文处理器
 *
 * @author tn
 * @version 1
 * @date 2023-02-11 12:48:16
 */
public interface JdevelopsContext {

    /**
     * 获取当前请求的Request对象
     *
     * @return JdevelopsRequest
     */
    JdevelopsRequest getRequest();


    /**
     * 获取当前请求的Response对象
     *
     * @return JdevelopsResponse
     */
    JdevelopsResponse getResponse();


    /**
     * 校验指定路由匹配符是否可以匹配成功指定路径
     *
     * @param pattern 路由匹配符
     * @param path    需要匹配的路径
     * @return see note
     */
    boolean matchPath(String pattern, String path);

    /**
     * 此上下文是否有效
     *
     * @return boolean
     */
    default boolean isValid() {
        return false;
    }

}

package cn.jdevelops.api.log.server;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 保存接口日志，需要时重写里面的保存方法即可实现数据自定义存入
 * @author tn
 * @version 1
 * @date 2020/6/15 16:09
 */
public interface ApiLogSave {

    /**
     * 接口日志输出 - 异步的
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 被调用的处理器对象，本质是一个方法对象，对反射中的Method对象进行了再包装，对方法进行封装加强，操作原始对象
     * @param ex  Exception
     * @throws Exception Exception
     */
    void saveLog(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex);

}

package cn.jdevelops.springs.context.entity;


import cn.jdevelops.springs.context.exception.JHttpException;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Request 包装类
 * @author tnnn
 * @version V1.0
 * @date 2023-02-11 12:58
 */
public interface JdevelopsRequest {


    /**
     * 获取底层源对象
     * @return see note
     */
    public Object getSource();

    /**
     * 在请求体里获取一个值
     * @param name 键
     * @return 值
     */
    public String getParam(String name);

    /**
     * 在请求体里获取一个值，值为空时返回默认值
     * @param name 键
     * @param defaultValue 值为空时的默认值
     * @return 值
     */
    public default String getParam(String name, String defaultValue) {
        String value = getParam(name);
        if(Objects.isNull(value)) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 检测提供的参数是否为指定值
     * @param name 键
     * @param value 值
     * @return 是否相等
     */
    public default boolean isParam(String name, String value) {
        String paramValue = getParam(name);
        return Objects.nonNull(paramValue) && paramValue.equals(value);
    }

    /**
     * 检测请求是否提供了指定参数
     * @param name 参数名称
     * @return 是否提供
     */
    public default boolean hasParam(String name) {
        return Objects.nonNull(getParam(name));
    }

    /**
     * 在请求体里获取一个值
     * @param name 键
     * @return 参数值
     */
    public default String getParamNotNull(String name) {
        String paramValue = getParam(name);
        if(Objects.isNull(paramValue)) {
            throw new JHttpException("缺少参数：" + name);
        }
        return paramValue;
    }


    /**
     * 在请求头里获取一个值
     * @param name 键
     * @return String
     */
    public String getHeader(String name);

    /**
     * 在请求头里获取一个值
     * @param name 键
     * @param defaultValue 值为空时的默认值
     * @return String
     */
    public default String getHeader(String name, String defaultValue) {
        String value = getHeader(name);
        if(Objects.isNull(value)) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 在Cookie作用域里获取一个值
     * @param name 键
     * @return 值
     */
    public String getCookieValue(String name);

    /**
     * 返回当前请求path (不包括上下文名称)
     * @return String
     */
    public String getRequestPath();

    /**
     * 返回当前请求path是否为指定值
     * @param path path
     * @return boolean
     */
    public default boolean isPath(String path) {
        return getRequestPath().equals(path);
    }

    /**
     * 返回当前请求的url，不带query参数，例：http://test.com/123
     * @return String
     */
    public String getUrl();

    /**
     * 返回当前请求的类型
     * @return String
     */
    public String getMethod();

    /**
     * 此请求是否为Ajax请求
     * @return boolean
     */
    public default boolean isAjax() {
        return getHeader("X-Requested-With") != null;
    }

    /**
     * 请求转发
     * @param path 请求地址
     * @param response response
     * @return Object
     */
    public default Object forward(String path, HttpServletResponse response) {
        throw new JHttpException("No implementation");
    }

}

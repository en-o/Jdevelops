package cn.jdevelops.springs.context.service;


import cn.jdevelops.springs.context.entity.JdevelopsRequest;
import cn.jdevelops.springs.context.entity.JdevelopsResponse;

/**
 * 上下文处理器
 * @author tn
 * @version 1
 * @date 2023-02-11 12:48:16
 */
public interface JdevelopsContext {

	/**
	 * 获取当前请求的Request对象
	 * @return JdevelopsRequest
	 */
	public JdevelopsRequest getRequest();


	/**
	 * 获取当前请求的Response对象
	 * @return JdevelopsResponse
	 */
	public JdevelopsResponse getResponse();



	/**
	 * 校验指定路由匹配符是否可以匹配成功指定路径
	 * @param pattern 路由匹配符
	 * @param path 需要匹配的路径
	 * @return see note
	 */
	public boolean matchPath(String pattern, String path);

	/**
	 * 此上下文是否有效
	 * @return boolean
	 */
	public default boolean isValid() {
		return false;
	}

}

package cn.jdevelops.springs.context;

import cn.jdevelops.springs.context.entity.JdevelopsRequest;
import cn.jdevelops.springs.context.entity.JdevelopsResponse;
import cn.jdevelops.springs.context.service.JdevelopsContext;

/**
 * Sa-Token 上下文持有类
 * @author kong
 *
 */
public class ContextHolder {

	/**
	 * 获取当前请求的 SaTokenContext
	 *
	 * @return see note
	 */
	public static JdevelopsContext getContext() {
		return ContextManager.getJdevelopsContext();
	}

	/**
	 * 获取当前请求的Request对象
	 *
	 * @return Request
	 */
	public static JdevelopsRequest getRequest() {
		return ContextManager.getJdevelopsContext().getRequest();
	}

	/**
	 * 获取当前请求的Response对象
	 *
	 * @return Response
	 */
	public static JdevelopsResponse getResponse() {
		return  ContextManager.getJdevelopsContext().getResponse();
	}


}

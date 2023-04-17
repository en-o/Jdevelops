package cn.jdevelops.sboot.web.context;


import cn.jdevelops.sboot.web.context.service.JdevelopsContext;
import cn.jdevelops.sboot.web.entity.http.JdevelopsRequest;
import cn.jdevelops.sboot.web.entity.http.JdevelopsResponse;

/**
 *  上下文持有类
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

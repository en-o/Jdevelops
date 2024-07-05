package cn.tannn.jdevelops.frameworks.web.starter.context;


import cn.tannn.jdevelops.frameworks.web.starter.context.service.JdevelopsContext;
import cn.tannn.jdevelops.frameworks.web.starter.entity.http.JdevelopsRequest;
import cn.tannn.jdevelops.frameworks.web.starter.entity.http.JdevelopsResponse;

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

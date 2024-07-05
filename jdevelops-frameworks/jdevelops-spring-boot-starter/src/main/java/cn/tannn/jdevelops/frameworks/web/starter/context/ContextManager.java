package cn.tannn.jdevelops.frameworks.web.starter.context;


import cn.tannn.jdevelops.frameworks.web.starter.context.service.JdevelopsContext;

/**
 * 上下文全局
 * @author tn
 * @date 2023-02-11 22:50:15
 */
public class ContextManager {

	/**
	 * 上下文Context Bean
	 */
	private volatile static JdevelopsContext jdevelopsContext;


	public static JdevelopsContext getJdevelopsContext() {
		return jdevelopsContext;
	}

	public static void setJdevelopsContext(JdevelopsContext jdevelopsContext) {
		ContextManager.jdevelopsContext = jdevelopsContext;
	}

}

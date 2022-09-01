package cn.jdevelops.aops;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获取上下文
 * @author tn
 * @version 1
 * @date 2020/6/19 15:30
 */
public class ContextUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		if (ContextUtil.applicationContext == null) {
			ContextUtil.applicationContext = applicationContext;
		}
	}

	/**
	 * 获取applicationContext
	 *
	 * @return ApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 通过name获取 Bean.
	 *
	 * @param name name
	 * @return Object
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	/**
	 * 通过class获取Bean.
	 *
	 * @param clazz clazz
	 * @return T t
	 */
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

}
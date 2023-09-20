package cn.jdevelops.data.ddss.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * get bean
 * @author tan
 */
@Component
public class DynamicSpringBeanUtil implements ApplicationContextAware {
  private static ApplicationContext ac;
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    ac = applicationContext;
  }
  public static <T> T getBean(Class<T> clazz) {
    return ac.getBean(clazz);
  }

  public static <T> T getBean(String beanName,Class<T> clazz) {
    return ac.getBean(beanName,clazz);
  }

  public static Object getBean(String beanName) {
    return ac.getBean(beanName);
  }
}

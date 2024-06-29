package cn.tannn.jdevelops.utils.jwt.util;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获取上下文
 * @author tn
 * @version 1
 *
 * @date 2020/6/19 15:30
 */
@ConditionalOnMissingBean(JwtContextUtil.class)
public class JwtContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(JwtContextUtil.applicationContext == null) {
            JwtContextUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 获取applicationContext
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**通过name获取 Bean.
     * @param name bean名字
     * @return Bean
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    /**通过class获取Bean.
     * @param clazz class
     * @param <T> 泛型
     * @return Bean
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**通过name,以及Clazz返回指定的Bean
     * @param name Bean名字
     * @param clazz class
     * @param <T> 泛型
     * @return Bean
     */
    public static <T> T getBean(String name, Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
}

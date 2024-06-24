package cn.tannn.jdevelops.jdectemplate.util;

import cn.tannn.jdevelops.jdectemplate.annotation.JdecTemplate;
import cn.tannn.jdevelops.jdectemplate.core.QueryHandler;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 代理工具类
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-23 10:29
 */
public class JdbcProxyCreator {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcProxyCreator.class);



    /**
     * 创建jdbc查询代理
     *
     * @param applicationContext ApplicationContext
     */
    public static void jdbcSelectProxy(ApplicationContext applicationContext
            , Class<? extends Annotation> annotation
            , JdbcTemplate jdbcTemplate
            , String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> interfaces = reflections.getTypesAnnotatedWith(JdecTemplate.class);

        Map<String, Object> proxyBeans = new HashMap<>();

        for (Class<?> beanClass : interfaces) {
            if (beanClass.isInterface()) {
                Method[] methods = beanClass.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(annotation)) {
                        LOG.debug("Found annotated method: {} in interface: {}", method.getName(), beanClass.getName());
                        Object proxy = createProxy(beanClass, jdbcTemplate, annotation);
                        proxyBeans.put(beanClass.getName(), proxy);
                    }
                }
            }
        }

        // 手动注册代理bean
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        for (Map.Entry<String, Object> entry : proxyBeans.entrySet()) {
            configurableApplicationContext.getBeanFactory().registerSingleton(entry.getKey(), entry.getValue());
        }
    }


    /**
     * 创建代理
     *
     * @param service 需要代理的服务
     * @return 代理类
     */
    private static Object createProxy(Class<?> service
            , JdbcTemplate jdbcTemplate
            , Class<? extends Annotation> annotation) {

        return Proxy.newProxyInstance(
                service.getClassLoader(),
                new Class<?>[]{service},
                new QueryHandler(jdbcTemplate, annotation)
        );
    }

}

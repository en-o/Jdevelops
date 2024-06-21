package cn.tannn.jdevelops.jdectemplate.util;

import cn.tannn.jdevelops.jdectemplate.core.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 代理工具类
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-23 10:29
 */
public class ProxyUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ProxyUtils.class);
    /**
     * 消费端存根 - 用户代理创建时不重复创建直接复用
     */
    private static Map<String, Object> stub = new HashMap<>();


    /**
     * 动态代理
     *
     * @param bean       需要代理的可能对象,是对其内容属性字段检测是否需要代理
     * @param annotation 指定注解
     */
    public static void jdbcSelectProxy(Object bean
            , JdbcTemplate jdbcTemplate
            , Class<? extends Annotation> annotation) {
        if (bean == null) {
            return;
        }
        // 获取标注了TConsumer注解的方法
        List<Method> methods = MethodUtils.findAnnotatedMethod(bean.getClass(), annotation);
        methods.forEach(method -> {
            LOG.info(" ===> {}", method.getName());
            // 为获取到的属性对象生成代理
            Class<?> service = method.getClass();
            // 获取全限定名称
            String serviceName = service.getCanonicalName();
            // 查询缓存
            Object consumer = stub.get(serviceName);
            if (consumer == null) {
                consumer = createProxy(service, jdbcTemplate);
                stub.put(serviceName, consumer);
            }
        });
    }


    /**
     * 创建代理
     *
     * @param service 需要代理的服务
     * @return 代理类
     */
    private static Object createProxy(Class<?> service, JdbcTemplate jdbcTemplate) {
        // 对 service进行操作时才会被触发
        return Proxy.newProxyInstance(service.getClassLoader(),
                new Class[]{service}, new QueryHandler(service, jdbcTemplate));
    }

}

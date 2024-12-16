package cn.tannn.jdevelops.jdectemplate.core;

import cn.tannn.jdevelops.annotations.jdbctemplate.JdbcTemplate;
import cn.tannn.jdevelops.jdectemplate.exception.JdbcTemplateException;
import cn.tannn.jdevelops.jdectemplate.util.AnnotationScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.tannn.jdevelops.jdectemplate.core.CreateProxy.createQueryProxy;


/**
 * 代理工具类 - 完成包扫描和代理设置
 * <p> 1. 扫描指定路径获取所有被{@link JdbcTemplate}标记的字段</p>
 * <p> 2. 为字段设置代理 </p>
 * <p> 3. 将实现对象加载到当前属性字段里去 </p>
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-23 10:29
 */
public class JdbcProxyCreator {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcProxyCreator.class);
    /**
     * 缓存 - 用户代理创建时不重复创建直接复用
     */
    private static Map<String, Object> cache_local = new HashMap<>();


    /**
     * 创建jdbc查询代理
     */
    public static void jdbcSelectProxy(ApplicationContext context
            , Class<? extends Annotation> annotation
            , org.springframework.jdbc.core.JdbcTemplate jdbcTemplate
            , String basePackage) {

        if (basePackage == null || basePackage.isEmpty()) {
            LOG.warn("dals-jdbctemplate 注解查询功能未启用, 因为没有设置包扫描路径");
            return;
        }
        // 只会扫描字段属性
        Set<BeanDefinition> beanDefinitions = AnnotationScanner.scanPackages(List.of(basePackage));
        if (beanDefinitions.isEmpty()) {
            LOG.warn("暂未查询到标记的实例, dals-jdbctemplate 注解查询功能不启用");
            return;
        }
        // 代理设置
        beanDefinitions.forEach(beanDefinition -> {
            Object bean = AnnotationScanner.getBean(context, beanDefinition);
            if (bean != null) {
                List<Field> fields = AnnotationScanner.findAnnotatedField(bean.getClass(), JdbcTemplate.class);
                fields.forEach(field -> {
                    LOG.info("dals-jdbctemplate field ===> {}", field.getName());
                    try {
                        // 为获取到的属性对象生成 jdbcTemplate代理
                        // 属性对象
                        Class<?> service = field.getType();
                        // 属性对象全限定名称
                        String serviceName = service.getCanonicalName();
                        // 查询缓存 - 由于会被多个地方用到，所有这里处理以就行了，后面再用直接取
                        Object proxy = cache_local.get(serviceName);
                        if (proxy == null) {
                            // 创建代理
                            proxy = createQueryProxy(service, jdbcTemplate, annotation);
                            cache_local.put(serviceName, proxy);
                        }
                        // 使用的关键
                        // 将实现对象加载到当前属性字段里去
                        field.setAccessible(true);
                        field.set(bean, proxy);
                    } catch (IllegalAccessException e) {
                        throw new JdbcTemplateException(e, 6, "proxy_create_error");
                    }
                });
            } else {
                LOG.warn("beanDefinition find error {}", beanDefinition.getBeanClassName());
            }
        });

    }


}

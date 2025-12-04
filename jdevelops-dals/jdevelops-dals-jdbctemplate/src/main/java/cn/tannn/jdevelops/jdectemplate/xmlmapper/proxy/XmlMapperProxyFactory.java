package cn.tannn.jdevelops.jdectemplate.xmlmapper.proxy;

import cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlMapper;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.registry.XmlMapperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * XML Mapper 代理工厂
 * <p>为 @XmlMapper 注解的接口创建动态代理</p>
 *
 * @author tnnn
 */
public class XmlMapperProxyFactory {

    private static final Logger LOG = LoggerFactory.getLogger(XmlMapperProxyFactory.class);

    /**
     * 创建 XML Mapper 代理对象
     *
     * @param mapperInterface XML Mapper 接口
     * @param registry XML Mapper 注册器
     * @param <T> 接口类型
     * @return 代理对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Class<T> mapperInterface, XmlMapperRegistry registry) {
        if (!mapperInterface.isInterface()) {
            throw new IllegalArgumentException("XmlMapper must be an interface: " + mapperInterface.getName());
        }

        // 获取命名空间
        String namespace = getNamespace(mapperInterface);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating XML Mapper proxy for interface: {}, namespace: {}",
                    mapperInterface.getName(), namespace);
        }

        // 创建代理
        return (T) Proxy.newProxyInstance(
                mapperInterface.getClassLoader(),
                new Class<?>[]{mapperInterface},
                new XmlMapperProxyInterceptor(namespace, registry)
        );
    }

    /**
     * 获取命名空间
     * <p>优先使用 @XmlMapper 注解的 namespace 属性</p>
     * <p>如果未指定，则使用接口的全限定名</p>
     */
    private static String getNamespace(Class<?> mapperInterface) {
        XmlMapper annotation = mapperInterface.getAnnotation(XmlMapper.class);
        if (annotation != null && !annotation.namespace().isEmpty()) {
            return annotation.namespace();
        }
        return mapperInterface.getName();
    }
}

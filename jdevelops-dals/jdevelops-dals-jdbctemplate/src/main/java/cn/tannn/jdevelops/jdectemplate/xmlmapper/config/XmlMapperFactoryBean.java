package cn.tannn.jdevelops.jdectemplate.xmlmapper.config;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.proxy.XmlMapperProxyFactory;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.registry.XmlMapperRegistry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * XML Mapper FactoryBean
 * <p>用于创建 Mapper 接口的代理实例</p>
 *
 * @author tnnn
 */
public class XmlMapperFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> mapperInterface;

    @Autowired
    private XmlMapperRegistry xmlMapperRegistry;

    public XmlMapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() throws Exception {
        return (T) XmlMapperProxyFactory.createProxy(mapperInterface, xmlMapperRegistry);
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}

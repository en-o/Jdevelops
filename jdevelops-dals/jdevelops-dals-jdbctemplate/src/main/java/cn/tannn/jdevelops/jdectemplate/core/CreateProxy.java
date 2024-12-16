package cn.tannn.jdevelops.jdectemplate.core;

import cn.tannn.jdevelops.annotations.jdbctemplate.Query;
import org.reflections.Reflections;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;
import java.util.Set;

/**
 * 创建代理
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/12/13 15:17
 */
public class CreateProxy {

    /**
     * 创建查询代理
     *
     * @param service 需要代理的服务
     * @return 代理类
     */
    public static Object createQueryProxy(Class<?> service
            , org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        if (service.isInterface()) {
            // 使用Reflections扫描接口实现类
            Class<?> implementation = getFirstImplementation(service);
            return Proxy.newProxyInstance(
                    implementation != null
                            ? implementation.getClassLoader()
                            : service.getClassLoader(),
                    new Class<?>[]{service},
                    new JdbcProxyMethodInterceptor(implementation, jdbcTemplate, Query.class)
            );
        } else {
            // 创建 Enhancer 实例
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(service);  // 设置目标类
            enhancer.setCallback(new JdbcCglibMethodInterceptor(jdbcTemplate, Query.class));  // 设置 MethodInterceptor
            return enhancer.create();
        }
    }


    /**
     * 获取接口的首个实现类
     *
     * @param interfaceClass 接口类
     * @return 实现类
     */
    public static Class<?> getFirstImplementation(
            Class<?> interfaceClass) {
        // 自动获取包名
        String packageName = getPackageName(interfaceClass);
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> implementations = reflections.getSubTypesOf((Class<Object>) interfaceClass);
        return implementations.isEmpty()
                ? null
                : implementations.iterator().next();
    }

    /**
     * 安全获取包名的方法
     * @param clazz 要获取包名的类
     * @return 包名
     */
    public static String getPackageName(Class<?> clazz) {
        if (clazz == null) {
            return "";
        }

        Package pkg = clazz.getPackage();
        if (pkg != null) {
            return pkg.getName();
        }
        // 如果Package为null，从完全限定名中提取
        String fullyQualifiedName = clazz.getName();
        int lastDotIndex = fullyQualifiedName.lastIndexOf('.');
        return lastDotIndex > 0 ? fullyQualifiedName.substring(0, lastDotIndex) : "";
    }
}

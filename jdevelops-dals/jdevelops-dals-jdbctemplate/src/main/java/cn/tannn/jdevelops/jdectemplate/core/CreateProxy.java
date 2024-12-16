package cn.tannn.jdevelops.jdectemplate.core;

import cn.tannn.jdevelops.annotations.jdbctemplate.Query;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

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
            return Proxy.newProxyInstance(
                    service.getClassLoader(),
                    new Class<?>[]{service},
                    new JdbcProxyMethodInterceptor(service, jdbcTemplate, Query.class)
            );
        } else {
            // 创建 Enhancer 实例
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(service);  // 设置目标类
            enhancer.setCallback(new JdbcCglibMethodInterceptor(jdbcTemplate, Query.class));  // 设置 MethodInterceptor
            return enhancer.create();
        }
    }

}

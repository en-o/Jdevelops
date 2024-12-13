package cn.tannn.jdevelops.jdectemplate.core;

import java.lang.annotation.Annotation;
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
            , org.springframework.jdbc.core.JdbcTemplate jdbcTemplate
            , Class<? extends Annotation> annotation) {
        return Proxy.newProxyInstance(
                service.getClassLoader(),
                new Class<?>[]{service},
                new QueryHandler(service, jdbcTemplate, annotation)
        );
    }

}

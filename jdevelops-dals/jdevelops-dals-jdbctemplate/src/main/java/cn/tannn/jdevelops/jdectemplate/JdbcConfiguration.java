package cn.tannn.jdevelops.jdectemplate;

import cn.tannn.jdevelops.jdectemplate.annotation.Query;
import cn.tannn.jdevelops.jdectemplate.util.ProxyUtils;
import cn.tannn.jdevelops.jdectemplate.util.ScanPackagesUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Set;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/21 下午3:07
 */
@ConditionalOnWebApplication
public class JdbcConfiguration {

//    @Bean
//    public QueryAop queryAop() {
//        return new QueryAop();
//    }

    @Value("${jdevelops.jdectemplate.scanPackages:cn.tannn.jdevelops}")
    private String[] scanPackages;


    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner jdbcDalsRunner(ApplicationContext context, JdbcTemplate jdbcTemplate) {
        return x -> scanConsumerAndProxy(scanPackages, context, jdbcTemplate);
    }

    /**
     * 扫描拥有注解的类并设置动态代理
     *
     * @param scanPackages 扫描指定路径的类
     */
    private void scanConsumerAndProxy(String[] scanPackages
            , ApplicationContext context
            , JdbcTemplate jdbcTemplate) {
        //
        if (scanPackages == null || scanPackages.length == 0) {
            throw new RuntimeException("jdbc_dals_scan_package_not_setting");
        }
        Set<BeanDefinition> beanDefinitions = ScanPackagesUtils.scanPackages(scanPackages);
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Object bean = ScanPackagesUtils.getBean(context, beanDefinition);
            ProxyUtils.jdbcSelectProxy(bean, jdbcTemplate, Query.class);
        }
    }
}

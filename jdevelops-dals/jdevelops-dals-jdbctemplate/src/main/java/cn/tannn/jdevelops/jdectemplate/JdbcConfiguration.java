package cn.tannn.jdevelops.jdectemplate;

import cn.tannn.jdevelops.annotations.jdbctemplate.Query;
import cn.tannn.jdevelops.jdectemplate.util.JdbcProxyCreator;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/21 下午3:07
 */
@ConditionalOnWebApplication
public class JdbcConfiguration {


    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner jdbcDalsRunner(ApplicationContext context, JdbcTemplate jdbcTemplate) {
        return x -> JdbcProxyCreator.jdbcSelectProxy(context, Query.class, jdbcTemplate, "cn.tannn");
    }

}

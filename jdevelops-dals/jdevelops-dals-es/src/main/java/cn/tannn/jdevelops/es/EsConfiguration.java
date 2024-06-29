package cn.tannn.jdevelops.es;

import cn.tannn.jdevelops.es.config.ElasticProperties;
import cn.tannn.jdevelops.es.core.ElasticService;
import cn.tannn.jdevelops.es.core.ElasticServiceImpl;
import cn.tannn.jdevelops.es.schema.CreateElasticsearchMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * es 注入
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/18 下午1:21
 */
@ConditionalOnWebApplication
public class EsConfiguration {

    @Bean
    public ElasticProperties elasticProperties(){
        return new ElasticProperties();
    }


    @Bean
    public ElasticService elasticService(){
        return new ElasticServiceImpl();
    }

    @Bean
    public CreateElasticsearchMapping createElasticsearchMapping(){
        return new CreateElasticsearchMapping();
    }
}

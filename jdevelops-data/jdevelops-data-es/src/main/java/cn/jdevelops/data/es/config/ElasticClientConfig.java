package cn.jdevelops.data.es.config;

import cn.jdevelops.data.es.exception.ElasticsearchException;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * @author tan
 */
@AutoConfiguration
public class ElasticClientConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticClientConfig.class);

    @Value("${spring.elasticsearch.uris}")
    private String hosts;

    @Value("${spring.elasticsearch.username}")
    private String userName;

    @Value("${spring.elasticsearch.password}")
    private String passWord;

    @Autowired
    private ElasticProperties elasticProperties;

    @Bean
    @ConditionalOnMissingBean(ElasticsearchClient.class)
    public ElasticsearchClient elasticsearchClient() {
        HttpHost[] httpHosts = toHttpHost();
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY, new UsernamePasswordCredentials(userName, passWord));

        RestClientBuilder builder = RestClient.builder(httpHosts);
        builder.setRequestConfigCallback(
                requestConfigBuilder -> requestConfigBuilder
                        .setSocketTimeout(elasticProperties.getSocketTimeout())
                        .setConnectTimeout(elasticProperties.getConnectTimeout())
                        .setConnectionRequestTimeout(elasticProperties.getConnectionRequestTimeout())
        );
        builder.setHttpClientConfigCallback(hc -> hc.
                setDefaultCredentialsProvider(credentialsProvider)
        );
        RestClient restClient = builder.build();
        // 创建ObjectMapper实例并注册JavaTimeModule
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        JacksonJsonpMapper jsonpMapper = new JacksonJsonpMapper(objectMapper);
        ElasticsearchTransport transport = new RestClientTransport(restClient, jsonpMapper);
        return new ElasticsearchClient(transport);
    }

    private HttpHost[] toHttpHost() {
        if (!StringUtils.hasLength(hosts)) {
            throw new ElasticsearchException("invalid elasticsearch configuration. elasticsearch.hosts不能为空！");
        }
        // 多个IP逗号隔开
        String[] hostArray = hosts.split(",");
        HttpHost[] httpHosts = new HttpHost[hostArray.length];
        HttpHost httpHost;
        for (int i = 0; i < hostArray.length; i++) {
            String[] strings = hostArray[i].split(":");
            httpHost = new HttpHost(strings[0], Integer.parseInt(strings[1]), "http");
            httpHosts[i] = httpHost;
        }
        return httpHosts;
    }

}

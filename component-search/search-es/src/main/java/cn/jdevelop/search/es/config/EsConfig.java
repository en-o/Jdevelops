package cn.jdevelop.search.es.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;

/**
 * @author l
 */
@Slf4j
@Configuration
public class EsConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(EsConfig.class);

	@Value("${spring.elasticsearch.rest.uris}")
	private String uris;
	/**
	 * 连接超时时间
	 */
	@Value("${spring.elasticsearch.rest.connection-timeout}")
	private int connectTimeOut;
	/**
	 * 最大连接数
	 */
	@Value("${spring.elasticsearch.rest.max-connection}")
	private int maxConnection;

	private RestHighLevelClient restHighLevelClient;

	private Environment environment;

	public EsConfig(Environment environment) {
		this.environment = environment;
	}

	/**
	 * Bean name default  函数名字
	 *
	 * @return restHighLevelClient
	 */
	@Bean(name = "restHighLevelClient")
	public RestHighLevelClient restHighLevelClient() {
		LOGGER.info("Elasticsearch初始化开始。。。。。");
		try {
			if (restHighLevelClient != null) {
				restHighLevelClient.close();
			}
			if (StringUtils.isBlank(uris)) {
				log.error("spring.elasticsearch.rest.uris is blank");
				return null;
			}
			String userName = environment.getProperty("spring.elasticsearch.rest.username");
			String password = environment.getProperty("spring.elasticsearch.rest.password");

			//解析yml中的配置转化为HttpHost数组
			String[] uriArr = uris.split(",");
			HttpHost[] httpHostArr = new HttpHost[uriArr.length];
			int i = 0;
			for (String uri : uriArr) {
				if (StringUtils.isEmpty(uris)) {
					continue;
				}
				try {
					//拆分出ip和端口号
					String[] split = uri.split(":");
					String host = split[0];
					String port = split[1];
					HttpHost httpHost = new HttpHost(host, Integer.parseInt(port), "http");
					httpHostArr[i++] = httpHost;
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
			RestClientBuilder builder = RestClient.builder(httpHostArr);
			// 判断，如果未配置用户名，则进行无用户名密码连接，配置了用户名，则进行用户名密码连接
			if (!StringUtils.isEmpty(userName)) {
				final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider.setCredentials(AuthScope.ANY,
						//es账号密码
						new UsernamePasswordCredentials(userName, password));
				builder.setHttpClientConfigCallback(httpClientBuilder -> {
					httpClientBuilder.setMaxConnTotal(maxConnection);
					httpClientBuilder.disableAuthCaching();
					httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
					return httpClientBuilder;
				});
			}
			builder.setRequestConfigCallback(requestConfigBuilder -> {
				requestConfigBuilder.setConnectTimeout(connectTimeOut);
				return requestConfigBuilder;
			});
			restHighLevelClient = new RestHighLevelClient(builder);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		LOGGER.info("Elasticsearch初始化成功并结束。。。。。");
		return restHighLevelClient;
	}
}

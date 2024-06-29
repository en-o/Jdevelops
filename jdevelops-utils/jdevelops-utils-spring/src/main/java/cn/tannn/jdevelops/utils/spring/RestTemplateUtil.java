package cn.tannn.jdevelops.utils.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * @author tnnn
 */
public class RestTemplateUtil {
	private static final int STATUS_CODE_SUCCESS = 200;

	private static final Logger LOG = LoggerFactory.getLogger(RestTemplateUtil.class);

	/**
	 * restTemplate 发送post请求
	 * @param restTemplate restTemplate
	 * @param url url
	 * @param json json
	 * @return ResponseEntity
	 */
	public static ResponseEntity<String> getRestTemplatePost(RestTemplate restTemplate,String url,String json){
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> requestEntity = new HttpEntity<>(json,headers);
		ResponseEntity<String> entity = restTemplate.postForEntity(url, requestEntity, String.class);
		LOG.debug("ResponseEntity : {}", entity);
		return entity;
	}
	/**
	 * restTemplate 发送get请求
	 * @param restTemplate  restTemplate
	 * @param url url
	 * @param json json
	 * @return ResponseEntity
	 */
	public static ResponseEntity<String> getRestTemplateGet(RestTemplate restTemplate,String url,String json){
		ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class,json);
		LOG.debug("ResponseEntity : {}", entity);
		return entity;
	}

}

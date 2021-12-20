package cn.jdevelops.spring.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tnnn
 */
@Slf4j
public class RestTemplateUtil {
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
		log.info("ResponseEntity="+ JSON.toJSONString(entity));
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
		log.info("ResponseEntity="+ JSON.toJSONString(entity));
		return entity;
	}

	/**
	 *
	 * @param entity entity
	 * @return Map
	 */
	public static Map<String, Object> parseResponseEntity(ResponseEntity<String> entity) {
		Map<String,Object> map = new HashMap<>(100);
		Integer code = entity.getStatusCodeValue();
		if(entity.getStatusCodeValue()==200) {
			JSONObject obj = JSON.parseObject(entity.getBody());
			map.put("code", code+"");
			map.put("data", obj);
		}
		log.info(JSON.toJSONString(map));
		return map;
	}



}
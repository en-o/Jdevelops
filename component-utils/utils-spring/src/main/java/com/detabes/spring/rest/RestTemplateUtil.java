package com.detabes.spring.rest;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RestTemplateUtil {
	/**
	 * restTemplate 发送post请求
	 * @param restTemplate
	 * @param url
	 * @param json
	 * @return
	 */
	public static ResponseEntity<String> getRestTemplatePost(RestTemplate restTemplate,String url,String json){
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> requestEntity = new HttpEntity<String>(json,headers);
		ResponseEntity<String> entity = restTemplate.postForEntity(url, requestEntity, String.class);
		log.info("ResponseEntity="+ JSONObject.toJSONString(entity));
		return entity;
	}
	/**
	 * restTemplate 发送get请求
	 * @param restTemplate
	 * @param url
	 * @param json
	 * @return
	 */
	public static ResponseEntity<String> getRestTemplateGet(RestTemplate restTemplate,String url,String json){
		ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class,json);
		log.info("ResponseEntity="+ JSONObject.toJSONString(entity));
		return entity;
	}

	public static Map<String, Object> parseResponseEntity(ResponseEntity<String> entity) {
		Map<String,Object> map = new HashMap<String,Object>();
		Integer code = entity.getStatusCodeValue();
		if(entity.getStatusCodeValue()==200) {
			JSONObject obj = JSONObject.parseObject(entity.getBody());
			map.put("code", code+"");
			map.put("data", obj);
		}
		log.info(JSONObject.toJSONString(map));
		return map;
	}



}
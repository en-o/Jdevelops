package cn.jdevelop.spring.http.resttemplate;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

/**
 * restTemplate不抛出异常
 * @author 谭宁
 */
public class NoErrorResultErrorHandler extends DefaultResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) {
    }
}
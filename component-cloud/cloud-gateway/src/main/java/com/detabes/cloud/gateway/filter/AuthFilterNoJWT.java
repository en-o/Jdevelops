package com.detabes.cloud.gateway.filter;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;

/**
 * 测试拦截器
 * @author tn
 * @version 1
 * @ClassName AuthFilter
 * @description  测试拦截器
 * @date 2020/8/11 12:08
 */
@Component
@Slf4j
public class AuthFilterNoJWT extends AbstractGatewayFilterFactory<AuthFilterNoJWT.AuthFilterProperties> {


    public AuthFilterNoJWT() {
        super(AuthFilterProperties.class);
    }

    /**
     * 默认需要放行的地址
     */
    private static final List<String> STATIC_URI_LIST = Arrays.asList("/**/*.html","/**/*.css","/**/*.js","/**/v2/api-docs-ext","/v2/**","/**/v2/api-docs","/webjars/**");

    /**
     * @description  鉴权过滤器参数实体类映射
     * @author  ccm
     * @date 2020/8/7 9:34
     */
    @Data
    public static class AuthFilterProperties {
        private List<String> excludePatterns; //放行的接口路径
    }

    @Autowired
    private AntPathMatcher antPathMatcher;


    @Override
    public GatewayFilter apply(AuthFilterProperties authFilterProperties) {
        return (exchange,  chain) -> {
            //获取请求路径
            ServerHttpRequest request = exchange.getRequest();
            String uri = request.getURI().getPath();

            log.info("鉴权过滤器接收到请求,uri={}",uri);

            //放行静态资源路径
            for(String staticUri: STATIC_URI_LIST) {
                if(antPathMatcher.match(staticUri,uri)) {
                    log.info("uri={},该路径为静态资源路径,放行",uri);
                    return chain.filter(exchange);
                }
            }


            //放行配置了不需要做鉴权的路径
            if(!CollectionUtils.isEmpty(authFilterProperties.getExcludePatterns())) {
                for(String excludePattern: authFilterProperties.getExcludePatterns()) {
                    if(antPathMatcher.match(excludePattern,uri)) {
                        log.info("uri={},该路径为配置了无需鉴权,放行",uri);
                        return chain.filter(exchange);
                    }
                }
            }
            return chain.filter(exchange);
        };
    }

}
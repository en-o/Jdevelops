package cn.jdevelops.cloud.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import cn.jdevelops.jwt.util.JwtUtil;
import cn.jdevelops.result.result.ResultVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * jwt登录验证
 * @author tn
 * @version 1
 * @date 2020/8/11 12:08
 */
@Component
@Slf4j
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.AuthFilterProperties> {


    public AuthFilter() {
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

            //                    //从请求头获取token
            String token = JwtUtil.getToken(request);
            //
            if(StringUtils.isEmpty(token)) {    //token为空,鉴权不通过,直接响应给客户端
                return this.response(exchange, ResultVO.fail("请先登录"));
            }
            //                    //解析token的接口进行鉴权 !JwtUtil.verity(token)
            Map<String, Object> stringClaimMap = JwtUtil.verityForMap(token);
            if(null==stringClaimMap) {    //鉴权失败,直接响应给客户端
                return this.response(exchange, ResultVO.fail("登录信息错误"));
            }

            //走到这里,代表token有效,将token注入到请求头中,路由转发到微服务后,微服务接口就可以拿到该token对应的用户id
            ServerHttpRequest.Builder builder = request.mutate();
            builder.headers(headers -> {
                headers.set("loginName",stringClaimMap!=null?stringClaimMap.get("loginName")+"":"");
            });
            return chain.filter(exchange);

        };
    }


    /**
     * @description  响应给客户端结果集
     * @author ccm
     * @date  2020/8/30 14:53
     * @param exchange ServerWebExchange
     * @param resultSet 返回值
     * @return  Mono
     */
    public Mono<Void> response(ServerWebExchange exchange, ResultVO resultSet) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        String responseBodyString = JSONObject.toJSONString(resultSet, SerializerFeature.WriteMapNullValue);
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(responseBodyString.getBytes());
        return response.writeWith(Mono.just(bodyDataBuffer));
    }


}
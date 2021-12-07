package cn.jdevelops.cloud.gateway.factory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 *  自定义一个谓词工厂类  - 存放swagger 搜索栏 的下拉项目的中文名
 *  SwaggerSelectNameRoutePredicateFactory
 * @author tn
 * @version 1
 * @date 2020/8/16 1:18
 */
@Component
public class DocNameRoutePredicateFactory extends AbstractRoutePredicateFactory<DocNameConfig> {


    public DocNameRoutePredicateFactory() {
        super(DocNameConfig.class);
    }



    @Override
    public Predicate<ServerWebExchange> apply(DocNameConfig config) {
        String name = config.getName();
        return exchange -> StringUtils.isNotEmpty(name);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("name");
    }
}

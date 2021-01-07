package com.detabes.cloud.gateway.sawagger.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author tn
 * @version 1
 * @ClassName SwaggerResourceConfig
 * @description 集成swagger文档
 * @date 2020/8/11 23:21
 */
@Slf4j
@Component
@Primary
@AllArgsConstructor
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    private final RouteLocator routeLocator;
    private final GatewayProperties gatewayProperties;


    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId())).forEach(route -> {
            route.getPredicates().stream()
                    .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                    .forEach(predicateDefinition -> {
                        SwaggerResource swaggerResource = swaggerResource(route.getId(),
                                route.getPredicates()
                        );
                        if(swaggerResource!=null){resources.add(swaggerResource);}
                    });
        });

        return resources;
    }

    /**
     * 返回为空时 就表示 该项目不需要生成doc (DocName = noSwagger )
     * @param name
     * @param predicateDefinition
     * @return
     */
    private SwaggerResource swaggerResource(String name,List<PredicateDefinition> predicateDefinition) {
        AtomicReference<String> location = new AtomicReference<>("");

        SwaggerResource swaggerResource = new SwaggerResource();
        //搜索栏的下拉数据
        swaggerResource.setName(name);

        for (PredicateDefinition it:predicateDefinition) {
            if(("Path").equalsIgnoreCase(it.getName())){
                location.set(it.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                        .replace("**", "v2/api-docs"));
            }
            if("DocName".equalsIgnoreCase(it.getName())){
                String selectName = it.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0");
                if(StringUtils.isNotEmpty(selectName)&&!"noSwagger".equals(selectName)){
                    swaggerResource.setName(selectName);
                }else{
                    return null;
                }
            }
        }
        log.info("name:{},location:{}",name,location);

        swaggerResource.setLocation(location.get());
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}

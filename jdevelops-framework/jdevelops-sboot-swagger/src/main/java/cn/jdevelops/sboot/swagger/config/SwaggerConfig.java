package cn.jdevelops.sboot.swagger.config;

import cn.jdevelops.sboot.swagger.core.util.RandomUtil;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;

import static cn.jdevelops.sboot.swagger.core.util.SwaggerUtil.basePackages;

/***
 * 创建Swagger配置
 * @see <a href="https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/blob/master/knife4j-springdoc-openapi-demo/src/main/java/com/xiaominfo/knife4j/demo/config/SwaggerConfig.java#">...</a>
 * @author tn
 * @version 1
 * @date 2023-03-12 18:54:25
 */
@ConditionalOnWebApplication
@Import({ConsoleConfig.class,SwaggerProperties.class})
@ConditionalOnClass({OpenAPI.class})
public class SwaggerConfig {

    /**
     * 根据@Tag 上的排序，写入x-order
     * @return the global open api customizer
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getTags()!=null){
                openApi.getTags().forEach(tag -> {
                    Map<String,Object> map=new HashMap<>(10);
                    map.put("x-order", RandomUtil.randomInt(0,100));
                    tag.setExtensions(map);
                });
            }
            if(openApi.getPaths()!=null){
                openApi.addExtension("x-test123","333");
                openApi.getPaths().addExtension("x-abb",RandomUtil.randomInt(1,100));
            }

        };
    }

    @Bean
    public GroupedOpenApi defaultApi(SwaggerProperties swaggerProperties){
        String[] paths = { "/**" };
        String[] packagedToMatch = basePackages(swaggerProperties.getBasePackage());
        return GroupedOpenApi.builder().group(swaggerProperties.getGroupName())
                .pathsToMatch(paths)
                .packagesToScan(packagedToMatch).build();
    }

    /**
     * new OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI(SwaggerProperties swaggerProperties) {
        return new OpenAPI()
                .info(new Info()
                        .title(swaggerProperties.getTitle())
                        .version(swaggerProperties.getVersion())
                        .contact(new Contact()
                                .name(swaggerProperties.getAuthor())
                                .url(swaggerProperties.getUrl())
                                .email(swaggerProperties.getEmail()))
                        .description(swaggerProperties.getDescription())
                        .termsOfService(swaggerProperties.getUrl())
                        .license(new License().name(swaggerProperties.getLicense())
                                .url(swaggerProperties.getUrl())));
    }

    /**
     * 访问安全的API（空了再看，我现在设置没什么用）
     */
    private Map<String, SecurityScheme> buildSecuritySchemes() {
        Map<String, SecurityScheme> securitySchemes = new HashMap<>();
        return securitySchemes;
    }


}

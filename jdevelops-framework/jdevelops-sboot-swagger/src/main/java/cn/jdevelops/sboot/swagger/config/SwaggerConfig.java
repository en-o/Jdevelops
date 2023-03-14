package cn.jdevelops.sboot.swagger.config;

import cn.jdevelops.sboot.swagger.core.entity.BuildSecuritySchemes;
import cn.jdevelops.sboot.swagger.core.entity.SwaggerSecurityScheme;
import cn.jdevelops.sboot.swagger.core.util.RandomUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.*;

import static cn.jdevelops.sboot.swagger.core.constant.PublicConstant.SWAGGER_HEADER_HANDER;
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

    /**
     * 根据自定一份配置文件设置默认读取的分组
     * @param swaggerProperties SwaggerProperties
     * @return GroupedOpenApi
     */
    @Bean
    public GroupedOpenApi defaultApi(SwaggerProperties swaggerProperties){
        BuildSecuritySchemes buildSecuritySchemes = buildSecuritySchemes(swaggerProperties);
        String[] paths = { "/**" };
        String[] packagedToMatch = basePackages(swaggerProperties.getBasePackage());
        return GroupedOpenApi.builder().group(swaggerProperties.getGroupName())
                .pathsToMatch(paths)
                // todo: Authorize 未生效，请求header里未包含参数 - 临时处理方法
                .addOperationCustomizer((operation, handlerMethod) -> operation.security(buildSecuritySchemes.getSecurityItem()))
                .packagesToScan(packagedToMatch).build();
    }

    /**
     * 根据配置文件初始化 接口文档的信息
     */
    @Bean
    public OpenAPI customOpenAPI(SwaggerProperties swaggerProperties) {
        BuildSecuritySchemes buildSecuritySchemes = buildSecuritySchemes(swaggerProperties);
        OpenAPI openAPI = new OpenAPI()
                // 添加安全方案
                .components(new Components().securitySchemes(buildSecuritySchemes.getSecuritySchemes()))
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
        /*
            todo： Authorize 未生效，请求header里未包含参数 ：https://gitee.com/xiaoym/knife4j/issues/I6AZLF
            全局接口都默认使用 apiKey 鉴权方式
            如果要多个且是否add进行，这里要配合buildSecuritySchemes和 swaggerProperties 来写
        */
        buildSecuritySchemes.getSecurityItem().forEach(openAPI::addSecurityItem);
        return openAPI;
    }

    /**
     *
     * OpenAPI 规范中支持的安全方案是
     * @see <a href="http://www.ballcat.cn/guide/feature/openapi.html#%E5%AE%89%E5%85%A8%E6%96%B9%E6%A1%88">...</a>
     * HTTP 身份验证
     * API key （作为 Header 或 查询参数）
     * OAuth2 的通用流程（implicit, password, application and access code），如RFC6749
     * OpenID Connect Discovery
     * 在 java 中的抽象类型对应 io.swagger.v3.oas.models.security.SecurityScheme
     */
    private BuildSecuritySchemes buildSecuritySchemes(SwaggerProperties swaggerProperties) {
        List<SecurityRequirement>  securityItem = new ArrayList<>();
        Map<String, SecurityScheme> securitySchemes = new HashMap<>();
        swaggerProperties.getSecurityScheme().forEach(swaggerSecurityScheme -> {
            securitySchemes.put(swaggerSecurityScheme.getScheme().getType().name(), swaggerSecurityScheme.getScheme());
           if(swaggerSecurityScheme.getSecurity()){
               securityItem.add(new SecurityRequirement().addList(swaggerSecurityScheme.getScheme().getType().name()));
           }
        });
        return new BuildSecuritySchemes(securityItem,securitySchemes);
    }


}

package cn.jdevelops.sboot.swagger.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.*;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.providers.JavadocProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cn.jdevelops.sboot.swagger.core.constant.PublicConstant.SWAGGER_HEADER_HANDER;
import static cn.jdevelops.sboot.swagger.core.util.SwaggerUtil.*;


/**
 * swagger配置
 *
 * @author tn
 * @version 1
 * @date 2020/6/18 15:49
 */
@ConditionalOnClass({OpenAPI.class})
@EnableConfigurationProperties(SwaggerProperties.class)
// 设置为 false 时，禁用
@ConditionalOnProperty(prefix = "springdoc.api-docs", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfig {

    /**
     * 项目访问根路径
     */
    @Value("${server.servlet.context-path:/}")
    private String serverName;

    /**
     * 项目端口
     */
    @Value("${server.port:8080}")
    private String serverPort;


    /**
     * API 摘要信息
     */
    private Info buildInfo(SwaggerProperties properties) {
        return new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .contact(new Contact().name(properties.getAuthor()).url(properties.getUrl()).email(properties.getEmail()))
                .license(new License().name(properties.getLicense()).url(properties.getLicenseUrl()));
    }


    @Bean
    public OpenAPI createApi(SwaggerProperties properties) {
        Map<String, SecurityScheme> securitySchemas = buildSecuritySchemes(properties);
        OpenAPI openAPI = new OpenAPI()
                // 接口信息
                .info(buildInfo(properties))
                // 接口安全配置
                .components(new Components().securitySchemes(securitySchemas))
                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
        securitySchemas.keySet().forEach(key -> openAPI.addSecurityItem(new SecurityRequirement().addList(key)));
        return openAPI;
    }


    /**
     * 安全模式，这里配置通过请求头 Authorization 传递 token 参数
     */
    private Map<String, SecurityScheme> buildSecuritySchemes(SwaggerProperties properties) {
        Map<String, SecurityScheme> securitySchemes = new HashMap<>();
        if (Boolean.TRUE.equals(properties.getAddHeaderToken())) {
            SecurityScheme securityScheme = new SecurityScheme()
                    // 类型
                    .type(SecurityScheme.Type.APIKEY)
                    // 请求头的 name
                    .name(HttpHeaders.AUTHORIZATION)
                    // token 所在位置
                    .in(SecurityScheme.In.HEADER);
            securitySchemes.put(HttpHeaders.AUTHORIZATION, securityScheme);
            securitySchemes.put(SWAGGER_HEADER_HANDER, securityScheme);
        }
        return securitySchemes;
    }

    /**
     * 自定义 OpenAPI 处理器
     */
    @Bean
    public OpenAPIService openApiBuilder(Optional<OpenAPI> openAPI,
                                         SecurityService securityParser,
                                         SpringDocConfigProperties springDocConfigProperties,
                                         PropertyResolverUtils propertyResolverUtils,
                                         Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomizers,
                                         Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomizers,
                                         Optional<JavadocProvider> javadocProvider) {

        return new OpenAPIService(openAPI, securityParser, springDocConfigProperties,
                propertyResolverUtils, openApiBuilderCustomizers, serverBaseUrlCustomizers, javadocProvider);
    }


    /**
     * 默认的分组
     */
    @Bean
    public GroupedOpenApi defualtGroupedOpenApi(SwaggerProperties properties) {
        return buildGroupedOpenApi(properties.getGroupName(), properties.getBasePackage());
    }

    public static GroupedOpenApi buildGroupedOpenApi(String group, String path) {
        return GroupedOpenApi.builder()
                .group(group)
                .pathsToMatch(basePackages(path))
                .addOperationCustomizer((operation, handlerMethod) -> operation
                        .addParametersItem(buildSecurityHeaderParameter()))
                .build();
    }

    /**
     * 构建 Authorization 认证请求头参数
     *
     * @return 认证参数
     */
    private static Parameter buildSecurityHeaderParameter() {
        return new Parameter()
                // header 名
                .name(SWAGGER_HEADER_HANDER)
                // 描述
                .description("认证 Token")
                // 请求 header
                .in(String.valueOf(SecurityScheme.In.HEADER))
                .schema(new StringSchema().name(SWAGGER_HEADER_HANDER).description("认证Token"));
    }


}

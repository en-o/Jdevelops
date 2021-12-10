package cn.jdevelops.doc.swagger.boot.core;

import cn.jdevelops.doc.core.swagger.bean.SwaggerBean;
import cn.jdevelops.doc.core.swagger.config.BaseConfig;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;

import java.net.InetAddress;

/**
 * swagger配置
 * @author tn
 * @version 1
 * @date 2020/6/18 15:49
 */
@Configuration
@EnableOpenApi
@EnableKnife4j
public class SwaggerConfig {


    public static final String SPIRIT = "/";
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

    @Autowired(required=false)
    private SwaggerBean swaggerBean;


    @Bean
    public Docket createRestApi() {
        Docket build = new Docket(swaggerBean.getDocket())
                .enable(swaggerBean.getShow())
                .apiInfo(apiInfo())
                .groupName(swaggerBean.getGroupName())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerBean.getBasePackage()))
                /*对所有路径进行监控*/
                .paths(PathSelectors.any())
                .build();
       if(Boolean.TRUE.equals(swaggerBean.getAddHeaderToken())){
           //全站统一参数token
           return  build.securitySchemes(BaseConfig.security())
                   .securityContexts(BaseConfig.securityContexts());
       }else {
           return build;
       }

    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact(swaggerBean.getContactName(),
                swaggerBean.getContactUrl(),
                swaggerBean.getContactEmail());
        String address = "127.0.0.1";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            address = inetAddress.getHostAddress();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(SPIRIT.equals(serverName)){
            serverName = "";
        }
        return new ApiInfoBuilder()
                .title(swaggerBean.getTitle())
                .description(swaggerBean.getDescription())
                .contact(contact)
                .version(swaggerBean.getVersion())
                .termsOfServiceUrl("http://"+address+":"+serverPort+serverName)
                .build();
    }
}

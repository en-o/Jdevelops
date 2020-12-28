package com.detabes.doc.swagger.cloud.core;

import com.detabes.doc.core.swagger.bean.SwaggerBean;
import com.detabes.doc.core.swagger.config.BaseConfig;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * @author tn
 * @version 1
 * @ClassName config
 * @description swagger配置
 * @date 2020/6/18 15:49
 */
@EnableSwagger2
@EnableKnife4j
public class SwaggerConfig {


    public static String SPIRIT = "/";

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

    public Docket createRestApi() {



        return new Docket(swaggerBean.getDocket())
                .enable(swaggerBean.getShow())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerBean.getBasePackage()))
                /*对所有路径进行监控*/
                .paths(PathSelectors.any())
                .build()//赋予插件体系
                //全站统一参数token
                .securitySchemes(BaseConfig.security())
                .securityContexts(BaseConfig.securityContexts())
                ;
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact(swaggerBean.getContactName(),
                swaggerBean.getContactUrl(),
                swaggerBean.getContactEmail());
        String address = "127.0.0.1";
        try {
            InetAddress inetAddress = Inet4Address.getLocalHost();
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

package cn.jdevelops.doc.swagger.boot.core;

import cn.jdevelops.doc.core.swagger.bean.SwaggerBean;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;

import static cn.jdevelops.doc.core.swagger.util.SwaggerUtil.*;

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

    @Resource
    private SwaggerBean swaggerBean;



    @Bean(value = "defaultApi")
    public Docket defaultApi() {
        return createRestApi(swaggerBean.getGroupName(), swaggerBean.getBasePackage());
    }


    public Docket createRestApi(String groupName, String packageName) {
        Docket build = new Docket(swaggerBean.getDocket())
                .enable(swaggerBean.getShow())
                .apiInfo(apiInfo(swaggerBean, serverName, serverPort))
                .groupName(groupName)
                .select()
                .apis(basePackage(packageName))
//                .apis(RequestHandlerSelectors.basePackage(swaggerBean.getBasePackage()))
                /*对所有路径进行监控*/
                .paths(PathSelectors.any())
                .build();
       if(Boolean.TRUE.equals(swaggerBean.getAddHeaderToken())){
           //全站统一参数token
           return  build.securitySchemes(security())
                   .securityContexts(securityContexts());
       }else {
           return build;
       }

    }






}

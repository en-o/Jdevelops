package cn.jdevelops.doc.core.swagger.util;

import cn.jdevelops.doc.core.swagger.bean.SwaggerBean;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static cn.jdevelops.doc.core.swagger.constant.PublicConstant.*;
import static com.google.common.collect.Lists.newArrayList;

/**
 * swagger 的一些公共方法
 * @author tnnn
 * @version V1.0
 * @date 2022-07-25 10:13
 */
public class SwaggerUtil {

    /**
     * swagger 多包扫描吗，以 ;分隔
     * @param basePackage basePackage
     * @return Predicate<RequestHandler>
     */
    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).map(handlerPackage(basePackage)).orElse(true);
    }

    /**
     *  Predicate 的私有方法
     * @param basePackage basePackage
     * @return Function<Class<?>, Boolean>
     */
    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(SPLITOR)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }
    /**
     *  Predicate 的私有方法
     * @param input RequestHandler
     * @return Optional<Class<?>>
     */
    private static Optional<Class<?>> declaringClass(RequestHandler input) {
        return Optional.ofNullable(input.declaringClass());
    }


    /**
     * 获取本地IP
     * @return  ip
     */
    public static String localIP(){
        String address = "127.0.0.1";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            address = inetAddress.getHostAddress();
        }catch (Exception ignored){
        }
        return address;
    }


    /**
     *  apiInfo
     * @param swaggerBean swaggerBean
     * @param serverName serverName
     * @param serverPort serverPort
     * @return ApiInfo
     */
    public static ApiInfo apiInfo(SwaggerBean swaggerBean,String serverName,String serverPort) {
        Contact contact = new Contact(swaggerBean.getContactName(),
                swaggerBean.getContactUrl(),
                swaggerBean.getContactEmail());
        if(SPIRIT.equals(serverName)){
            serverName = "";
        }
        return new ApiInfoBuilder()
                .title(swaggerBean.getTitle())
                .description(swaggerBean.getDescription())
                .contact(contact)
                .version(swaggerBean.getVersion())
                .license("jdevelops")
                .termsOfServiceUrl("http://"+localIP()+":"+serverPort+serverName)
                .build();
    }



    /**
     * 统一填写一次token
     * @return List
     */
    public static List<SecurityScheme> security() {
        return newArrayList(
                new ApiKey(SWAGGER_HEADER_HANDER, SWAGGER_HEADER_HANDER, "header")
        );
    }


    /**
     * defaultAuth
     * @return List<SecurityReference>
     */
    private static List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences=new ArrayList<>();
        securityReferences.add(new SecurityReference(SWAGGER_HEADER_HANDER, authorizationScopes));
        return securityReferences;
    }

    /**
     *  securityContexts
     * @return  List<SecurityContext>
     */
    public static List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts=new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

}

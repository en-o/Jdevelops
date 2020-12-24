package com.detabes.doc.core.swagger.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;

/**
 * @author tn
 * @version 1
 * @ClassName SwaggerBean
 * @description swagger配置类
 * @date 2020/6/19 9:56
 */
@ConfigurationProperties(prefix = "detabes.swagger")
@Component
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SwaggerBean {

    /**
     * controller接口所在的包
     */
    private String basePackage="com.detabes.controller";


    /**
     * 网页标题
     */
    private String title="崇锐APIs";

    /**
     * 当前文档的详细描述
     */
    private String description="详细描述";

    /**
     * 当前文档的版本
     */
    private String version="1.0";


    /**
     *  联络 url
     */
    private String contactUrl="http://detabes.com:9528/";

    /**
     *  联络人-作者
     */
    private String contactName="detabes";

    /**
     *  联络邮箱
     */
    private String contactEmail="rarelit@sina.com";

    /**
     * swagger Docket  :
     *     SWAGGER_2 (默认)
     *     SWAGGER_12
     */
    private String docket = "SWAGGER_2";

    /**
     *  true 显示swagger， false 关闭swagger
     */
    private Boolean show=true;

    /**
     *  分组
     */
    private String groupName;



    public DocumentationType getDocket() {
        switch (docket){
            case "SWAGGER_12":
                return DocumentationType.SWAGGER_12;
            default:
                return DocumentationType.SWAGGER_2;
        }
    }
}

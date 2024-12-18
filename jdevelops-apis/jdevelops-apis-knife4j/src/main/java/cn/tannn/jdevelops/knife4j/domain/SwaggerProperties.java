package cn.tannn.jdevelops.knife4j.domain;


import cn.tannn.jdevelops.knife4j.core.entity.SwaggerSecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static cn.tannn.jdevelops.knife4j.core.constant.PublicConstant.SWAGGER_HEADER_HANDER;


/**
 * swagger配置类
 *
 * @author tn
 * @version 1
 * @date 2020/6/19 9:56
 */
@ConfigurationProperties(prefix = "jdevelops.swagger")
public class SwaggerProperties {

    /**
     * controller接口所在的包(可以设置多个)，eg. cn.jdevelops.controller(默认)
     */
    private List<String> basePackage;


    /**
     * 网页标题
     */
    private String title;

    /**
     * 当前文档的详细描述
     */
    private String description;

    /**
     * 当前文档的版本
     */
    private String version;


    /**
     * 作者
     */
    private String author;

    /**
     * url
     */
    private String url;

    /**
     * email
     */
    private String email;

    /**
     * license
     */
    private String license;

    /**
     * license-url
     */
    private String licenseUrl;

    /**
     * 分组 必须英文 e.g jdevelopsAPI
     */
    private String groupName;

    /**
     * 分组名
     */
    private String displayName;

    /**
     * OpenAPI 规范的安全方案
     * 默认apikey,且name等于token
     */
    private List<SwaggerSecurityScheme> swaggerSecuritySchemes;

    /**
     * 是否设置默认的 securityScheme ，true 要设置
     * <p>
     * 优先级在 swaggerSecuritySchemes.security 之下:
     * 1. 存在 swaggerSecuritySchemes 以swaggerSecuritySchemes.security为准
     * 2. swaggerSecuritySchemes.security 都是 false 的情况下有效
     * </p>
     */
    private Boolean securitySchemeDefault;

    /**
     * 控制台配置
     * <p>swagger控制台输出配置生效，默认true[生效]</p>
     * <p>占坑 ConsoleConfig 的 ConditionalOnProperty#jdevelops.swagger.console.enabled</p>
     */
    @NestedConfigurationProperty
    private Console console = new Console();


    @Override
    public String toString() {
        return "SwaggerProperties{" +
                "basePackage=" + basePackage +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                ", author='" + author + '\'' +
                ", url='" + url + '\'' +
                ", email='" + email + '\'' +
                ", license='" + license + '\'' +
                ", licenseUrl='" + licenseUrl + '\'' +
                ", groupName='" + groupName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", swaggerSecuritySchemes=" + swaggerSecuritySchemes +
                ", securitySchemeDefault=" + securitySchemeDefault +
                ", console=" + console +
                '}';
    }

    public List<String> getBasePackage() {
        if (Objects.isNull(basePackage)) {
            return Collections.singletonList("cn.tannn.jdevelops.controller");
        }
        return basePackage;
    }

    public void setBasePackage(List<String> basePackage) {
        this.basePackage = basePackage;
    }


    public String getTitle() {
        if (Objects.isNull(title)) {
            return "JdevelopsAPIs";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        if (Objects.isNull(description)) {
            return "详细描述";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        if (Objects.isNull(version)) {
            return "2.0.8-SNAPSHOT";
        }
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        if (Objects.isNull(author)) {
            return "tan";
        }
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        if (Objects.isNull(url)) {
            return "https://t.tannn.cn/";
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        if (Objects.isNull(email)) {
            return "1445763190@qq.com";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicense() {
        if (Objects.isNull(license)) {
            return "tan";
        }
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        if (Objects.isNull(licenseUrl)) {
            return "https://t.tannn.cn/";
        }
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getGroupName() {
        if (Objects.isNull(groupName)) {
            return "jdevelopsAPI";
        }
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDisplayName() {
        if (Objects.isNull(displayName)) {
            return "默认接口组";
        }
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<SwaggerSecurityScheme> getSwaggerSecuritySchemes() {
        // 为空设置默认token
        if (Objects.isNull(swaggerSecuritySchemes) || swaggerSecuritySchemes.isEmpty()) {
            if (Boolean.TRUE.equals(getSecuritySchemeDefault())) {
                return Collections.singletonList(new SwaggerSecurityScheme(new SecurityScheme()
                        // 类型
                        .type(SecurityScheme.Type.APIKEY)
                        // 请求头的 name
                        .name(SWAGGER_HEADER_HANDER)
                        // token 所在位置
                        .in(SecurityScheme.In.HEADER), true));
            } else {
                return new ArrayList<>();
            }
        } else if (Boolean.TRUE.equals(getSecuritySchemeDefault())) {
            //  那 Security == true
            List<SwaggerSecurityScheme> collect = swaggerSecuritySchemes.stream().filter(SwaggerSecurityScheme::getSecurity).toList();
            // 设置的都没有效果,那就设置一个默认，（securitySchemeDefault == ture）
            if (collect.isEmpty()) {
                return Collections.singletonList(new SwaggerSecurityScheme(new SecurityScheme()
                        // 类型
                        .type(SecurityScheme.Type.APIKEY)
                        // 请求头的 name
                        .name(SWAGGER_HEADER_HANDER)
                        // token 所在位置
                        .in(SecurityScheme.In.HEADER), true));
            }
        }
        return swaggerSecuritySchemes;
    }

    public void setSwaggerSecuritySchemes(List<SwaggerSecurityScheme> swaggerSecuritySchemes) {
        this.swaggerSecuritySchemes = swaggerSecuritySchemes;
    }

    public Boolean getSecuritySchemeDefault() {
        if (Objects.isNull(securitySchemeDefault)) {
            return true;
        }
        return securitySchemeDefault;
    }

    public void setSecuritySchemeDefault(Boolean securitySchemeDefault) {
        this.securitySchemeDefault = securitySchemeDefault;
    }


    public Console getConsole() {
        return console;
    }

    public void setConsole(Console console) {
        this.console = console;
    }

    /**
     * 内部类表示控制台配置
     */
    public static class Console {
        /**
         * swagger控制台输出配置生效，默认true[生效]
         * <p>占坑 ConsoleConfig 的 ConditionalOnProperty#jdevelops.swagger.console.enabled</p>
         */
        private Boolean enabled = true;

        // getter and setter
        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = Objects.requireNonNullElse(enabled, true);
        }

        @Override
        public String toString() {
            return "Console{" +
                    "enabled=" + enabled +
                    '}';
        }
    }

}

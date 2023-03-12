package cn.jdevelops.sboot.swagger.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;


/**
 * swagger配置类
 * @author tn
 * @version 1
 * @date 2020/6/19 9:56
 */
@ConfigurationProperties(prefix = "jdevelops.swagger")
public class SwaggerProperties {

    /**
     * controller接口所在的包(多个包以逗号(或者分号)拼接，eg. package.api;package.controller
     * ps: 注意如果包名前缀一样只需要写一个就行 eg. package.api1;package.api2 ＝ package.api
     */
    private String basePackage;


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
     *  分组
     */
    private String groupName;



    @Override
    public String toString() {
        return "SwaggerProperties{" +
                "basePackage='" + basePackage + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                ", author='" + author + '\'' +
                ", url='" + url + '\'' +
                ", email='" + email + '\'' +
                ", license='" + license + '\'' +
                ", licenseUrl='" + licenseUrl + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }

    public String getBasePackage() {
        if(Objects.isNull(basePackage)){
            return "cn.jdevelops.controller";
        }
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getTitle() {
        if(Objects.isNull(title)){
            return "JdevelopsAPIs";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        if(Objects.isNull(description)){
            return "详细描述";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        if(Objects.isNull(version)){
            return "2.0.7";
        }
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        if(Objects.isNull(author)){
            return "tan";
        }
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        if(Objects.isNull(url)){
            return "https://tannn.cn/";
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        if(Objects.isNull(email)){
            return "1445763190@qq.com";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicense() {
        if(Objects.isNull(license)){
            return "tan";
        }
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        if(Objects.isNull(licenseUrl)){
            return "https://tannn.cn/";
        }
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getGroupName() {
        if(Objects.isNull(groupName)){
            return "jdevelopsAPI";
        }
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


}

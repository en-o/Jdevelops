package cn.jdevelops.sboot.swagger.config;


import org.springframework.boot.context.properties.ConfigurationProperties;


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
    private String basePackage="cn.jdevelops.controller";


    /**
     * 网页标题
     */
    private String title="JdevelopsAPIs";

    /**
     * 当前文档的详细描述
     */
    private String description="详细描述";

    /**
     * 当前文档的版本
     */
    private String version="2.0.7";


    /**
     * 作者
     */
    private String author="tan";

    /**
     * url
     */
    private String url="https://tannn.cn/";

    /**
     * email
     */
    private String email="1445763190@qq.com";

    /**
     * license
     */
    private String license="tan";

    /**
     * license-url
     */
    private String licenseUrl="https://tannn.cn/";

    /**
     *  分组
     */
    private String groupName;

    /**
     *  是否添加全局Header token参数
     */
    private Boolean addHeaderToken = true;


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
                ", addHeaderToken=" + addHeaderToken +
                '}';
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getAddHeaderToken() {
        return addHeaderToken;
    }

    public void setAddHeaderToken(Boolean addHeaderToken) {
        this.addHeaderToken = addHeaderToken;
    }
}

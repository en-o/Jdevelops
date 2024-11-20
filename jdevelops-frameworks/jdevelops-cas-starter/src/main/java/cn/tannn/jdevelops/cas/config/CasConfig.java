package cn.tannn.jdevelops.cas.config;

import io.micrometer.common.util.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * cas登录配置
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/10/11 下午1:42
 */
@ConfigurationProperties(prefix = "jdevelops.cas")
public class CasConfig {

    /**
     * cas 服务器地址【存在默认值,http://127.0.0.1】
     */
    private String service;

    /**
     * cas认证票据检测接口 【存在默认值】
     *  <p> 默认 /cas/serviceValidate</p>
     *  <p> 不带 casService</p>
     *  <p> 完整路径 service +  validate</p>
     */
    private String validate;


    /**
     * cas登录接口 【存在默认值】
     *  <p> 默认 /cas/login</p>
     *  <p> 不带 casService</p>
     *  <p> 完整路径 service +  login</p>
     */
    private String login;


    /**
     * cas退出接口 【存在默认值】
     *  <p> 默认 /cas/logout</p>
     *  <p> 不带 casService</p>
     *  <p> 完整路径 service +  logout</p>
     */
    private String logout;


    /**
     * 接口前缀不加接口名[接口名内置]
     * <p> 开发阶段 http://127.0.0.1:9019
     * <p> 线上阶段 https://xx.cn/nginx配置
     */
    private String apiUrlPrefix;


    /**
     * 前端页前缀不加路径[路径内置]
     * <p> 开发阶段 http://192.168.1.69:9528
     * <p> 线上阶段 https://xx.cn/nginx配置
     */
    private String webUrlPrefix;

    /**
     * 前端退出页面路径
     * <p> 默认 /#/login</p>
     * <p> 完整路径 webUrlPrefix +  webLogoutPage</p>
     */
    private String webLogoutPage;

    /**
     * 前端解析token页路径
     * <p> 默认 /#/sso</p>
     * <p> 完整路径 webUrlPrefix +  webParsingTokenPage</p>
     */
    private String webParsingTokenPage;

    /**
     * true: 调用cas退出[默认true]， false: 不调用
     */
    private Boolean redirectLogout;


    public CasConfig() {
    }

    public CasConfig(String service
            , String validate
            , String login
            , String logout
            , String apiUrlPrefix
            , String webUrlPrefix
            , String webLogoutPage
            , String webParsingTokenPage
            , Boolean redirectLogout) {
        this.service = service;
        this.validate = validate;
        this.login = login;
        this.logout = logout;
        this.apiUrlPrefix = apiUrlPrefix;
        this.webUrlPrefix = webUrlPrefix;
        this.webLogoutPage = webLogoutPage;
        this.webParsingTokenPage = webParsingTokenPage;
        this.redirectLogout = redirectLogout;
    }

    public String getService() {
        return StringUtils.isBlank(service) ? "https://127.0.0.1" : service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getValidate() {
        return StringUtils.isBlank(validate) ? "/cas/serviceValidate" : validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getLogin() {
        return StringUtils.isBlank(login) ? "/cas/login" : login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogout() {
        return StringUtils.isBlank(logout) ? "/cas/logout" : logout;
    }

    public void setLogout(String logout) {
        this.logout = logout;
    }

    public String getApiUrlPrefix() {
        return apiUrlPrefix;
    }

    public void setApiUrlPrefix(String apiUrlPrefix) {
        this.apiUrlPrefix = apiUrlPrefix;
    }

    public String getWebUrlPrefix() {
        return webUrlPrefix;
    }

    public void setWebUrlPrefix(String webUrlPrefix) {
        this.webUrlPrefix = webUrlPrefix;
    }

    public String getWebLogoutPage() {
        return StringUtils.isBlank(webLogoutPage) ? "/#/login" : webLogoutPage;
    }

    public void setWebLogoutPage(String webLogoutPage) {
        this.webLogoutPage = webLogoutPage;
    }

    public String getWebParsingTokenPage() {
        return StringUtils.isBlank(webParsingTokenPage) ? "/#/sso" : webParsingTokenPage;
    }

    public void setWebParsingTokenPage(String webParsingTokenPage) {
        this.webParsingTokenPage = webParsingTokenPage;
    }

    public Boolean getRedirectLogout() {
        return redirectLogout == null || redirectLogout;
    }

    public void setRedirectLogout(Boolean redirectLogout) {
        this.redirectLogout = redirectLogout;
    }

    /**
     * 完整cas登录地址
     */
    public String fullLoginUrl() {
        return service + getLogin();
    }

    /**
     * 完整cas验证地址
     */
    public String fullValidateUrl() {
        return service + getValidate();
    }
    /**
     * 完整cas退出地址
     */
    public String fullLogoutUrl() {
        return service + getLogout();
    }



    /**
     * 统一认证成功后的跳转地址的票据认证完整地址
     */
    public String jRedirect() {
        return apiUrlPrefix + "/login/cas";
    }


    /**
     * 票据认证成功后跳转前端解析token地址
     */
    public String vRedirect() {
        return webUrlPrefix + getWebParsingTokenPage();
    }

    /**
     * 退出后 单点登录跳转到本项目前端的完整地址
     */
    public String logoutRedirect() {
        return webUrlPrefix + getWebLogoutPage();
    }


    @Override
    public String toString() {
        return "CasConfig{" +
                "service='" + service + '\'' +
                ", validate='" + validate + '\'' +
                ", login='" + login + '\'' +
                ", logout='" + logout + '\'' +
                ", apiUrlPrefix='" + apiUrlPrefix + '\'' +
                ", webUrlPrefix='" + webUrlPrefix + '\'' +
                ", webLogoutPage='" + webLogoutPage + '\'' +
                ", webParsingTokenPage='" + webParsingTokenPage + '\'' +
                ", redirectLogout=" + redirectLogout +
                '}';
    }
}

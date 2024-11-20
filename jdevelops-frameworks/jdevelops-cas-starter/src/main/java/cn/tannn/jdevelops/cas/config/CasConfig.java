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
     * cas认证票据检测接口 【存在默认值】
     * 不带 casService
     */
    private String validate;


    /**
     * cas登录接口 【存在默认值】
     * 不带 casService
     */
    private String login;


    /**
     * cas退出接口 【存在默认值】
     * 不带 casService
     */
    private String logout;



    /**
     * cas 服务器地址【存在默认值】
     */
    private String service;

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
     * true: 调用cas退出[默认true]， false: 不调用
     */
    private Boolean isRedirectLogout;



    public String getValidate() {
        return  StringUtils.isBlank(validate)?"/cas/serviceValidate":validate;
    }

    public String getLogin() {
        return  StringUtils.isBlank(login)?"/cas/login":login;
    }

    public String getLogout() {
        return  StringUtils.isBlank(logout)?"/cas/logout":logout;
    }

    public String getService() {
        return  StringUtils.isBlank(service)?"https://dc.swupl.edu.cn":service;
    }


    public String fullLoginUrl() {
        return  service+login;
    }

    public String fullValidateUrl() {
        return  service+validate;
    }

    public String fullLogoutUrl() {
        return  service+logout;
    }

    /**
     *  统一认证成功后的跳转地址的票据认证
     */
    public String jRedirect(){
        return apiUrlPrefix+"/login/cas";
    }

    public Boolean getRedirectLogout() {
        return isRedirectLogout == null || isRedirectLogout;
    }



    /**
     *  票据认证成功后跳转前端访问地址
     */
    public String vRedirect(){
        return webUrlPrefix+"/#/sso";
    }

    /**
     *  退出后 单点登录跳转到本项目的登录页
     */
    public String logoutRedirect(){
        return webUrlPrefix+"/#/login";
    }
}

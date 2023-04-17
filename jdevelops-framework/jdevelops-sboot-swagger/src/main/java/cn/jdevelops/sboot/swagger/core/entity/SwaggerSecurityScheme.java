package cn.jdevelops.sboot.swagger.core.entity;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.context.properties.NestedConfigurationProperty;


/**
 * swagger 安全方案
 * @author tnnn
 * @version V1.0
 * @date 2023-03-13 15:29
 */
public class SwaggerSecurityScheme {

    /**
     * 安全方案类型
     * HTTP 身份验证
     * API key （作为 Header 或 查询参数）
     * OAuth2 的通用流程（implicit, password, application and access code），如RFC6749
     * OpenID Connect Discovery
     */
    @NestedConfigurationProperty
    SecurityScheme scheme;

    /**
     *  全局接口都默认使用的鉴权方式
     *  true: 全局（默认）， false  不设置全局
     */
    Boolean security;

    public SwaggerSecurityScheme() {
    }

    public SwaggerSecurityScheme(SecurityScheme scheme, Boolean security) {
        this.scheme = scheme;
        this.security = security;
    }

    public SecurityScheme getScheme() {
        return scheme;
    }

    public void setScheme(SecurityScheme scheme) {
        this.scheme = scheme;
    }

    public Boolean getSecurity() {
        return security;
    }

    public void setSecurity(Boolean security) {
        this.security = security;
    }

    @Override
    public String toString() {
        return "SwaggerSecurityScheme{" +
                "scheme=" + scheme +
                ", security=" + security +
                '}';
    }
}

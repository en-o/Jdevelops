package cn.jdevelops.sboot.authentication.sas.server.oauth.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户端信息维护表
 * @see https://springdoc.cn/spring-authorization-server/core-model-components.html#registered-client
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/28 8:47
 */
@Entity
@Table(name = "oauth2_registered_client")
@org.hibernate.annotations.Table(appliesTo = "oauth2_registered_client", comment = "客户端信息表")
@Getter
@Setter
@ToString
public class Oauth2RegisteredClient implements Serializable {

    /**
     * 客户端表ID
     */
    @Id
    @Comment("客户端表ID")
    @Column(columnDefinition = " varchar(100)  not null")
    private String id;

    /**
     * 客户端ID[Basic Auth 登录用的登录名]
     */
    @Comment("客户端ID[Basic Auth 登录用的登录名]")
    @Column(columnDefinition = " varchar(100)  not null")
    private String clientId;

    /**
     * 客户端ID签发日期
     */
    @Comment("客户端ID签发日期")
    @Column(columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL")
    private LocalDateTime clientIdIssuedAt;

    /**
     * 客户端secret，{noop}开头表示明文[Basic Auth 登录用的登录密码]
     */
    @Comment("客户端secret，{noop}开头表示明文[Basic Auth 登录用的登录密码]")
    @Column(columnDefinition = " varchar(100)  DEFAULT null")
    private String clientSecret;

    /**
     * 客户端secret到期日期
     */
    @Comment("客户端secret到期日期")
    @Column(columnDefinition = "timestamp DEFAULT NULL")
    private LocalDateTime clientSecretExpiresAt;

    /**
     * 一个用于描述客户端名称。该名称可能会在某些情况下使用，例如在同意页中显示客户端名称时
     */
    @Comment("一个用于描述客户端名称。该名称可能会在某些情况下使用，例如在同意页中显示客户端名称时")
    @Column(columnDefinition = " varchar(200) NOT NULL")
    private String clientName;

    /**
     * 授权方法[用于使用Provider对客户端进行身份验证的方法] {@link ClientAuthenticationMethod}
     * 客户端可能使用的认证方法。支持的值是 client_secret_basic， client_secret_post， private_key_jwt， client_secret_jwt 和 none（ 公共客户端）
     */
    @Comment(" 授权方法[用于使用Provider对客户端进行身份验证的方法] {@link ClientAuthenticationMethod}")
    @Column(columnDefinition = " varchar(1000) NOT NULL")
    private String clientAuthenticationMethods;

    /**
     * 授权类型[@see https://blog.51cto.com/u_15268610/6748969, {@link AuthorizationGrantType}]
     */
    @Comment("授权类型[@see https://blog.51cto.com/u_15268610/6748969, {@link AuthorizationGrantType}]")
    @Column(columnDefinition = " varchar(1000) NOT NULL")
    private String authorizationGrantTypes;

    /**
     * 授权成功回调地址[一般是客户端首页，注册了的回调才能被正确使用]
     */
    @Comment("授权成功回调地址[一般是客户端首页，注册了的回调才能被正确使用]")
    @Column(columnDefinition = " varchar(1000) DEFAULT NULL")
    private String redirectUris;

    /**
     * 授权范围 [e.g {@link OidcScopes}]
     * 客户允许请求的scope
     */
    @Comment("授权范围{@link OidcScopes}")
    @Column(columnDefinition = " varchar(1000) NOT NULL")
    private String scopes;

    /**
     * 客户端的自定义设置[例如，需要 PKCE，需要授权许可，以及其他]
     */
    @Comment("客户端的自定义设置[例如，需要 PKCE，需要授权许可，以及其他]")
    @Column(columnDefinition = "varchar(2000) NOT NULL")
    private String clientSettings;

    /**
     * 发给客户端的OAuth2令牌的自定义设置[例如，访问/刷新令牌的生存时间，重用刷新令牌，以及其他]
     */
    @Comment("发给客户端的OAuth2令牌的自定义设置[例如，访问/刷新令牌的生存时间，重用刷新令牌，以及其他]")
    @Column(columnDefinition = "varchar(2000) NOT NULL")
    private String tokenSettings;

}

package cn.jdevelops.authentication.sas.server.oauth.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 授权信息表
 * @see https://springdoc.cn/spring-authorization-server/core-model-components.html#oauth2-authorization
 * @see https://docs.spring.io/spring-authorization-server/docs/0.4.3/reference/html/core-model-components.html
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/28 8:47
 */
@Entity
@Table(name = "oauth2_authorization")
@org.hibernate.annotations.Table(appliesTo = "oauth2_authorization", comment = "授权信息表")
@Getter
@Setter
@ToString
public class Oauth2Authorization implements Serializable {

    /**
     * 授权信息表ID
     */
    @Id
    @Comment("授权信息表ID")
    @Column(columnDefinition = " varchar(100)  not null")
    private String id;


    /**
     * 客户端ID {@Oauth2RegisteredClient#getId()}
     * Oauth2RegisteredClient 的唯一标识符ID
     */
    @Comment("权客户端ID {@Oauth2RegisteredClient#getId()}")
    @Column(columnDefinition = " varchar(100)  not null")
    private String registeredClientId;

    /**
     * 登录名 {@link UserDetails#getUsername()}
     * 资源所有者（或客户端）的 principal 名称
     */
    @Comment("登录名 {@link UserDetails#getUsername()}")
    @Column(columnDefinition = " varchar(200)  not null")
    private String principalName;

    /**
     * 使用的授权类型[@see https://blog.51cto.com/u_15268610/6748969, {@link AuthorizationGrantType}]
     */
    @Comment("授权类型[@see https://blog.51cto.com/u_15268610/6748969, {@link AuthorizationGrantType}]")
    @Column(columnDefinition = " varchar(100) NOT NULL")
    private String authorizationGrantType;

    /**
     * 客户授权的 scope 集
     * 客户允许请求的scope
     */
    @Comment("客户授权的 scope 集")
    @Column(columnDefinition = " varchar(1000) DEFAULT NULL")
    private String authorizedScopes;

    /**
     * 所执行的授权许可类型所特有的额外属性—[例如，经过验证的 Principal、 OAuth2AuthorizationRequest 以及其他]
     */
    @Comment("所执行的授权许可类型所特有的额外属性—[例如，经过验证的 Principal、 OAuth2AuthorizationRequest 以及其他]")
    @Column(columnDefinition = " text  DEFAULT NULL")
    private String attributes;

    /**
     * 授权code
     */
    @Comment("state")
    @Column(columnDefinition = "varchar(500) DEFAULT NULL")
    private String state;

    /**
     * 授权code
     */
    @Comment("授权code")
    @Column(columnDefinition = " text  DEFAULT NULL")
    private String authorizationCodeValue;

    /**
     * 授权code签发时间
     */
    @Comment("授权code签发时间")
    @Column(columnDefinition = "timestamp DEFAULT NULL")
    private LocalDateTime authorizationCodeIssuedAt;

    /**
     * 授权code过期时间
     */
    @Comment("授权code过期时间")
    @Column(columnDefinition = " timestamp  DEFAULT NULL")
    private LocalDateTime authorizationCodeExpiresAt;

    /**
     * 授权code元信息
     */
    @Comment("授权code元信息")
    @Column(columnDefinition = " text  DEFAULT NULL")
    private String authorizationCodeMetadata;

    /**
     * 授权成功后返回的token
     */
    @Comment("授权成功后返回的token")
    @Column(columnDefinition = " text  DEFAULT NULL")
    private String accessTokenValue;

    /**
     * token的签发时间
     */
    @Comment("token的过期时间")
    @Column(columnDefinition = " timestamp  DEFAULT NULL")
    private LocalDateTime accessTokenIssuedAt;

    /**
     * token的过期时间
     */
    @Comment("token的过期时间")
    @Column(columnDefinition = " timestamp  DEFAULT NULL")
    private LocalDateTime accessTokenExpiresAt;

    /**
     * token的元信息
     */
    @Comment("token的元信息")
    @Column(columnDefinition = " text  DEFAULT NULL")
    private String accessTokenMetadata;

    /**
     * token 类型
     */
    @Comment("token 类型")
    @Column(columnDefinition = "varchar(100) DEFAULT NULL")
    private String accessTokenType;

    /**
     * token授权范围
     */
    @Comment("token授权范围")
    @Column(columnDefinition = "varchar(1000) DEFAULT NULL")
    private String accessTokenScopes;


    /**
     * oidcIdToken
     */
    @Comment("oidcIdToken")
    @Column(columnDefinition = " text  DEFAULT NULL")
    private String oidcIdTokenValue;

    /**
     * oidcIdToken签发时间
     */
    @Comment("oidcIdToken签发时间")
    @Column(columnDefinition = " timestamp  DEFAULT NULL")
    private LocalDateTime oidcIdTokenIssuedAt;

    /**
     * oidcIdToken过期时间
     */
    @Comment("oidcIdToken过期时间")
    @Column(columnDefinition = " timestamp  DEFAULT NULL")
    private LocalDateTime oidcIdTokenExpiresAt;

    /**
     * oidcIdToken元信息
     */
    @Comment("oidcIdToken元信息")
    @Column(columnDefinition = " text  DEFAULT NULL")
    private String oidcIdTokenMetadata;


    /**
     * 刷新认证token用的token
     */
    @Comment("刷新认证token用的token")
    @Column(columnDefinition = " text  DEFAULT NULL")
    private String refreshTokenValue;

    /**
     * 刷新token的签发时间
     */
    @Comment("刷新token的签发时间")
    @Column(columnDefinition = " timestamp  DEFAULT NULL")
    private LocalDateTime refreshTokenIssuedAt;

    /**
     * 刷新token的过期时间
     */
    @Comment("刷新token的过期时间")
    @Column(columnDefinition = " timestamp  DEFAULT NULL")
    private LocalDateTime refreshTokenExpiresAt;

    /**
     * 刷新token的元信息
     */
    @Comment("刷新token的元信息")
    @Column(columnDefinition = " text  DEFAULT NULL")
    private String refreshTokenMetadata;

}

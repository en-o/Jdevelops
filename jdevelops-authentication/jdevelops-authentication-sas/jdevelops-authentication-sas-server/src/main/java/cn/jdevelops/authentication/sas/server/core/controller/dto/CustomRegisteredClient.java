package cn.jdevelops.authentication.sas.server.core.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

/**
 * 客户端注册
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/28 14:31
 */
@Getter
@Setter
@ToString
public class CustomRegisteredClient {

    /**
     * 客户端ID
     */
    @NotBlank
    private String clientId;
    /**
     * 客户端Secret
     */
    @NotBlank
    private String clientSecret;

    /**
     * 客户端名称
     */
    @NotBlank
    private String clientName;

    /**
     * 客户端回调地址
     */
    @NotEmpty
    private Set<String> redirectUris;

    /**
     * 默认 CLIENT_SECRET_BASIC
     */
    private Set<ClientAuthenticationMethod> clientAuthenticationMethods;


    /**
     * 授权模式 {@link AuthorizationGrantType}
     */
    @NotEmpty
    private Set<AuthorizationGrantType> authorizationGrantTypes;

    /**
     * 授权范围  scopes 有默认值
     */
    private Set<String> scopes;


    public Set<ClientAuthenticationMethod> getClientAuthenticationMethods() {
        if (clientAuthenticationMethods == null || clientAuthenticationMethods.isEmpty()) {
            Set<ClientAuthenticationMethod> authenticationMethods = new HashSet<>();
            authenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
            return authenticationMethods;
        }
        return clientAuthenticationMethods;
    }


    public Set<String> getScopes() {
        if (scopes == null || scopes.isEmpty()) {
            // OIDC 支持
            Set<String> scopes = new HashSet<>();
            scopes.add(OidcScopes.OPENID);
            scopes.add(OidcScopes.PROFILE);
            return scopes;
        }
        return scopes;
    }
}

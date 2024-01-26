package cn.jdevelops.authentication.sas.server.core.controller.dto;

import cn.jdevelops.util.authorization.error.constant.JdevelopsScopes;
import cn.jdevelops.util.authorization.error.exception.AuthorizationException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.util.CollectionUtils;

import javax.security.auth.login.LoginException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.oauth2.core.oidc.OidcScopes.OPENID;

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
     * 客户端过期时间[为空永不过期,格式 日期:2023-01-01]
     */
    private String clientSecretExpiresAt;

    /**
     * 客户端名称
     */
    @NotBlank
    private String clientName;


    /**
     * 客户端回调地址[回调地址名单，不在此列将被拒绝 而且只能使用IP或者域名 不能使用 localhost]
     * <p>客户端模式（client_credentials）必填</p>
     */
    private Set<String> redirectUris;

    /**
     *  身份验证模式{@link AuthorizationGrantType} 默认 CLIENT_SECRET_BASIC
     *  <p>
     *  CLIENT_SECRET_BASIC： 使用 HTTP 基本身份验证。客户端通过在请求头中添加 Authorization 头，将客户端标识和客户端密钥（秘密）进行 Base64 编码并传递给授权服务器。
     *  CLIENT_SECRET_POST： 客户端通过在请求体中添加 client_id 和 client_secret 这两个参数进行身份验证。这通常用于在不支持 HTTP 基本身份验证的环境中，例如浏览器 JavaScript 应用。
     *  CLIENT_SECRET_BASIC 和 CLIENT_SECRET_POST 的组合： 客户端同时支持基本身份验证和通过请求体传递客户端密钥。
     *  PRIVATE_KEY_JWT： 客户端使用私钥签名 JWT（JSON Web Token）进行身份验证。这通常用于客户端是一个移动应用或 Web 应用的情况。
     *  NONE： 客户端无需进行身份验证。这通常用于无法保护客户端密钥的客户端，如纯前端 JavaScript 应用。
     *  </p>
     */
    private Set<ClientAuthenticationMethod> clientAuthenticationMethods;


    /**
     * 授权模式 {@link AuthorizationGrantType}
     * <p>
     *     ● authorization_code  ：   授权码模式
     *     ● refresh_token ： 刷新令牌模式
     *     ● client_credentials ：客户端凭证模式
     *     ● authorization_mobile ： 手机号登录模式
     *     ● authorization_password ：账户密码模式
     * </p>
     */
    @NotEmpty
    private Set<AuthorizationGrantType> authorizationGrantTypes;

    /**
     * 授权范围  scopes 有默认值[consent.html页面用的，也是给接口的权限注解‘@PreAuthorize("hasAuthority('SCOPE_profile')")’用的]
     * {@link JdevelopsScopes}
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


    public Set<String> getRedirectUris() {
        if(CollectionUtils.isEmpty(redirectUris)&& !authorizationGrantTypes.isEmpty()
                && authorizationGrantTypes.contains(AuthorizationGrantType.CLIENT_CREDENTIALS)
        ){
            throw AuthorizationException.specialMessage("客户端模式不允许回调地址为空");
        }
        return redirectUris;
    }

    public Set<String> getScopes() {
        if (CollectionUtils.isEmpty(scopes)) {
            // 添加全部支持 支持
            Set<String> allScopes = new HashSet<>();
            allScopes.add(OidcScopes.OPENID);
            allScopes.add(OidcScopes.PROFILE);
            allScopes.add(OidcScopes.PHONE);
            allScopes.add(OidcScopes.ADDRESS);
            allScopes.add(OidcScopes.EMAIL);
            allScopes.add(JdevelopsScopes.STATUS);
            return allScopes;
        }
        return scopes;
    }
}

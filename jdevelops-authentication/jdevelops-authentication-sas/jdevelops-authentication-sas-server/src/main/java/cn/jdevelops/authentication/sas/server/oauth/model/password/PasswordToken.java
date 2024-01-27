package cn.jdevelops.authentication.sas.server.oauth.model.password;

import cn.jdevelops.authentication.sas.server.oauth.constant.OAuth2Model;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

/**
 * 密码模式（自定义） - 自定义授权码
 * @author tan
 */
public class PasswordToken extends OAuth2AuthorizationGrantAuthenticationToken {

    public PasswordToken(Authentication clientPrincipal,
                         @Nullable Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(OAuth2Model.GRANT_TYPE_PASSWORD),
                clientPrincipal, additionalParameters);
    }
}

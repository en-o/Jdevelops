package cn.jdevelops.sboot.authentication.sas.server.oauth.model.mobile;

import cn.jdevelops.sboot.authentication.sas.server.oauth.constant.OAuth2Model;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

/**
 * 短信模式（自定义） - 自定义授权码
 * @author tan
 */
public class MobileToken extends OAuth2AuthorizationGrantAuthenticationToken {

    public MobileToken(Authentication clientPrincipal,
                       @Nullable Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(OAuth2Model.GRANT_TYPE_MOBILE),
                clientPrincipal, additionalParameters);
    }

}

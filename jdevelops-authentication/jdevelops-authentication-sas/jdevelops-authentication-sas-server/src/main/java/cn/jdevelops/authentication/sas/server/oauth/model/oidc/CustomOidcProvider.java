package cn.jdevelops.authentication.sas.server.oauth.model.oidc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;


/**
 * @author tan
 */
public class CustomOidcProvider implements AuthenticationProvider {
    private final Log logger = LogFactory.getLog(this.getClass());
    private final OAuth2AuthorizationService authorizationService;
    private Function<OidcUserInfoAuthenticationContext, CustomOidcUserInfo> userInfoMapper;

    public CustomOidcProvider(OAuth2AuthorizationService authorizationService,
                              CustomOidcUserInfoService customOidcUserInfoService) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        this.authorizationService = authorizationService;
        userInfoMapper = new DefaultOidcUserInfoMapper(customOidcUserInfoService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OidcUserInfoAuthenticationToken userInfoAuthentication = (OidcUserInfoAuthenticationToken)authentication;
        AbstractOAuth2TokenAuthenticationToken<?> accessTokenAuthentication = null;
        if (AbstractOAuth2TokenAuthenticationToken.class.isAssignableFrom(userInfoAuthentication.getPrincipal().getClass())) {
            accessTokenAuthentication = (AbstractOAuth2TokenAuthenticationToken)userInfoAuthentication.getPrincipal();
        }

        if (accessTokenAuthentication != null && accessTokenAuthentication.isAuthenticated()) {
            String accessTokenValue = accessTokenAuthentication.getToken().getTokenValue();
            OAuth2Authorization authorization = this.authorizationService.findByToken(accessTokenValue, OAuth2TokenType.ACCESS_TOKEN);
            if (authorization == null) {
                throw new OAuth2AuthenticationException("invalid_token");
            } else {
                if (logger.isTraceEnabled()) {
                    logger.trace("Retrieved authorization with access token");
                }

                OAuth2Authorization.Token<OAuth2AccessToken> authorizedAccessToken = authorization.getAccessToken();
                if (!authorizedAccessToken.isActive()) {
                    throw new OAuth2AuthenticationException("invalid_token");
                } else {
                    //从认证结果中获取userInfo
                    CustomOidcUserInfo customOidcUserInfo = (CustomOidcUserInfo)userInfoAuthentication.getUserInfo();
                    //从authorizedAccessToken中获取授权范围
                    Set<String> scopeSet = (HashSet<String>)authorizedAccessToken.getClaims().get("scope") ;
                    //获取授权范围对应userInfo的字段信息
                    Map<String, Object> claims = DefaultOidcUserInfoMapper.getClaimsRequestedByScope(customOidcUserInfo.getClaims(),scopeSet);
                    if (logger.isTraceEnabled()) {
                        logger.trace("Authenticated user info request");
                    }
                    //构造新的OidcUserInfoAuthenticationToken
                    CustomOidcToken customOidcToken = new CustomOidcToken(accessTokenAuthentication, new CustomOidcUserInfo(claims));

                    if (logger.isTraceEnabled()) {
                        logger.trace("Validated user info request");
                    }
                    //使用OidcUserInfoAuthenticationToken重新構造OidcUserInfoAuthenticationContext
                    OidcUserInfoAuthenticationContext authenticationContext = OidcUserInfoAuthenticationContext.with(customOidcToken).accessToken(authorizedAccessToken.getToken()).authorization(authorization).build();
                    CustomOidcUserInfo userInfo = userInfoMapper.apply(authenticationContext);
                    if (logger.isTraceEnabled()) {
                        logger.trace("Authenticated user info request");
                    }
                    return new CustomOidcToken(accessTokenAuthentication, userInfo);



                }
            }
        } else {
            throw new OAuth2AuthenticationException("invalid_token");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OidcUserInfoAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setUserInfoMapper(Function<OidcUserInfoAuthenticationContext, CustomOidcUserInfo> userInfoMapper) {
        Assert.notNull(userInfoMapper, "userInfoMapper cannot be null");
        this.userInfoMapper = userInfoMapper;
    }

    private static final class DefaultOidcUserInfoMapper implements Function<OidcUserInfoAuthenticationContext, CustomOidcUserInfo> {
        private static final List<String> EMAIL_CLAIMS = Arrays.asList("email", "email_verified");
        private static final List<String> PHONE_CLAIMS = Arrays.asList("phone_number", "phone_number_verified");
        private static final List<String> PROFILE_CLAIMS = Arrays.asList("name", "username", "description", "status", "profile");

        private final CustomOidcUserInfoService customOidcUserInfoService;
        private DefaultOidcUserInfoMapper(CustomOidcUserInfoService customOidcUserInfoService) {
            this.customOidcUserInfoService = customOidcUserInfoService;
        }

        @Override
        public CustomOidcUserInfo apply(OidcUserInfoAuthenticationContext authenticationContext) {
            OAuth2Authorization authorization = authenticationContext.getAuthorization();
            OAuth2Authorization.Token oAuth2Token = authorization.getToken(OidcIdToken.class);
            Map<String, Object> claims;
            if(Objects.nonNull(oAuth2Token)){
                OidcIdToken idToken = (OidcIdToken)oAuth2Token.getToken();
                claims = idToken.getClaims();
            }else{
                java.security.Principal principal = authorization.getAttribute("java.security.Principal");
                //查询用户信息
                CustomOidcUserInfo customOidcUserInfo = this.customOidcUserInfoService.loadUser(principal.getName());
                claims = customOidcUserInfo.getClaims();
            }

            OAuth2AccessToken accessToken = authenticationContext.getAccessToken();
            //todo 获取授权范围对应userInfo的字段信息
            Map<String, Object> scopeRequestedClaims = getClaimsRequestedByScope(claims, accessToken.getScopes());
            return new CustomOidcUserInfo(scopeRequestedClaims);
        }

        /**
         * 根据不同权限抽取不同数据
         */
        private static Map<String, Object> getClaimsRequestedByScope(Map<String, Object> claims, Set<String> requestedScopes) {
            Set<String> scopeRequestedClaimNames = new HashSet<>(32);
            scopeRequestedClaimNames.add("sub");
            // todo 根据不同的 scope 返回不同的数据
            if (requestedScopes.contains("address")) {
                scopeRequestedClaimNames.add("address");
            }

            if (requestedScopes.contains("email")) {
                scopeRequestedClaimNames.addAll(EMAIL_CLAIMS);
            }

            if (requestedScopes.contains("phone")) {
                scopeRequestedClaimNames.addAll(PHONE_CLAIMS);
            }

            if (requestedScopes.contains("profile")) {
                scopeRequestedClaimNames.addAll(PROFILE_CLAIMS);
            }

            Map<String, Object> requestedClaims = new HashMap<>(claims);
            requestedClaims.keySet().removeIf((claimName) -> !scopeRequestedClaimNames.contains(claimName));
            return requestedClaims;
        }
    }
}

package cn.jdevelops.authentication.sas.server.core.config;


import cn.jdevelops.authentication.sas.server.core.controller.ServerController;
import cn.jdevelops.authentication.sas.server.jose.Jwks;
import cn.jdevelops.authentication.sas.server.oauth.model.mobile.MobileConverter;
import cn.jdevelops.authentication.sas.server.oauth.model.mobile.MobileProvider;
import cn.jdevelops.authentication.sas.server.oauth.model.oidc.CustomOidcConverter;
import cn.jdevelops.authentication.sas.server.oauth.model.oidc.CustomOidcProvider;
import cn.jdevelops.authentication.sas.server.oauth.model.oidc.CustomOidcUserInfoService;
import cn.jdevelops.authentication.sas.server.oauth.model.password.PasswordConverter;
import cn.jdevelops.authentication.sas.server.oauth.model.password.PasswordProvider;
import cn.jdevelops.authentication.sas.server.user.entity.AuthenticationAccount;
import cn.jdevelops.authentication.sas.server.user.service.JUserDetailsService;
import cn.jdevelops.util.authorization.error.CustomAuthenticationFailureHandler;
import cn.jdevelops.util.authorization.error.CustomExceptionTranslationFilter;
import cn.jdevelops.util.authorization.error.UnAccessDeniedHandler;
import cn.jdevelops.util.authorization.error.UnAuthenticationEntryPoint;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.apache.catalina.util.StandardSessionIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author tan
 */
@Configuration
public class AuthorizationServerConfig {

	private static final Logger log = LoggerFactory.getLogger(AuthorizationServerConfig.class);

	private final JUserDetailsService jUserDetailsService;

	private final  UserDetailsService userDetailsService;

	private final CustomOidcUserInfoService customOidcUserInfoService;

	/**
	 * 授权页面
	 */
	private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

	public AuthorizationServerConfig(JUserDetailsService jUserDetailsService,
									 UserDetailsService userDetailsService,
									 CustomOidcUserInfoService customOidcUserInfoService) {
		this.jUserDetailsService = jUserDetailsService;
		this.userDetailsService = userDetailsService;
		this.customOidcUserInfoService = customOidcUserInfoService;
	}

	/**
	 * Spring Authorization Server 相关配置  <br/>
	 * 主要配置OAuth 2.1和OpenID Connect 1.0相关的东西
	 */
	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
																	  OAuth2AuthorizationService authorizationService,
																	  OAuth2TokenGenerator<?> tokenGenerator)
			throws Exception {

		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.authorizationEndpoint(authorizationEndpoint ->
						authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI))
				// 设置自定义密码模式
				.tokenEndpoint(tokenEndpoint ->
						tokenEndpoint
								.accessTokenRequestConverter(new PasswordConverter())
								.authenticationProvider(
										new PasswordProvider(authorizationService,tokenGenerator)
								))
				//设置自定义手机验证码模式
				.tokenEndpoint(tokenEndpoint ->
						tokenEndpoint
								.accessTokenRequestConverter(
										new MobileConverter())
								.authenticationProvider(
										new MobileProvider(
												authorizationService, tokenGenerator)))
				// 拦截异常 处理
				.tokenEndpoint(tokenEndpoint -> tokenEndpoint.errorResponseHandler(new CustomAuthenticationFailureHandler()))
				// 客户端异常处理
				.clientAuthentication(clientAuthentication->{
					clientAuthentication.errorResponseHandler(new CustomAuthenticationFailureHandler());
				})
				// Enable OpenID Connect 1.0[开启OpenID Connect 1.0] 自定义
				.oidc(oidcCustomizer->{
					oidcCustomizer.userInfoEndpoint(userInfoEndpointCustomizer->{
						userInfoEndpointCustomizer.userInfoRequestConverter(new CustomOidcConverter(customOidcUserInfoService));
						userInfoEndpointCustomizer.authenticationProvider(new CustomOidcProvider(authorizationService,customOidcUserInfoService));
					});
				});
		http
				.addFilterBefore(new CustomExceptionTranslationFilter(), ExceptionTranslationFilter.class)
				// Redirect to the login page when not authenticated from the[将需要认证的请求，重定向到login页面行登录认证。]
				// authorization endpoint
				.exceptionHandling((exceptions) -> exceptions
						.authenticationEntryPoint(
								new LoginUrlAuthenticationEntryPoint("/page/login")
						)
						.authenticationEntryPoint(new UnAuthenticationEntryPoint("/page/login"))
						.accessDeniedHandler(new UnAccessDeniedHandler())
				)
				// Accept access tokens for User Info and/or Client Registration[使用jwt处理接收到的access token]
				.oauth2ResourceServer(oauth2ResourceServer ->
						oauth2ResourceServer.jwt(Customizer.withDefaults()));
		return http.build();
	}



	// ===========使用数据库 start ===========
	/**
	 * 客户端信息 [新增客户端的注册是在数据库中注册{@link ServerController#addClient()]
	 * @see https://springdoc.cn/spring-authorization-server/core-model-components.html#registered-client-repository
	 * 对应表：oauth2_registered_client
	 */
	@Bean
	public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
		return new JdbcRegisteredClientRepository(jdbcTemplate);
	}

	/**
	 * [授权信息](https://springdoc.cn/spring-authorization-server/core-model-components.html#oauth2-authorization-service)
	 * 对应表：oauth2_authorization
	 */
	@Bean
	public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
														   RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
	}

	/**
	 * [授权确认](https://springdoc.cn/spring-authorization-server/core-model-components.html#oauth2-authorization-consent-service)
	 * 对应表：oauth2_authorization_consent
	 */
	@Bean
	public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
																		 RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}

	// ===========使用数据库 end ===========



	/**
	 * 配置 JWK，为JWT(id_token)提供加密密钥，用于加密/解密或签名/验签
	 * [JWK详细见](https://datatracker.ietf.org/doc/html/draft-ietf-jose-json-web-key-41)
	 */
	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = Jwks.generateRsa();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	/**
	 * 配置jwt解析器
	 */
	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}




	/**
	 * 配置授权服务器请求地址
	 */
	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}



	/**
	 *配置token生成器
	 */
	@Bean
	OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
		JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
		jwtGenerator.setJwtCustomizer(jwtCustomizer());
		OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
		OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
		return new DelegatingOAuth2TokenGenerator(
				jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
	}

	/**
	 * 自定义token的内容
	 */
	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
		return context -> {
			// 根据登录名 查询用户信息
			Optional<AuthenticationAccount> userInfo = jUserDetailsService.findUserInfo(context.getPrincipal().getName());
			JwsHeader.Builder headers = context.getJwsHeader();
			JwtClaimsSet.Builder claims = context.getClaims();
			if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
				//客户端模式不参与用户权限信息处理
				if(!AuthorizationGrantType.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType())){
					claims.claims(claimsConsumer->{
						UserDetails userDetails = userDetailsService.loadUserByUsername(context.getPrincipal().getName());
						claimsConsumer.merge("scope",userDetails.getAuthorities(),(scope,authorities)->{
							Set<String> scopeSet = (Set<String>)scope;
							Set<String> cloneSet = scopeSet.stream().map(String::new).collect(Collectors.toSet());
							Collection<SimpleGrantedAuthority> simpleGrantedAuthorities = ( Collection<SimpleGrantedAuthority>)authorities;
							simpleGrantedAuthorities.stream().forEach(simpleGrantedAuthority -> {
								if(!cloneSet.contains(simpleGrantedAuthority.getAuthority())){
									cloneSet.add(simpleGrantedAuthority.getAuthority());
								}
							});
							return cloneSet;
						});
					});
				}
			} else if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
				// Customize headers/claims for id_token
				claims.claim(IdTokenClaimNames.AUTH_TIME, Date.from(Instant.now()));
				StandardSessionIdGenerator standardSessionIdGenerator = new StandardSessionIdGenerator();
				claims.claim("sid", standardSessionIdGenerator.generateSessionId());
				// 给id_token添加附加信息 - 类似 userinfo查询的效果
				userInfo.ifPresent(user -> {
					claims.claim("loginName", user.getLoginName());
					claims.claim("name", user.getNickname());
					claims.claim("description", user.getDescription());
				});

			}
		};
	}

}


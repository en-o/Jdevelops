package cn.jdevelops.authentication.sas.server.oauth.model.oidc;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;


/**
 * Oidc - 用户将表单信息转换成 Authentication
 * @author tan
 */
public class CustomOidcConverter implements AuthenticationConverter {

    private CustomOidcUserInfoService customOidcUserInfoService;


    public CustomOidcConverter(CustomOidcUserInfoService customOidcUserInfoService){
        this.customOidcUserInfoService = customOidcUserInfoService;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //查询用户信息 /userinfo 接口的数据集
        CustomOidcUserInfo customOidcUserInfo = customOidcUserInfoService.loadUser(authentication.getName());

        //返回自定义的OidcUserInfoAuthenticationToken
        return new OidcUserInfoAuthenticationToken(authentication, customOidcUserInfo);
    }

}

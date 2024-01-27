package cn.jdevelops.authentication.sas.server.oauth.model.password;

import cn.jdevelops.authentication.sas.server.oauth.constant.OAuth2Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 仿照下面：
 * https://github.com/spring-projects/spring-authorization-server/blob/f6b07f5649fe5cec0507d0448733b389939a0960/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/web/authentication/OAuth2AuthorizationCodeRequestAuthenticationConverter.java#L155
 * 密码模式（自定义） - 用户将表单信息转换成 Authentication
 * @author tan
 */
public class PasswordConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!OAuth2Model.GRANT_TYPE_PASSWORD.equals(grantType)) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        //从request中提取请求参数，然后存入MultiValueMap<String, String>
        MultiValueMap<String, String> parameters = getParameters(request);

        // username (REQUIRED)
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) ||
                parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            throw new OAuth2AuthenticationException("无效请求，用户名不能为空！");
        }
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) ||
                parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            throw new OAuth2AuthenticationException("无效请求，密码不能为空！");
        }

        //收集要传入PasswordGrantAuthenticationToken构造方法的参数，
        //该参数接下来在PasswordGrantAuthenticationProvider中使用
        Map<String, Object> additionalParameters = new HashMap<>();
        //遍历从request中提取的参数，排除掉grant_type、client_id、code等字段参数，其他参数收集到additionalParameters中
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CODE)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        //返回自定义的PasswordGrantAuthenticationToken对象
        return new PasswordToken(clientPrincipal, additionalParameters);
    }

    /**
     *从request中提取请求参数，然后存入MultiValueMap<String, String>
     */
    private static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }

}

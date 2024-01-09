package cn.jdevelops.sboot.authentication.sas.server.oauth.model.oidc;

import cn.jdevelops.sboot.authentication.sas.server.user.entity.AuthenticationAccount;
import cn.jdevelops.sboot.authentication.sas.server.user.service.AuthenticationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 *  自定义 oidc的 userinfo接口的数据获取和组装
 * @author tan
 */
@Service
public class CustomOidcUserInfoService {

    @Resource
    private AuthenticationService authenticationService;

    public CustomOidcUserInfo loadUser(String username) {
        Optional<AuthenticationAccount> userInfo = authenticationService.findUser(username);
        return new CustomOidcUserInfo(this.createUser(userInfo));
    }

    private Map<String, Object> createUser(Optional<AuthenticationAccount> userInfo) {
        if(userInfo.isPresent()){
            AuthenticationAccount sysUser = userInfo.get();
            return CustomOidcUserInfo.customBuilder()
                    .nickname(sysUser.getNickname())
                    .username(sysUser.getLoginName())
                    .description(sysUser.getDescription())
                    .status(sysUser.getStatus())
                    .phoneNumber(sysUser.getPhoneNumber())
                    .email(sysUser.getEmail())
                    .profile(sysUser.getProfile())
                    .address(sysUser.getAddress())
                    .build()
                    .getClaims();
        }
        return Collections.emptyMap();
    }

}

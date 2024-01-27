package cn.jdevelops.util.authorization.error.constant;

import org.springframework.security.oauth2.core.oidc.OidcScopes;

/**
 * 规范定义的范围值
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/1/26 10:13
 */
public interface JdevelopsScopes extends OidcScopes {

    /**
     * 用户状态
     */
    String STATUS = "status";
}

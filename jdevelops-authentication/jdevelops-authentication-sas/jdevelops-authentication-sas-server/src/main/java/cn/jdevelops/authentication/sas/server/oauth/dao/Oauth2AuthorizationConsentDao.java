package cn.jdevelops.authentication.sas.server.oauth.dao;

import cn.jdevelops.authentication.sas.server.oauth.entity.Oauth2AuthorizationConsent;
import cn.jdevelops.authentication.sas.server.oauth.entity.key.Oauth2AuthorizationConsentUPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 授权确认
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-28 00:26
 */
public interface Oauth2AuthorizationConsentDao
        extends JpaRepository<Oauth2AuthorizationConsent, Oauth2AuthorizationConsentUPK>
        , JpaSpecificationExecutor<Oauth2AuthorizationConsent> {
    // 这张表最常用的就是查询了，只是个记录信息
}

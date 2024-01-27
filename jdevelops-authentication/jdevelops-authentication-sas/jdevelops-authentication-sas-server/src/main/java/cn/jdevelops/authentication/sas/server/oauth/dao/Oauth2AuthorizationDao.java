package cn.jdevelops.authentication.sas.server.oauth.dao;

import cn.jdevelops.authentication.sas.server.oauth.entity.Oauth2Authorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 授权信息
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-28 00:24
 */
public interface Oauth2AuthorizationDao extends JpaRepository<Oauth2Authorization,String>
        , JpaSpecificationExecutor<Oauth2Authorization> {
    // 这张表最常用的就是查询了，只是个记录信息
}

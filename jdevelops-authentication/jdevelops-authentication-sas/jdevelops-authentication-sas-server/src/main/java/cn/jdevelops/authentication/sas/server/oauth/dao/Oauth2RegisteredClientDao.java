package cn.jdevelops.authentication.sas.server.oauth.dao;

import cn.jdevelops.authentication.sas.server.oauth.entity.Oauth2RegisteredClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * 客户端信息
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-28 00:28
 */
public interface Oauth2RegisteredClientDao extends JpaRepository<Oauth2RegisteredClient,String>
        , JpaSpecificationExecutor<Oauth2RegisteredClient> {


    /**
     * 查询客户端
     * @param clientId clientId
     * @return Oauth2RegisteredClient
     */
    Optional<Oauth2RegisteredClient> findByClientId(String clientId);

    /**
     * 删除已经注册的客户端
     * @param clientId 客户端ID
     */
    void deleteByClientId(String clientId);

    /**
     * 删除已经注册的客户端
     * @param clientIds 客户端ID
     */
    void deleteByClientIdIn(List<String> clientIds);

}

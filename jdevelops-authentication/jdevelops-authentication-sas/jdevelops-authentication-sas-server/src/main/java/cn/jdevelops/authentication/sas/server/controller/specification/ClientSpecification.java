package cn.jdevelops.authentication.sas.server.controller.specification;

import cn.jdevelops.authentication.sas.server.oauth.entity.Oauth2RegisteredClient;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-28 01:21
 */
public class ClientSpecification {


    /**
     * like scopes
     * @param scopes 授权范围
     * @return Specification
     */
    public static Specification<Oauth2RegisteredClient> scopesLike(String scopes){
        if(null == scopes || scopes.isEmpty()){
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        }else {
            return (root, query, builder) -> builder.like(root.get("scopes"), "%" + scopes + "%");
        }
    }

    /**
     * like clientName
     * @param clientName 客户端名
     * @return Specification
     */
    public static Specification<Oauth2RegisteredClient> clientNameLike(String clientName){
        if(null == clientName || clientName.isEmpty()){
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        }else {
            return (root, query, builder) -> builder.like(root.get("clientName"), "%" + clientName + "%");
        }
    }

    /**
     * between clientSecretExpiresAtSection
     * @param clientSecretExpiresAtSection 过期时间查询 【2023-01-01,2023-01-02】
     * @return Specification
     */
    public static Specification<Oauth2RegisteredClient> clientSecretExpiresAtSection(String clientSecretExpiresAtSection){
        if(null != clientSecretExpiresAtSection && !clientSecretExpiresAtSection.isEmpty()){
            String[] split = clientSecretExpiresAtSection.split(",");
            if (split.length == 2) {
                return (root, query, builder) -> builder.between(root.get("clientName"), split[0], split[1]);
            }
        }
        return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
    }

}

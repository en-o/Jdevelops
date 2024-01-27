package cn.jdevelops.authentication.sas.server.controller.specification;

import cn.jdevelops.authentication.sas.server.oauth.entity.Oauth2Authorization;
import org.springframework.data.jpa.domain.Specification;

/**
 * 授权
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-28 02:09
 */
public class AuthorizationSpecification {


    /**
     * like principalName
     * @param principalName 登录名
     * @return Specification
     */
    public static Specification<Oauth2Authorization> loginNameLike(String principalName){
        if(null == principalName || principalName.isEmpty()){
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        }else {
            return (root, query, builder) -> builder.like(root.get("principalName"), "%" + principalName + "%");
        }
    }

    /**
     * EQ clientId
     * @param clientId 客户端ID
     * @return Specification
     */
    public static Specification<Oauth2Authorization> clientIdEq(String clientId){
        if(null == clientId || clientId.isEmpty()){
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        }else {
            return (root, query, builder) -> builder.equal(root.get("registeredClientId"), clientId);
        }
    }
}

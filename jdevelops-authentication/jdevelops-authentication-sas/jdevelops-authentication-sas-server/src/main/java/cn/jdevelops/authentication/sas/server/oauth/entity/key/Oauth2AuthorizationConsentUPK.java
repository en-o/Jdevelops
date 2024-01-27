package cn.jdevelops.authentication.sas.server.oauth.entity.key;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * 授权确认表的联合主键
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/28 8:47
 */
@Embeddable
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Oauth2AuthorizationConsentUPK implements Serializable,Cloneable {

    /**
     * 客户端ID {@Oauth2RegisteredClient#getId()}
     * Oauth2RegisteredClient 的唯一标识符ID
     */
    @Comment("权客户端ID {@Oauth2RegisteredClient#getId()}")
    @Column(columnDefinition = " varchar(100)  not null")
    private String registeredClientId;

    /**
     * 登录名 {@link UserDetails#getUsername()}
     * 资源所有者的 principal 名称
     */
    @Comment("登录名 {@link UserDetails#getUsername()}")
    @Column(columnDefinition = " varchar(200)  not null")
    private String principalName;

}

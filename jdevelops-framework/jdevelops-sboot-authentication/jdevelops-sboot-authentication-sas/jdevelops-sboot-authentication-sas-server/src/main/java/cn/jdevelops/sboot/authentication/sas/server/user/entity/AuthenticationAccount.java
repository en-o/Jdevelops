package cn.jdevelops.sboot.authentication.sas.server.user.entity;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户的实体，用来加载登录用户的必要数据字段
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/28 8:45
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationAccount implements Serializable {

    /**
     * 用户编号
     */
    private String no;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 个人简介
     */
    private String description;

    /**
     * 状态:0[停用],1[正常],2[封禁]
     */
    private Integer status;

    /**
     * 用户角色[逗号隔开]
     */
    private List<String> roles;


    public List<String> getRoles() {
        if(roles == null){
            return new ArrayList<>();
        }
        return roles;
    }
}

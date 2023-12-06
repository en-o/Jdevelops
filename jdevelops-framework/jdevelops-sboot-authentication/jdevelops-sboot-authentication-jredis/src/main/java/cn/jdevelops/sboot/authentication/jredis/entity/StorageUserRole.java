package cn.jdevelops.sboot.authentication.jredis.entity;

import java.util.Collections;
import java.util.List;

/**
 * 存储用户角色
 * 判断顺序： roles -> permissions
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/12/4 16:07
 */
public class StorageUserRole {

    /**
     * subject  token.subject[用户唯一编码，建议登录名]
     */
    String subject;

    /**
     * 用户角色 为空则默认不验证
     */
    private List<String> roles;

    /**
     * 用户权限[用户能当问的接口组(url组) e.g /edit/password  ] 为空则默认不验证
     * <p>
     *      （2）* 匹配0个或多个字符
     *      （3）**匹配0个或多个目录
     *      参考： @see  UserRoleUtilTest
     * </p>
     *
     */
    private  List<String> permissions;

    /**
     *  默认所有权限不用，内置  空list
     * @param subject  token.subject[用户唯一编码，建议登录名]
     */
    public StorageUserRole(String subject) {
        this.subject = subject;
        this.roles = Collections.emptyList();
        this.permissions = Collections.emptyList();
    }

    public StorageUserRole(String subject, List<String> roles, List<String> permissions) {
        this.subject = subject;
        this.roles = roles;
        this.permissions = permissions;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "StorageUserRole{" +
                "subject='" + subject + '\'' +
                ", roles=" + roles +
                ", permissions=" + permissions +
                '}';
    }


}

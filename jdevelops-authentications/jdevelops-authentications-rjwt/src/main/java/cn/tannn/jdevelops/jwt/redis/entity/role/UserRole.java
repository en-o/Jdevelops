package cn.tannn.jdevelops.jwt.redis.entity.role;

import java.util.StringJoiner;

/**
 * 用户角色
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-02-11 04:35
 */

public class UserRole {
    /**
     * token.subject[用户唯一编码，建议登录名]
     */
    String userCode;

    /**
     * 角色code
     */
    String roleCode;

    /**
     * 角色名
     */
    String roleName;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserRole.class.getSimpleName() + "[", "]")
                .add("userCode='" + userCode + "'")
                .add("roleCode='" + roleCode + "'")
                .add("roleName='" + roleName + "'")
                .toString();
    }
}

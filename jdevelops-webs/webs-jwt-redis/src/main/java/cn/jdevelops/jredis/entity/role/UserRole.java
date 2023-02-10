package cn.jdevelops.jredis.entity.role;

/**
 * 用户角色
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-02-11 04:35
 */
public class UserRole {
    /**
     * 用户唯一编码，建议登录名
     * = subject
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


    public UserRole() {
    }

    public UserRole(String userCode, String roleCode, String roleName) {
        this.userCode = userCode;
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "userCode='" + userCode + '\'' +
                ", roleCode='" + roleCode + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }

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
}

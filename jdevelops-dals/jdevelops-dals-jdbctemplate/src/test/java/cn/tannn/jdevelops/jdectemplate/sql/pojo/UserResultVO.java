package cn.tannn.jdevelops.jdectemplate.sql.pojo;

import cn.tannn.jdevelops.jdectemplate.annotations.SqlReColumn;

/**
 * 用户返回结果实体
 */
public class UserResultVO {

    @SqlReColumn(alias = "u.id")
    private Long id;

    @SqlReColumn(alias = "u.name")
    private String name;

    @SqlReColumn(alias = "u.email")
    private String email;

    @SqlReColumn(alias = "d.name as dept_name")
    private String deptName;

    @SqlReColumn(alias = "r.name as role_name")
    private String roleName;

    @SqlReColumn(alias = "u.create_time")
    private java.time.LocalDateTime createTime;

    // getters and setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public java.time.LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.time.LocalDateTime createTime) {
        this.createTime = createTime;
    }
}

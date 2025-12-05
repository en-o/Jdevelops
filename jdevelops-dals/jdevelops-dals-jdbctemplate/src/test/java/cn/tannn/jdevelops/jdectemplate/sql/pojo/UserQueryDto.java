package cn.tannn.jdevelops.jdectemplate.sql.pojo;

import cn.tannn.jdevelops.annotations.jdbctemplate.sql.*;
import cn.tannn.jdevelops.jdectemplate.sql.enums.JoinType;
import cn.tannn.jdevelops.jdectemplate.sql.enums.PageType;
import cn.tannn.jdevelops.jdectemplate.sql.enums.QueryType;

import java.util.List;

/**
 * 用户查询条件实体
 */
@SqlTable(name = "users", alias = "u", joins = {
        @SqlJoin(type = JoinType.LEFT, table = "departments", alias = "d", condition = "u.dept_id = d.id"),
        @SqlJoin(type = JoinType.LEFT, table = "roles", alias = "r", condition = "u.role_id = r.id")
})
@SqlOrderBy("u.create_time DESC")
public  class UserQueryDto {

    @SqlColumn(name = "name", queryType = QueryType.LIKE)
    private String userName;

    @SqlColumn(name = "status", queryType = QueryType.EQ)
    private Integer status;

    @SqlColumn(name = "dept_id", queryType = QueryType.IN)
    private List<Integer> deptIds;

    @SqlColumn(name = "create_time", queryType = QueryType.BETWEEN)
    private String createTimeRange; // 格式: "2023-01-01,2023-12-31"

    @SqlColumn(name = "age", queryType = QueryType.GE)
    private Integer minAge;

    @SqlPage(type = PageType.PAGE_INDEX)
    private Integer pageIndex;

    @SqlPage(type = PageType.PAGE_SIZE)
    private Integer pageSize;

    // getters and setters...
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Integer> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(List<Integer> deptIds) {
        this.deptIds = deptIds;
    }

    public String getCreateTimeRange() {
        return createTimeRange;
    }

    public void setCreateTimeRange(String createTimeRange) {
        this.createTimeRange = createTimeRange;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}

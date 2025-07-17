package cn.tannn.jdevelops.jdectemplate.sql.pojo;


import cn.tannn.jdevelops.jdectemplate.annotations.*;
import cn.tannn.jdevelops.jdectemplate.enums.JoinType;
import cn.tannn.jdevelops.jdectemplate.enums.NullHandleStrategy;
import cn.tannn.jdevelops.jdectemplate.enums.PageType;
import cn.tannn.jdevelops.jdectemplate.enums.QueryType;

import java.util.List;

/**
 * 账户查询对象示例
 */
@SqlTable(
    name = "tb_account",
    alias = "a",
    joins = {
        @SqlJoin(
            type = JoinType.LEFT,
            table = "tb_account_sensitive",
            alias = "b",
            condition = "b.user_id = a.id"
        )
    }
)
@SqlOrderBy("a.id DESC")
public class AccountQueryExample {

    @SqlColumn(name = "login_name", queryType = QueryType.EQ)
    private String loginName;

    @SqlColumn(name = "name", queryType = QueryType.LIKE, nullStrategy = NullHandleStrategy.IGNORE)
    private String name;

    @SqlColumn(name = "gender", queryType = QueryType.EQ)
    private Integer gender;

    @SqlColumn(name = "status", queryType = QueryType.IN)
    private List<Integer> status;

    @SqlColumn(name = "create_time", queryType = QueryType.GE, paramName = "createTimeStart")
    private String createTimeStart;

    @SqlColumn(name = "create_time", queryType = QueryType.LE, paramName = "createTimeEnd")
    private String createTimeEnd;

    @SqlColumn(name = "masked_phone", tableAlias = "b", queryType = QueryType.LIKE)
    private String phone;

    @SqlColumn(tableAlias = "b", queryType = QueryType.BETWEEN)
    private String times;

    @SqlPage(type = PageType.PAGE_INDEX)
    private Integer pageIndex;

    @SqlPage(type = PageType.PAGE_SIZE)
    private Integer pageSize;

    // 或者使用分页对象
//     @SqlPage(type = PageType.PAGE_INFO)
//     private PageInfo pageInfo;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}

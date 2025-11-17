package cn.tannn.jdevelops.jdectemplate.example;

import cn.tannn.jdevelops.jdectemplate.annotations.*;
import cn.tannn.jdevelops.jdectemplate.enums.JoinType;
import cn.tannn.jdevelops.jdectemplate.enums.PageType;
import cn.tannn.jdevelops.jdectemplate.enums.QueryType;
import cn.tannn.jdevelops.jdectemplate.sql.*;

import java.util.List;

/**
* 使用示例和测试代码
  */
  public class SqlBuilderUsageExample {

  // ================================ 示例实体类 ================================

  /**
    * 用户查询条件实体
      */
      @SqlTable(name = "users", alias = "u", joins = {
      @SqlJoin(type = JoinType.LEFT, table = "departments", alias = "d", condition = "u.dept_id = d.id"),
      @SqlJoin(type = JoinType.LEFT, table = "roles", alias = "r", condition = "u.role_id = r.id")
      })
      @SqlOrderBy("u.create_time DESC")
      public static class UserQueryDto {

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
      public String getUserName() { return userName; }
      public void setUserName(String userName) { this.userName = userName; }
      public Integer getStatus() { return status; }
      public void setStatus(Integer status) { this.status = status; }
      public List<Integer> getDeptIds() { return deptIds; }
      public void setDeptIds(List<Integer> deptIds) { this.deptIds = deptIds; }
      public String getCreateTimeRange() { return createTimeRange; }
      public void setCreateTimeRange(String createTimeRange) { this.createTimeRange = createTimeRange; }
      public Integer getMinAge() { return minAge; }
      public void setMinAge(Integer minAge) { this.minAge = minAge; }
      public Integer getPageIndex() { return pageIndex; }
      public void setPageIndex(Integer pageIndex) { this.pageIndex = pageIndex; }
      public Integer getPageSize() { return pageSize; }
      public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
      }

  /**
    * 用户返回结果实体
      */
      public static class UserResultVO {

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
      public Long getId() { return id; }
      public void setId(Long id) { this.id = id; }
      public String getName() { return name; }
      public void setName(String name) { this.name = name; }
      public String getEmail() { return email; }
      public void setEmail(String email) { this.email = email; }
      public String getDeptName() { return deptName; }
      public void setDeptName(String deptName) { this.deptName = deptName; }
      public String getRoleName() { return roleName; }
      public void setRoleName(String roleName) { this.roleName = roleName; }
      public java.time.LocalDateTime getCreateTime() { return createTime; }
      public void setCreateTime(java.time.LocalDateTime createTime) { this.createTime = createTime; }
      }

  // ================================ 使用示例方法 ================================

  /**
    * 示例1：基本用法 - 向后兼容
      */
      public static void example1_BasicUsage() {
      System.out.println("=== 示例1：基本用法（向后兼容） ===");

      UserQueryDto query = new UserQueryDto();
      query.setUserName("张三");
      query.setStatus(1);
      query.setPageIndex(0);
      query.setPageSize(10);

      // 使用原有的工厂方法
      DynamicSqlBuilder builder1 = DynamicSqlBuilderFactory.buildJdbc(query, UserResultVO.class);
      System.out.println("原有方法SQL: " + builder1.getSql());
      System.out.println("原有方法参数: " + java.util.Arrays.toString(builder1.getPositionalParams()));

      // 使用新的简化方法
      DynamicSqlBuilder builder2 = DynamicSqlBuilderFactory.basic(query, UserResultVO.class);
      System.out.println("新简化方法SQL: " + builder2.getSql());
      System.out.println();
      }

  /**
    * 示例2：链式构建 - 基本用法
      */
      public static void example2_ChainBasic() {
      System.out.println("=== 示例2：链式构建基本用法 ===");

      UserQueryDto query = new UserQueryDto();
      query.setUserName("李四");
      query.setStatus(1);

      DynamicSqlBuilder builder = DynamicSqlBuilderFactory.chain(query)
      .select(UserResultVO.class)
      .from()
      .where()
      .orderBy("u.name ASC")
      .limit(0, 20)
      .build();

      System.out.println("链式构建SQL: " + builder.getSql());
      System.out.println("链式构建参数: " + java.util.Arrays.toString(builder.getPositionalParams()));
      System.out.println();
      }

  /**
    * 示例3：自定义SELECT字段
      */
      public static void example3_CustomSelect() {
      System.out.println("=== 示例3：自定义SELECT字段 ===");

      UserQueryDto query = new UserQueryDto();
      query.setStatus(1);

      DynamicSqlBuilder builder = DynamicSqlBuilderFactory.chain(query)
      .select("u.id, u.name, COUNT(*) OVER() as total_count")
      .from()
      .where()
      .orderBy()
      .limit()
      .build();

      System.out.println("自定义SELECT SQL: " + builder.getSql());
      System.out.println();
      }

  /**
    * 示例4：扩展WHERE条件
      */
      public static void example4_ExtendWhere() {
      System.out.println("=== 示例4：扩展WHERE条件 ===");

      UserQueryDto query = new UserQueryDto();
      query.setUserName("王五");

      DynamicSqlBuilder builder = DynamicSqlBuilderFactory.chain(query)
      .select(UserResultVO.class)
      .from()
      .where(sqlBuilder -> {
      // 添加额外的条件
      sqlBuilder.gt("u.create_time", "2023-01-01");
      sqlBuilder.isNotNull("u.email");
      sqlBuilder.or(orBuilder -> orBuilder
      .eq("d.type", "TECH")
      .eq("r.level", "SENIOR")
      );
      })
      .orderBy()
      .limit()
      .build();

      System.out.println("扩展WHERE SQL: " + builder.getSql());
      System.out.println("扩展WHERE参数: " + java.util.Arrays.toString(builder.getPositionalParams()));
      System.out.println();
      }

  /**
    * 示例5：自定义FROM子句
      */
      public static void example5_CustomFrom() {
      System.out.println("=== 示例5：自定义FROM子句 ===");

      UserQueryDto query = new UserQueryDto();
      query.setStatus(1);

      DynamicSqlBuilder builder = DynamicSqlBuilderFactory.chain(query)
      .select("u.id, u.name, p.avatar, COUNT(ur.role_id) as role_count")
      .from("FROM users u " +
      "LEFT JOIN user_profiles p ON u.id = p.user_id " +
      "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
      "LEFT JOIN roles r ON ur.role_id = r.id")
      .where(sqlBuilder -> {
      sqlBuilder.eq("u.status", 1);
      sqlBuilder.isNotNull("p.avatar");
      })
      .groupBy("u.id, u.name, p.avatar")
      .having("COUNT(ur.role_id) > 0")
      .orderBy("role_count DESC")
      .limit()
      .build();

      System.out.println("自定义FROM SQL: " + builder.getSql());
      System.out.println();
      }

  /**
    * 示例6：扩展FROM子句
      */
      public static void example6_ExtendFrom() {
      System.out.println("=== 示例6：扩展FROM子句 ===");

      UserQueryDto query = new UserQueryDto();
      query.setUserName("赵六");

      DynamicSqlBuilder builder = DynamicSqlBuilderFactory.chain(query)
      .select("u.*, p.avatar, s.score")
      .from(fromBuilder -> {
      // 在原有JOIN基础上添加更多关联
      fromBuilder.append(" LEFT JOIN user_profiles p ON u.id = p.user_id");
      fromBuilder.append(" LEFT JOIN user_scores s ON u.id = s.user_id");
      })
      .where()
      .orderBy()
      .limit()
      .build();

      System.out.println("扩展FROM SQL: " + builder.getSql());
      System.out.println();
      }

  /**
    * 示例7：统计查询
      */
      public static void example7_StatQuery() {
      System.out.println("=== 示例7：统计查询 ===");

      UserQueryDto query = new UserQueryDto();
      query.setStatus(1);

      // 使用工厂方法快速构建统计查询
      DynamicSqlBuilder builder = DynamicSqlBuilderFactory.statQuery(query,
      "d.id, d.name, COUNT(*) as user_count, AVG(u.age) as avg_age",
      "d.id, d.name");

      System.out.println("统计查询SQL: " + builder.getSql());

      // 或者使用链式构建
      DynamicSqlBuilder builder2 = DynamicSqlBuilderFactory.chain(query)
      .select("d.name as dept_name, COUNT(*) as user_count, MAX(u.salary) as max_salary")
      .from()
      .where()
      .groupBy("d.id, d.name")
      .having("COUNT(*) > 5")
      .orderBy("user_count DESC")
      .limit()
      .build();

      System.out.println("链式统计SQL: " + builder2.getSql());
      System.out.println();
      }

# XML Mapper 快速开始 - 完整示例

本文档提供 XML Mapper 的完整使用示例,涵盖所有常用场景的XML写法。

---

## 📦 项目结构

```
src/main/
├── java/
│   └── com/example/
│       ├── entity/
│       │   └── User.java          # 实体类
│       ├── mapper/
│       │   ├── UserMapper.java    # Mapper 接口
│       │   └── dto/
│       │       └── UserQuery.java # 查询参数
│       └── service/
│           └── UserService.java   # 业务层
└── resources/
    ├── application.yml              # 配置文件
    └── jmapper/
        └── UserMapper.xml           # XML Mapper 文件
```

---

## 1️⃣ 配置文件

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test_db
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver

  jdevelops:
    jdbctemplate:
      xml-mapper:
        # XML Mapper 文件扫描路径
        locations: classpath*:jmapper/**/*.xml
        # 是否启用(默认 true)
        enabled: true
```

---

## 2️⃣ 实体类

### User.java

```java
package com.example.entity;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String username;
    private String email;
    private Integer age;
    private Integer status;
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
```

### UserQuery.java

```java
package com.example.mapper.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UserQuery {
    private Long id;
    private List<Long> ids;
    private String username;
    private String email;
    private Integer status;
    private List<Integer> statusList;
    private Integer minAge;
    private Integer maxAge;
    private String keyword;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String orderBy;
    private Integer pageSize;
    private Integer offset;

    // Getters and Setters
    // ... (省略)
}
```

---

## 3️⃣ Mapper 接口

### UserMapper.java

```java
package com.example.mapper;

import cn.tannn.jdevelops.annotations.jdbctemplate.xml.*;
import com.example.entity.User;
import com.example.mapper.dto.UserQuery;

import java.util.List;

@XmlMapper(namespace = "com.example.mapper.UserMapper")
public interface UserMapper {

    // ========== 查询操作 ==========

    /**
     * 根据ID查询用户
     */
    @XmlSelect("findById")
    User findById(UserQuery query);

    /**
     * 动态条件查询用户列表
     */
    @XmlSelect("findUsers")
    List<User> findUsers(UserQuery query);

    /**
     * 根据ID列表批量查询
     */
    @XmlSelect("findByIds")
    List<User> findByIds(UserQuery query);

    /**
     * 统计用户数量
     */
    @XmlSelect("countUsers")
    Integer countUsers(UserQuery query);

    /**
     * 分页查询用户
     */
    @XmlSelect("findUsersPage")
    List<User> findUsersPage(UserQuery query);

    /**
     * 高级查询(多条件+排序)
     */
    @XmlSelect("findUsersAdvanced")
    List<User> findUsersAdvanced(UserQuery query);

    // ========== 插入操作 ==========

    /**
     * 插入用户(返回自增ID)
     */
    @XmlInsert("insertUser")
    Long insertUser(User user);

    /**
     * 批量插入用户
     */
    @XmlInsert("batchInsert")
    int batchInsert(List<User> users);

    // ========== 更新操作 ==========

    /**
     * 动态更新用户信息
     */
    @XmlUpdate("updateUser")
    int updateUser(User user);

    // ========== 删除操作 ==========

    /**
     * 根据ID删除用户
     */
    @XmlDelete("deleteById")
    int deleteById(User user);

    /**
     * 根据ID列表批量删除用户
     */
    @XmlDelete("deleteByIds")
    int deleteByIds(UserQuery query);
}
```

---

## 4️⃣ XML Mapper 文件(完整示例)

### UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.example.mapper.UserMapper">

    <!-- ============================================ -->
    <!-- SQL 片段定义 -->
    <!-- ============================================ -->

    <!-- 基础字段列表 -->
    <sql id="baseColumns">
        id, username, email, age, status, created_at
    </sql>

    <!-- 表名 -->
    <sql id="tableName">
        users
    </sql>

    <!-- ============================================ -->
    <!-- 查询操作 -->
    <!-- ============================================ -->

    <!-- 1. 简单查询 - 根据ID查询单条记录 -->
    <select id="findById" resultType="com.example.entity.User">
        SELECT
            <include refid="baseColumns"/>
        FROM <include refid="tableName"/>
        WHERE id = #{id}
    </select>

    <!-- 2. 动态SQL查询 - 支持多种条件组合 -->
    <select id="findUsers" resultType="com.example.entity.User">
        SELECT
            <include refid="baseColumns"/>
        FROM <include refid="tableName"/>
        <where>
            <!-- 用户名模糊查询 -->
            <if test="username != null and username != ''">
                AND username LIKE #{username}
            </if>
            <!-- 邮箱精确匹配 -->
            <if test="email != null and email != ''">
                AND email = #{email}
            </if>
            <!-- 状态精确匹配 -->
            <if test="status != null">
                AND status = #{status}
            </if>
            <!-- 最小年龄 -->
            <if test="minAge != null">
                AND age >= #{minAge}
            </if>
            <!-- 最大年龄 -->
            <if test="maxAge != null">
                AND age &lt;= #{maxAge}
            </if>
        </where>
        ORDER BY created_at DESC
    </select>

    <!-- 3. IN查询 - 根据ID列表批量查询 -->
    <select id="findByIds" resultType="com.example.entity.User">
        SELECT
            <include refid="baseColumns"/>
        FROM <include refid="tableName"/>
        WHERE id IN
        <foreach collection="ids" item="id" open="(" separator="," close=")">
            #{id}
        </foreach>
    </select>

    <!-- 4. 统计查询 - 返回数量 -->
    <select id="countUsers" resultType="java.lang.Integer">
        SELECT COUNT(*)
        FROM <include refid="tableName"/>
        <where>
            <if test="status != null">
                AND status = #{status}
            </if>
        </where>
    </select>

    <!-- 5. 分页查询 -->
    <select id="findUsersPage" resultType="com.example.entity.User">
        SELECT
            <include refid="baseColumns"/>
        FROM <include refid="tableName"/>
        <where>
            <if test="username != null and username != ''">
                AND username LIKE CONCAT('%', #{username}, '%')
            </if>
            <if test="status != null">
                AND status = #{status}
            </if>
        </where>
        ORDER BY created_at DESC
        LIMIT #{pageSize} OFFSET #{offset}
    </select>

    <!-- 6. 高级查询 - 关键字搜索 + 状态列表 + 日期范围 + 自定义排序 -->
    <select id="findUsersAdvanced" resultType="com.example.entity.User">
        SELECT
            <include refid="baseColumns"/>
        FROM <include refid="tableName"/>
        <where>
            <!-- 关键字搜索(用户名或邮箱) -->
            <if test="keyword != null and keyword != ''">
                AND (username LIKE CONCAT('%', #{keyword}, '%')
                OR email LIKE CONCAT('%', #{keyword}, '%'))
            </if>
            <!-- 状态列表(IN 查询) -->
            <if test="statusList != null and statusList.size() > 0">
                AND status IN
                <foreach collection="statusList" item="status" open="(" separator="," close=")">
                    #{status}
                </foreach>
            </if>
            <!-- 开始日期 -->
            <if test="startDate != null">
                AND created_at >= #{startDate}
            </if>
            <!-- 结束日期 -->
            <if test="endDate != null">
                AND created_at &lt;= #{endDate}
            </if>
        </where>
        <!-- 自定义排序 -->
        ORDER BY
        <if test="orderBy != null and orderBy != ''">
            ${orderBy}
        </if>
        <if test="orderBy == null or orderBy == ''">
            created_at DESC
        </if>
    </select>

    <!-- ============================================ -->
    <!-- 插入操作 -->
    <!-- ============================================ -->

    <!-- 1. 单条插入 - 返回自增ID -->
    <insert id="insertUser"
            useGeneratedKeys="true"
            keyProperty="id"
            keyColumn="id">
        INSERT INTO <include refid="tableName"/>
        (username, email, age, status, created_at)
        VALUES
        (#{username}, #{email}, #{age}, #{status}, NOW())
    </insert>

    <!-- 2. 批量插入 - 使用 foreach -->
    <insert id="batchInsert">
        INSERT INTO <include refid="tableName"/>
        (username, email, age, status, created_at)
        VALUES
        <foreach collection="list" item="user" separator=",">
            (#{user.username}, #{user.email}, #{user.age}, #{user.status}, NOW())
        </foreach>
    </insert>

    <!-- ============================================ -->
    <!-- 更新操作 -->
    <!-- ============================================ -->

    <!-- 1. 动态更新 - 只更新非 null 字段 -->
    <update id="updateUser">
        UPDATE <include refid="tableName"/>
        <set>
            <if test="username != null">
                username = #{username},
            </if>
            <if test="email != null">
                email = #{email},
            </if>
            <if test="age != null">
                age = #{age},
            </if>
            <if test="status != null">
                status = #{status},
            </if>
        </set>
        WHERE id = #{id}
    </update>

    <!-- ============================================ -->
    <!-- 删除操作 -->
    <!-- ============================================ -->

    <!-- 1. 根据ID删除 -->
    <delete id="deleteById">
        DELETE FROM <include refid="tableName"/>
        WHERE id = #{id}
    </delete>

    <!-- 2. 批量删除 - 根据ID列表 -->
    <delete id="deleteByIds">
        DELETE FROM <include refid="tableName"/>
        WHERE id IN
        <foreach collection="ids" item="id" open="(" separator="," close=")">
            #{id}
        </foreach>
    </delete>

</mapper>
```

---

## 5️⃣ 使用示例

### UserService.java

```java
package com.example.service;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.mapper.dto.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    // ========== 查询示例 ==========

    /**
     * 根据ID查询用户
     */
    public User getUserById(Long userId) {
        UserQuery query = new UserQuery();
        query.setId(userId);
        return userMapper.findById(query);
    }

    /**
     * 动态条件查询
     */
    public List<User> searchUsers(String username, Integer status, Integer minAge) {
        UserQuery query = new UserQuery();
        query.setUsername("%" + username + "%");  // 模糊查询
        query.setStatus(status);
        query.setMinAge(minAge);
        return userMapper.findUsers(query);
    }

    /**
     * 批量查询
     */
    public List<User> getUsersByIds(List<Long> userIds) {
        UserQuery query = new UserQuery();
        query.setIds(userIds);
        return userMapper.findByIds(query);
    }

    /**
     * 统计用户数量
     */
    public Integer countActiveUsers() {
        UserQuery query = new UserQuery();
        query.setStatus(1);
        return userMapper.countUsers(query);
    }

    /**
     * 分页查询
     */
    public List<User> getUsersPage(int page, int size) {
        UserQuery query = new UserQuery();
        query.setPageSize(size);
        query.setOffset((page - 1) * size);
        return userMapper.findUsersPage(query);
    }

    /**
     * 高级查询
     */
    public List<User> advancedSearch(String keyword, List<Integer> statusList) {
        UserQuery query = new UserQuery();
        query.setKeyword(keyword);
        query.setStatusList(statusList);
        query.setOrderBy("age DESC, created_at DESC");
        return userMapper.findUsersAdvanced(query);
    }

    // ========== 插入示例 ==========

    /**
     * 创建用户(返回自增ID)
     */
    @Transactional
    public Long createUser(String username, String email, Integer age) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setAge(age);
        user.setStatus(1);

        // 返回自增ID
        Long userId = userMapper.insertUser(user);

        // 对象的ID也会被自动回填
        System.out.println("插入成功，ID: " + userId);
        System.out.println("对象ID: " + user.getId());

        return userId;
    }

    /**
     * 批量创建用户
     */
    @Transactional
    public int batchCreateUsers(List<User> users) {
        return userMapper.batchInsert(users);
    }

    // ========== 更新示例 ==========

    /**
     * 更新用户信息(只更新非null字段)
     */
    @Transactional
    public boolean updateUserInfo(Long userId, String username, String email) {
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setEmail(email);
        // age 和 status 为 null，不会被更新

        int rows = userMapper.updateUser(user);
        return rows > 0;
    }

    // ========== 删除示例 ==========

    /**
     * 删除用户
     */
    @Transactional
    public boolean deleteUser(Long userId) {
        User user = new User();
        user.setId(userId);

        int rows = userMapper.deleteById(user);
        return rows > 0;
    }

    /**
     * 批量删除用户
     */
    @Transactional
    public int batchDeleteUsers(List<Long> userIds) {
        UserQuery query = new UserQuery();
        query.setIds(userIds);
        return userMapper.deleteByIds(query);
    }
}
```

---

## 6️⃣ 完整的 Controller 示例

```java
package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 搜索用户
     */
    @GetMapping("/search")
    public List<User> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer minAge) {
        return userService.searchUsers(username, status, minAge);
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    public List<User> getUsersPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getUsersPage(page, size);
    }

    /**
     * 创建用户
     */
    @PostMapping
    public Long createUser(@RequestBody User user) {
        return userService.createUser(
                user.getUsername(),
                user.getEmail(),
                user.getAge()
        );
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public boolean updateUser(
            @PathVariable Long id,
            @RequestBody User user) {
        return userService.updateUserInfo(
                id,
                user.getUsername(),
                user.getEmail()
        );
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
```

---

## 7️⃣ 数据库表结构

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    email VARCHAR(100) NOT NULL COMMENT '邮箱',
    age INT COMMENT '年龄',
    status INT DEFAULT 1 COMMENT '状态: 1-正常 2-禁用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

---

## 8️⃣ 测试示例

```java
package com.example;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.mapper.dto.UserQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsertAndFind() {
        // 插入用户
        User user = new User();
        user.setUsername("test_user");
        user.setEmail("test@example.com");
        user.setAge(25);
        user.setStatus(1);

        Long userId = userMapper.insertUser(user);
        assertNotNull(userId);
        System.out.println("新增用户ID: " + userId);

        // 查询用户
        UserQuery query = new UserQuery();
        query.setId(userId);
        User found = userMapper.findById(query);

        assertNotNull(found);
        assertEquals("test_user", found.getUsername());
    }

    @Test
    void testDynamicQuery() {
        UserQuery query = new UserQuery();
        query.setStatus(1);
        query.setMinAge(18);
        query.setMaxAge(30);

        List<User> users = userMapper.findUsers(query);
        assertNotNull(users);

        users.forEach(user -> {
            assertTrue(user.getAge() >= 18 && user.getAge() <= 30);
            assertEquals(1, user.getStatus());
        });
    }

    @Test
    void testBatchInsert() {
        List<User> users = Arrays.asList(
                createUser("batch1", "batch1@example.com", 21),
                createUser("batch2", "batch2@example.com", 22),
                createUser("batch3", "batch3@example.com", 23)
        );

        int rows = userMapper.batchInsert(users);
        assertTrue(rows > 0);
    }

    private User createUser(String username, String email, Integer age) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setAge(age);
        user.setStatus(1);
        return user;
    }
}
```

---

## 📝 总结

本文档提供了 XML Mapper 的完整使用示例,涵盖:

✅ **查询操作** - 简单查询、动态SQL、IN查询、统计、分页、高级查询
✅ **插入操作** - 单条插入返回ID、批量插入
✅ **更新操作** - 动态更新(只更新非null字段)
✅ **删除操作** - 单条删除、批量删除
✅ **动态SQL** - `<if>`、`<where>`、`<set>`、`<foreach>` 标签
✅ **SQL复用** - `<sql>` 和 `<include>` 片段复用

更多功能请参考 [XML_MAPPER.md](./XML_MAPPER.md) 功能说明文档。

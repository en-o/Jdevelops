# JdbcTemplate XML Mapper - 快速开始

## 📖 简介

基于 JdbcTemplate 的 XML Mapper 功能，让你像使用 MyBatis 一样编写 SQL，但更轻量、更简单。

**核心特性：**
- ✅ XML 配置 SQL，清晰易读
- ✅ 动态 SQL 支持（if、foreach、where、set）
- ✅ SQL 片段复用
- ✅ 完美支持 Java Record 类
- ✅ 自动扫描加载
- ✅ Spring Boot 自动配置

## 🚀 3 步快速开始

### 第一步：配置启用

在 `application.yml` 中启用 XML Mapper：

```yaml
jdevelops:
  jdbc:
    xmlmapper:
      enabled: true  # 启用 XML Mapper
      # locations: classpath*:jmapper/**/*.xml  # 可选，默认扫描 jmapper 目录
```

### 第二步：创建 XML 文件

在 `resources/jmapper/` 目录下创建 `UserMapper.xml`：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="cn.tannn.jdevelops.mapper.UserMapper">

    <!-- 查询用户 -->
    <select id="findById" resultType="cn.tannn.example.User">
        SELECT id, username, email, age, status
        FROM users WHERE id = #{id}
    </select>

    <!-- 动态查询 -->
    <select id="findUsers" resultType="cn.tannn.example.User">
        SELECT id, username, email, age, status
        FROM users
        <where>
            <if test="username != null and username != ''">
                AND username LIKE #{username}
            </if>
            <if test="status != null">
                AND status = #{status}
            </if>
        </where>
    </select>

    <!-- 插入用户 -->
    <insert id="insertUser">
        INSERT INTO users (username, email, age, status)
        VALUES (#{username}, #{email}, #{age}, #{status})
    </insert>

    <!-- 动态更新 -->
    <update id="updateUser">
        UPDATE users
        <set>
            <if test="username != null">username = #{username},</if>
            <if test="email != null">email = #{email},</if>
            <if test="age != null">age = #{age},</if>
        </set>
        WHERE id = #{id}
    </update>

</mapper>
```

### 第三步：在代码中使用

```java
@Service
public class UserService {

    @Autowired
    private XmlMapperRegistry xmlMapperRegistry;

    private static final String NAMESPACE = "cn.tannn.jdevelops.mapper.UserMapper";

    // 查询
    public User findById(Long id) {
        UserQuery query = new UserQuery();
        query.setIds(Arrays.asList(id));

        return (User) xmlMapperRegistry.executeQuery(
            NAMESPACE, "findById", query, User.class
        );
    }

    // 动态查询
    @SuppressWarnings("unchecked")
    public List<User> findUsers(String username, Integer status) {
        UserQuery query = new UserQuery();
        query.setUsername(username);
        query.setStatus(status);

        return (List<User>) xmlMapperRegistry.executeQuery(
            NAMESPACE, "findUsers", query, User.class
        );
    }

    // 插入
    public int insertUser(User user) {
        return xmlMapperRegistry.executeUpdate(
            NAMESPACE, "insertUser", user
        );
    }

    // 更新
    public int updateUser(User user) {
        return xmlMapperRegistry.executeUpdate(
            NAMESPACE, "updateUser", user
        );
    }
}
```

### 可选：定义 Mapper 接口（更规范）

除了直接使用 `XmlMapperRegistry`，你还可以定义接口来获得更好的类型安全：

```java
import cn.tannn.jdevelops.annotations.jdbctemplate.xml.*;

@XmlMapper(namespace = "cn.tannn.jdevelops.mapper.UserMapper")
public interface UserMapper {

    @XmlSelect("findById")
    User findById(UserQuery query);

    @XmlSelect("findUsers")
    List<User> findUsers(UserQuery query);

    @XmlInsert("insertUser")
    int insertUser(User user);

    @XmlUpdate("updateUser")
    int updateUser(User user);

    @XmlDelete("deleteById")
    int deleteById(User user);
}
```

**注解说明：**
- `@XmlMapper`: 标记接口为 XML Mapper 接口（包：`cn.tannn.jdevelops.annotations.jdbctemplate.xml`）
  - `namespace`: 对应 XML 文件的命名空间（可选，默认使用接口全限定名）
  - `xmlPath`: 对应的 XML 文件路径（可选）
- `@XmlSelect`: 标记查询方法（对应 XML 中的 `<select>`）
  - `value`: SQL 语句 ID
  - `tryc`: 是否吞掉异常（可选，默认 false）
- `@XmlInsert`: 标记插入方法（对应 XML 中的 `<insert>`）
- `@XmlUpdate`: 标记更新方法（对应 XML 中的 `<update>`）
- `@XmlDelete`: 标记删除方法（对应 XML 中的 `<delete>`）


## 🎯 核心概念

### 1. Namespace（命名空间）

XML 文件中的 `namespace` 用于区分不同的 Mapper：

```xml
<mapper namespace="cn.tannn.jdevelops.mapper.UserMapper">
    <!-- SQL 语句 -->
</mapper>
```

**规范：**
- 使用包名格式，如 `cn.tannn.jdevelops.mapper.UserMapper`
- 全局唯一
- 在代码中通过 namespace 调用 SQL

### 2. Statement ID（语句ID）

每个 SQL 语句都有唯一的 ID：

```xml
<select id="findById" resultType="User">
    SELECT * FROM users WHERE id = #{id}
</select>
```

**规范：**
- 同一 namespace 下唯一
- 使用动词+名词格式：`findById`、`findUsers`、`updateUser`

### 3. 参数绑定

#### `#{}` - 预编译参数（推荐）

```xml
SELECT * FROM users WHERE username = #{username}
```

- ✅ 防止 SQL 注入
- ✅ 自动类型转换
- ✅ 性能更好

#### `${}` - 直接替换（慎用）

```xml
SELECT * FROM ${tableName} ORDER BY ${orderBy}
```

- ⚠️ 存在 SQL 注入风险
- ⚠️ 仅用于表名、列名、排序字段等

### 4. 返回类型

使用 `resultType` 指定返回类型：

```xml
<!-- 返回实体类 -->
<select id="findById" resultType="cn.tannn.example.User">
    SELECT * FROM users WHERE id = #{id}
</select>

<!-- 返回基本类型 -->
<select id="count" resultType="java.lang.Integer">
    SELECT COUNT(*) FROM users
</select>

<!-- 返回 Record（完美支持） -->
<select id="findById" resultType="cn.tannn.example.UserRecord">
    SELECT id, username, email FROM users WHERE id = #{id}
</select>
```

## 🎨 动态 SQL 标签

### `<if>` - 条件判断

```xml
<if test="username != null and username != ''">
    AND username LIKE #{username}
</if>
```

### `<where>` - WHERE 子句

自动添加 WHERE，自动去除开头的 AND/OR：

```xml
<where>
    <if test="username != null">AND username = #{username}</if>
    <if test="status != null">AND status = #{status}</if>
</where>
```

### `<set>` - SET 子句

自动去除末尾的逗号：

```xml
<set>
    <if test="username != null">username = #{username},</if>
    <if test="email != null">email = #{email},</if>
</set>
```

### `<foreach>` - 循环

用于 IN 查询和批量操作：

```xml
<!-- IN 查询 -->
WHERE id IN
<foreach collection="ids" item="id" open="(" separator="," close=")">
    #{id}
</foreach>

<!-- 批量插入 -->
INSERT INTO users (username, email) VALUES
<foreach collection="users" item="user" separator=",">
    (#{user.username}, #{user.email})
</foreach>
```

### `<sql>` 和 `<include>` - SQL 片段复用

```xml
<!-- 定义片段 -->
<sql id="columns">id, username, email, age, status</sql>

<!-- 引用片段 -->
<select id="findById">
    SELECT <include refid="columns"/>
    FROM users WHERE id = #{id}
</select>
```

## 💡 实用示例

### 示例 1：条件查询

```xml
<select id="findUsers" resultType="User">
    SELECT * FROM users
    <where>
        <if test="username != null and username != ''">
            AND username LIKE CONCAT('%', #{username}, '%')
        </if>
        <if test="email != null and email != ''">
            AND email = #{email}
        </if>
        <if test="status != null">
            AND status = #{status}
        </if>
        <if test="minAge != null">
            AND age >= #{minAge}
        </if>
        <if test="maxAge != null">
            AND age <= #{maxAge}
        </if>
    </where>
    ORDER BY created_at DESC
</select>
```

### 示例 2：批量查询

```xml
<select id="findByIds" resultType="User">
    SELECT * FROM users
    WHERE id IN
    <foreach collection="ids" item="id" open="(" separator="," close=")">
        #{id}
    </foreach>
</select>
```

### 示例 3：批量插入

```xml
<insert id="batchInsert">
    INSERT INTO users (username, email, age) VALUES
    <foreach collection="users" item="user" separator=",">
        (#{user.username}, #{user.email}, #{user.age})
    </foreach>
</insert>
```

### 示例 4：动态更新

```xml
<update id="updateUser">
    UPDATE users
    <set>
        <if test="username != null">username = #{username},</if>
        <if test="email != null">email = #{email},</if>
        <if test="age != null">age = #{age},</if>
        <if test="status != null">status = #{status},</if>
    </set>
    WHERE id = #{id}
</update>
```

### 示例 5：复杂查询

```xml
<select id="findUsersAdvanced" resultType="User">
    SELECT * FROM users
    <where>
        <!-- 关键字搜索 -->
        <if test="keyword != null and keyword != ''">
            AND (username LIKE CONCAT('%', #{keyword}, '%')
                 OR email LIKE CONCAT('%', #{keyword}, '%'))
        </if>
        <!-- 状态列表 -->
        <if test="statusList != null and statusList.size() > 0">
            AND status IN
            <foreach collection="statusList" item="status" open="(" separator="," close=")">
                #{status}
            </foreach>
        </if>
        <!-- 日期范围 -->
        <if test="startDate != null">
            AND created_at >= #{startDate}
        </if>
        <if test="endDate != null">
            AND created_at <= #{endDate}
        </if>
    </where>
    <!-- 动态排序 -->
    ORDER BY
    <if test="orderBy != null and orderBy != ''">
        ${orderBy}
    </if>
    <if test="orderBy == null or orderBy == ''">
        created_at DESC
    </if>
</select>
```

## 📌 最佳实践

### 1. 使用专门的查询对象

```java
// ✅ 推荐
public class UserQuery {
    private String username;
    private Integer status;
    private List<Long> ids;
    // getters and setters
}

// ❌ 不推荐
Map<String, Object> params = new HashMap<>();
```

### 2. 定义常量 Namespace

```java
public class UserMapperNamespace {
    public static final String NAMESPACE = "cn.tannn.jdevelops.mapper.UserMapper";

    public static final String FIND_BY_ID = "findById";
    public static final String FIND_USERS = "findUsers";
    public static final String INSERT_USER = "insertUser";
}
```

### 3. SQL 片段复用

```xml
<!-- 定义常用片段 -->
<sql id="baseColumns">
    id, username, email, age, status, created_at, updated_at
</sql>

<sql id="commonWhere">
    <if test="status != null">AND status = #{status}</if>
    <if test="deleted != null">AND deleted = #{deleted}</if>
</sql>

<!-- 在多个查询中复用 -->
<select id="findById">
    SELECT <include refid="baseColumns"/>
    FROM users WHERE id = #{id}
</select>

<select id="findUsers">
    SELECT <include refid="baseColumns"/>
    FROM users
    <where>
        <include refid="commonWhere"/>
        <if test="username != null">
            AND username = #{username}
        </if>
    </where>
</select>
```

### 4. 安全防护

```xml
<!-- ✅ 安全：使用 #{} -->
WHERE username = #{username}

<!-- ⚠️ 危险：仅在必要时使用 ${} -->
ORDER BY ${orderBy}
```

### 5. 异常处理

```xml
<!-- 添加 tryc="true" 可以静默处理异常 -->
<select id="findById" resultType="User" tryc="true">
    SELECT * FROM users WHERE id = #{id}
</select>
```

## ⚙️ 配置说明

### 完整配置示例

```yaml
jdevelops:
  jdbctemplate:
    xmlmapper:
      # 是否启用（必填）
      enabled: true
      # XML 文件位置（可选，默认 classpath*:jmapper/**/*.xml）
      locations: classpath*:jmapper/**/*.xml
```

### 配置说明

| 配置项 | 说明 | 默认值 | 必填 |
|--------|------|--------|------|
| `enabled` | 是否启用 XML Mapper | false | 是 |
| `locations` | XML 文件扫描路径 | classpath*:jmapper/**/*.xml | 否 |

### 路径配置

支持 Spring 资源路径通配符：

```yaml
# 单个目录
locations: classpath:jmapper/*.xml

# 多级目录（推荐）
locations: classpath*:jmapper/**/*.xml

# 多个路径（逗号分隔）
locations: classpath:jmapper/*.xml,classpath:sql/*.xml
```

## 📚 完整示例

项目中包含完整的测试示例：

```
src/test/
├── java/cn/tannn/jdevelops/jdectemplate/xmlmapper/
│   ├── XmlMapperTest.java          # 测试用例
│   └── example/
│       ├── User.java               # 实体类
│       ├── UserQuery.java          # 查询对象
│       └── UserMapper.java         # Mapper 接口（可选）
└── resources/
    └── jmapper/
        └── UserMapper.xml          # XML 配置
```

## 🆚 与 MyBatis 对比

| 特性 | MyBatis | JdbcTemplate XML Mapper |
|------|---------|-------------------------|
| 学习成本 | 较高 | 低 |
| 配置复杂度 | 较高 | 简单（Spring Boot 自动配置） |
| Mapper 接口 | 必须 | 可选 |
| Record 支持 | 需配置 | 原生支持 |
| 依赖 | 多个 | 仅 Spring JDBC |
| 适用场景 | 大型项目 | 中小型项目 |

## ❓ 常见问题

### 1. XML 文件放在哪里？

默认放在 `resources/jmapper/` 目录下，支持多级子目录。

### 2. 如何调试 SQL？

开启 SQL 日志：

```yaml
logging:
  level:
    cn.tannn.jdevelops.jdectemplate.xmlmapper.executor.XmlSqlExecutor: DEBUG
```

### 3. 支持 Record 吗？

完全支持！使用 `DataClassRowMapper`，无需额外配置：

```java
public record UserRecord(Long id, String username, String email) {}
```

```xml
<select id="findById" resultType="cn.tannn.example.UserRecord">
    SELECT id, username, email FROM users WHERE id = #{id}
</select>
```

### 4. 如何处理异常？

```xml
<!-- 方式一：添加 tryc 属性 -->
<select id="findById" tryc="true">
    SELECT * FROM users WHERE id = #{id}
</select>

<!-- 方式二：在代码中捕获 -->
try {
    User user = findById(id);
} catch (Exception e) {
    // 处理异常
}
```

### 5. 性能如何？

- 首次加载时解析 XML（毫秒级）
- 后续查询使用缓存，无性能损耗
- SQL 执行性能与原生 JdbcTemplate 相同

## 📖 更多信息

- **详细文档**: 查看 `XML_MAPPER.md`
- **测试示例**: 查看 `src/test` 目录
- **问题反馈**: 提交 Issue

## 🎉 总结

JdbcTemplate XML Mapper 让你以 MyBatis 的方式编写 SQL，但更轻量、更简单：

1. ✅ **3 步即可上手**：配置 → 编写 XML → 调用
2. ✅ **动态 SQL 强大**：if、foreach、where、set 一应俱全
3. ✅ **原生 Record 支持**：拥抱现代 Java
4. ✅ **安全可靠**：预编译参数防止注入
5. ✅ **轻量高效**：零额外依赖，Spring Boot 自动配置

开始使用吧！🚀

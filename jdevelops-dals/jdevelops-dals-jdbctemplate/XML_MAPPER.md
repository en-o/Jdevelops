# JdbcTemplate XML Mapper

基于 JdbcTemplate 实现的类似 MyBatis XML Mapper 功能，让 SQL 编写更加清晰，审查更加方便。

## 特性

- ✅ **XML 配置 SQL**：类似 MyBatis 的 XML 语法，SQL 清晰易读
- ✅ **动态 SQL 支持**：支持 `<if>`、`<foreach>`、`<where>`、`<set>` 等动态标签
- ✅ **SQL 复用**：通过 `<sql>` 和 `<include>` 实现 SQL 片段复用
- ✅ **参数绑定**：支持 `#{param}` 预编译参数和 `${param}` 直接替换
- ✅ **自动扫描**：自动扫描并加载指定路径的 XML Mapper 文件
- ✅ **Record 支持**：使用 `DataClassRowMapper`，完美支持 Java Record 类
- ✅ **简化开发**：无需编写大量 Java 代码，SQL 维护更方便

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.tannn.jdevelops</groupId>
    <artifactId>jdevelops-dals-jdbctemplate</artifactId>
    <version>最新版本</version>
</dependency>
```

### 2. 配置文件

在 `application.yml` 中启用 XML Mapper 功能：

```yaml
jdevelops:
  jdbc:
    xmlmapper:
      # 启用 XML Mapper 功能（必须设置为 true）
      enabled: true
      # XML Mapper 文件位置（支持 Spring 通配符）
      # 默认值：classpath*:jmapper/**/*.xml
      locations: classpath*:jmapper/**/*.xml
```

**配置说明：**
- `enabled`: 必须设置为 `true` 才能启用 XML Mapper 功能
- `locations`: XML 文件扫描路径，支持 `classpath*:` 前缀和通配符 `**/*.xml`
- 默认扫描 `resources/jmapper/` 目录下的所有 `.xml` 文件

### 3. 创建 XML Mapper 文件

在 `resources/jmapper/` 目录下创建 `UserMapper.xml`：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="cn.tannn.jdevelops.jdectemplate.xmlmapper.example.UserMapper">

    <!-- SQL 片段，可复用 -->
    <sql id="baseColumns">
        id, username, email, age, status, created_at
    </sql>

    <!-- 简单查询 -->
    <select id="findById" resultType="cn.tannn.jdevelops.jdectemplate.xmlmapper.example.User">
        SELECT
        <include refid="baseColumns"/>
        FROM users
        WHERE id = #{id}
    </select>

    <!-- 动态 SQL 查询 -->
    <select id="findUsers" resultType="cn.tannn.jdevelops.jdectemplate.xmlmapper.example.User">
        SELECT
        <include refid="baseColumns"/>
        FROM users
        <where>
            <if test="username != null and username != ''">
                AND username LIKE #{username}
            </if>
            <if test="email != null and email != ''">
                AND email = #{email}
            </if>
            <if test="status != null">
                AND status = #{status}
            </if>
        </where>
        ORDER BY created_at DESC
    </select>

    <!-- 批量查询 -->
    <select id="findByIds" resultType="cn.tannn.jdevelops.jdectemplate.xmlmapper.example.User">
        SELECT
        <include refid="baseColumns"/>
        FROM users
        WHERE id IN
        <foreach collection="ids" item="id" open="(" separator="," close=")">
            #{id}
        </foreach>
    </select>

    <!-- 插入 -->
    <insert id="insertUser">
        INSERT INTO users (username, email, age, status, created_at)
        VALUES (#{username}, #{email}, #{age}, #{status}, NOW())
    </insert>

    <!-- 动态更新 -->
    <update id="updateUser">
        UPDATE users
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

    <!-- 删除 -->
    <delete id="deleteById">
        DELETE FROM users WHERE id = #{id}
    </delete>

</mapper>
```

**XML 文件说明：**
- `namespace`: 命名空间，建议使用包名格式，必须唯一
- `id`: SQL 语句的唯一标识符，在同一个 namespace 下必须唯一
- `resultType`: 返回结果的类型（全限定类名），支持普通类和 Record 类
- XML 文件必须放在配置的扫描路径下（默认 `resources/jmapper/`）

### 4. 使用示例

#### 方式一：使用 XmlMapperRegistry（推荐）

```java
@Service
public class UserService {

    @Autowired
    private XmlMapperRegistry xmlMapperRegistry;

    // namespace 常量
    private static final String NAMESPACE = "cn.tannn.jdevelops.jdectemplate.xmlmapper.example.UserMapper";

    /**
     * 根据 ID 查询用户
     */
    public User findById(Long id) {
        UserQuery query = new UserQuery();
        query.setIds(Arrays.asList(id));

        return (User) xmlMapperRegistry.executeQuery(
                NAMESPACE,
                "findById",
                query,
                User.class
        );
    }

    /**
     * 动态条件查询用户
     */
    @SuppressWarnings("unchecked")
    public List<User> findUsers(String username, String email, Integer status) {
        UserQuery query = new UserQuery();
        query.setUsername(username);
        query.setEmail(email);
        query.setStatus(status);

        return (List<User>) xmlMapperRegistry.executeQuery(
                NAMESPACE,
                "findUsers",
                query,
                User.class
        );
    }

    /**
     * 批量查询
     */
    @SuppressWarnings("unchecked")
    public List<User> findByIds(List<Long> ids) {
        UserQuery query = new UserQuery();
        query.setIds(ids);

        return (List<User>) xmlMapperRegistry.executeQuery(
                NAMESPACE,
                "findByIds",
                query,
                User.class
        );
    }

    /**
     * 插入用户
     */
    public int insertUser(User user) {
        return xmlMapperRegistry.executeUpdate(
                NAMESPACE,
                "insertUser",
                user
        );
    }

    /**
     * 更新用户
     */
    public int updateUser(User user) {
        return xmlMapperRegistry.executeUpdate(
                NAMESPACE,
                "updateUser",
                user
        );
    }

    /**
     * 删除用户
     */
    public int deleteById(Long id) {
        User user = new User();
        user.setId(id);

        return xmlMapperRegistry.executeUpdate(
                NAMESPACE,
                "deleteById",
                user
        );
    }
}
```

#### 方式二：定义接口（可选，更规范）

1. 创建 Mapper 接口：

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


2. 在服务中使用：

```java
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findById(Long id) {
        UserQuery query = new UserQuery();
        query.setIds(Arrays.asList(id));
        return userMapper.findById(query);
    }
}
```

## 支持的动态标签

### 1. `<if>` 条件判断

根据条件动态添加 SQL 片段：

```xml
<if test="username != null and username != ''">
    AND username = #{username}
</if>
```

**支持的条件表达式：**
- `param != null` - 非空判断
- `param != ''` - 非空字符串判断
- `param == value` - 相等判断
- `param > value` - 大于判断
- `param >= value` - 大于等于判断
- `param < value` - 小于判断
- `param <= value` - 小于等于判断
- `condition1 and condition2` - 与运算
- `condition1 or condition2` - 或运算

### 2. `<foreach>` 循环

用于处理集合参数，常用于 IN 查询和批量操作：

```xml
<foreach collection="ids" item="id" open="(" separator="," close=")">
    #{id}
</foreach>
```

**属性说明：**
- `collection`: 集合/数组的参数名（必填）
- `item`: 循环变量名（必填）
- `index`: 索引变量名（可选）
- `open`: 开始字符（可选）
- `separator`: 分隔符（可选）
- `close`: 结束字符（可选）

**示例：批量插入**
```xml
<insert id="batchInsert">
    INSERT INTO users (username, email, age) VALUES
    <foreach collection="users" item="user" separator=",">
        (#{user.username}, #{user.email}, #{user.age})
    </foreach>
</insert>
```

### 3. `<where>` 子句

自动去除开头的 AND 或 OR，并在有条件时添加 WHERE 关键字：

```xml
<where>
    <if test="username != null">
        AND username = #{username}
    </if>
    <if test="email != null">
        AND email = #{email}
    </if>
</where>
```

**处理逻辑：**
- 如果内部有内容，自动添加 `WHERE` 关键字
- 自动去除第一个条件前面的 `AND` 或 `OR`
- 如果内部没有内容，则不添加 `WHERE`

### 4. `<set>` 子句

自动去除末尾的逗号，用于 UPDATE 语句：

```xml
<set>
    <if test="username != null">
        username = #{username},
    </if>
    <if test="email != null">
        email = #{email},
    </if>
</set>
```

**处理逻辑：**
- 自动添加 `SET` 关键字
- 自动去除最后一个逗号
- 确保至少有一个字段被更新

### 5. `<sql>` 和 `<include>` SQL 片段复用

定义可复用的 SQL 片段：

```xml
<!-- 定义 SQL 片段 -->
<sql id="baseColumns">
    id, username, email, age, status
</sql>

<!-- 引用 SQL 片段 -->
<select id="findById">
    SELECT
    <include refid="baseColumns"/>
    FROM users WHERE id = #{id}
</select>
```

## 参数绑定

### 1. `#{}` 预编译参数（推荐）

使用预编译参数，防止 SQL 注入：

```xml
SELECT * FROM users WHERE username = #{username}
```

**转换结果：**
```sql
-- 编译后的 SQL
SELECT * FROM users WHERE username = ?
-- 参数会被安全地绑定
```

**优点：**
- 防止 SQL 注入
- 性能更好（预编译）
- 自动类型转换

### 2. `${}` 直接替换（慎用）

直接替换为字符串，**存在 SQL 注入风险**：

```xml
SELECT * FROM ${tableName} ORDER BY ${orderBy}
```

**转换结果：**
```sql
-- 直接替换
SELECT * FROM users ORDER BY created_at DESC
```

**使用场景：**
- 动态表名
- 动态列名
- 动态排序字段

**⚠️ 警告：** 仅在必要时使用，并确保输入来源可信，避免 SQL 注入！

## 表达式语法

支持的条件表达式（基于 OGNL）：

```xml
<!-- 非空判断 -->
<if test="username != null">...</if>

<!-- 非空字符串判断 -->
<if test="username != null and username != ''">...</if>

<!-- 数值比较 -->
<if test="age > 18">...</if>
<if test="age >= 18">...</if>
<if test="age < 60">...</if>
<if test="age <= 60">...</if>

<!-- 相等判断 -->
<if test="status == 1">...</if>

<!-- 逻辑运算 -->
<if test="username != null and age > 18">...</if>
<if test="status == 1 or status == 2">...</if>

<!-- 集合判断 -->
<if test="ids != null and ids.size() > 0">...</if>
```

## 高级特性

### 1. 批量操作

```xml
<insert id="batchInsert">
    INSERT INTO users (username, email, age) VALUES
    <foreach collection="users" item="user" separator=",">
        (#{user.username}, #{user.email}, #{user.age})
    </foreach>
</insert>
```

### 2. 复杂查询

```xml
<select id="findUsersAdvanced">
    SELECT * FROM users
    <where>
        <if test="keyword != null and keyword != ''">
            AND (username LIKE CONCAT('%', #{keyword}, '%')
            OR email LIKE CONCAT('%', #{keyword}, '%'))
        </if>
        <if test="statusList != null and statusList.size() > 0">
            AND status IN
            <foreach collection="statusList" item="status" open="(" separator="," close=")">
                #{status}
            </foreach>
        </if>
    </where>
</select>
```

### 3. 异常处理

添加 `tryc="true"` 属性可以静默处理查询异常：

```xml
<select id="findById" resultType="User" tryc="true">
    SELECT * FROM users WHERE id = #{id}
</select>
```

**说明：**
- 当 `tryc="true"` 时，查询失败不抛出异常，返回 null 或空集合
- 更新操作失败时返回 0
- 适用于非关键查询场景

### 4. 支持 Java Record

使用 `DataClassRowMapper`，完美支持 Java Record 类：

```java
// 定义 Record
public record UserRecord(Long id, String username, String email, Integer age) {}

// 在 XML 中使用
<select id="findById" resultType="cn.tannn.example.UserRecord">
    SELECT id, username, email, age FROM users WHERE id = #{id}
</select>

// 查询
UserRecord user = (UserRecord) xmlMapperRegistry.executeQuery(
    NAMESPACE, "findById", query, UserRecord.class
);
```

## 对比 MyBatis

### 相同点
- ✅ XML 配置 SQL 语句
- ✅ 支持动态 SQL（if, foreach, where, set）
- ✅ 支持 SQL 片段复用
- ✅ 参数绑定（#{} 和 ${}）
- ✅ 表达式语法（OGNL）

### 不同点

| 特性 | MyBatis | JdbcTemplate XML Mapper |
|------|---------|------------------------|
| **Mapper 接口** | 必须定义 | 可选（直接使用 Registry） |
| **ResultMap** | 支持 | 不支持（使用 DataClassRowMapper） |
| **缓存** | 内置支持 | 不支持（需自行实现） |
| **插件机制** | 支持 | 不支持 |
| **依赖** | 较多 | 仅依赖 Spring JDBC |
| **Record 支持** | 需额外配置 | 原生支持 |
| **集成方式** | 独立框架 | Spring Boot 自动配置 |
| **学习成本** | 较高 | 较低 |

## 最佳实践

### 1. 命名规范

- **namespace**: 使用包名格式，如 `cn.tannn.jdevelops.mapper.UserMapper`
- **SQL ID**: 使用动词+名词格式，如 `findById`、`updateUser`、`deleteByIds`
- **SQL 片段 ID**: 使用名词格式，如 `baseColumns`、`whereClause`

### 2. 参数对象

优先使用专门的查询对象，而不是 Map：

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
params.put("username", "test");
params.put("status", 1);
```

### 3. SQL 片段复用

将常用的列、条件定义为 SQL 片段：

```xml
<!-- 基础列 -->
<sql id="baseColumns">
    id, username, email, age, status, created_at, updated_at
</sql>

<!-- 公共条件 -->
<sql id="commonWhere">
    <if test="status != null">
        AND status = #{status}
    </if>
    <if test="deleted != null">
        AND deleted = #{deleted}
    </if>
</sql>
```

### 4. 动态 SQL

合理使用 `<if>` 和 `<where>` 标签处理可选条件：

```xml
<select id="findUsers">
    SELECT <include refid="baseColumns"/> FROM users
    <where>
        <if test="username != null and username != ''">
            AND username LIKE #{username}
        </if>
        <if test="status != null">
            AND status = #{status}
        </if>
    </where>
</select>
```

### 5. 安全性

- **优先使用** `#{}` 而不是 `${}`，防止 SQL 注入
- 仅在必要时使用 `${}`（表名、列名、排序字段等）
- 对用户输入进行验证和过滤

```xml
<!-- ✅ 安全 -->
SELECT * FROM users WHERE username = #{username}

<!-- ⚠️ 危险（仅在必要时使用） -->
SELECT * FROM ${tableName} ORDER BY ${orderBy}
```

### 6. 代码审查

- XML 格式让 SQL 清晰可见，便于审查
- 集中管理 SQL，便于优化和维护
- 使用版本控制跟踪 SQL 变更

## 注意事项

1. **XML 位置**：XML 文件必须放在配置的扫描路径下（默认 `resources/jmapper/`）
2. **命名空间**：namespace 需要唯一，建议使用包名格式
3. **参数类型**：参数对象支持 POJO、Map、基本类型
4. **结果类型**：resultType 指定返回类型的全限定名，支持普通类和 Record
5. **SQL 注入**：谨慎使用 `${}` 语法，避免 SQL 注入风险
6. **性能**：首次加载时会扫描并解析 XML，后续查询使用缓存
7. **热加载**：暂不支持 XML 文件热加载，需重启应用

## 完整示例

完整的测试示例请查看：
- **XML 文件**: `src/test/resources/jmapper/UserMapper.xml`
- **测试代码**: `src/test/java/cn/tannn/jdevelops/jdectemplate/xmlmapper/XmlMapperTest.java`
- **实体类**: `src/test/java/cn/tannn/jdevelops/jdectemplate/xmlmapper/example/User.java`
- **查询对象**: `src/test/java/cn/tannn/jdevelops/jdectemplate/xmlmapper/example/UserQuery.java`
- **Mapper 接口**: `src/test/java/cn/tannn/jdevelops/jdectemplate/xmlmapper/example/UserMapper.java`

## 总结

XML Mapper 功能为 JdbcTemplate 带来了类似 MyBatis 的开发体验：

- **清晰易读**：SQL 与 Java 代码分离，便于审查和维护
- **动态灵活**：支持丰富的动态 SQL 标签
- **安全可靠**：预编译参数防止 SQL 注入
- **轻量高效**：基于 JdbcTemplate，无额外依赖
- **现代支持**：原生支持 Java Record 类

是一个轻量级的 SQL 管理方案，适合中小型项目和简单场景。

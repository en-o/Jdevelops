# XML Mapper 功能说明

## 📖 简介

XML Mapper 是 JdbcTemplate 的 XML 配置增强功能,类似 MyBatis 的 XML Mapper 机制,但更加轻量、简洁。
> 主要是因为我们使用的大部分是jpa，原来构建的一个链式sql又不好审查所有有了这个

### 特性

- ✅ **类 MyBatis 语法** - 熟悉的 XML 配置方式
- ✅ **动态 SQL** - 支持 `<if>`、`<where>`、`<foreach>`、`<set>` 等标签
- ✅ **类型安全** - 通过接口注解方式,提供编译时类型检查
- ✅ **自增ID返回** - INSERT 操作支持返回自增ID并自动回填
- ✅ **灵活的返回类型** - 自动适配方法返回类型(int/Long/String等)
- ✅ **SQL 复用** - 通过 `<sql>` 和 `<include>` 实现 SQL 片段复用
- ✅ **命名参数** - 使用 `#{paramName}` 方式引用参数
- ✅ **无需繁重配置** - 自动扫描 XML 文件,开箱即用

### 适用场景

- 复杂 SQL 查询(多表关联、复杂条件)
- 动态 SQL 场景(根据条件拼接 SQL)
- 需要 SQL 与代码分离
- 团队熟悉 XML 配置方式

---

## 🚀 快速开始

### 1. 添加依赖

```xml
<dependency>
  <groupId>cn.tannn.jdevelops</groupId>
  <artifactId>jdevelops-dals-jdbctemplate</artifactId>
  <!-- 1.0.4开始  -->
  <version>${latest.version}</version>
</dependency>
```

### 2. 配置 XML 扫描路径

在 `application.yml` 中配置全局扫描路径，框架启动时自动加载所有匹配的 XML 文件：

```yaml
spring:
  jdevelops:
    jdbctemplate:
      xml-mapper:
        # 是否启用 XML Mapper 功能（默认 true）
        enabled: true
        # XML Mapper 文件扫描路径（支持通配符和多路径）
        locations: classpath*:jmapper/**/*.xml
```

**配置说明：**
- `enabled`: 是否启用 XML Mapper 功能，默认 `true`
- `locations`: XML 文件扫描路径，支持：
  - `classpath:` - 扫描当前项目类路径
  - `classpath*:` - 扫描所有 jar 包和类路径（推荐，支持依赖包中的 XML）
  - `**/*.xml` - 通配符，递归扫描所有子目录的 XML 文件
  - 多路径配置：`classpath*:jmapper/**/*.xml,classpath*:mapper/**/*.xml`

### 3. 创建 XML Mapper 文件

在 `resources/jmapper/` 下创建 `UserMapper.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.example.mapper.UserMapper">
    <!-- 查询用户 -->
    <select id="findById" resultType="com.example.entity.User">
        SELECT * FROM users WHERE id = #{id}
    </select>

    <!-- 插入用户(返回自增ID) -->
    <insert id="insertUser" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO users (username, email, age)
        VALUES (#{username}, #{email}, #{age})
    </insert>
</mapper>
```

### 4. 创建 Mapper 接口

```java
@XmlMapper(namespace = "com.example.mapper.UserMapper")
public interface UserMapper {

    @XmlSelect("findById")
    User findById(UserQuery query);

    @XmlInsert("insertUser")
    Long insertUser(User user);
}
```

### 5. 使用 Mapper

```java
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void example() {
        // 查询
        UserQuery query = new UserQuery();
        query.setId(1L);
        User user = userMapper.findById(query);

        // 插入
        User newUser = new User();
        newUser.setUsername("test");
        newUser.setEmail("test@example.com");
        newUser.setAge(25);

        Long userId = userMapper.insertUser(newUser);  // 返回自增ID
        System.out.println("新增用户ID: " + userId);
        System.out.println("对象ID已回填: " + newUser.getId());
    }
}
```

---

## 📚 核心功能

### 1. SQL 标签

#### `<select>` - 查询操作

```xml
<select id="findUsers" resultType="com.example.entity.User">
    SELECT * FROM users WHERE status = #{status}
</select>
```

**属性:**
- `id` **(必填)** - SQL 语句唯一标识,对应接口方法名
- `resultType` - 返回结果类型(全限定类名)
- `parameterType` - 参数类型(全限定类名)
- `tryc` - 是否吞掉异常(默认 false)
- `timeout` - 超时时间(秒)

#### `<insert>` - 插入操作

```xml
<insert id="insertUser" useGeneratedKeys="true" keyProperty="id" keyColumn="id">
    INSERT INTO users (username, email, age)
    VALUES (#{username}, #{email}, #{age})
</insert>
```

**属性:**
- `id` **(必填)** - SQL 语句唯一标识
- `useGeneratedKeys` - 是否返回自增ID(默认 false)
- `keyProperty` - 接收自增ID的属性名(默认 id)
- `keyColumn` - 数据库自增列名(默认 id)
- `tryc` - 是否吞掉异常
- `timeout` - 超时时间(秒)

**返回值处理:**
- `useGeneratedKeys=false` → 返回影响行数(int)
- `useGeneratedKeys=true` → 返回自增ID(根据方法返回类型自动转换)

#### `<update>` - 更新操作

```xml
<update id="updateUser">
    UPDATE users SET username = #{username} WHERE id = #{id}
</update>
```

#### `<delete>` - 删除操作

```xml
<delete id="deleteById">
    DELETE FROM users WHERE id = #{id}
</delete>
```

### 2. 动态 SQL 标签

#### `<if>` - 条件判断

```xml
<select id="findUsers" resultType="User">
    SELECT * FROM users
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

**test 表达式:**
- 支持 null 判断: `test="username != null"`
- 支持空字符串判断: `test="username != ''"`
- 支持逻辑运算: `test="age != null and age > 18"`

#### `<where>` - WHERE 子句

```xml
<where>
    <if test="username != null">
        AND username = #{username}
    </if>
</where>
```

**特性:**
- 自动添加 WHERE 关键字
- 自动去除第一个 AND 或 OR

#### `<set>` - SET 子句

```xml
<update id="updateUser">
    UPDATE users
    <set>
        <if test="username != null">
            username = #{username},
        </if>
        <if test="email != null">
            email = #{email},
        </if>
    </set>
    WHERE id = #{id}
</update>
```

**特性:**
- 自动添加 SET 关键字
- 自动去除最后一个逗号

#### `<foreach>` - 循环遍历

```xml
<select id="findByIds" resultType="User">
    SELECT * FROM users
    WHERE id IN
    <foreach collection="ids" item="id" open="(" separator="," close=")">
        #{id}
    </foreach>
</select>
```

**属性:**
- `collection` **(必填)** - 集合参数名
- `item` **(必填)** - 当前元素变量名
- `open` - 前缀(如 `(`)
- `close` - 后缀(如 `)`)
- `separator` - 分隔符(如 `,`)
- `index` - 索引变量名

**批量插入示例:**

```xml
<insert id="batchInsert">
    INSERT INTO users (username, email, age)
    VALUES
    <foreach collection="list" item="user" separator=",">
        (#{user.username}, #{user.email}, #{user.age})
    </foreach>
</insert>
```

#### `<sql>` 和 `<include>` - SQL 片段复用

```xml
<!-- 定义 SQL 片段 -->
<sql id="baseColumns">
    id, username, email, age, status, created_at
</sql>

<!-- 引用 SQL 片段 -->
<select id="findById" resultType="User">
    SELECT <include refid="baseColumns"/>
    FROM users
    WHERE id = #{id}
</select>
```

### 3. 参数引用

#### 命名参数 `#{}`

```xml
<select id="findById">
    SELECT * FROM users WHERE id = #{id}
</select>
```

**特性:**
- 自动防止 SQL 注入
- 支持对象属性访问: `#{user.username}`
- 支持集合元素访问: `#{user.username}` (在 foreach 中)

#### 单参数访问

**单参数对象:**
```xml
<!-- 接口方法: User findById(UserQuery query) -->
<select id="findById" resultType="User">
    SELECT * FROM users
    WHERE id = #{id}              <!-- 直接访问对象属性 -->
    AND status = #{status}
</select>
```

**单参数 List:**
```xml
<!-- 接口方法: int batchInsert(List<User> users) -->
<insert id="batchInsert">
    INSERT INTO users (username, email, age)
    VALUES
    <foreach collection="list" item="user" separator=",">
        (#{user.username}, #{user.email}, #{user.age})
    </foreach>
</insert>
```

**单参数基本类型:**
```xml
<!-- 接口方法: User findById(Long id) -->
<select id="findById" resultType="User">
    SELECT * FROM users WHERE id = #{id}
</select>
```

#### 多参数访问

**多参数方法需要使用 `arg0`, `arg1`, `arg2` 访问参数:**

```xml
<!-- 接口方法: List<User> findUsersPage(UserQuery query, PageRequest pageRequest) -->
<select id="findUsersPage" resultType="User">
    SELECT * FROM users
    <where>
        <!-- 第一个参数: arg0 -->
        <if test="arg0.status != null">
            AND status = #{arg0.status}
        </if>
        <if test="arg0.username != null">
            AND username = #{arg0.username}
        </if>
    </where>
    <!-- 第二个参数: arg1 -->
    ORDER BY ${arg1.orderBySql}
    LIMIT #{arg1.pageSize} OFFSET #{arg1.offset}
</select>
```

**多参数示例 - 三个参数:**
```xml
<!-- 接口方法: List<User> search(String keyword, Integer status, Integer minAge) -->
<select id="search" resultType="User">
    SELECT * FROM users
    <where>
        <if test="arg0 != null and arg0 != ''">
            AND username LIKE CONCAT('%', #{arg0}, '%')
        </if>
        <if test="arg1 != null">
            AND status = #{arg1}
        </if>
        <if test="arg2 != null">
            AND age >= #{arg2}
        </if>
    </where>
</select>
```

#### 参数访问 Map 对照表

| 接口方法参数数量 | XML 中访问方式 | 示例 |
|------------|------------|------|
| 单个对象 | 直接访问属性 | `#{username}`, `#{status}` |
| 单个 List | `collection="list"` | `<foreach collection="list" item="user">` |
| 单个 Map | 直接访问 key | `#{status}`, `#{minAge}` |
| 2个参数 | `arg0`, `arg1` | `#{arg0.status}`, `#{arg1.pageSize}` |
| 3个参数 | `arg0`, `arg1`, `arg2` | `#{arg0}`, `#{arg1}`, `#{arg2}` |
| N个参数 | `arg0` ... `argN-1` | 以此类推 |

#### 特殊参数名 - List 和 Map

```xml
<!-- 方法参数为 List 时，使用 collection="list" -->
<insert id="batchInsert">
    INSERT INTO users (username) VALUES
    <foreach collection="list" item="user" separator=",">
        (#{user.username})
    </foreach>
</insert>

<!-- 方法参数为 Map 时，直接使用 key 名 -->
<select id="findByCondition">
    SELECT * FROM users
    WHERE status = #{status}
    AND age > #{minAge}
</select>
```

### 4. 接口注解

#### `@XmlMapper` - 标记 Mapper 接口

用于标记一个接口是 XML Mapper 接口，框架会自动为其创建代理对象并注册到 Spring 容器。

```java
@XmlMapper(namespace = "com.example.mapper.UserMapper")
public interface UserMapper {
    // ...
}
```

**核心属性:**

| 属性 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| `value` | String | 否 | 接口名首字母小写 | Spring Bean 名称 |
| `namespace` | String | 否 | 接口全限定类名 | XML Mapper 命名空间 |

**属性详解:**

**1. `value` - Spring Bean 名称（可选）**

- **作用**: 自定义注册到 Spring 容器的 Bean 名称
- **默认值**: 接口简单名称首字母小写（UserMapper → userMapper）
- **实现位置**: `XmlMapperScannerRegistrar#generateBeanName`
- **使用场景**:
  - 避免 Bean 名称冲突（多个模块有同名接口）
  - 统一命名规范（如添加前缀/后缀）
  - 明确 Bean 用途（如 userQueryMapper、userCommandMapper）

**示例:**
```java
// 示例1: 自定义 Bean 名称
@XmlMapper(value = "customUserMapper", namespace = "...")
public interface UserMapper {
    // Bean 名称: customUserMapper
}

// 示例2: 默认 Bean 名称
@XmlMapper(namespace = "...")
public interface UserMapper {
    // Bean 名称: userMapper（接口名首字母小写）
}

// 示例3: 避免名称冲突
@XmlMapper(value = "moduleAUserMapper", namespace = "...")
public interface UserMapper {  // 模块A
    // Bean 名称: moduleAUserMapper
}

@XmlMapper(value = "moduleBUserMapper", namespace = "...")
public interface UserMapper {  // 模块B
    // Bean 名称: moduleBUserMapper
}
```

**2. `namespace` - XML Mapper 命名空间（推荐填写）**

- **作用**: 关联 Mapper 接口与 XML 配置文件
- **默认值**: 接口全限定类名（com.example.mapper.UserMapper）
- **实现位置**: `XmlMapperProxyFactory#getNamespace`
- **要求**: 必须与 XML 文件中的 `<mapper namespace="...">` 属性保持一致
- **推荐做法**:
  - 显式指定 namespace，避免因类名重构导致 XML 配置失效
  - 使用接口的全限定类名，保持与 MyBatis 一致
  - 确保 XML 文件中的 namespace 与此属性完全一致（区分大小写）

**示例:**
```java
// 示例1: 显式指定 namespace（推荐）
@XmlMapper(namespace = "cn.tannn.jdevelops.mapper.UserMapper")
public interface UserMapper {
    // namespace: cn.tannn.jdevelops.mapper.UserMapper
}

// 对应的 XML 文件
<?xml version="1.0" encoding="UTF-8"?>
<mapper namespace="cn.tannn.jdevelops.mapper.UserMapper">
    <select id="findById" resultType="User">
        SELECT * FROM users WHERE id = #{id}
    </select>
</mapper>

// 示例2: 不指定 namespace（使用默认值）
package com.example.mapper;

@XmlMapper  // namespace 默认为: com.example.mapper.UserMapper
public interface UserMapper {
    // namespace: com.example.mapper.UserMapper（接口全限定类名）
}
```

**注意事项:**
- namespace 必须与 XML 文件中的 namespace 完全一致
- namespace 区分大小写
- 如果重构类名或包名，记得同步更新 XML 文件的 namespace
- 推荐显式指定，避免默认行为导致的配置失效

#### `@XmlSelect` - 标记查询方法

```java
@XmlSelect("findById")
User findById(UserQuery query);

@XmlSelect(value = "findUsers", tryc = true)
List<User> findUsers(UserQuery query);
```

**属性:**
- `value` **(必填)** - SQL 语句 ID
- `tryc` - 是否吞掉异常(默认 false)

#### `@XmlInsert` - 标记插入方法

```java
@XmlInsert("insertUser")
Long insertUser(User user);  // 返回自增ID

@XmlInsert("batchInsert")
int batchInsert(List<User> users);  // 返回影响行数
```

#### `@XmlUpdate` - 标记更新方法

```java
@XmlUpdate("updateUser")
int updateUser(User user);
```

#### `@XmlDelete` - 标记删除方法

```java
@XmlDelete("deleteById")
int deleteById(User user);
```

### 5. 返回类型自动适配

方法返回类型会自动转换:

```java
// 单个对象
@XmlSelect("findById")
User findById(Long id);

// 列表
@XmlSelect("findUsers")
List<User> findUsers(UserQuery query);

// 基本类型
@XmlSelect("countUsers")
Integer countUsers(UserQuery query);

@XmlSelect("countUsers")
int countUsers(UserQuery query);  // 自动拆箱

// INSERT 返回自增ID(支持多种类型)
@XmlInsert("insertUser")
Long insertUser(User user);     // Long

@XmlInsert("insertUser")
Integer insertUser(User user);  // Integer

@XmlInsert("insertUser")
int insertUser(User user);      // int

@XmlInsert("insertUser")
String insertUser(User user);   // String
```

---

## 🔧 高级功能

### 1. Registry 直接调用(无需接口)

#### 基本用法

```java
@Autowired
private XmlMapperRegistry registry;

public void example() {
    String namespace = "com.example.mapper.UserMapper";

    // 执行查询 - 单参数
    UserQuery query = new UserQuery();
    query.setId(1L);
    Object result = registry.executeQuery(
        namespace,
        "findById",
        query,          // 单个参数
        User.class
    );

    // 执行查询 - 多参数（使用 List 传递）
    PageRequest pageRequest = new PageRequest(1, 10);
    Object listResult = registry.executeQuery(
        namespace,
        "findUsersPage",
        Arrays.asList(query, pageRequest),  // 多参数用 List 包装
        User.class
    );

    // 执行更新 - 插入
    User user = new User();
    user.setUsername("test");
    Object rows = registry.executeUpdate(
        namespace,
        "insertUser",
        user
    );

    // 执行更新 - 批量插入
    List<User> users = Arrays.asList(user1, user2, user3);
    Object batchRows = registry.executeUpdate(
        namespace,
        "batchInsert",
        users
    );
}
```

#### Registry 返回值处理

**查询操作返回值:**
```java
// SELECT 返回单个对象时，结果是 List
Object result = registry.executeQuery(namespace, "findById", query, User.class);
if (result instanceof List) {
    List<User> list = (List<User>) result;
    User user = list.isEmpty() ? null : list.get(0);  // 取第一个
}

// SELECT 返回列表
Object result = registry.executeQuery(namespace, "findUsers", query, User.class);
List<User> users = (List<User>) result;

// SELECT 返回统计数据
Object result = registry.executeQuery(namespace, "countUsers", query, Integer.class);
Integer count = (Integer) result;
```

**更新操作返回值:**
```java
// INSERT/UPDATE/DELETE 返回影响行数或自增ID
Object result = registry.executeUpdate(namespace, "insertUser", user);

// 如果配置了 useGeneratedKeys=true，返回自增ID
if (result instanceof Number) {
    Long id = ((Number) result).longValue();
}

// 如果没有配置 useGeneratedKeys，返回影响行数
Integer rows = (Integer) result;
```

### 2. 分页查询快速示例

框架提供了内置的分页支持，只需一个注解即可实现完整的分页功能。

#### 简单分页示例

**步骤 1: 定义 Mapper 接口**

```java
import cn.tannn.jdevelops.annotations.jdbctemplate.xml.*;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageRequest;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageResult;

@XmlMapper(namespace = "com.example.mapper.UserMapper")
public interface UserMapper {

    /**
     * 分页查询用户（框架自动处理）
     */
    @XmlPageSelect(
        dataStatement = "findUsersPage",     // 数据查询 SQL ID
        countStatement = "countUsersTotal"   // 统计查询 SQL ID
    )
    PageResult<User> findUsersPage(UserQuery query, PageRequest pageRequest);
}
```

**步骤 2: 配置 XML SQL**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.example.mapper.UserMapper">

    <!-- 数据查询 SQL (注意：双参数使用 arg0, arg1 访问) -->
    <select id="findUsersPage" resultType="com.example.entity.User">
        SELECT id, username, email, age, status, created_at
        FROM users
        <where>
            <if test="arg0.status != null">
                AND status = #{arg0.status}
            </if>
            <if test="arg0.username != null and arg0.username != ''">
                AND username LIKE CONCAT('%', #{arg0.username}, '%')
            </if>
        </where>
        ORDER BY
        <if test="arg1.orderBySql != null and arg1.orderBySql != ''">
            ${arg1.orderBySql}
        </if>
        <if test="arg1.orderBySql == null or arg1.orderBySql == ''">
            created_at DESC
        </if>
        LIMIT #{arg1.pageSize} OFFSET #{arg1.offset}
    </select>

    <!-- 统计查询 SQL (单参数直接访问属性) -->
    <select id="countUsersTotal" resultType="java.lang.Long">
        SELECT COUNT(*)
        FROM users
        <where>
            <if test="status != null">
                AND status = #{status}
            </if>
            <if test="username != null and username != ''">
                AND username LIKE CONCAT('%', #{username}, '%')
            </if>
        </where>
    </select>
</mapper>
```

**步骤 3: 使用分页查询**

```java
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 分页查询用户（一行代码完成）
     */
    public PageResult<User> getUsersPage(int pageNum, int pageSize, String username, Integer status) {
        // 1. 创建分页参数
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        pageRequest.setOrderBy("created_at");
        pageRequest.setOrderDir("DESC");

        // 2. 创建查询条件
        UserQuery query = new UserQuery();
        query.setUsername(username);
        query.setStatus(status);

        // 3. 框架自动完成：数据查询 + 统计查询 + 结果组装
        return userMapper.findUsersPage(query, pageRequest);
    }
}
```

**Controller 使用**

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/page")
    public PageResult<User> getUsersPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status) {

        return userService.getUsersPage(pageNum, pageSize, username, status);
    }
}
```

**响应示例:**
```json
{
  "pageNum": 1,
  "pageSize": 10,
  "total": 100,
  "pages": 10,
  "hasNext": true,
  "hasPrevious": false,
  "list": [
    {
      "id": 1,
      "username": "user1",
      "email": "user1@example.com",
      "age": 25,
      "status": 1,
      "createdAt": "2024-01-01T10:00:00"
    }
  ]
}
```

**关键要点:**
- ✅ 使用 `@XmlPageSelect` 注解，框架自动处理分页
- ✅ 双参数方法：数据查询 SQL 中使用 `arg0` (query)、`arg1` (pageRequest)
- ✅ 单参数方法：统计查询 SQL 中直接访问属性
- ✅ 返回 `PageResult<T>` 包含完整分页信息（总数、总页数、是否有上一页/下一页等）
- ✅ 一行代码完成分页，无需手动组装结果

> 📘 **详细分页文档**: [XML_MAPPER_PAGE.md](./XML_MAPPER_PAGE.md) - 包含三种分页方式（手动组合、注解方式、Registry 编程式）的完整说明

---

### 3. Mapper 管理

```java
@Autowired
private XmlMapperRegistry registry;

// 获取所有已注册的 Mapper
Set<String> namespaces = registry.getRegisteredMappers();

// 获取指定 Mapper
XmlMapper mapper = registry.getMapper("com.example.mapper.UserMapper");

// 获取 SQL 语句配置
SqlStatement statement = registry.getSqlStatement(
    "com.example.mapper.UserMapper",
    "findById"
);

// 重新加载 Mapper
registry.reloadMapper("com.example.mapper.UserMapper");

// 清除缓存
registry.clearCache();
```

---

## ⚙️ 配置选项

### 全局配置

```yaml
spring:
  jdevelops:
    jdbctemplate:
      xml-mapper:
        # 是否启用 XML Mapper 功能（默认 true）
        enabled: true

        # XML Mapper 文件扫描路径（支持通配符和多路径）
        locations: classpath*:jmapper/**/*.xml
```

### 配置项详细说明

#### `enabled`
- **类型**: `Boolean`
- **默认值**: `true`
- **说明**: 是否启用 XML Mapper 功能
- **使用场景**:
  - 开发环境禁用: `enabled: false`
  - 测试环境启用: `enabled: true`

#### `locations`
- **类型**: `String`
- **默认值**: `classpath*:jmapper/**/*.xml`
- **说明**: XML Mapper 文件扫描路径，支持通配符和多路径配置
- **路径前缀**:
  - `classpath:` - 扫描当前项目类路径下的文件
  - `classpath*:` - 扫描所有 jar 包和类路径（**推荐**，支持加载依赖包中的 XML）
  - `file:` - 扫描文件系统绝对路径
- **通配符**:
  - `**` - 匹配任意层级目录
  - `*` - 匹配单层目录或文件名
  - `*.xml` - 匹配所有 XML 文件
- **示例**:

```yaml
# 示例1: 扫描单个目录
locations: classpath:jmapper/*.xml

# 示例2: 递归扫描子目录（推荐）
locations: classpath*:jmapper/**/*.xml

# 示例3: 多路径配置（逗号分隔）
locations: classpath*:jmapper/**/*.xml,classpath*:mapper/**/*.xml

# 示例4: 混合路径
locations: classpath*:jmapper/**/*.xml,file:/opt/config/mappers/*.xml
```

### 注解配置（@XmlMapper）

`@XmlMapper` 注解用于标记 Mapper 接口，框架会自动为其创建代理对象并注册到 Spring 容器。

```java
@XmlMapper(
    value = "userMapper",                              // Bean 名称（可选）
    namespace = "com.example.mapper.UserMapper"        // 命名空间（推荐填写）
)
public interface UserMapper {
    // ...
}
```

**核心属性:**

| 属性 | 类型 | 必填 | 默认值 | 实现位置 |
|------|------|------|--------|----------|
| `value` | String | 否 | 接口名首字母小写 | XmlMapperScannerRegistrar#generateBeanName |
| `namespace` | String | 否 | 接口全限定类名 | XmlMapperProxyFactory#getNamespace |

#### `value` - Bean 名称
- **类型**: `String`
- **默认值**: 接口名（首字母小写），如 `UserMapper` → `userMapper`
- **说明**: 自定义 Spring Bean 名称，通过 `@AliasFor` 映射到 `@Component` 的 `value` 属性
- **实现位置**: `XmlMapperScannerRegistrar#generateBeanName`
- **使用场景**:
  - 避免 Bean 名称冲突（多个模块有同名接口）
  - 统一命名规范（如添加前缀/后缀）
  - 明确 Bean 用途（如 userQueryMapper、userCommandMapper）

**示例:**
```java
// 示例1: 自定义 Bean 名称
@XmlMapper(value = "customUserMapper", namespace = "...")
public interface UserMapper {
    // Bean 名称: customUserMapper
}

// 示例2: 避免名称冲突
@XmlMapper(value = "moduleAUserMapper", namespace = "...")
public interface UserMapper {  // 模块A
    // Bean 名称: moduleAUserMapper
}
```

#### `namespace` - 命名空间
- **类型**: `String`
- **默认值**: 接口全限定类名，如 `com.example.mapper.UserMapper`
- **说明**: 对应 XML 文件中的 `<mapper namespace="...">` 属性，用于关联接口和 XML 配置
- **实现位置**: `XmlMapperProxyFactory#getNamespace`
- **推荐做法**:
  - 显式指定 namespace，避免因类名重构导致 XML 配置失效
  - 使用接口的全限定类名，保持与 MyBatis 一致
  - 确保 XML 文件中的 namespace 与此属性完全一致（区分大小写）
- **注意事项**:
  - 必须与 XML 文件中的 `namespace` 保持一致
  - namespace 区分大小写
  - 如果重构类名或包名，记得同步更新 XML 文件的 namespace

**示例:**
```java
// 显式指定 namespace（推荐）
@XmlMapper(namespace = "cn.tannn.jdevelops.mapper.UserMapper")
public interface UserMapper {
    // namespace: cn.tannn.jdevelops.mapper.UserMapper
}

// 对应的 XML 文件
<?xml version="1.0" encoding="UTF-8"?>
<mapper namespace="cn.tannn.jdevelops.mapper.UserMapper">
    <select id="findById" resultType="User">
        SELECT * FROM users WHERE id = #{id}
    </select>
</mapper>
```

### 配置示例

#### 标准项目配置

```yaml
# application.yml
spring:
  jdevelops:
    jdbctemplate:
      xml-mapper:
        enabled: true
        locations: classpath*:jmapper/**/*.xml
```

```
项目结构:
src/main/resources/
  └── jmapper/
      ├── UserMapper.xml
      ├── OrderMapper.xml
      └── module/
          └── ProductMapper.xml
```

```java
// 所有 Mapper 都使用全局 locations 扫描
@XmlMapper(namespace = "com.example.mapper.UserMapper")
public interface UserMapper { }

@XmlMapper(namespace = "com.example.mapper.OrderMapper")
public interface OrderMapper { }
```

---

## 📊 性能优化

1. **SQL 缓存** - XML 解析后会缓存,避免重复解析
2. **连接池** - 使用 Spring JdbcTemplate 的连接池
3. **批量操作** - 使用 `<foreach>` 实现批量插入/更新

---

## 🆚 对比

| 特性 | XML Mapper | MyBatis | Spring Data JPA |
|---|---|---|---|
| 学习成本 | 低 | 中 | 高 |
| 配置复杂度 | 低 | 中 | 低 |
| 动态 SQL | ✅ | ✅ | ❌ |
| SQL 可见性 | ✅ | ✅ | ❌ |
| 类型安全 | ✅ | ⚠️ | ✅ |
| 自增ID返回 | ✅ | ✅ | ✅ |
| 轻量级 | ✅ | ❌ | ❌ |

---

## 💡 最佳实践

1. **命名规范**
  - XML 文件名与接口名保持一致
  - namespace 使用接口全限定名
  - SQL ID 使用方法名

2. **SQL 组织**
  - 将常用字段提取为 `<sql>` 片段
  - 复杂 SQL 使用 XML,简单 SQL 使用 `@Query`

3. **性能优化**
  - 合理使用索引
  - 避免 SELECT *,明确指定字段
  - 批量操作使用 `<foreach>`

4. **异常处理**
  - 默认不要使用 `tryc=true`
  - 让异常向上传播,由统一异常处理器处理

5. **参数传递规范**
  - **单参数**: 直接访问属性 `#{username}`, `#{status}`
  - **多参数**: 必须使用 `arg0`, `arg1`, `arg2` 访问 `#{arg0.status}`, `#{arg1.pageSize}`
  - **List参数**: 使用 `collection="list"` 在 foreach 中遍历
  - **Map参数**: 直接使用 key 名访问 `#{keyName}`

6. **动态排序安全**
  - 排序字段使用 `${}` 时，必须进行白名单验证
  - 避免直接将用户输入拼接到排序语句

7. **分页查询规范**
  - 分页查询和统计查询的 WHERE 条件必须保持一致
  - 使用 `<sql>` 片段复用查询条件
  - 推荐使用框架内置的 `@XmlPageSelect` 注解

8. **批量操作优化**
  - 批量插入使用 `<foreach>` 而不是循环调用单条插入
  - 批量操作时注意数据库的最大参数限制
  - 大量数据建议分批处理（每批500-1000条）

---

## 🚨 常见问题

### 1. 多参数方法参数访问错误

**❌ 错误写法:**
```xml
<!-- 接口: List<User> findUsersPage(UserQuery query, PageRequest pageRequest) -->
<select id="findUsersPage">
    WHERE status = #{status}  <!-- 错误：多参数方法不能直接访问 -->
    LIMIT #{pageSize}         <!-- 错误 -->
</select>
```

**✅ 正确写法:**
```xml
<select id="findUsersPage">
    WHERE status = #{arg0.status}      <!-- 正确：使用 arg0 访问第一个参数 -->
    LIMIT #{arg1.pageSize}             <!-- 正确：使用 arg1 访问第二个参数 -->
</select>
```

### 2. Registry 查询单个对象时返回值处理

**❌ 错误写法:**
```java
Object result = registry.executeQuery(namespace, "findById", query, User.class);
User user = (User) result;  // ClassCastException: 返回的是 List 不是 User
```

**✅ 正确写法:**
```java
Object result = registry.executeQuery(namespace, "findById", query, User.class);
List<User> list = (List<User>) result;
User user = list.isEmpty() ? null : list.get(0);
```

### 3. foreach 中的 collection 名称

**List 参数:**
```xml
<!-- 接口: int batchInsert(List<User> users) -->
<insert id="batchInsert">
    <foreach collection="list" item="user" separator=",">  <!-- 必须使用 "list" -->
        (#{user.username}, #{user.email})
    </foreach>
</insert>
```

**多参数中的 List:**
```xml
<!-- 接口: int batchInsert(String type, List<User> users) -->
<insert id="batchInsert">
    INSERT INTO ${arg0}_table (username, email)  <!-- arg0 是 type -->
    VALUES
    <foreach collection="arg1" item="user" separator=",">  <!-- arg1 是 List -->
        (#{user.username}, #{user.email})
    </foreach>
</insert>
```

### 4. 动态排序 SQL 注入风险

**❌ 危险写法:**
```xml
<!-- 直接使用用户输入的排序字段，存在 SQL 注入风险 -->
ORDER BY ${orderBy}
```

**✅ 安全写法:**
```java
// Java 代码中进行白名单验证
List<String> allowedFields = Arrays.asList("id", "username", "age", "created_at");
if (!allowedFields.contains(orderBy)) {
    orderBy = "created_at";  // 默认值
}
```

```xml
<!-- 使用验证后的字段 -->
ORDER BY ${orderBy}
```

### 5. 批量操作的参数数量限制

MySQL 等数据库对预编译参数数量有限制（如 MySQL 默认 65535），批量插入大量数据时需要分批：

```java
// 分批插入，每批 500 条
int batchSize = 500;
for (int i = 0; i < users.size(); i += batchSize) {
    List<User> batch = users.subList(i, Math.min(i + batchSize, users.size()));
    userMapper.batchInsert(batch);
}
```

---

## 📝 参考示例

完整示例请查看测试项目:
- **测试用例路径:** `Jdevelops-Example/dal-jdbctemplate/src/test/java/cn/tannn/demo/jdevelops/daljdbctemplate/`
  - [XmlMapper_annotation_Test.java](https://github.com/en-o/Jdevelops-Example/blob/master/dal-jdbctemplate/src/test/java/cn/tannn/demo/jdevelops/daljdbctemplate/XmlMapper_annotation_Test.java) - 接口注解方式完整测试
  - [XmlMapper_registry_Test.java](https://github.com/en-o/Jdevelops-Example/blob/master/dal-jdbctemplate/src/test/java/cn/tannn/demo/jdevelops/daljdbctemplate/XmlMapper_registry_Test.java) - Registry 方式完整测试

- **XML 配置路径:** `Jdevelops-Example/dal-jdbctemplate/src/main/resources/jmapper/`
  - [UserMapper.xml](https://github.com/en-o/Jdevelops-Example/blob/master/dal-jdbctemplate/src/main/resources/jmapper/UserMapper.xml) - 完整的 SQL 配置示例

- **分页功能文档:**
  - [XML_MAPPER_PAGE.md](./XML_MAPPER_PAGE.md) - 分页查询完整指南

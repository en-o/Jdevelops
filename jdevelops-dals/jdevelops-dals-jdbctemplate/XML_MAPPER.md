# XML Mapper 功能说明

## 📖 简介

XML Mapper 是 JdbcTemplate 的 XML 配置增强功能,类似 MyBatis 的 XML Mapper 机制,但更加轻量、简洁。

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
    <version>${latest.version}</version>
</dependency>
```

### 2. 配置 XML 扫描路径

```yaml
spring:
  jdevelops:
    jdbctemplate:
      xml-mapper:
        # XML Mapper 文件扫描路径(支持通配符)
        locations: classpath*:jmapper/**/*.xml
```

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

#### 特殊参数名

```xml
<!-- 方法参数为 List 时 -->
<insert id="batchInsert">
    INSERT INTO users (username) VALUES
    <foreach collection="list" item="user" separator=",">
        (#{user.username})
    </foreach>
</insert>

<!-- 方法参数为 Map 时 -->
<select id="findByCondition">
    SELECT * FROM users
    WHERE status = #{status}
    AND age > #{minAge}
</select>
```

### 4. 接口注解

#### `@XmlMapper` - 标记 Mapper 接口

```java
@XmlMapper(namespace = "com.example.mapper.UserMapper")
public interface UserMapper {
    // ...
}
```

**属性:**
- `namespace` **(必填)** - XML Mapper 的命名空间(对应 XML 的 namespace 属性)

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

```java
@Autowired
private XmlMapperRegistry registry;

public void example() {
    String namespace = "com.example.mapper.UserMapper";

    // 执行查询
    Object result = registry.executeQuery(
        namespace,
        "findById",
        query,
        User.class
    );

    // 执行更新
    Object rows = registry.executeUpdate(
        namespace,
        "insertUser",
        user
    );
}
```

### 2. Mapper 管理

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

```yaml
spring:
  jdevelops:
    jdbctemplate:
      xml-mapper:
        # XML Mapper 文件扫描路径(支持通配符)
        locations: classpath*:jmapper/**/*.xml
        # 是否启用 XML Mapper 功能
        enabled: true
        # 是否在启动时验证 XML 文件
        validate: true
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

---

## 📝 参考示例

完整示例请查看:
- [XML_MAPPER_QUICK_START.md](./XML_MAPPER_QUICK_START.md) - 快速开始完整示例
- [XmlMapper_annotation_Test.java](./src/test/java/cn/tannn/demo/jdevelops/daljdbctemplate/XmlMapper_annotation_Test.java) - 接口方式测试
- [XmlMapper_registry_Test.java](./src/test/java/cn/tannn/demo/jdevelops/daljdbctemplate/XmlMapper_registry_Test.java) - Registry 方式测试
- [UserMapper.xml](./src/main/resources/jmapper/UserMapper.xml) - XML 配置示例

# XML Mapper 功能说明

## 📖 简介

XML Mapper 是 JdbcTemplate 的 XML 配置增强功能,类似 MyBatis 的 XML Mapper 机制,但更加轻量、简洁。
> 由于我们长期以 JPA 为主，近期业务突变、复杂手写 SQL 激增，JPA 对此支持有限且需高度设计约束，而现有模型零散难以快速落地。
> 此前为缓解这一问题，曾用 JdbcTemplate 构建链式 SQL，但链式代码可读性与审计成本仍高。
> 为此，我们在 JdbcTemplate 之上引入 XML 映射方案，既延续 JdbcTemplate 的轻量执行能力，又通过集中化 XML 管理复杂 SQL，实现与原有 JPA 主流程的互补

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

### 3. 特殊符号处理

在 XML 文件中，某些字符具有特殊含义（如 `<`、`>`、`&` 等），直接在 SQL 语句中使用这些字符会导致 XML 解析错误。框架支持两种处理方式。

#### 方法一：XML 实体转义

使用 XML 标准的实体转义符号替换特殊字符：

| 特殊字符 | 实体转义 | 说明 |
|---------|---------|------|
| `<`     | `&lt;`  | 小于号 |
| `>`     | `&gt;`  | 大于号 |
| `&`     | `&amp;` | 和号 |
| `"`     | `&quot;` | 双引号 |
| `'`     | `&apos;` | 单引号 |

**示例:**
```xml
<select id="findUsers" resultType="User">
    SELECT * FROM users
    <where>
        <!-- 小于：age < 30 -->
        <if test="maxAge != null">
            AND age &lt; #{maxAge}
        </if>

        <!-- 大于：age > 18 -->
        <if test="minAge != null">
            AND age &gt; #{minAge}
        </if>

        <!-- 小于等于：age <= 30 -->
        <if test="maxAgeEqual != null">
            AND age &lt;= #{maxAgeEqual}
        </if>

        <!-- 不等于：status <> 0 -->
        <if test="status != null">
            AND status &lt;&gt; #{status}
        </if>
    </where>
</select>
```

**优点:** 标准 XML 处理方式，所有解析器都支持
**缺点:** 可读性稍差，复杂 SQL 中维护困难

#### 方法二：CDATA 区块

CDATA (Character Data) 区块告诉 XML 解析器：这段内容是纯文本，不要解析其中的特殊字符。

**语法格式:**
```xml
<![CDATA[
    这里可以直接使用 <、>、& 等特殊符号
]]>
```

**示例:**
```xml
<select id="findUsers" resultType="User">
    SELECT * FROM users
    <where>
        <if test="maxAge != null">
            <![CDATA[
            AND age < #{maxAge}
            ]]>
        </if>

        <if test="minAge != null and maxAge != null">
            <![CDATA[
            AND (age >= #{minAge} AND age <= #{maxAge})
            ]]>
        </if>
    </where>
</select>
```

**优点:** SQL 语句可读性强，可以直接复制 SQL 到 CDATA 中
**缺点:** 稍微增加代码行数

#### 方法三：混合使用

在同一个查询中可以混合使用两种方式：

```xml
<select id="findUsers" resultType="User">
    SELECT * FROM users
    <where>
        <!-- 简单比较：使用实体转义 -->
        <if test="status != null">
            AND status &lt;&gt; #{status}
        </if>

        <!-- 复杂条件：使用 CDATA -->
        <if test="minAge != null and maxAge != null">
            <![CDATA[
            AND (age > #{minAge} AND age < #{maxAge})
            ]]>
        </if>
    </where>
</select>
```

#### 常用操作示例

**BETWEEN 范围查询:**
```xml
<!-- 方式1: BETWEEN 不需要特殊处理 -->
<if test="minAge != null and maxAge != null">
    AND age BETWEEN #{minAge} AND #{maxAge}
</if>

<!-- 方式2: 使用 CDATA -->
<if test="startDate != null and endDate != null">
    <![CDATA[
    AND created_at BETWEEN #{startDate} AND #{endDate}
    ]]>
</if>
```

**复杂条件组合:**
```xml
<where>
    <!-- 年龄范围 -->
    <if test="minAge != null and maxAge != null">
        <![CDATA[
        AND (age >= #{minAge} AND age <= #{maxAge})
        ]]>
    </if>

    <!-- OR 条件 -->
    <if test="status1 != null and status2 != null">
        AND (status = #{status1} OR status = #{status2})
    </if>

    <!-- NOT IN 条件 -->
    <if test="excludeIds != null and excludeIds.size() > 0">
        AND id NOT IN
        <foreach collection="excludeIds" item="id" open="(" separator="," close=")">
            #{id}
        </foreach>
    </if>
</where>
```

**最佳实践建议:**

| 场景 | 推荐方式 | 示例 |
|------|---------|------|
| 简单比较（单个符号） | 实体转义 | `age &lt; 30` |
| 复杂条件（多个符号） | CDATA | `<![CDATA[ age > 18 AND age < 60 ]]>` |
| BETWEEN 操作 | 直接使用 | `age BETWEEN #{min} AND #{max}` |
| 混合场景 | 灵活选择 | 根据可读性决定 |

**注意事项:**
1. ❌ CDATA 不能嵌套使用
2. ❌ CDATA 中不能使用动态 SQL 标签（如 `<if>`、`<foreach>`）
3. ✅ 参数占位符 `#{}` 在 CDATA 中正常工作
4. ✅ 选择方式时优先考虑代码可读性

**测试用例参考:**
- 测试文件: `Jdevelops-Example/dal-jdbctemplate/src/test/java/.../XmlMapper_annotation_Test.java`
- 测试方法: 测试 70-79（特殊符号处理）
- XML 配置: `UserMapper.xml` 第 285-544 行

### 4. OGNL 表达式支持

在 XML Mapper 的 `<if>` 条件判断中，框架支持丰富的 OGNL 表达式，包括比较运算符、逻辑运算符和方法调用。

#### 4.1 比较运算符

框架支持以下比较运算符：

| 运算符 | 说明 | 示例 | 支持类型 |
|--------|------|------|----------|
| `==` | 等于 | `status == 1` | 所有类型 |
| `!=` | 不等于 | `status != null` | 所有类型 |
| `>` | 大于 | `age > 18` | 数值类型 |
| `<` | 小于 | `age < 60` | 数值类型 |
| `>=` | 大于等于 | `score >= 60` | 数值类型 |
| `<=` | 小于等于 | `price <= 100` | 数值类型 |

**逻辑运算符:**
- `and` / `&&` - 逻辑与
- `or` / `||` - 逻辑或

**示例：**

```xml
<!-- 空值判断 -->
<if test="username != null and username != ''">
    AND username LIKE #{username}
</if>

<!-- 数值比较 -->
<if test="age > 18">
    AND age > 18
</if>

<!-- 集合大小判断 -->
<if test="statusList != null and statusList.size() > 0">
    AND status IN
    <foreach collection="statusList" item="status" open="(" separator="," close=")">
        #{status}
    </foreach>
</if>

<!-- 复杂条件 -->
<if test="minAge != null and minAge >= 0 and maxAge != null and maxAge <= 150">
    AND age BETWEEN #{minAge} AND #{maxAge}
</if>

<!-- 多条件组合 -->
<if test="(status == 1 or status == 2) and age > 18">
    AND (status IN (1, 2)) AND age > 18
</if>
```

**注意事项:**
- ⚠️ 比较运算符会按顺序处理，先处理 `>=`、`<=`、`!=`、`==`，最后处理 `>`、`<`
- ⚠️ 数值比较支持 `Integer`、`Long`、`Double` 等数值类型
- ⚠️ 方法调用结果也可以参与比较（如 `list.size() > 0`、`status.getCode() == 1`）

#### 4.2 枚举和 Record 方法调用

框架支持在 `test` 表达式中调用以下方法：

**Java 枚举（Enum）方法:**
- ✅ `name()` - 返回枚举常量名称（如 `ACTIVE`）
- ✅ `ordinal()` - 返回枚举常量序号（从0开始）
- ✅ `toString()` - 返回枚举的字符串表示
- ✅ **自定义 getter 方法** - 多值枚举的自定义字段方法（如 `getCode()`, `getName()`, `getDescription()`）

**Java 17 Record 类:**
- ✅ **访问器方法** - Record 的字段访问器（如 `id()`, `name()`, `email()`）

**集合/数组方法:**
- ✅ `size()` - List/Set 的大小
- ✅ `length` - 数组长度
- ✅ `isEmpty()` - 判断集合是否为空

**支持场景:**
- ✅ 单参数对象: `test="platform.name() != 'NONE'"`
- ✅ 多参数方法: `test="arg0.platform.name() != 'NONE'"`
- ✅ 嵌套调用: `test="userInfo.platform().name() != 'NONE'"`
- ✅ 复杂表达式: `test="status.getCode() > 0 and status.name() != 'DELETED'"`

#### 简单枚举方法调用

**定义枚举:**

```java
public enum UserPlatform {
    NONE,      // ordinal = 0
    WEB,       // ordinal = 1
    MOBILE,    // ordinal = 2
    DESKTOP    // ordinal = 3
}
```

**查询参数:**

```java
public class UserQuery {
    private UserPlatform platform;

    public UserPlatform getPlatform() {
        return platform;
    }
}
```

**示例 1: 使用 name() 方法**

```xml
<select id="findUsersByPlatform" resultType="User">
    SELECT * FROM users
    <where>
        <!-- 测试枚举的 name() 方法 -->
        <if test="platform != null and platform.name() != 'NONE'">
            AND username LIKE '%test%'
        </if>
        <if test="status != null">
            AND status = #{status}
        </if>
    </where>
    ORDER BY created_at DESC
</select>
```

**说明:**
- `platform.name()` 返回枚举常量名称（字符串）
- `platform.name() != 'NONE'` 判断平台是否不是 NONE
- 当 platform 为 WEB/MOBILE/DESKTOP 时，条件为 true，会添加 `username LIKE '%test%'` 条件

**示例 2: 使用 ordinal() 方法**

```xml
<select id="findUsersByPlatformOrdinal" resultType="User">
    SELECT * FROM users
    <where>
        <!-- 测试枚举的 ordinal() 方法 -->
        <if test="platform != null and platform.ordinal() > 0">
            AND username LIKE '%test%'
        </if>
    </where>
    ORDER BY created_at DESC
</select>
```

**说明:**
- `platform.ordinal()` 返回枚举的序号（整数）
- `platform.ordinal() > 0` 判断平台是否不是第一个枚举值（NONE）
- 当 platform 为 WEB/MOBILE/DESKTOP 时，ordinal() > 0 为 true

#### 多值枚举方法调用

多值枚举是带有自定义字段和 getter 方法的枚举，可以存储更丰富的业务信息。

**定义多值枚举:**

```java
public enum UserStatus {
    INACTIVE(0, "未激活", "用户账号未激活"),
    ACTIVE(1, "已激活", "用户账号正常"),
    LOCKED(2, "已锁定", "用户账号被锁定"),
    DELETED(9, "已删除", "用户账号已删除");

    private final int code;
    private final String name;
    private final String description;

    UserStatus(int code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    // Getter 方法
    public int getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}
```

**查询参数:**

```java
public class UserQuery {
    private UserStatus userStatus;

    public UserStatus getUserStatus() {
        return userStatus;
    }
}
```

**示例 3: 使用 getCode() 方法**

```xml
<select id="findUsersByUserStatusCode" resultType="User">
    SELECT * FROM users
    <where>
        <!-- 测试多值枚举的 getCode() 方法 -->
        <if test="userStatus != null and userStatus.getCode() == 1">
            AND status = #{userStatus.getCode()}
        </if>

        <!-- 也可以测试不等于 -->
        <if test="userStatus != null and userStatus.getCode() != 0">
            AND username LIKE '%test%'
        </if>
    </where>
    ORDER BY created_at DESC
</select>
```

**说明:**
- `userStatus.getCode()` 调用枚举的自定义 getCode() 方法
- 可以在 test 表达式中进行数值比较（`== 1`, `!= 0`, `> 0` 等）
- 也可以在 SQL 参数中使用 `#{userStatus.getCode()}`

**示例 4: 使用 getName() 方法**

```xml
<select id="findUsersByUserStatusName" resultType="User">
    SELECT * FROM users
    <where>
        <!-- 测试多值枚举的 getName() 方法 -->
        <if test="userStatus != null and userStatus.getName() == '已激活'">
            AND status = 1
        </if>

        <if test="userStatus != null and userStatus.getName() != '未激活'">
            AND username LIKE '%test%'
        </if>
    </where>
    ORDER BY created_at DESC
</select>
```

**说明:**
- `userStatus.getName()` 返回自定义的中文名称
- 可以进行字符串比较（`==`, `!=` 等）

**示例 5: 组合使用多个方法**

```xml
<select id="findUsersByUserStatusComplex" resultType="User">
    SELECT * FROM users
    <where>
        <!-- 组合使用多值枚举的多个方法 -->
        <if test="userStatus != null">
            <!-- 使用 getCode() 方法 -->
            <if test="userStatus.getCode() > 0">
                <![CDATA[
                AND status >= #{userStatus.getCode()}
                ]]>
            </if>

            <!-- 使用 name() 方法（枚举名称） -->
            <if test="userStatus.name() != 'DELETED'">
                AND username LIKE '%test%'
            </if>

            <!-- 使用 getName() 方法（自定义名称字段） -->
            <if test="userStatus.getName() != null and userStatus.getName() != ''">
                AND status != 9
            </if>
        </if>
    </where>
    ORDER BY created_at DESC
</select>
```

**说明:**
- 可以在同一个查询中组合使用多个枚举方法
- `name()` 返回枚举常量名（如 "ACTIVE"）
- `getName()` 返回自定义字段值（如 "已激活"）
- 注意区分：`name()` 是枚举内置方法，`getName()` 是自定义 getter 方法

#### 多参数方法中的枚举方法调用

当接口方法有多个参数时，需要使用 `arg0`, `arg1` 访问参数，枚举方法调用同样适用。

**接口方法:**

```java
@XmlMapper(namespace = "com.example.mapper.UserMapper")
public interface UserMapper {

    @XmlSelect("findUsersByPlatformWithArg0")
    List<User> findUsersByPlatformWithArg0(UserQuery query, Integer limit);
}
```

**XML 配置:**

```xml
<select id="findUsersByPlatformWithArg0" resultType="User">
    SELECT * FROM users
    <where>
        <!-- 使用 arg0 访问第一个参数的枚举方法 -->
        <if test="arg0.platform != null and arg0.platform.name() != 'NONE'">
            AND username LIKE '%test%'
        </if>

        <if test="arg0.status != null">
            AND status = #{arg0.status}
        </if>
    </where>
    ORDER BY created_at DESC
    LIMIT #{arg1}  <!-- 第二个参数：limit -->
</select>
```

**多值枚举的多参数示例:**

```xml
<select id="findUsersByUserStatusWithArg0" resultType="User">
    SELECT * FROM users
    <where>
        <!-- arg0.userStatus.getCode() -->
        <if test="arg0.userStatus != null and arg0.userStatus.getCode() == 1">
            AND status = #{arg0.userStatus.getCode()}
        </if>

        <!-- arg0.userStatus.getName() -->
        <if test="arg0.userStatus != null and arg0.userStatus.getName() == '已激活'">
            AND username LIKE '%test%'
        </if>
    </where>
    ORDER BY created_at DESC
    LIMIT #{arg1}  <!-- limit -->
</select>
```

#### Record 类访问器方法调用

Java 17 的 Record 类自动生成访问器方法，框架支持在 XML 中调用这些方法。

**定义 Record:**

```java
public record UserInfo(Long id, String loginName, String name, UserPlatform platform) {
    // Record 自动生成 id(), loginName(), name(), platform() 访问器方法
}
```

**查询参数:**

```java
public class UserQuery {
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
```

**示例 6: 调用 Record 访问器方法**

```xml
<select id="findUsersByUserInfo" resultType="User">
    SELECT * FROM users
    <where>
        <!-- 调用 Record 的访问器方法 -->
        <if test="userInfo != null and userInfo.id() != null">
            AND id = #{userInfo.id()}
        </if>

        <if test="userInfo != null and userInfo.loginName() != null">
            AND login_name = #{userInfo.loginName()}
        </if>

        <!-- 嵌套调用：Record 的 platform() 返回枚举，再调用枚举的 name() -->
        <if test="userInfo != null and userInfo.platform() != null and userInfo.platform().name() != 'NONE'">
            AND platform != 0
        </if>
    </where>
</select>
```

**说明:**
- `userInfo.id()` - 调用 Record 的 id() 访问器方法
- `userInfo.loginName()` - 调用 Record 的 loginName() 访问器方法
- `userInfo.platform().name()` - 链式调用：先调用 platform() 获取枚举，再调用 name() 获取枚举名称

#### 链式方法调用

框架支持链式方法调用，可以组合多个方法调用。

**示例 7: 链式方法调用**

```xml
<select id="findUsers" resultType="User">
    SELECT * FROM users
    <where>
        <!-- Record 的方法调用链 -->
        <if test="userInfo != null and userInfo.loginName() != null">
            AND login_name = #{userInfo.loginName().toUpperCase()}
        </if>

        <!-- 枚举的方法调用链 -->
        <if test="platform != null">
            AND platform_name = #{platform.name().toLowerCase()}
        </if>

        <!-- 嵌套对象的方法调用链 -->
        <if test="userInfo != null and userInfo.platform().ordinal() > 0">
            AND status = 1
        </if>
    </where>
</select>
```

**说明:**
- `userInfo.loginName().toUpperCase()` - 先获取 loginName，再转大写
- `platform.name().toLowerCase()` - 先获取枚举名称，再转小写
- `userInfo.platform().ordinal()` - 先获取 platform 枚举，再获取序号

#### 使用场景和最佳实践

**1. 动态条件判断**

使用枚举方法可以根据枚举状态动态添加 SQL 条件：

```xml
<select id="findUsers" resultType="User">
    SELECT * FROM users
    <where>
        <!-- 根据平台类型添加不同条件 -->
        <if test="platform != null and platform.name() == 'WEB'">
            AND web_specific_column IS NOT NULL
        </if>

        <if test="platform != null and platform.name() == 'MOBILE'">
            AND mobile_specific_column IS NOT NULL
        </if>

        <!-- 根据状态码添加条件 -->
        <if test="userStatus != null and userStatus.getCode() > 0">
            AND deleted_at IS NULL
        </if>
    </where>
</select>
```

**2. 业务规则判断**

多值枚举可以封装复杂的业务规则：

```xml
<select id="findActiveUsers" resultType="User">
    SELECT * FROM users
    <where>
        <!-- 只查询激活状态的用户 -->
        <if test="userStatus != null and userStatus.getCode() == 1">
            AND status = #{userStatus.getCode()}
        </if>

        <!-- 排除已删除的用户 -->
        <if test="userStatus != null and userStatus.name() != 'DELETED'">
            AND deleted_at IS NULL
        </if>
    </where>
</select>
```

**3. 参数验证**

在 SQL 执行前验证枚举参数的有效性：

```xml
<select id="findUsers" resultType="User">
    SELECT * FROM users
    <where>
        <!-- 验证平台参数不是 NONE -->
        <if test="platform == null or platform.name() == 'NONE'">
            AND 1=0  <!-- 返回空结果 -->
        </if>

        <if test="platform != null and platform.ordinal() > 0">
            AND platform_type = #{platform.ordinal()}
        </if>
    </where>
</select>
```

#### 注意事项

**1. Null 安全检查**

始终在调用方法前检查对象是否为 null：

```xml
<!-- ✅ 正确 -->
<if test="platform != null and platform.name() != 'NONE'">
    AND username LIKE '%test%'
</if>

<!-- ❌ 错误：可能抛出 NullPointerException -->
<if test="platform.name() != 'NONE'">
    AND username LIKE '%test%'
</if>
```

**2. 方法返回值类型**

了解方法返回值类型，使用正确的比较方式：

```xml
<!-- name() 返回 String，使用字符串比较 -->
<if test="platform.name() == 'WEB'">...</if>

<!-- ordinal() 返回 int，使用数值比较 -->
<if test="platform.ordinal() > 0">...</if>

<!-- getCode() 返回 int，使用数值比较 -->
<if test="userStatus.getCode() == 1">...</if>

<!-- getName() 返回 String，使用字符串比较 -->
<if test="userStatus.getName() == '已激活'">...</if>
```

**3. 枚举名称 vs 自定义字段**

区分枚举的 `name()` 方法和自定义的 `getName()` getter：

```java
// 枚举定义
public enum UserStatus {
    ACTIVE(1, "已激活");  // 枚举名称是 "ACTIVE"，getName() 返回 "已激活"

    private final String name;
    public String getName() { return name; }
}
```

```xml
<!-- name() - 返回枚举常量名称 "ACTIVE" -->
<if test="userStatus.name() == 'ACTIVE'">...</if>

<!-- getName() - 返回自定义字段值 "已激活" -->
<if test="userStatus.getName() == '已激活'">...</if>
```

**4. 参数访问规范**

- **单参数**: 直接访问枚举方法 `test="platform.name() != 'NONE'"`
- **多参数**: 使用 arg0/arg1 访问 `test="arg0.platform.name() != 'NONE'"`
- **嵌套对象**: 支持链式调用 `test="userInfo.platform().name() != 'NONE'"`

#### 实现原理

框架通过 OGNL 表达式解析器识别方法调用模式（以 `()` 结尾），使用 Java 反射 API 动态调用方法：

```java
// 核心实现（简化版）
private static Object invokeMethod(Object obj, String methodName) {
    Method method = obj.getClass().getMethod(methodName);
    return method.invoke(obj);
}
```

**支持的方法类型:**
- ✅ 无参数的 public 方法（如 `name()`, `ordinal()`, `getCode()`）
- ✅ 返回值为基本类型或对象的方法
- ✅ 链式方法调用（返回对象的方法可以继续调用）
- ❌ 带参数的方法（暂不支持）
- ❌ 私有方法或受保护方法（暂不支持）

#### 测试用例参考

**完整测试示例:**

- **测试文件**:
  - `Jdevelops-Example/dal-jdbctemplate/src/test/java/.../XmlMapper_annotation_Test.java`
  - `Jdevelops-Example/dal-jdbctemplate/src/test/java/.../XmlMapper_registry_Test.java`

- **测试方法**:
  - 测试 80-82（简单枚举方法：name(), ordinal()）
  - 测试 83-87（多值枚举方法：getCode(), getName(), getDescription()）

- **XML 配置**: `UserMapper.xml`
  - 第 550-598 行（简单枚举测试）
  - 第 604-697 行（多值枚举测试）

- **单元测试**: `OgnlUtilTest.java`
  - 测试枚举和 Record 方法调用的底层实现

**Java 调用示例:**

```java
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> findUsersByPlatform() {
        UserQuery query = new UserQuery();
        query.setPlatform(UserPlatform.WEB);  // 设置枚举
        query.setStatus(1);

        // XML 中会判断 platform.name() != 'NONE'
        return userMapper.findUsersByPlatform(query);
    }

    public List<User> findUsersByStatus() {
        UserQuery query = new UserQuery();
        query.setUserStatus(UserStatus.ACTIVE);  // 设置多值枚举

        // XML 中会判断 userStatus.getCode() == 1
        return userMapper.findUsersByUserStatusCode(query);
    }
}
```

#### 错误处理

**常见错误和解决方案:**

| 错误现象 | 原因 | 解决方案 |
|---------|------|---------|
| 方法调用返回 null | 对象为 null | 添加 null 检查：`platform != null and platform.name() != 'NONE'` |
| 方法不存在异常 | 方法名拼写错误 | 检查方法名是否正确（区分大小写） |
| 类型转换错误 | 返回值类型不匹配 | 使用正确的比较方式（字符串 vs 数值） |
| 链式调用失败 | 中间对象为 null | 每个链式节点都要检查 null |

**调试建议:**

1. 开启 DEBUG 日志查看 OGNL 表达式解析过程
2. 在测试中先验证枚举方法是否返回预期值
3. 使用简单的条件进行测试，确认方法调用正常
4. 检查枚举定义是否正确，getter 方法是否为 public

### 5. 参数引用

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

#### 入参实体示例

在开始参数访问说明之前，先了解一个典型的查询参数实体结构：

```java
/**
 * 用户查询参数实体
 */
public class UserQuery {
    // 基础查询条件
    private String username;
    private String email;
    private Integer status;
    private Integer minAge;
    private Integer maxAge;

    // 高级查询参数
    private String keyword;
    private List<Integer> statusList;  // List 类型属性
    private String startDate;
    private String endDate;

    // 分页参数
    private Integer pageSize;
    private Integer offset;

    // ID 列表（用于批量操作）
    private List<Long> ids;  // List 类型属性

    // 单个 ID
    private Long id;

    // Getters and Setters...
}
```

**说明:**
- 单参数对象时，XML 中可以直接访问对象的属性（如 `#{username}`, `#{status}`）
- 对象中的 List 属性可以在 `<foreach>` 中使用 `collection="属性名"` 访问（如 `collection="ids"`）
- 多参数方法时，需要使用 `arg0`, `arg1` 等方式访问（详见下文）

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

**1. 单参数 List（直接传入 List）**

当方法参数直接是 List 类型时，使用 `collection="list"` 访问：

```java
// 接口方法
int batchInsert(List<User> users);
```

```xml
<!-- XML 配置 -->
<insert id="batchInsert">
    INSERT INTO users (username, email) VALUES
    <foreach collection="list" item="user" separator=",">
        (#{user.username}, #{user.email})
    </foreach>
</insert>
```

**2. 对象属性是 List（嵌套访问）**

当方法参数是对象，对象中包含 List 属性时，使用 `collection="属性名"` 访问：

```java
// 接口方法
int deleteByIds(UserQuery query);
```

```java
// UserQuery 类
public class UserQuery {
    private List<Long> ids;  // List 类型属性
    // Getters and Setters...
}
```

```xml
<!-- XML 配置 - 访问对象的 ids 属性 -->
<delete id="deleteByIds">
    DELETE FROM users
    WHERE id IN
    <foreach collection="ids" item="itemId" open="(" separator="," close=")">
        #{itemId}
    </foreach>
</delete>
```

**关键点:**
- ✅ `collection="ids"` - `ids` 是 UserQuery 对象的属性名
- ✅ 单参数对象时，直接使用属性名访问 List 属性
- ✅ `item="itemId"` - 定义当前遍历元素的变量名，在 `#{}` 中使用

**3. 多参数中的 List（使用 arg0/arg1 访问）**

当方法有多个参数，其中某个参数包含 List 属性时，使用 `arg0.属性名` 或 `arg1.属性名` 访问：

```java
// 接口方法
int deleteByIdsWithCondition(UserQuery query, Integer status);
```

```xml
<!-- XML 配置 - 第一个参数的 ids 属性 -->
<delete id="deleteByIdsWithCondition">
    DELETE FROM users
    WHERE status = #{arg1}
    AND id IN
    <foreach collection="arg0.ids" item="itemId" open="(" separator="," close=")">
        #{itemId}
    </foreach>
</delete>
```

**4. 对象属性是 List of Objects（嵌套对象 List）**

当对象中的 List 属性包含复杂对象时，这是最常见的业务场景之一：

```java
// 接口方法
int batchInsertFromQuery(UserQuery query);
```

```java
// UserQuery 类
public class UserQuery {
    private List<UserMapperEntity> users;  // 复杂对象的 List

    // Getters and Setters...
    public List<UserMapperEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserMapperEntity> users) {
        this.users = users;
    }
}
```

```java
// UserMapperEntity 类
public class UserMapperEntity {
    private Long id;
    private String username;
    private String email;
    private Integer age;
    private Integer status;
    // Getters and Setters...
}
```

```xml
<!-- XML 配置 - 批量插入复杂对象 -->
<insert id="batchInsertFromQuery">
    INSERT INTO users (username, email, age, status, created_at)
    VALUES
    <foreach collection="users" item="user" separator=",">
        (#{user.username}, #{user.email}, #{user.age}, #{user.status}, NOW())
    </foreach>
</insert>
```

**关键点:**
- ✅ `collection="users"` - `users` 是 UserQuery 对象的属性名
- ✅ `item="user"` - 定义当前遍历的 UserMapperEntity 对象变量名
- ✅ `#{user.username}` - 访问 UserMapperEntity 对象的属性
- ✅ 可以访问复杂对象的所有属性：`#{user.username}`, `#{user.email}`, `#{user.age}`

**Java 调用示例:**
```java
// 创建用户列表
List<UserMapperEntity> users = Arrays.asList(
    new UserMapperEntity(null, "user1", "user1@example.com", 25, 1),
    new UserMapperEntity(null, "user2", "user2@example.com", 26, 1),
    new UserMapperEntity(null, "user3", "user3@example.com", 27, 1)
);

// 创建 UserQuery 对象，设置 users 属性
UserQuery query = new UserQuery();
query.setUsers(users);

// 执行批量插入
int rows = userMapper.batchInsertFromQuery(query);
// 返回: 3（插入3条记录）
```

**实际测试用例参考:**
- 测试文件: `Jdevelops-Example/dal-jdbctemplate/src/test/java/.../XmlMapper_annotation_Test.java`
- 测试方法: `testBatchInsertFromQueryWithListOfBeans()` (测试用例 32)
- 接口方法: `UserMapper#batchInsertFromQuery(UserQuery query)`
- XML 配置: `UserMapper.xml#batchInsertFromQuery`

**5. 单参数 Map**

当方法参数是 Map 时，直接使用 key 名访问：

```java
// 接口方法
List<User> findByCondition(Map<String, Object> params);
```

```xml
<!-- XML 配置 - 直接使用 Map 的 key -->
<select id="findByCondition">
    SELECT * FROM users
    WHERE status = #{status}
    AND age > #{minAge}
</select>
```

**参数访问对照表（扩展版）:**

| 场景 | 接口方法 | XML 中访问方式 | 示例 |
|------|---------|---------------|------|
| 单参数对象 | `findById(UserQuery query)` | 直接访问属性 | `#{username}`, `#{status}` |
| 单参数 List | `batchInsert(List<User> users)` | `collection="list"` | `<foreach collection="list" item="user">` |
| 单参数 Map | `findByCondition(Map params)` | 直接访问 key | `#{status}`, `#{minAge}` |
| 对象的 List 属性（基本类型） | `deleteByIds(UserQuery query)` | `collection="属性名"` | `<foreach collection="ids" item="id">` |
| 对象的 List 属性（复杂对象）⭐ | `batchInsert(UserQuery query)` | `collection="属性名"` | `<foreach collection="users" item="user">` + `#{user.username}` |
| 多参数 | `find(UserQuery q, PageRequest p)` | `arg0`, `arg1` | `#{arg0.status}`, `#{arg1.pageSize}` |
| 多参数的 List 属性 | `delete(UserQuery q, Integer s)` | `arg0.属性名` | `<foreach collection="arg0.ids" item="id">` |

**注意:** ⭐ 标记的是最常见的业务场景 - 对象属性是 `List<Bean>`，详见上文"4. 对象属性是 List of Objects"。

**常见错误示例:**

❌ **错误1: 混淆单参数 List 和对象属性 List**
```xml
<!-- 错误：方法参数是 UserQuery，不能用 "list" -->
<foreach collection="list" item="id">  <!-- 错误！ -->
    #{id}
</foreach>

<!-- 正确：使用对象的属性名 -->
<foreach collection="ids" item="id">  <!-- 正确 -->
    #{id}
</foreach>
```

❌ **错误2: 多参数时忘记使用 arg0/arg1**
```xml
<!-- 错误：多参数方法不能直接访问属性 -->
<foreach collection="ids" item="id">  <!-- 错误！ -->
    #{id}
</foreach>

<!-- 正确：使用 arg0 访问第一个参数的属性 -->
<foreach collection="arg0.ids" item="id">  <!-- 正确 -->
    #{id}
</foreach>
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

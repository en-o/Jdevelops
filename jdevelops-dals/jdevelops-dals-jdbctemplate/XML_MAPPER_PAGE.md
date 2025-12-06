# XML Mapper 分页查询完整指南

本文档详细介绍如何使用 XML Mapper 实现分页功能，提供**三种方式**：手动组合、框架内置分页（注解方式）、Registry 编程式分页。

---

## 📑 目录

1. [功能对比](#功能对比)
2. [分页组件](#分页组件)
3. [方式一：手动组合分页](#方式一手动组合分页)
4. [方式二：框架内置分页 - 注解方式（推荐）](#方式二框架内置分页---注解方式推荐)
5. [方式三：框架内置分页 - Registry 编程式](#方式三框架内置分页---registry-编程式)
6. [XML Mapper 配置](#xml-mapper-配置)
7. [使用示例](#使用示例)
8. [注意事项](#注意事项)
9. [性能优化](#性能优化)
10. [完整测试用例](#完整测试用例)

---

## 🎯 功能对比

| 方式 | 代码量 | 灵活性 | 适用场景 | 推荐度 |
|------|--------|--------|---------|--------|
| 手动组合 | 多（需手动调用两个方法并组装） | 高 | 需要特殊处理的场景 | ⭐⭐⭐ |
| 注解方式 | 少（一行代码完成） | 中 | 标准分页查询 | ⭐⭐⭐⭐⭐ |
| Registry 编程式 | 中（无需接口定义） | 高 | 动态场景、框架内部使用 | ⭐⭐⭐⭐ |

---

## 📦 分页组件

### 1. PageRequest - 分页请求参数

**位置:** `cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageRequest`

```java
public class PageRequest {
    private Integer pageNum = 1;      // 当前页码(从1开始)
    private Integer pageSize = 10;    // 每页大小
    private String orderBy;           // 排序字段
    private String orderDir = "DESC"; // 排序方向: ASC/DESC

    // 计算偏移量
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }

    // 获取完整的排序SQL
    public String getOrderBySql() {
        if (orderBy != null && !orderBy.trim().isEmpty()) {
            String dir = "DESC".equalsIgnoreCase(orderDir) ? "DESC" : "ASC";
            return orderBy + " " + dir;
        }
        return null;
    }
}
```

**使用示例：**
```java
PageRequest pageRequest = new PageRequest(1, 10);
pageRequest.setOrderBy("created_at");
pageRequest.setOrderDir("DESC");
```

### 2. PageResult - 分页响应结果

**位置:** `cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageResult`

```java
public class PageResult<T> {
    private Integer pageNum;      // 当前页码
    private Integer pageSize;     // 每页大小
    private Long total;           // 总记录数
    private Integer pages;        // 总页数（自动计算）
    private List<T> list;         // 数据列表
    private Boolean hasNext;      // 是否有下一页（自动计算）
    private Boolean hasPrevious;  // 是否有上一页（自动计算）

    public PageResult(Integer pageNum, Integer pageSize, Long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
        this.pages = (int) Math.ceil((double) total / pageSize);
        this.hasNext = pageNum < this.pages;
        this.hasPrevious = pageNum > 1;
    }
}
```

---

## 方式一：手动组合分页

### Mapper 接口定义

```java
@XmlMapper(namespace = "com.example.mapper.UserMapper")
public interface UserMapper {

    /**
     * 分页查询用户(带条件)
     */
    @XmlSelect("findUsersPageWithTotal")
    List<UserMapperEntity> findUsersPageWithTotal(UserQuery query, PageRequest pageRequest);

    /**
     * 统计符合条件的用户总数(用于分页)
     */
    @XmlSelect("countUsersByCondition")
    Long countUsersByCondition(UserQuery query);
}
```

### Service 层实现

```java
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 分页查询用户（手动组合）
     */
    public PageResult<UserMapperEntity> getUsersPage(int pageNum, int pageSize) {
        // 1. 创建分页参数
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);

        // 2. 创建查询条件
        UserQuery query = new UserQuery();

        // 3. 查询数据列表
        List<UserMapperEntity> list = userMapper.findUsersPageWithTotal(query, pageRequest);

        // 4. 查询总数
        Long total = userMapper.countUsersByCondition(query);

        // 5. 构建分页结果
        return new PageResult<>(pageNum, pageSize, total, list);
    }
}
```

---

## 方式二：框架内置分页 - 注解方式（推荐）

### @XmlPageSelect 注解

**位置:** `cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlPageSelect`

```java
@XmlPageSelect(
    dataStatement = "findUsersPageWithTotal",   // 数据查询 SQL ID（必填）
    countStatement = "countUsersByCondition",   // 统计查询 SQL ID（必填）
    tryc = false                                // 是否吞掉异常（默认 false）
)
PageResult<User> findUsersPageAuto(UserQuery query, PageRequest pageRequest);
```

**注解说明：**
- `dataStatement`: 数据查询SQL的ID，对应XML中的 `<select id="xxx">`
- `countStatement`: 统计查询SQL的ID，用于查询总记录数
- `tryc`: 是否吞掉异常，true时查询失败返回空的PageResult

**方法要求：**
- 必须有 `PageRequest` 类型的参数
- 返回类型必须是 `PageResult<T>`
- 可以有其他查询参数（会传递给数据查询和统计查询）

### Mapper 接口定义

```java
import cn.tannn.jdevelops.annotations.jdbctemplate.xml.*;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageRequest;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageResult;

@XmlMapper(namespace = "com.example.mapper.UserMapper")
public interface UserMapper {

    /**
     * 框架内置分页查询
     *
     * @param query 查询条件
     * @param pageRequest 分页参数（框架提供）
     * @return 分页结果（框架提供）
     */
    @XmlPageSelect(
        dataStatement = "findUsersPageWithTotal",
        countStatement = "countUsersByCondition"
    )
    PageResult<UserMapperEntity> findUsersPageAuto(UserQuery query, PageRequest pageRequest);
}
```

### Service 层实现

```java
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 分页查询用户（框架内置）
     */
    public PageResult<UserMapperEntity> getUsersPage(int pageNum, int pageSize) {
        // 1. 创建分页参数
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        pageRequest.setOrderBy("created_at");
        pageRequest.setOrderDir("DESC");

        // 2. 创建查询条件
        UserQuery query = new UserQuery();
        query.setStatus(1);

        // 3. 一键分页查询（框架自动处理）
        return userMapper.findUsersPageAuto(query, pageRequest);
    }

    /**
     * 多条件分页查询
     */
    public PageResult<UserMapperEntity> searchUsers(String username, Integer status,
                                         Integer minAge, Integer maxAge,
                                         int pageNum, int pageSize) {
        // 创建分页参数
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        pageRequest.setOrderBy("age");
        pageRequest.setOrderDir("ASC");

        // 创建查询条件
        UserQuery query = new UserQuery();
        query.setUsername(username);
        query.setStatus(status);
        query.setMinAge(minAge);
        query.setMaxAge(maxAge);

        // 框架自动处理分页
        return userMapper.findUsersPageAuto(query, pageRequest);
    }
}
```

---

## 方式三：框架内置分页 - Registry 编程式

除了注解方式，还可以通过 `XmlMapperRegistry` 编程式调用，适用于**动态场景**或**无需定义接口**的情况。

### Registry 编程式分页

```java
import cn.tannn.jdevelops.jdectemplate.xmlmapper.registry.XmlMapperRegistry;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageRequest;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageResult;

@Service
public class UserService {

    @Autowired
    private XmlMapperRegistry registry;

    /**
     * 使用 Registry 执行分页查询（无需定义接口）
     */
    public PageResult<UserMapperEntity> getUsersPageByRegistry(UserQuery query, int pageNum, int pageSize) {
        // 1. 创建分页参数
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        pageRequest.setOrderBy("created_at");
        pageRequest.setOrderDir("DESC");

        // 2. 调用 Registry 的内置分页方法（一键分页）
        return registry.executePageQuery(
                "com.example.mapper.UserMapper",  // namespace
                "findUsersPageWithTotal",          // 数据查询 SQL ID
                "countUsersByCondition",           // 统计查询 SQL ID
                query,                             // 查询参数
                pageRequest,                       // 分页参数
                UserMapperEntity.class            // 结果类型
        );
    }

    /**
     * 动态 namespace 场景
     */
    public PageResult<UserMapperEntity> dynamicPageQuery(String namespace, String dataStmt,
                                                          String countStmt, UserQuery query) {
        PageRequest pageRequest = new PageRequest(1, 10);

        return registry.executePageQuery(
                namespace,      // 动态 namespace
                dataStmt,       // 动态数据查询 SQL
                countStmt,      // 动态统计查询 SQL
                query,
                pageRequest,
                UserMapperEntity.class
        );
    }
}
```

**Registry 方式的优势：**
- ✅ 无需定义 Mapper 接口
- ✅ 支持动态 namespace 和 SQL ID
- ✅ 适用于框架内部或通用组件开发
- ✅ 同样享受框架自动分页功能

---

## 📝 XML Mapper 配置

### UserMapper.xml

```xml
<!-- 分页查询 SQL 片段(通用查询条件 - 单参数) -->
<sql id="pageWhereConditions">
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
            AND age &lt;= #{maxAge}
        </if>
    </where>
</sql>

<!-- 分页查询用户(带条件) - 注意多参数需要使用 arg0/arg1 -->
<select id="findUsersPageWithTotal" resultType="UserMapperEntity">
    SELECT
        <include refid="baseColumns"/>
    FROM users_mapper
    <where>
        <if test="arg0.username != null and arg0.username != ''">
            AND username LIKE CONCAT('%', #{arg0.username}, '%')
        </if>
        <if test="arg0.email != null and arg0.email != ''">
            AND email = #{arg0.email}
        </if>
        <if test="arg0.status != null">
            AND status = #{arg0.status}
        </if>
        <if test="arg0.minAge != null">
            AND age >= #{arg0.minAge}
        </if>
        <if test="arg0.maxAge != null">
            AND age &lt;= #{arg0.maxAge}
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

<!-- 统计符合条件的用户总数(单参数) -->
<select id="countUsersByCondition" resultType="java.lang.Long">
    SELECT COUNT(*)
    FROM users_mapper
    <include refid="pageWhereConditions"/>
</select>
```

**注意事项:**
- 多参数方法中,使用 `arg0`、`arg1` 访问参数(第一个参数 query 是 arg0,第二个参数 pageRequest 是 arg1)
- **重要**: `findUsersPageWithTotal` 是双参数方法,必须使用 `arg0.status`、`arg1.pageSize` 这样的形式访问属性
- `countUsersByCondition` 是单参数方法,可以直接使用 `status` 访问属性,因此可以复用 `pageWhereConditions` 片段
- `${arg1.orderBySql}` 使用 `${}` 而不是 `#{}`,因为排序字段需要直接拼接
- 为保证条件一致,分页查询和统计查询的 WHERE 条件逻辑应该相同

---

## 💻 使用示例

### Controller 示例（注解方式）

```java
import cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageRequest;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageResult;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 分页查询用户（框架内置分页）
     */
    @GetMapping("/page")
    public PageResult<UserMapperEntity> getUsersPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "DESC") String sortDir) {

        // 创建分页参数
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        if (sortField != null) {
            pageRequest.setOrderBy(sortField);
            pageRequest.setOrderDir(sortDir);
        }

        // 创建查询条件
        UserQuery query = new UserQuery();
        query.setUsername(username);
        query.setStatus(status);

        // 框架自动分页
        return userMapper.findUsersPageAuto(query, pageRequest);
    }
}
```

### Controller 示例（Registry 方式）

```java
@RestController
@RequestMapping("/api/dynamic")
public class DynamicQueryController {

    @Autowired
    private XmlMapperRegistry registry;

    /**
     * 动态分页查询（Registry 编程式）
     */
    @GetMapping("/page")
    public PageResult<UserMapperEntity> dynamicPageQuery(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status) {

        PageRequest pageRequest = new PageRequest(pageNum, pageSize);

        UserQuery query = new UserQuery();
        query.setStatus(status);

        // 使用 Registry 动态调用
        return registry.executePageQuery(
                "com.example.mapper.UserMapper",
                "findUsersPageWithTotal",
                "countUsersByCondition",
                query,
                pageRequest,
                UserMapperEntity.class
        );
    }
}
```

**请求示例:**
```
GET /api/users/page?pageNum=1&pageSize=10&status=1&sortField=age&sortDir=ASC
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
      "status": 1
    }
  ]
}
```

---

## ⚠️ 注意事项

### 1. 参数访问方式（重要）

**多参数方法中的参数访问:**

```xml
<!-- 接口方法签名:
     PageResult<User> findUsersPageAuto(UserQuery query, PageRequest pageRequest)
-->
<select id="findUsersPageWithTotal" resultType="User">
    SELECT * FROM users
    <where>
        <!-- query 是第一个参数，使用 arg0 访问 -->
        <if test="arg0.username != null">
            AND username = #{arg0.username}
        </if>
        <if test="arg0.status != null">
            AND status = #{arg0.status}
        </if>
    </where>
    ORDER BY
    <!-- pageRequest 是第二个参数，使用 arg1 访问 -->
    <if test="arg1.orderBySql != null and arg1.orderBySql != ''">
        ${arg1.orderBySql}
    </if>
    <if test="arg1.orderBySql == null or arg1.orderBySql == ''">
        created_at DESC
    </if>
    <!-- 分页参数 -->
    LIMIT #{arg1.pageSize} OFFSET #{arg1.offset}
</select>
```

**单参数方法中的参数访问:**

```xml
<!-- 接口方法签名: Long countUsersByCondition(UserQuery query) -->
<select id="countUsersByCondition" resultType="java.lang.Long">
    SELECT COUNT(*) FROM users
    <where>
        <!-- 单参数可以直接访问属性 -->
        <if test="username != null">
            AND username = #{username}
        </if>
        <if test="status != null">
            AND status = #{status}
        </if>
    </where>
</select>
```

**参数访问规则总结:**

| 方法参数数量 | XML 中访问方式 | 示例 |
|------------|------------|------|
| 单参数 | 直接访问属性 | `#{username}`, `#{status}` |
| 双参数 | `arg0`, `arg1` | `#{arg0.status}`, `#{arg1.pageSize}` |
| 三参数 | `arg0`, `arg1`, `arg2` | 以此类推 |

### 2. 排序字段安全性

**使用 `${}` 拼接排序字段时要注意 SQL 注入风险:**

```java
// 好的做法: 限制可排序的字段
public PageResult<User> getUsersPage(String sortField) {
    // 白名单验证
    List<String> allowedFields = Arrays.asList("id", "username", "age", "created_at");
    if (!allowedFields.contains(sortField)) {
        sortField = "created_at";  // 默认排序字段
    }

    PageRequest pageRequest = new PageRequest();
    pageRequest.setOrderBy(sortField);
    // ...
}
```

### 3. 三种方式选择指南

| 场景 | 推荐方式 | 理由 |
|------|---------|------|
| 标准分页查询 | **注解方式** | 代码简洁，类型安全，团队统一规范 |
| 需要特殊处理 | 手动组合 | 灵活性高，可自定义处理逻辑 |
| 动态 SQL 场景 | **Registry 编程式** | 运行时决定调用哪个 SQL，无需定义接口 |
| 新项目 | **注解方式** | 开发效率高，维护成本低 |
| 框架内部开发 | **Registry 编程式** | 通用性强，不依赖具体接口 |
| 老项目迁移 | 三种并存 | 渐进式迁移，降低风险 |

### 4. 常见错误

**❌ 错误 1：多参数方法直接访问属性**
```xml
<!-- 错误：双参数方法不能直接访问属性 -->
<select id="findUsersPageWithTotal">
    WHERE status = #{status}  <!-- 错误！ -->
</select>
```

**✅ 正确写法：**
```xml
<select id="findUsersPageWithTotal">
    WHERE status = #{arg0.status}  <!-- 正确 -->
</select>
```

**❌ 错误 2：分页查询和统计查询条件不一致**
```xml
<!-- 数据查询 -->
<select id="findUsersPageWithTotal">
    WHERE status = #{arg0.status} AND age >= #{arg0.minAge}
</select>

<!-- 统计查询 - 条件不一致，导致分页结果错误 -->
<select id="countUsersByCondition">
    WHERE status = #{status}  <!-- 缺少 age 条件 -->
</select>
```

**✅ 正确写法：使用 SQL 片段复用条件**
```xml
<!-- 定义通用查询条件 -->
<sql id="commonWhere">
    <where>
        <if test="status != null">AND status = #{status}</if>
        <if test="minAge != null">AND age >= #{minAge}</if>
    </where>
</sql>

<!-- 数据查询 -->
<select id="findUsersPageWithTotal">
    SELECT * FROM users
    <where>
        <if test="arg0.status != null">AND status = #{arg0.status}</if>
        <if test="arg0.minAge != null">AND age >= #{arg0.minAge}</if>
    </where>
    LIMIT #{arg1.pageSize} OFFSET #{arg1.offset}
</select>

<!-- 统计查询 - 条件一致 -->
<select id="countUsersByCondition">
    SELECT COUNT(*) FROM users
    <include refid="commonWhere"/>  <!-- 复用条件 -->
</select>
```

---

## 🚀 性能优化

### 1. 添加索引

对于大数据量查询，建议添加索引：

```sql
CREATE INDEX idx_status ON users(status);
CREATE INDEX idx_age ON users(age);
CREATE INDEX idx_created_at ON users(created_at);
```

### 2. 避免深分页

```java
// 限制最大页码
if (pageNum > 100) {
    pageNum = 100;
}
```

### 3. 使用游标分页（超大数据量）

```sql
<!-- 使用 ID 游标而不是 OFFSET -->
SELECT * FROM users
WHERE id > #{lastId}
ORDER BY id
LIMIT #{pageSize}
```

---

## 📚 完整测试用例

### 1. 手动组合方式测试

```java
@Test
@DisplayName("07. 分页查询 - 第一页（手动组合）")
void testPageQuery_Manual() {
    // 创建分页参数
    PageRequest pageRequest = new PageRequest(1, 5);
    pageRequest.setOrderBy("created_at");
    pageRequest.setOrderDir("DESC");

    // 创建查询条件
    UserQuery query = new UserQuery();
    query.setStatus(1);

    // 手动组合分页
    List<UserMapperEntity> list = userMapper.findUsersPageWithTotal(query, pageRequest);
    Long total = userMapper.countUsersByCondition(query);
    PageResult<UserMapperEntity> pageResult = new PageResult<>(
            pageRequest.getPageNum(),
            pageRequest.getPageSize(),
            total,
            list
    );

    // 验证结果
    assertNotNull(pageResult.getList());
    assertTrue(pageResult.getList().size() <= 5);
    assertEquals(1, pageResult.getPageNum());
    assertNotNull(pageResult.getTotal());

    System.out.println("分页查询结果: " + pageResult);
    System.out.println("总记录数: " + pageResult.getTotal());
    System.out.println("总页数: " + pageResult.getPages());
}
```

### 2. 注解方式测试

```java
@Test
@DisplayName("11. 【框架内置分页】注解方式一键分页")
void testPageQueryAuto() {
    // 创建分页参数
    PageRequest pageRequest = new PageRequest(1, 5);
    pageRequest.setOrderBy("created_at");
    pageRequest.setOrderDir("DESC");

    // 创建查询条件
    UserQuery query = new UserQuery();
    query.setStatus(1);

    // 框架自动分页（一行代码）
    PageResult<UserMapperEntity> pageResult =
            userMapper.findUsersPageAuto(query, pageRequest);

    // 验证结果
    assertNotNull(pageResult);
    assertNotNull(pageResult.getList());
    assertEquals(1, pageResult.getPageNum());
    assertTrue(pageResult.getTotal() > 0);

    System.out.println("=========================================");
    System.out.println("【框架内置分页】注解方式测试结果:");
    System.out.println("当前页码: " + pageResult.getPageNum());
    System.out.println("总记录数: " + pageResult.getTotal());
    System.out.println("总页数: " + pageResult.getPages());
    System.out.println("当前页数据量: " + pageResult.getList().size());
    System.out.println("是否有下一页: " + pageResult.getHasNext());
    System.out.println("=========================================");
}
```

### 3. Registry 编程式测试

```java
@Test
@DisplayName("56. 【框架内置分页】Registry 编程式一键分页")
void testPageQueryAutoWithRegistry() {
    String namespace = "cn.tannn.demo.jdevelops.daljdbctemplate.mapper.UserMapper";

    // 创建分页参数
    PageRequest pageRequest = new PageRequest(1, 5);
    pageRequest.setOrderBy("created_at");
    pageRequest.setOrderDir("DESC");

    // 创建查询条件
    UserQuery query = new UserQuery();
    query.setStatus(1);

    // 使用 Registry 的 executePageQuery 方法（框架自动处理分页）
    PageResult<UserMapperEntity> pageResult = registry.executePageQuery(
            namespace,
            "findUsersPageWithTotal",      // 数据查询 SQL
            "countUsersByCondition",       // 统计查询 SQL
            query,                         // 查询参数
            pageRequest,                   // 分页参数
            UserMapperEntity.class         // 结果类型
    );

    // 验证结果
    assertNotNull(pageResult);
    assertNotNull(pageResult.getList());
    assertTrue(pageResult.getList().size() <= 5);
    assertNotNull(pageResult.getTotal());
    assertTrue(pageResult.getTotal() > 0);

    System.out.println("=========================================");
    System.out.println("【Registry 内置分页】测试结果:");
    System.out.println("当前页码: " + pageResult.getPageNum());
    System.out.println("总记录数: " + pageResult.getTotal());
    System.out.println("总页数: " + pageResult.getPages());
    System.out.println("是否有下一页: " + pageResult.getHasNext());
    System.out.println("=========================================");
}
```

### 4. 多条件分页测试

```java
@Test
@DisplayName("12. 【框架内置分页】多条件分页")
void testPageQueryAutoWithConditions() {
    // 创建分页参数
    PageRequest pageRequest = new PageRequest(1, 3);
    pageRequest.setOrderBy("age");
    pageRequest.setOrderDir("ASC");

    // 创建查询条件
    UserQuery query = new UserQuery();
    query.setStatus(1);
    query.setMinAge(20);
    query.setMaxAge(28);

    // 框架自动处理分页
    PageResult<UserMapperEntity> pageResult =
            userMapper.findUsersPageAuto(query, pageRequest);

    // 验证结果
    assertNotNull(pageResult);
    assertTrue(pageResult.getList().size() <= 3);

    System.out.println("【多条件分页】总数=" + pageResult.getTotal() +
            ", 当前页=" + pageResult.getList().size());

    // 验证数据
    pageResult.getList().forEach(user -> {
        assertTrue(user.getAge() >= 20 && user.getAge() <= 28,
                "年龄应该在20-28之间");
        assertEquals(1, user.getStatus(), "状态应该为1");
    });
}
```

---

## 🎊 总结

### 三种分页方式对比

| 特性 | 手动组合 | 注解方式 | Registry 编程式 |
|------|---------|---------|---------------|
| 代码量 | 多（3行+） | 少（1行） | 中（1行，但参数多） |
| 类型安全 | ✅ | ✅ | ⚠️（需手动转换） |
| 灵活性 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| 开发效率 | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| 学习成本 | 低 | 低 | 中 |
| 适用场景 | 特殊处理 | 标准分页（推荐） | 动态场景、框架内部 |
| 需要接口定义 | ✅ | ✅ | ❌ |

### 框架内置分页的优势

1. ✅ **一键分页** - 一行代码完成数据查询 + 统计查询 + 结果组装
2. ✅ **类型安全** - 编译期检查，避免运行时错误
3. ✅ **代码简洁** - 减少70%的分页代码
4. ✅ **统一标准** - 团队统一使用框架提供的分页组件
5. ✅ **自动计算** - 自动计算总页数、是否有上一页/下一页等
6. ✅ **灵活配置** - 支持动态排序、多条件查询
7. ✅ **三种方式** - 支持注解方式、Registry 编程式、手动组合
8. ✅ **无需接口** - Registry 方式支持动态调用，无需定义接口

### 使用建议

- **新项目**: 推荐使用 `@XmlPageSelect` 注解方式，开发效率最高
- **动态场景**: 使用 Registry 编程式，支持运行时决定调用哪个 SQL
- **框架开发**: 使用 Registry 编程式，通用性强，不依赖具体接口
- **老项目**: 可以保留原有的手动组合方式，新功能使用框架内置分页
- **复杂场景**: 如果需要特殊处理，可以使用手动组合方式
- **团队规范**: 建议统一使用框架内置分页，提高代码一致性

### 最佳实践清单

- [x] 分页查询和统计查询的 WHERE 条件保持一致
- [x] 使用 `<sql>` 片段复用查询条件
- [x] 多参数方法使用 `arg0`, `arg1` 访问参数
- [x] 排序字段使用白名单验证，防止 SQL 注入
- [x] 优先使用注解方式，代码更简洁
- [x] 动态场景使用 Registry 编程式
- [x] 大数据量添加索引优化性能
- [x] 避免深分页，限制最大页码

---

**完整示例代码:**

测试项目路径: `Jdevelops-Example/dal-jdbctemplate`

- **测试用例:**
  - [XmlMapper_annotation_Test.java](https://github.com/en-o/Jdevelops-Example/blob/master/dal-jdbctemplate/src/test/java/cn/tannn/demo/jdevelops/daljdbctemplate/XmlMapper_annotation_Test.java) - 包含手动组合和注解方式分页测试（测试用例 07-12）
  - [XmlMapper_registry_Test.java](https://github.com/en-o/Jdevelops-Example/blob/master/dal-jdbctemplate/src/test/java/cn/tannn/demo/jdevelops/daljdbctemplate/XmlMapper_registry_Test.java) - Registry 编程式分页测试（测试用例 55-56）

- **XML 配置:**
  - [UserMapper.xml](https://github.com/en-o/Jdevelops-Example/blob/master/dal-jdbctemplate/src/main/resources/jmapper/UserMapper.xml) - 完整的分页 SQL 配置

- **相关文档:**
  - [XML_MAPPER.md](./XML_MAPPER.md) - XML Mapper 核心功能说明

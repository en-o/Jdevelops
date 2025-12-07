# XML Mapper 问题排查指南

## ✅ 已修复的问题

### 问题：Count查询条件缺失

**原因**：
- Data查询传参：`Arrays.asList(queryParameter, pageRequest)` - 可以用 `arg0.xxx`、`arg1.xxx`
- Count查询传参：`queryParameter` - 只能直接用 `xxx`，不能用 `arg0`、`arg1`

**修复**：
已修改 `XmlMapperRegistry.java:170`，让count查询也接收相同格式的参数：
```java
Object countResult = sqlExecutor.execute(countStatement,
        Arrays.asList(queryParameter, pageRequest), Long.class);
```

**现在**：count和data查询都可以使用完全相同的XML条件了！

---

## 🔍 待排查的问题

### 问题1：orgNo条件没有生成

**现象**：
- 传入参数：`{"orgNo":"A001003", ...}`
- 期望生成：`AND a.org_no = #{arg0.orgNo}`
- 实际：条件没有生成

**可能的原因**：

#### 原因1：XML中的条件判断写法不对

**错误写法**：
```xml
<!-- ❌ 只检查orgNo，但参数是List，arg0才是queryParameter -->
<if test="orgNo != null and orgNo != ''">
    AND a.org_no = #{arg0.orgNo}
</if>
```

**正确写法**：
```xml
<!-- ✅ 使用arg0.orgNo -->
<if test="arg0.orgNo != null and arg0.orgNo != ''">
    AND a.org_no = #{arg0.orgNo}
</if>
```

#### 原因2：字段值是空字符串

你传入的参数中，`name`、`status`、`loginName` 都是空字符串 `""`，而不是 `null`。

如果 `orgNo` 也是空字符串，需要在XML中同时检查：
```xml
<if test="arg0.orgNo != null and arg0.orgNo != ''">
    AND a.org_no = #{arg0.orgNo}
</if>
```

#### 原因3：record类型的字段访问

如果 `AccountPage` 是record类型，可能需要使用方法调用：
```xml
<!-- 如果是record -->
<if test="arg0.orgNo() != null and arg0.orgNo() != ''">
    AND a.org_no = #{arg0.orgNo()}
</if>

<!-- 或者使用getter -->
<if test="arg0.getOrgNo() != null and arg0.getOrgNo() != ''">
    AND a.org_no = #{arg0.orgNo}
</if>
```

**排查步骤**：
1. 开启DEBUG日志，查看OgnlUtil的输出
2. 检查XML中的test表达式是否正确
3. 确认传入的orgNo值不是空字符串

---

### 问题2：loginPlatform导致查询返回空，但SQL可以查到数据

**现象**：
- 传入参数：`{"loginPlatform":"ADMIN", ...}`
- 生成的SQL在数据库中可以查到数据
- 但通过代码查询返回空结果

**这是典型的参数绑定问题**！SQL是对的，但参数值不对。

**可能的原因**：

#### 原因1：枚举参数绑定方式错误

**错误写法**：
```xml
<!-- ❌ 直接绑定枚举对象 -->
<if test="arg0.loginPlatform != null">
    AND a.login_platform = #{arg0.loginPlatform}
</if>
```

当你执行时，可能绑定的是枚举对象本身，而不是枚举的name或value。

**正确写法**：
```xml
<!-- ✅ 使用枚举的name()方法 -->
<if test="arg0.loginPlatform != null">
    AND a.login_platform = #{arg0.loginPlatform.name()}
</if>

<!-- 或者使用ordinal()获取序号 -->
<if test="arg0.loginPlatform != null">
    AND a.login_platform = #{arg0.loginPlatform.ordinal()}
</if>
```

#### 原因2：数据库字段类型不匹配

如果数据库中 `login_platforms` 是逗号分隔的字符串（如 `"WEB,ADMIN,MOBILE"`），应该使用：

```xml
<if test="arg0.loginPlatform != null">
    AND FIND_IN_SET(#{arg0.loginPlatform.name()}, a.login_platforms) > 0
</if>
```

#### 原因3：枚举名称与数据库值不匹配

**Java枚举**：
```java
enum PlatformType {
    WEB_ADMIN,  // 枚举name
    MOBILE
}
```

**数据库值**：`ADMIN`（不是 `WEB_ADMIN`）

**解决方案**：
```java
enum PlatformType {
    WEB_ADMIN("ADMIN"),  // 构造函数传入数据库值
    MOBILE("MOBILE");

    private final String dbValue;

    PlatformType(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }
}
```

```xml
<!-- 使用自定义的数据库值 -->
<if test="arg0.loginPlatform != null">
    AND a.login_platform = #{arg0.loginPlatform.dbValue}
</if>
```

**排查步骤**：
1. 开启JdbcTemplate的DEBUG日志，查看实际绑定的参数值
2. 确认枚举的name()或value与数据库字段值一致
3. 如果是逗号分隔的字段，使用FIND_IN_SET函数

---

## 📋 推荐的XML写法模板

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.sunway.dymm.mapper.UserMapper">

    <!-- 公共WHERE条件 -->
    <sql id="accountQueryConditions">
        <where>
            <!-- 组织编码 -->
            <if test="arg0.orgNo != null and arg0.orgNo != ''">
                AND a.org_no = #{arg0.orgNo}
            </if>

            <!-- 登录名（LIKE查询） -->
            <if test="arg0.loginName != null and arg0.loginName != ''">
                AND a.login_name LIKE CONCAT('%', #{arg0.loginName}, '%')
            </if>

            <!-- 姓名（LIKE查询） -->
            <if test="arg0.name != null and arg0.name != ''">
                AND a.name LIKE CONCAT('%', #{arg0.name}, '%')
            </if>

            <!-- 状态 -->
            <if test="arg0.status != null">
                AND a.status = #{arg0.status}
            </if>

            <!-- 登录平台（使用FIND_IN_SET，假设数据库是逗号分隔） -->
            <if test="arg0.loginPlatform != null">
                AND FIND_IN_SET(#{arg0.loginPlatform.name()}, a.login_platforms) > 0
            </if>

            <!-- 组织名称（LIKE查询） -->
            <if test="arg0.orgName != null and arg0.orgName != ''">
                AND a.org_name LIKE CONCAT('%', #{arg0.orgName}, '%')
            </if>
        </where>
    </sql>

    <!-- 数据查询 -->
    <select id="findAccountPage" resultType="com.sunway.dymm.controller.user.vo.AccountFull2">
        SELECT
            a.id,
            a.create_time AS createTime,
            a.login_name AS loginName,
            a.name,
            a.status,
            a.login_platforms AS loginPlatforms,
            a.org_no AS orgNo,
            a.org_name AS orgName,
            b.phone
        FROM tb_account a
        LEFT JOIN tb_account_sensitive b ON b.user_id = a.id

        <include refid="accountQueryConditions"/>

        ORDER BY a.id DESC
    </select>

    <!-- 统计查询（使用相同的WHERE条件） -->
    <select id="countAccountTotal" resultType="java.lang.Long">
        SELECT COUNT(*)
        FROM tb_account a
        LEFT JOIN tb_account_sensitive b ON b.user_id = a.id

        <include refid="accountQueryConditions"/>
    </select>

</mapper>
```

---

## 🔧 开启调试日志

在 `application.yml` 中添加：

```yaml
logging:
  level:
    # XML Mapper日志
    cn.tannn.jdevelops.jdectemplate.xmlmapper: DEBUG

    # JdbcTemplate日志（查看实际执行的SQL和参数）
    org.springframework.jdbc.core.JdbcTemplate: DEBUG
    org.springframework.jdbc.core.StatementCreatorUtils: TRACE
```

查看日志输出：
1. `OgnlUtil` 会输出每个表达式的求值结果
2. `XmlSqlExecutor` 会输出生成的SQL
3. `JdbcTemplate` 会输出实际绑定的参数值

---

## 🎯 关键检查点

### 检查点1：参数格式
```
✅ 现在已修复：count和data查询都使用 Arrays.asList(queryParameter, pageRequest)
✅ 可以在XML中使用：arg0（queryParameter）、arg1（pageRequest）
```

### 检查点2：条件判断
```xml
<!-- ✅ 正确：检查arg0的属性 -->
<if test="arg0.orgNo != null and arg0.orgNo != ''">

<!-- ❌ 错误：直接检查orgNo（参数是List，没有orgNo属性） -->
<if test="orgNo != null and orgNo != ''">
```

### 检查点3：枚举绑定
```xml
<!-- ✅ 正确：使用name()或自定义的dbValue -->
AND a.login_platform = #{arg0.loginPlatform.name()}

<!-- ❌ 错误：直接绑定枚举对象 -->
AND a.login_platform = #{arg0.loginPlatform}
```

### 检查点4：空字符串处理
```xml
<!-- ✅ 正确：同时检查null和空字符串 -->
<if test="arg0.name != null and arg0.name != ''">

<!-- ❌ 错误：只检查null，空字符串会生成条件 -->
<if test="arg0.name != null">
```

---

## 📞 还有问题？

如果按照上面的排查仍然有问题，请提供：
1. 完整的XML mapper文件内容
2. 完整的Java实体类（AccountPage、AccountFull2）
3. 开启DEBUG日志后的输出
4. 生成的SQL语句
5. 实际的数据库字段类型和值

---

**修复时间**：2025-12-08

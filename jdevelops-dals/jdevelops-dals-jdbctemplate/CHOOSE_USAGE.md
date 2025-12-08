# Choose/When/Otherwise 标签使用说明

## 概述

`<choose>`、`<when>` 和 `<otherwise>` 标签类似于 Java 中的 `switch-case` 或 `if-else if-else` 结构，用于在多个条件中选择一个执行。

## 语法结构

```xml
<choose>
    <when test="条件表达式1">
        SQL 片段1
    </when>
    <when test="条件表达式2">
        SQL 片段2
    </when>
    <otherwise>
        默认 SQL 片段
    </otherwise>
</choose>
```

## 执行逻辑

1. 从上到下依次评估每个 `<when>` 的 `test` 条件
2. 找到第一个条件为 `true` 的 `<when>`，执行其内容，然后结束
3. 如果所有 `<when>` 条件都为 `false`，执行 `<otherwise>` 的内容（如果存在）
4. `<otherwise>` 是可选的

## 使用示例

### 示例 1: 基本用法 - orgNo 判断

这是您提到的实际场景：

```xml
<select id="findByOrgNo" resultType="User">
    SELECT * FROM t_user a
    WHERE 1=1
    <choose>
        <when test="orgNo == '-'">
            AND a.org_no IS NULL
        </when>
        <otherwise>
            AND a.org_no = #{orgNo}
        </otherwise>
    </choose>
</select>
```

**说明：**
- 当 `orgNo` 等于 `'-'` 时，添加条件 `AND a.org_no IS NULL`
- 其他情况下，添加条件 `AND a.org_no = #{orgNo}`

### 示例 2: 多个 when 条件

```xml
<select id="findByStatus" resultType="User">
    SELECT * FROM t_user a
    WHERE 1=1
    <choose>
        <when test="status == 1">
            AND a.status = 1
            AND a.enabled = true
        </when>
        <when test="status == 2">
            AND a.status = 2
            AND a.enabled = false
        </when>
        <when test="status == 0">
            AND a.status = 0
        </when>
        <otherwise>
            AND a.status IN (1, 2, 0)
        </otherwise>
    </choose>
</select>
```

**说明：**
- 只有第一个匹配的 `when` 会被执行
- 其他 `when` 即使条件为 `true` 也不会执行

### 示例 3: 嵌套使用（choose 嵌套 if）

```xml
<select id="complexQuery" resultType="User">
    SELECT * FROM t_user a
    WHERE 1=1
    <choose>
        <when test="queryType == 'ORG'">
            <if test="orgNo != null and orgNo != ''">
                AND a.org_no = #{orgNo}
            </if>
            <if test="deptNo != null and deptNo != ''">
                AND a.dept_no = #{deptNo}
            </if>
        </when>
        <when test="queryType == 'USER'">
            <if test="userId != null">
                AND a.id = #{userId}
            </if>
            <if test="username != null and username != ''">
                AND a.username LIKE CONCAT('%', #{username}, '%')
            </if>
        </when>
        <otherwise>
            AND a.enabled = true
        </otherwise>
    </choose>
</select>
```

### 示例 4: 与 where 标签结合

```xml
<select id="dynamicWhere" resultType="User">
    SELECT * FROM t_user a
    <where>
        <if test="username != null and username != ''">
            AND a.username = #{username}
        </if>
        <choose>
            <when test="orgLevel == 1">
                AND a.org_no IN (SELECT org_no FROM t_org WHERE level = 1)
            </when>
            <when test="orgLevel == 2">
                AND a.org_no IN (SELECT org_no FROM t_org WHERE level = 2)
            </when>
            <otherwise>
                AND a.org_no IS NOT NULL
            </otherwise>
        </choose>
    </where>
</select>
```

### 示例 5: 权限控制场景

```xml
<select id="findByPermission" resultType="User">
    SELECT * FROM t_user a
    WHERE 1=1
    <choose>
        <when test="role == 'ADMIN'">
            -- 管理员可以查看所有数据
        </when>
        <when test="role == 'MANAGER'">
            -- 管理者只能查看本部门数据
            AND a.dept_no = #{currentUserDeptNo}
        </when>
        <when test="role == 'USER'">
            -- 普通用户只能查看自己的数据
            AND a.id = #{currentUserId}
        </when>
        <otherwise>
            -- 其他角色无权限
            AND 1=0
        </otherwise>
    </choose>
</select>
```

### 示例 6: 与 foreach 结合

```xml
<select id="findByDynamicCondition" resultType="User">
    SELECT * FROM t_user a
    WHERE 1=1
    <choose>
        <when test="ids != null and ids.size() > 0">
            AND a.id IN
            <foreach collection="ids" item="id" open="(" separator="," close=")">
                #{id}
            </foreach>
        </when>
        <when test="orgNos != null and orgNos.size() > 0">
            AND a.org_no IN
            <foreach collection="orgNos" item="orgNo" open="(" separator="," close=")">
                #{orgNo}
            </foreach>
        </when>
        <otherwise>
            AND a.enabled = true
        </otherwise>
    </choose>
</select>
```

## 条件表达式（test 属性）

支持 OGNL 表达式，常用的操作符：

- 比较：`==`, `!=`, `>`, `<`, `>=`, `<=`
- 逻辑：`&&`（and）, `||`（or）, `!`（not）
- 空值判断：`!= null`, `== null`
- 字符串判断：`!= ''`, `== ''`
- 集合判断：`!= null and size() > 0`

示例：
```xml
<when test="status != null and status == 1">
<when test="username != null and username != ''">
<when test="ids != null and ids.size() > 0">
<when test="age >= 18 and age < 60">
<when test="type == 'A' || type == 'B'">
```

## 注意事项

1. **只执行一个分支**：`choose` 只会执行第一个匹配的 `when` 分支，即使后面还有其他条件为 `true` 的 `when` 也不会执行

2. **when 顺序很重要**：应该将更具体的条件放在前面

3. **otherwise 是可选的**：如果没有 `otherwise` 且所有 `when` 都不匹配，则不会添加任何 SQL

4. **test 属性必需**：每个 `when` 必须有 `test` 属性

5. **至少一个 when**：`choose` 标签内必须至少有一个 `when` 标签

## 实现类

- `ChooseSqlNode.java` - Choose 节点实现
- `WhenSqlNode.java` - When 节点实现
- `OtherwiseSqlNode.java` - Otherwise 节点实现

## 相关标签

- `<if>` - 单个条件判断
- `<where>` - 动态 WHERE 子句
- `<set>` - 动态 SET 子句
- `<foreach>` - 循环遍历
- `<trim>` - 前缀/后缀修剪

## 测试

参考测试类：`ChooseSqlNodeTest.java`
参考 XML 示例：`ChooseExampleMapper.xml`

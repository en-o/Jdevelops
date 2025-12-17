# XmlMapper 离线 DTD 支持

## 功能说明

XmlMapper 组件支持在**有网**和**无网**环境下使用，通过内置的离线 DTD 处理机制，自动将在线 DTD 引用替换为本地 DTD 文件。

**核心特性：**
- ✅ 自动处理在线 DTD 引用，无需修改 XML 文件
- ✅ 支持有网和无网环境无缝切换
- ✅ 完全兼容标准 MyBatis DTD 声明
- ✅ 内置本地 DTD 文件，开箱即用

---

## 工作原理

### 在线 DTD 自动转换

当 XML 文件使用在线 DTD 引用时（如 `http://mybatis.org/dtd/mybatis-3-mapper.dtd`），组件会自动拦截该请求并使用内置的本地 DTD 文件替代。

**处理流程：**

```
XML 文件声明在线 DTD
    ↓
XML 解析器尝试加载 DTD
    ↓
OfflineEntityResolver 拦截请求
    ↓
自动加载本地 DTD：/dtd/mybatis-3-mapper.dtd
    ↓
完成 XML 解析
```

### 支持的 DTD 引用

#### 1. 在线 DTD（自动转换）

**HTTP 协议：**
```xml
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
```

**HTTPS 协议：**
```xml
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
```

**公共标识符：**
```xml
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN">
```

#### 2. 本地 DTD（保持原样）

如果 XML 文件已经使用本地路径，则不做处理：

```xml
<!DOCTYPE mapper SYSTEM "../../dtd/mybatis-3-mapper.dtd">
```

---

## 使用方式

### 推荐方式（标准 MyBatis 风格）

直接使用标准的 MyBatis DTD 声明，组件会自动处理：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.example.mapper.UserMapper">

    <!-- SQL 片段 -->
    <sql id="baseColumns">
        id, username, email, created_at
    </sql>

    <!-- 查询语句 -->
    <select id="findById" resultType="com.example.entity.User">
        SELECT <include refid="baseColumns"/>
        FROM users
        WHERE id = #{id}
    </select>

    <!-- 动态 SQL -->
    <select id="findUsers" resultType="com.example.entity.User">
        SELECT <include refid="baseColumns"/>
        FROM users
        <where>
            <if test="username != null and username != ''">
                AND username LIKE #{username}
            </if>
            <if test="email != null">
                AND email = #{email}
            </if>
        </where>
    </select>

    <!-- 插入语句 -->
    <insert id="insertUser">
        INSERT INTO users (username, email, created_at)
        VALUES (#{username}, #{email}, NOW())
    </insert>

    <!-- 更新语句 -->
    <update id="updateUser">
        UPDATE users
        <set>
            <if test="username != null">username = #{username},</if>
            <if test="email != null">email = #{email},</if>
        </set>
        WHERE id = #{id}
    </update>

    <!-- 删除语句 -->
    <delete id="deleteById">
        DELETE FROM users WHERE id = #{id}
    </delete>

</mapper>
```

---

## 技术实现

### 内置本地 DTD

组件内置了 MyBatis 3.0 Mapper DTD 文件，位置：

```
jar包根目录/dtd/mybatis-3-mapper.dtd
```

源码位置：
```
src/main/resources/dtd/mybatis-3-mapper.dtd
```

### OfflineEntityResolver

自动拦截和处理 DTD 加载的核心组件：

**功能：**
1. 检测 XML 文件的 DTD 引用
2. 识别在线 DTD URL
3. 自动替换为本地 DTD
4. 保留本地 DTD 引用的原有行为

**识别规则：**

组件仅拦截在线 URL 引用，本地路径保持不变：

✅ **会被拦截并替换为本地 DTD：**
- `http://mybatis.org/dtd/mybatis-3-mapper.dtd`
- `https://mybatis.org/dtd/mybatis-3-mapper.dtd`
- 其他以 `http://` 或 `https://` 开头且包含 `mybatis` 和 `mapper.dtd` 的 URL

❌ **不会被拦截（保持原样）：**
- `classpath:dtd/mybatis-3-mapper.dtd`
- `../../dtd/mybatis-3-mapper.dtd`
- `file:///path/to/mybatis-3-mapper.dtd`
- 任何其他非 `http://` 或 `https://` 开头的路径

---

## 常见问题

### Q1: 为什么需要离线 DTD 支持？

在无网或内网环境下，XML 解析器无法访问在线 DTD URL（如 `http://mybatis.org/dtd/mybatis-3-mapper.dtd`），会导致 `java.net.UnknownHostException` 异常。

### Q2: 我需要修改现有的 XML 文件吗？

**不需要！** 继续使用标准的 MyBatis DTD 声明即可，组件会自动处理。

### Q3: 如何验证离线模式是否生效？

启用 DEBUG 日志查看详细信息：

```yaml
logging:
  level:
    cn.tannn.jdevelops.jdectemplate.xmlmapper.parser: DEBUG
```

日志示例：
```
DEBUG Resolving entity - publicId: -//mybatis.org//DTD Mapper 3.0//EN,
      systemId: http://mybatis.org/dtd/mybatis-3-mapper.dtd
DEBUG Detected online MyBatis Mapper DTD, using local DTD file: /dtd/mybatis-3-mapper.dtd
```

### Q4: 支持哪些环境？

- ✅ 有网环境
- ✅ 无网环境
- ✅ 内网环境
- ✅ 防火墙限制环境

### Q5: 依赖此 jar 包的项目能否使用？

**可以！** 当其他项目依赖 `jdevelops-dals-jdbctemplate` jar 包时，内置的 DTD 文件会被打包到 jar 中，可以正常读取。

### Q6: 本地 DTD 文件丢失会怎样？

组件有容错机制：
1. 尝试加载本地 DTD
2. 如果文件不存在，记录警告日志
3. 返回空 DTD，不阻塞解析过程

---

## 支持的 XML 标签

本地 DTD 支持以下 MyBatis Mapper 标签：

**根标签：**
- `<mapper>` - 映射器根标签

**SQL 语句标签：**
- `<select>` - 查询语句
- `<insert>` - 插入语句
- `<update>` - 更新语句
- `<delete>` - 删除语句

**辅助标签：**
- `<sql>` - SQL 片段定义
- `<include>` - 引用 SQL 片段

**动态 SQL 标签：**
- `<if>` - 条件判断
- `<where>` - WHERE 子句
- `<set>` - SET 子句
- `<foreach>` - 循环遍历
- `<choose>` - 多条件选择
- `<when>` - 条件分支
- `<otherwise>` - 默认分支
- `<trim>` - 前缀后缀处理

---

## 配置说明

### 无需配置

组件开箱即用，无需额外配置。

### 可选配置

如需查看详细日志，可配置日志级别：

```yaml
logging:
  level:
    # XmlMapper 解析器日志
    cn.tannn.jdevelops.jdectemplate.xmlmapper.parser: DEBUG
```

---

## 最佳实践

### 1. 使用标准 DTD 声明

推荐使用标准的 MyBatis DTD 声明，保持与官方一致：

```xml
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
```

### 2. 遵循命名规范

- Mapper 文件命名：`*Mapper.xml`
- namespace 使用接口全限定名
- SQL id 使用驼峰命名

### 3. 合理使用 SQL 片段

将重复的 SQL 代码抽取为 `<sql>` 片段：

```xml
<sql id="baseColumns">
    id, username, email, status, created_at, updated_at
</sql>

<select id="findAll" resultType="User">
    SELECT <include refid="baseColumns"/>
    FROM users
</select>
```

### 4. 启用 DEBUG 日志（开发环境）

开发阶段建议启用 DEBUG 日志，了解解析过程：

```yaml
logging:
  level:
    cn.tannn.jdevelops.jdectemplate.xmlmapper.parser: DEBUG
```

---

## 适用场景

### 典型应用场景

1. **无网环境部署**
   - 内网系统
   - 专网环境
   - 离线应用

2. **网络受限环境**
   - 防火墙限制
   - 代理限制
   - 网络不稳定

3. **安全要求高的环境**
   - 不允许外网访问
   - 数据安全要求高

---

## 示例代码

完整示例请参考：
```
src/test/resources/jmapper/UserMapper.xml
```

---

## 相关资源

- **MyBatis 官方文档：** https://mybatis.org/
- **DTD 规范说明：** https://www.w3.org/TR/REC-xml/#dt-doctype

---

**文档版本：** 1.0.0
**更新日期：** 2025-12-17
**维护者：** tnnn

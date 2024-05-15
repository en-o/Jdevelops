> 为项目大多数用的都是JPA且很多固定的操作方法，所以对JPA做了下包装加了一些常用的方法

# 依赖
```xml
 <!-- jpa -->
  <dependency>
      <groupId>cn.tannn.jdevelops</groupId>
      <artifactId>jdevelops-dals-jpa</artifactId>
      <version>0.0.1-SNAPSHOT</version>
  </dependency>
  <dependency>
      <groupId>cn.tannn.jdevelops</groupId>
      <artifactId>jdevelops-apis-exception</artifactId>
      <version>0.0.1-SNAPSHOT</version>
      <scope>provided</scope>
  </dependency>
  <!-- 这个根据自己的数据库选择，目前测试过pg和人大金仓 -->
  <dependency>
      <groupId>com.mysql</groupId>
      <artifactId>mysql-connector-j</artifactId>
  </dependency>
  <!-- jpa -->
```
# 使用过程
> 如下操作后就能使用一些自定义的操作功能

1. [实体继承](https://github.com/en-o/Jdevelops-Example/blob/main/dal-jpa/src/main/java/cn/tannn/jdevelops/demo/jpa/entity/CommonBean.java)`JpaAuditFnFields`
2. [repository继承 ](https://github.com/en-o/Jdevelops-Example/blob/main/dal-jpa/src/main/java/cn/tannn/jdevelops/demo/jpa/dao/UserDao.java)`JpaBasicsRepository` 
3. [service继承](https://github.com/en-o/Jdevelops-Example/blob/main/dal-jpa/src/main/java/cn/tannn/jdevelops/demo/jpa/service/UserService.java)`J2Service`
4. [serviceImpl 继承](https://github.com/en-o/Jdevelops-Example/blob/main/dal-jpa/src/main/java/cn/tannn/jdevelops/demo/jpa/service/impl/UserServiceImpl.java) `J2ServiceImpl`
   1. 实现构造 `public xxServiceImpl() { super(Bean.class);}`
5. controller 中使用 service 
   1. `private final UserService  userService;`

# 相关注解使用
> 以注解内部注释为准，使用请参考[接口示例](#XYQWQ)

## 注解
> **配合 **`**cn.tannn.jdevelops.jpa.select.EnhanceSpecification#beanWhere()**`** 使用**

| 注解 | 作用 | 示例 |
| --- | --- | --- |
| `@JpaSelectOperator` | 组装复杂条件 | ```java
@JpaSelectOperator(operatorWrapper = SQLOperatorWrapper.EQ ,connect = SQLConnect.AND)
private String phone;
```
 |
| `@JpaSelectNullField` | 处理空值 | ```java
@JpaSelectNullField(ignoreNullEnhance = false)
private String userIcon;
```
 |
| `@JpaSelectIgnoreField`   | 忽略字段 | ```java
@JpaSelectIgnoreField
Sorteds sort;
```
 |

### JpaSelectOperator
| 属性 | 类型 | 说明 | 备注 |
| --- | --- | --- | --- |
| operatorWrapper | SQLOperatorWrapper | 运算符 | 根据注解内容进行条件拼接，例如： 用的EQ 则： where 字段 = 值 |
| nullField | JpaSelectNullField | 处理空值 | 以直接使用`@JpaSelectNullField`为主（当这里跟直接使用同时存在时以直接使用为准） |
| fieldName | String | 自定义查询用的字段名 | 
1. 空时默认使用属性字段
2. 如果是级联请用，级联对象名加其属性名组合（e.g 有个级联对象 Address address , 这里就用address.no根据其no查询）
 |
| function | SpecBuilderDateFun | 时间格式处理 | 数据库字段是时间格式，实体字段也是时间格式的时候，构建查询会出错，所以这里要用函数格式化下 |
| connect | SQLConnect | 连接符 | 根据注解内容进行条件拼接，例如： 用的AND 则： where 字段 = 值 and 字段 = 值 |

#### Tips

1. 时间处理
> 入参 string , 数据库 `timestamp`

```java
/**
* 时间查询 - 数据库的数据是 timestamp
*/
@JpaSelectOperator(operatorWrapper = SQLOperatorWrapper.EQ, function = SpecBuilderDateFun.DATE_FORMAT)
private String createTime;
```

### JpaSelectNullField
| 属性 | 类型 | 说明 | 备注 |
| --- | --- | --- | --- |
| ignoreNull | boolean | 空值验证 | 
1. true: 空值不作为查询参数
2. false: 需要查询为空的数据
 |
| ignoreNullEnhance | boolean | ignoreNull = true有效 | 
1. true: 不允许为 [null,""," "] 
2. false: 不允许为 null
 |

### JpaSelectIgnoreField
> 这个注解就是一个标记没有元数据需要写入


## 更新注解
> **配合**`**cn.tannn.jdevelops.jpa.utils.JpaUtils#updateBean()**`**  使用**
> - 如果自定义`updateBean` 可以参考 `JpaUtils#updateBean()的方式使用本注解

| 注解 | 作用 | 示例 |
| --- | --- | --- |
| `@JpaUpdate` | 更新时做元数据记录 | ```java
@JpaUpdate(ignore = true)
private String phone;

@JpaUpdate(unique = true)
private String userNo;

@JpaUpdate(autoTime = true)
private LocalDateTime updateTime;

```
 |


### JpaUpdate
| 属性 | 类型 | 说明 | 备注 |
| --- | --- | --- | --- |
| unique | boolean | 空值验证 | 
1. true: 空值不作为查询参数
2. false: 需要查询为空的数据
 |
| ignore | boolean | ignoreNull = true有效 | 
1. true: 不允许为 [null,""," "] 
2. false: 不允许为 null
 |
| autoTime | boolean |  更新时自动添加时间 | 
1.  如果是 `{@link LocalDateTime}` 类型可以使用  `{@link JpaUpdate#autoTime()}`  注解来自动复制
2. 如何不是 那就自己手动在 `getter` 方法里处理下把（应为我没测过
 |


# 审计功能设置
> 自定处理 `CreatedDate``LastModifiedDate``LastModifiedBy``CreatedBy`
> - [Spring Data JPA 中文文档](https://springdoc.cn/spring-data-jpa/#auditing)

我已经默认启用了`@EnableJpaAuditing`，但是操作者和更新这默认是用的`http`中获取的IP填充，要想根据实际情况填充可以进行如下操作： 
```java
/**
 * @author tan
 */
@Component
public class TokenAuditorNameServiceImpl implements AuditorNameService {
    
    @Resource
    private HttpServletRequest request;
    
    @Override
    public Optional<String> settingAuditorName() {
        // 写死
        // return Optional.of("tan");
         // 获取token中的用户信息
        try {
            SignEntity signEntity = RsJwtWebUtil.getTokenBySignEntity(request);
            return Optional.of(signEntity.getSubject());
        } catch (Exception e) {
            log.error("自动填充数据创建者时获取当前登录用户的loginName失败");
        }
        return Optional.of("administrator");
    }
}

```

# 分页
> **使用参考**： [💬接口包裹类](https://www.yuque.com/tanning/yg9ipo/qcyw5934uv8ga089?view=doc_embed&inner=a6RaQ) 中的分页设置，**但是此处使用的是他的继承类**

- Pagings
- Sorteds
- PagingSorteds
```java
@JpaSelectIgnoreField
PagingSorteds page;

public PagingSorteds getPage() {
    return page==null?new PagingSorteds():page;
}
```
```java
@JpaSelectIgnoreField
Pagings page;

public Pagings getPage() {
    return page==null?new Pagings():page;
}
```
```java
@JpaSelectIgnoreField
Sorteds sort;

public Sorteds getSort() {
    return sort == null ? new Sorteds() : sort;
}
```
# 常用工具类
## 分页工具类
> 本身是参数对象，同时里面有着自己相关的 static 方法

### Pagings
### PagingSorteds
### Sorteds

## IObjects
> 当前项目用的非规范的特殊方法，尽量不要使用


## JpaUtils
> jpa 处理时用的一些方法

### JpaUtils.functionTimeFormat
```java
@Test
void testSpecCustomTime() {
    // from sys_user user0_ where date_format(user0_.create_time, ?)=?
    SpecificationUtil<User> instance = SpecificationUtil.getInstance();
    Specification<User> between = instance.specification((r,q,b)->
            b.equal(JpaUtils.functionTimeFormat(
                    SpecBuilderDateFun.DATE_FORMAT
                    ,r
                    ,b,"createTime"),"2021-11-17 11:08:38"));
    userService.getJpaBasicsDao().findAll(between).forEach(System.out::println);
}
```
### JpaUtils.getPredicate
```java
Specification<B> specification = (root, criteriaQuery, builder) -> {
        Predicate where = JpaUtils.getPredicate(operator, builder, root.get(fieldName), value);
        if (where == null) {
            throw new JpaException("占不支持的表达式: " + operator);
        }
        return where;
    };
    return commonDao.findAll(specification, Sorteds.sort(sort));
```
## SpecificationUtil
> 内嵌的一些 `Specification` 写法，方便自己构建 `Specification`的时候抄袭,
> 具体请取代码内部查看

```java
  public static Specification<User> userNo() {
      return (root, query, builder) -> builder.like(root.get("phone"), "123");
  }
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1642320/1715760892501-3b22678c-db50-4c2a-a83c-a01acde2e32b.png#averageHue=%23ecf1f6&clientId=uc89d1779-66d8-4&from=paste&height=636&id=u14812811&originHeight=636&originWidth=369&originalType=binary&ratio=1&rotation=0&showTitle=false&size=58023&status=done&style=none&taskId=u250807d4-d10c-480b-ba15-b4e9ea214f9&title=&width=369)
# J2Service 内嵌接口
## 接口文档
[ 测试 J2Service](https://yrzyjs4ns6.apifox.cn/api-174223111) 
[J2Service 内嵌接口文档备注](https://www.yuque.com/tanning/yg9ipo/vg6vou7gvlg4ryzc?singleDoc=&view=doc_embed)

## 简单说明

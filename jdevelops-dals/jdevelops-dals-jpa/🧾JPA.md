> 为项目大多数用的都是JPA且很多固定的操作方法，所以对JPA做了下包装加了一些常用的方法
> - **具体使用示例请看  **[dal-jpa](https://github.com/en-o/Jdevelops-Example/blob/main/dal-jpa/src/main/java/cn/tannn/jdevelops/demo/jpa/controller/UserController.java)** 里的单元测试**

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
> - [具体如何使用方法请看](https://github.com/en-o/Jdevelops-Example/tree/main/dal-jpa/src/test/java/cn/tannn/jdevelops/demo/jpa/page)
> - Pagings
> - Sorteds
> - PagingSorteds

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
## PagingSorteds
> 注意他里里面的 排序是个list

### 获取`org.springframework.data.domain.Pageable`
```java
new PagingSorteds().pageable()
```
### 构造`org.springframework.data.domain.Pageable`
```java
PagingSorteds.pageable(new Pagings(),new Sorteds())
```
### 空处理
```java
PagingSorteds.pageable(null)
```
### 修改默认值
> [跟 result 的区别在于放回的对象不同](https://www.yuque.com/tanning/yg9ipo/qcyw5934uv8ga089#nVOYw)

```java
// 为空 覆盖默认值
sortPage = new PagingSorteds().fixSort(0, "name","sex");
sortPage = new PagingSorteds().fixSort("name","sex");
// 不空 以原来的为准
sortPage = new PagingSorteds(20,new Sorted(0,"fix")).fixSort("name","sex");

// 为空  覆盖默认值
sortPage = new PagingSorteds().append("name");
// 不空  追加排序
sortPage = new PagingSorteds(20,new Sorted(0,"fix")).append(0, "name");

```
## Pagings
### 获取`org.springframework.data.domain.Pageable`
```java
new Pageable().pageable()
```
### 获取`Pageable`注入排序 `org.springframework.data.domain.Sort`
```java
new Pageable().pageable(org.springframework.data.domain.Sort)
```
### 获取`Pageable`注入排序 `cn.tannn.jdevelops.jpa.request.Sorteds`
```java
new Pageable().pageable(cn.tannn.jdevelops.jpa.request.Sorteds)
```
### 构造`org.springframework.data.domain.Pageable`
```java
PagingSorteds.pageable(new Pagings(),new Sorteds())
```
### 内嵌 `org.springframework.data.domain.Sort`
> 此方法是这对后端想通过`J2service#findPage(Pagings)`传入`Pagings` 对象构建 `Pageable`对象时想要加入个临时的排序又不想用 `PagingSorteds`这这复杂的对象时时使用，请不要暴露给前端

```java
Pagings p = new Pagings().sort(Sort.by("name"))
// pageable 获取加载设置的sort转换成 Pageable
Pageable page = p.pageable()
```
## Sorteds
### 获取`org.springframework.data.domain.Sort`
```java
new Sorteds().sort()
```
### 构造`org.springframework.data.domain.Sort`
```java
Sorteds.sort(new Sorteds())
Sorteds.sort(new Sorted())
```
### List<Sorteds> 转 Sort
```java
Sorteds.sorteds2Sort(List<Sorteds> sort)
```
### 获取 `Sort.Direction`
```java
Sorteds.direction(Sorteds#getOrderDesc())
```
### 修改默认值
```java
// 为空 覆盖默认值
sort = new Sorteds().fixSort(0, "name","sex");
sort = new Sorteds().fixSort("name","sex");
// 不空 以原来的为准
sort = new Sorteds(1,"id").fixSort("name","sex");
```

# 常用工具类
> 处理下面列举的之外还有 [💬接口包裹类](https://www.yuque.com/tanning/yg9ipo/qcyw5934uv8ga089?view=doc_embed&inner=OcYfD) 的工具类也能直接使用

## 分页工具类
> 本身是参数对象，同时里面有着自己相关的 static 方法

[https://www.yuque.com/tanning/yg9ipo/qe15wg78n3orgrah#WjcNz](#WjcNz)

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
## EnhanceSpecification
> 这个接口时本项目组装 `where` 语句的核心方法

1. 配合[注解](#RT0zG)使用
2. 具体示例在：[EnhanceSpecificationTest](https://github.com/en-o/Jdevelops-Example/blob/main/dal-jpa/src/test/java/cn/tannn/jdevelops/demo/jpa/EnhanceSpecificationTest.java)
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserComplexFind {

    /**
     * like OR (not null and not "" and not " ")
     */
    @JpaSelectOperator(operatorWrapper = SQLOperatorWrapper.LIKE, connect = SQLConnect.OR)
    private String userNo;

    /**
     * in AND (not null and not "" and not " ")
     */
    @JpaSelectOperator(operatorWrapper = SQLOperatorWrapper.IN, connect = SQLConnect.AND)
    private List<String> name;

    /**
     * and (not null and not "" and not " ")
     */
    @JpaSelectOperator(operatorWrapper = SQLOperatorWrapper.EQ)
    private String loginName;

    /**
     * like or (not null)
     */
    @JpaSelectOperator(operatorWrapper = SQLOperatorWrapper.LIKE, nullField = @JpaSelectNullField(ignoreNullEnhance = false)
            , connect = SQLConnect.OR)
    private String userIcon;

    /**
     * 默认 eq / and  / not null
     */
    private String loginPwd;

    /**
     * 忽略
     */
    @JpaSelectIgnoreField
    private String phone;

    /**
     * not null
     */
    @JpaSelectNullField(ignoreNullEnhance = false)
    private String address;

    /**
     * 处理时间格式
     */
    @JpaSelectOperator(function = SpecBuilderDateFun.DATE_FORMAT)
    private String createTime;
}
```
```java
   UserComplexFind userComplexFind = new UserComplexFind();
        userComplexFind.setUserNo("weq");
        userComplexFind.setName(Arrays.asList("a","b"));
        userComplexFind.setLoginName("ta");
        userComplexFind.setUserIcon("1");
        userComplexFind.setLoginPwd("23");
        userComplexFind.setPhone("123");
        userComplexFind.setAddress("1");
        userComplexFind.setCreateTime("2024-05-17 10:44:57");
        Specification<User> spec = EnhanceSpecification.beanWhere(userComplexFind);
        System.out.println(userDao.findAll(spec));
```
```sql
 from sys_user user0_ where
        (
          ( user0_.user_no like ? or user0_.name in (? , ?) )
          and user0_.login_name=?
          and (user0_.user_icon like ?) or user0_.login_pwd=?
        )
         and user0_.address=?
         and date_format(user0_.create_time, ?)=?
```
# J2Service 内嵌接口
## 接口文档
[ 测试 J2Service](https://yrzyjs4ns6.apifox.cn/api-174223111) 
[J2Service 内嵌接口文档备注](https://www.yuque.com/tanning/yg9ipo/vg6vou7gvlg4ryzc?singleDoc=&view=doc_embed)
## 级联
[Jdevelops-Example/dal-jpa/src/test/java/cn/tannn/jdevelops/demo/jpa/RelationSelectTest.java at main · en-o/Jdevelops-Example](https://github.com/en-o/Jdevelops-Example/blob/main/dal-jpa/src/test/java/cn/tannn/jdevelops/demo/jpa/RelationSelectTest.java)
## 简单说明
> **具体操作请看代码里的注释说明 和 **[dal-jpa， 具体请看单元测试](https://github.com/en-o/Jdevelops-Example/blob/main/dal-jpa/src/main/java/cn/tannn/jdevelops/demo/jpa/controller/UserController.java)

### 存储
```java
List<B> saves(List<B> bean);
B saveOne(B bean);
<V> B saveOneByVo(V bean);
```
### 删除
```java
int delete(Specification<B> spec);
int deleteEq(String fieldName, Object value);
int delete(String fieldName, SQLOperator operator, Object... value);
<T> int delete(T wheres);
```
### 更新
```java
<T> Boolean update(T bean, SQLOperator operator, String... uniqueKey);
```
### 单查询
```java
Optional<B> findOnly(String fieldName, Object value);
Optional<B> findOnly(String fieldName, Object value, String fieldName2, Object value2);
Optional<B> findOnly(Specification<B> spec);
```
### 多查询
```java
List<B> finds();
List<B> finds(String fieldName, SQLOperator operator, Object... value);
List<B> finds(String fieldName, SQLOperator operator, Sorteds sort, Object... value);
List<B> finds(Specification<B> spec, Sorteds sort);
<T> List<B> finds(T req);
<T> List<B> finds(T req, Sorteds sort);
```
### 分页查询
```java
Page<B> findPage(Pagings pageable);
Page<B> findPage(PagingSorteds pageable);
<T> Page<B>  findPage(T req, Pagings pageable);
<T> Page<B>  findPage(T req, PagingSorteds pageable);
```
### ORM操作
> [entityManagerDemo](https://github.com/en-o/Jdevelops-Example/tree/main/dal-jpa/src/test/java/cn/tannn/jdevelops/demo/jpa/entityManager)
> [使用文档](https://www.yuque.com/tanning/mbquef/fkwisqfhwicoz5mw)	

```java
EntityManager getEntityManager();
<ID, R extends JpaBasicsRepository<B, ID>> R getJpaBasicsDao();
```
```java

/**
 * 测试 {@link J2Service#getJpaBasicsDao()}  {@link J2Service#getEntityManager()}
 */
@SpringBootTest
class OrmTest {


    @Autowired
    private UserService userService;

    /**
     * 在 service 层 获取 repository 在controller层直接使用 dao层的方法
     */
    @Test
    void genServiceJpaBasicsDao() {
        UserDao jpaBasicsDao = userService.getJpaBasicsDao();
        System.out.println("用UserDao接收后调用： ");
        jpaBasicsDao.customSql().forEach(System.out::println);
        // 如果直接调用的，不能得到 UserDao 里自定义构建的方法，只能使用内置的方法
        // 想要使用在UserDao中自定义的方法就用UserDao接收一下在调用
        System.out.println("直接调用： " + userService.getJpaBasicsDao().findById(1L));
    }
    

    /**
     * 在 service 层 获取 EntityManager
     * <p> 删改增都要加事务
     * @see EntityManagerTest
     */
    @Test
    void genServiceEntityManager() {
        // getEntityManager 方法说明中有简单的使用教程
        // 或者参考 J2Service#updateBean
        EntityManager entityManager = userService.getEntityManager();
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u where id = :id", User.class);
        query.setParameter("id", 1L);
        System.out.println(query.getResultList());
    }


}

```
# 其他文档
## JPA 生成的数据库表顺序问题
> spring boot 3.x 可以解决了，2.x 需要重写 `org.hibernate.cfg.propertycontainer`

[Just a moment...](https://stackoverflow.com/questions/1298322/wrong-ordering-in-generated-table-in-jpa/65731578#65731578)
[Ordering columns in a Table in JPA/Hibernate](https://robertniestroj.hashnode.dev/ordering-columns-in-a-table-in-jpahibernate)

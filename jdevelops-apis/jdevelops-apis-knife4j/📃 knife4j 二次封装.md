> 对[ knife4j](https://doc.xiaominfo.com/) 第二次封装，主要是对一些[依赖的预处理](https://doc.xiaominfo.com/docs/faq/v4/knife4j-parameterobject-flat-param)


---

# 测试环境

1. springboot 2.7.9 +
# 依赖
```xml
<dependency>
      <groupId>cn.tannn.jdevelops</groupId>
      <artifactId>jdevelops-apis-knife4j</artifactId>
      <version>0.0.1-SNAPSHOT</version>
  </dependency>
```
# 使用
> 默认引入 jdevelops-sboot-swagger 依赖就可以直接使用了,但是

## 必须设置的配置
> 参数解析问题，这个默认false的所有设置以下，不想设置用 `@ParameterObject`注解在参数上也行
> - 如果配置文件无效可以使用注解试试

```yaml
springdoc:
  # 默认是false，需要设置为true
  default-flat-param-object: true
```
> 默认扫描的是 `cn.jdevelops`如果你的包前缀不是的话，要设置一下

```yaml
jdevelops:
  swagger:
    base-package: # list 
      - cn.tannn.springbootparentswagger.controller
      - cn.tannn.springbootparentswagger.api
```
> 开启 knife4j 的增强模式

```yaml
knife4j:
  enable: true
```

> jdevelops.swagger

| 字段 | 注释 | 备注 |
| --- | --- | --- |
| basePackage | _controller接口所在的包(可以设置多个)_ | _cn.jdevelops.controller(默认)_ |
| title | _标题_ |  |
| description | _当前文档的详细描述_ |  |
| version | _当前文档的版本_ |  |
| author | _作者_ |  |
| url | url |  |
| email | _email_ |  |
| license | _license_ |  |
| licenseUrl | _license-url_ |  |
| groupName | _分组_ |  |
| displayName | _分组名_ |  |
| swaggerSecuritySchemes | _OpenAPI 规范的安全方案_ | _默认apikey,且name等于token_ |
| securitySchemeDefault | _是否设置默认的 securityScheme ，true 要设置_ | 默认true |

```yaml
jdevelops:
  swagger:
    base-package:
      - cn.tannn.springbootparentswagger.controller
      - cn.tannn.springbootparentswagger.api
#    security-scheme-default: false
    swagger-security-schemes:
      - {
          security: true,
          scheme:
            {
              in: HEADER,
              type: APIKEY,
              name: tokens,
            }
      }
      - {
        security: false,
        scheme:
          {
            type: OAUTH2,
            flows:{
              password:
                {
                  token-url: http://ballcat-admin:8080/oauth/token
                }
            }
          }
      }
```
## 参考文档

1. knife4j 特性文档
> [https://doc.xiaominfo.com/docs/features/enhance](https://doc.xiaominfo.com/docs/features/enhance)

2. knife4j 参数配置文档地址：
> [https://doc.xiaominfo.com/docs/features/enhance](https://doc.xiaominfo.com/docs/features/enhance)

3. openai3注解
> [https://doc.xiaominfo.com/docs/oas/openapi3-annotation](https://doc.xiaominfo.com/docs/oas/openapi3-annotation)

## 扫描多个包设置
> 默认组【group】

```yaml
jdevelops:
  swagger:
    base-package: # list 
      - cn.tannn.springbootparentswagger.controller
      - cn.tannn.springbootparentswagger.api
```
## 接口分组
> 想对接口分下组，配置文件的方式我忘了
> ![image.png](https://cdn.nlark.com/yuque/0/2023/png/1642320/1703470150507-6146664b-5a49-4a8a-aa47-f101bc911015.png#averageHue=%23fdfdfd&clientId=u1f37ec7e-3153-4&from=paste&height=822&id=u8e29cdc2&originHeight=822&originWidth=1802&originalType=binary&ratio=1&rotation=0&showTitle=false&size=67735&status=done&style=none&taskId=uf5b8313b-a6db-4a94-abaa-e4192d4a964&title=&width=1802)

```java
package cn.tannn.springbootparentswagger.config;

import cn.jdevelops.sboot.swagger.core.entity.BuildSecuritySchemes;
import cn.jdevelops.sboot.swagger.domain.SwaggerProperties;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cn.jdevelops.sboot.swagger.core.util.SwaggerUtil.buildSecuritySchemes;

/**
 * 接口分组
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-03-12 21:05
 */
@Component
public class GroupedOpenApis {
    
   @Bean
    public GroupedOpenApi api2(SwaggerProperties swaggerProperties){
        // 加入  Authorize
        BuildSecuritySchemes buildSecuritySchemes = buildSecuritySchemes(swaggerProperties.getSwaggerSecuritySchemes());
        //  表示 packagesToScan下的所有接口
        String[] paths = { "/**" };
        // 需要扫描的包路径
        String[] packagedToMatch = {"com.tan.api"};

        return GroupedOpenApi.builder().group("mobile")
                .displayName("移动端")
                .pathsToMatch(paths)
                .addOperationCustomizer((operation, handlerMethod) -> operation.security(buildSecuritySchemes.getSecurityItem()))
                .packagesToScan(packagedToMatch).build();
    }

    @Bean
    public GroupedOpenApi api3(SwaggerProperties swaggerProperties){
        // 加入  Authorize
        BuildSecuritySchemes buildSecuritySchemes = buildSecuritySchemes(swaggerProperties.getSwaggerSecuritySchemes());
        //  表示 packagesToScan下的所有接口
        String[] paths = { "/**" };
        // 需要扫描的包路径
        String[] packagedToMatch = {"com.tan.api"};

        return GroupedOpenApi.builder().group("admin")
                .displayName("管理端")
                .pathsToMatch(paths)
                .addOperationCustomizer((operation, handlerMethod) -> operation.security(buildSecuritySchemes.getSecurityItem()))
                .packagesToScan(packagedToMatch).build();
    }
}

```
## 接口文档访问权限设置
> [https://doc.xiaominfo.com/docs/features/accessControl](https://doc.xiaominfo.com/docs/features/accessControl)

> 生产环境屏蔽资源

```yaml
knife4j:
  # 开启增强配置 
  enable: true
　# 开启生产环境屏蔽
  production: true
```
> 访问页面加权控制

```yaml
knife4j:
  # 开启增强配置 
  enable: true
# 开启Swagger的Basic认证功能,默认是false
  basic:
      enable: true
      # Basic认证用户名
      username: test
      # Basic认证密码
      password: 123
```
## 开启Authorize
> 中文value可能会乱码，用的时候用 `java.net.URLDecoder.decode(name, "UTF-8")` 处理下

1. **默认开启的authorize，如下**
> 默认在_HEADER中，且key名为 token_

![image.png](https://cdn.nlark.com/yuque/0/2023/png/1642320/1678789388664-3eab2838-65de-4f9f-b6f9-857273badf1f.png#averageHue=%23cfb788&clientId=uf322240e-64a3-4&from=paste&height=386&id=ua0e4ff7c&name=image.png&originHeight=386&originWidth=1095&originalType=binary&ratio=1&rotation=0&showTitle=false&size=37526&status=done&style=none&taskId=u35d023bc-d9f4-40b7-837c-1ce647f78f4&title=&width=1095#averageHue=%23cfb788&id=P5D9Q&originHeight=386&originWidth=1095&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)

2. **如果不想要开启默认authorize**
```java
jdevelops:
   security-scheme-default: false
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/1642320/1678789528643-12a0724f-2096-4c5b-9926-c86000fee861.png#averageHue=%23b9a473&clientId=uf322240e-64a3-4&from=paste&height=313&id=ub2468630&name=image.png&originHeight=313&originWidth=903&originalType=binary&ratio=1&rotation=0&showTitle=false&size=25150&status=done&style=none&taskId=ue6f7e884-03f2-4af6-9682-2315c87edd7&title=&width=903#averageHue=%23b9a473&id=GTCgL&originHeight=313&originWidth=903&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)

3. **自定义authorize**
> 设计的是List 但是目前同一个 type 只会有一个，后面在做处理把，目前开中用到基本就token，而且也不知道会不会有同一个多个相同type的情况

```yaml
jdevelops:
    swagger-security-schemes:
      - {
          security: false,
          scheme:
            {
              in: HEADER,
              type: APIKEY,
              name: tokens,
            }
      }
      - {
        security: true,
        scheme:
          {
            type: OAUTH2,
            flows:{
              password:
                {
                  token-url: http://ballcat-admin:8080/oauth/token
                }
            }
          }
      }
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/1642320/1678791677543-e431ebcf-688e-4793-87a8-dbbbaf3eaeb6.png#averageHue=%23fdfdfc&clientId=uf322240e-64a3-4&from=paste&height=298&id=u4e5aa16b&name=image.png&originHeight=298&originWidth=1547&originalType=binary&ratio=1&rotation=0&showTitle=false&size=29366&status=done&style=none&taskId=u1488cfde-892b-4b73-be79-e0057badbfb&title=&width=1547#averageHue=%23fdfdfc&id=ZFVB4&originHeight=298&originWidth=1547&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)

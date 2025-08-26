> 主要是完成自建拦截器后可以自动注入WebMvcConfigurer


---

# 拦截器
> 模仿 HandlerInterceptor 实现了四个拦截器

## 前置拦截器
> HandlerInterceptor.preHandle

**ApiBeforeInterceptor**

1. **如果设置了多个会根据顺序执行，当其中有一个放回的结果是false则会中断执行，进行错误抛出**
## 后置拦截器
> HandlerInterceptor.postHandle

**ApiAfterInterceptor**
## Finally拦截器
> HandlerInterceptor.afterCompletion

**ApiFinallyInterceptor**
## 异步拦截器
> AsyncHandlerInterceptor.afterConcurrentHandlingStarted

**ApiAsyncInterceptor**
## 全局拦截器
> _HandlerInterceptor (before -> after -> afterCompletion)_

**ApiInterceptor**
# 使用

1. 继承自己需要的拦截器
2. 实现拦截器里的方法
3. 加上spring的注解
| 注解 | 用处 | 备注 |
| --- | --- | --- |
| @Component  | 注入 | 注入到springbean进行管理 |
| @Order  | 排序 | 获取bean的时候是可以有顺序的 |

```java


/**
 * 接口幂等性拦截器
 *
 * @author @author 网络
 */
@AutoConfiguration
@Order(3)
public class ApiIdempotentInterceptor implements ApiBeforeInterceptor {

    @Override
    public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) {
       
        return true;
    }

}

```
# 依赖
> 2.0.7

```xml
<dependency>
    <groupId>cn.tannn.jdevelops</groupId>
    <artifactId>jdevelops-webs-interceptor</artifactId>
</dependency>
```


# 基础配置
> 出错了在配置，当前配置是由于使用h2数据库的时候控制台加载不出来
```yaml
jdevelops:
  interceptor:
    core:
      # override-default-exclude-paths: true # 覆盖默认 exclude-paths， false是追加
      # 只是示例，我默认放行了H2
      exclude-paths:
        - /h3/*
```

## 接口限流使用案例
> 待完善地方：生成key的时候可以将参数值带上，更加合理 已经在代码中TODO处理
 1. 引入依赖
```maven
        <dependency>
            <groupId>cn.jdevelops</groupId>
            <artifactId>cache-redis-currentlimit</artifactId>
            <version>2.0.0</version>
        </dependency>

```
2. 使用方式
```java
package com.test.controller;


import Limiter;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


/**
 * @author lmz
 * @date 2021/3/9  12:01
 */
@RestController
@RequestMapping("/test")
@Api(tags = "用户测试接口")
public class TestController{
  /**
     * 参数说明
     * 1.  value 表示最大访问次数
     * 2. key 表示当前请求的key,默认为方法名
     * 3. timeout 表示当前请求的时间长度
     * 4. timeUnit 时间单位
     * ---- test请求在10s内最多访问3次
     */
    @Limiter(value = 3,key = "test",timeout = 10,timeUnit = TimeUnit.SECONDS)
    @GetMapping("/test")
    public String test(){
        return "访问成功";
    }
}

```
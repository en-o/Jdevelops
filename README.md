# databstech 后端组件

## 代码提交说明
- 先 fork 项目 或者建立 自己的 分支
- 改动或新增后 Pull Requests  到  **develop ** 分支中去
- 管理员回来进行合并
- 现在的master 是第一个正式可用的版本


# build 中 ~
## [项目文档总详情：有如下模块的说明文档](https://www.yuque.com/tanning/vplgcr/afxx6v)
### 已完成模块跟项目
#### utils（工具类）    
> 1. util-constant                常量跟一些基类
> 2. util-jwt-web                 直接应用可以使用的jwt
> 3. util-jwt                     没有拦截器的jwt主要是个gateway用的
> 4. util-websocket               websocket基础配置直接引用就能用
> 5. util-aop                     一些切面（接口访问情况配置保存/查看等）
> 6. util-list                    集合工具类
> 7. util-string                  单体工具类
> 8. util-encryption              密钥工具类
> 9. util-xxl-sso-core            单点登录 - 核心包（服务端和客户端都需要引入）
#### docs（文档）
> 1. doc-standalone               Swagger文档，springboot单机版使用，完整swagger
> 2. doc-micro                    Swagger文档，springboot单机版使用，没有ui界面
> 3. doc-gateway                  Swagger文档，gateway聚合文档！准确说是整个 gateway+doc 的配置可以简单配置就能使用
> 4. doc-log-p6spy                sql性能分析，还包括了一个log日志xml文件
#### apis（接口规范）
> 1. api-result                   结果集标准统一化
> 2. api-exception-result         结果集+全局异常 标准统一化
> 3. api-annotation               一些自定义注解
#### cache（数据库）
> 1. cache-jpa                    jpa相关的基类跟配置
> 2. cache-redis                  redis发布订阅相关配置
#### project（业务系统）
> 1. project-xxl-job              任务调度服务 - 定时器相关

#### demo （测    试案例）
> 1. component-demo/***-demo      一些demo
> 2. project-xxl-sso              单点登录    - 相关demo




## [project-xxl-job/README.md](https://gitee.com/detabes/component-databstech/blob/master/component-project/project-xxl-job/README.md)
## [project-xxl-SSO/SSO.md](https://gitee.com/detabes/component-databstech/blob/master/component-project/project-xxl-sso/SSO.md)
 
## [码云生成的项目中的接口文档-停止维护中](https://apidoc.gitee.com/detabes/component-databstech)

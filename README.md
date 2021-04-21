# 后端组件 
# [版本v1.0.0 已经发布到私服上了](https://gitee.com/detabes/detabes-component/tree/v1/)
## 项目使用，在pom文件中加入 私库地址
```xml
 <repositories>
        <repository>
            <id>local-nexus</id>
            <url>http://192.168.0.3:1100/repository/maven-public/</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
```
## 现在此分支准备构建2.0.0 版本


# 待新增功能
- 公共service
    - 公共多条件查询
    - 公共增删改
- 公共dao
    - 修改updateEntity 可以根据指定字段查询更新
- 公共方法
    - jdbc 分页查询封装
    - solr 分页查询封装
    - 添加一个公共的分页返回对象

* [ ] aop模块
    - [x] apilog [接口调用日志] 
    - [x] apisign [接口验签]
    - [ ] apiVersion [接口版本]
    - [ ] apiIdempotent [接口幂等]  

* [ ] api模块
    - [x] exception [接口结果集]
    - [x] result [全局异常]
    - [x] annotation [自定义的注解]
    
* [ ] bean模块
    - [ ] enum [枚举]
        - [x] api相关枚举
        - [x] util相关枚举
    - [ ] entity [实体]
        - [x] 实体公共基类
        - [x] 实体序列化
    - [x] constant [常量]
        - [x] time [时间]
        - [x] unit [单位]

* [ ] cache模块
    - [ ] jpa
        - [x] jpaCore
        - [x] jpaServer
    - [ ] mybatis
        - [x] mybatisCore
        - [x] mybatisServer
    - [ ] redis
        - [x] redisCore
        - [x] redisSubscribePublished
    - [ ] MongoDB
* [ ] 短信发送模块
    - [x] sms-aliyun 阿里云短信发送
    - [x] sms-mail 邮件发送

* [ ] doc模块
    - [ ] doc-core
        - [x] swagger-core
    - [ ] doc-swagger
        - [x] 单机版swagger
        - [x] cloude版swagger
        - [ ] gateway版swagger
    
* [ ] log模块
    - [ ] logs-cache 
        - [x] logs-cache-p6spy sql调优用
 
* [ ] starter
    - [x] starters-utils
    - [x] starters-beans
    - [ ] starters-detabes-web
        - [x] starters-detabes-boot-web
        - [x] starters-detabes-boot-web-jpa
        - [ ] starters-detabes-cloud-web

* [ ] 工具模块
    - [x] String [String工具]
    - [x] environment [环境工具类] 
    - [x] spring [String工具类] 
    - [x] time [时间工具]
    - [x] Map/Bean [实体工具]
    - [x] List [集合工具]
    - [x] encryption [加解密工具]
    - [x] jwt [jwt工具]
    - [x] http [http工具]
            
* [ ] web模块    
    - [x] websocket
        - [x] websocket-core
        - [x] websocket-client
    - [ ] webservice
    - [x] jwt

    
    
    
    

1. 注意必须使用springboot3.4及以上版本
2. 必须要的以来：fastjson2,spring-boot-starter-data-redis:3.4.x

# MQ 使用
> MqConstants内置的topic
1. 业务代码参考MqConfiguration注册启动监听器
2. 业务代码参考MqStreamListener实现监听器
3. 根据ResMqProperties进行相关配置

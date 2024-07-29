# 数据库文件地址
https://github.com/quartz-scheduler/quartz/blob/master/quartz-core/src/main/resources/org/quartz/impl/jdbcjobstore/tables_mysql_innodb.sql
# 表结构
| Table Name | Description |
|------------|-------------|
|QRTZ_CALENDARS|存储Quartz的Calendar信息|
|QRTZ_CRON_TRIGGERS|存储CronTrigger，包括Cron表达式和时区信息|
|QRTZ_FIRED_TRIGGERS|存储与已触发的Trigger相关的状态信息，以及相联Job的执行信息|
|QRTZ_PAUSED_TRIGGER_GRPS|存储已暂停的Trigger组的信息|
|QRTZ_SCHEDULER_STATE|存储少量的有关Scheduler的状态信息，和别的Scheduler实例|
|QRTZ_LOCKS|存储程序的悲观锁的信息|
|QRTZ_JOB_DETAILS|存储每一个已配置的Job的详细信息|
|QRTZ_JOB_LISTENERS|存储有关已配置的JobListener的信息|
|QRTZ_SIMPLE_TRIGGERS|存储简单的Trigger，包括重复次数、间隔、以及已触的次数|
|QRTZ_BLOG_TRIGGERS|Trigger作为Blob类型存储|
|QRTZ_TRIGGER_LISTENERS|存储已配置的TriggerListener的信息|
|QRTZ_TRIGGERS|存储已配置的Trigger的信息|

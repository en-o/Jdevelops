# 数据库文件地址
https://github.com/quartz-scheduler/quartz/blob/master/quartz-core/src/main/resources/org/quartz/impl/jdbcjobstore/tables_mysql_innodb.sql
# 表说明
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

# 表结构
> https://blog.csdn.net/sqlgao22/article/details/100697214

## qrtz_job_details
> 存储每一个已配置的 jobDetail 的详细信息

| **表字段** | **含义** |
| --- | --- |
| sched_name | 调度名称 |
| job_name | 集群中job的名字 |
| job_group | 集群中job的所属组的名字 |
| description | 详细描述信息 |
| job_class_name | 集群中个notejob实现类的全限定名,quartz就是根据这个路径到classpath找到该job类 |
| is_durable | 是否持久化,把该属性设置为1，quartz会把job持久化到数据库中 |
| is_nonconcurrent | 是否并发执行 |
| is_update_data | 是否更新数据 |
| requests_recovery | 是否接受恢复执行，默认为false，设置了RequestsRecovery为true，则该job会被重新执行 |
| job_data | 一个blob字段，存放持久化job对象 |

## qrtz_triggers
> 保存触发器的基本信息

| **表字段** | **含义** |
| --- | --- |
| sched_name | 调度名称 |
| trigger_name | 触发器的名字 |
| trigger_group | 触发器所属组的名字 |
| job_name | qrtz_job_details表job_name的外键 |
| job_group | qrtz_job_details表job_group的外键 |
| description | 详细描述信息 |
| prev_fire_time | 上一次触发时间（毫秒） |
| next_fire_time | 下一次触发时间，默认为-1，意味不会自动触发 |
| priority | 优先级 |
| trigger_state | 当前触发器状态，设置为ACQUIRED,如果设置为WAITING,则job不会触发 （ WAITING:等待 PAUSED:暂停ACQUIRED:正常执行 BLOCKED：阻塞 ERROR：错误） |
| trigger_type | 触发器的类型，使用cron表达式 |
| start_time | 开始时间 |
| end_time | 结束时间 |
| calendar_name | 日程表名称，表qrtz_calendars的calendar_name字段外键 |
| misfire_instr | 措施或者是补偿执行的策略 |
| job_data | 一个blob字段，存放持久化job对象 |

## qrtz_cron_triggers
>  存储触发器的cron表达式表

| **表字段** | **含义** |
| --- | --- |
| sched_name | 调度名称 |
| trigger_name | qrtz_triggers表trigger_name的外键 |
| trigger_group | qrtz_triggers表trigger_group的外键 |
| cron_expression | cron表达式 |
| time_zone_id | 时区 |

## qrtz_scheduler_state
> 存储集群中note实例信息，quartz会定时读取该表的信息判断集群中每个实例的当前状态

| **表字段** | **含义** |
| --- | --- |
| sched_name | 调度名称 |
| instance_name | 之前配置文件中org.quartz.scheduler.instanceId配置的名字，就会写入该字段 |
| last_checkin_time | 上次检查时间 |
| checkin_interval | 检查间隔时间 |

## qrtz_blob_triggers Trigger
> 作为 Blob 类型存储(用于 Quartz 用户用 JDBC 创建他们自己定制的 Trigger 类型，JobStore 并不知道如何存储实例的时候)

| **表字段** | **含义** |
| --- | --- |
| sched_name | 调度名称 |
| trigger_name | qrtz_triggers表trigger_name的外键 |
| trigger_group | qrtz_triggers表trigger_group的外键 |
| blob_data | 一个blob字段，存放持久化Trigger对象 |

## qrtz_calendars
>  以 Blob 类型存储存放日历信息， quartz可配置一个日历来指定一个时间范围

| **表字段** | **含义** |
| --- | --- |
| sched_name | 调度名称 |
| calendar_name | 日历名称 |
| calendar | 一个blob字段，存放持久化calendar对象 |

## qrtz_fired_triggers
> 存储与已触发的 Trigger 相关的状态信息，以及相联 Job 的执行信息

| **表字段** | **含义** |
| --- | --- |
| sched_name | 调度名称 |
| entry_id | 调度器实例id |
| trigger_name | qrtz_triggers表trigger_name的外键 |
| trigger_group | qrtz_triggers表trigger_group的外键 |
| instance_name | 调度器实例名 |
| fired_time | 触发的时间 |
| sched_time | 定时器制定的时间 |
| priority | 优先级 |
| state | 状态 |
| job_name | 集群中job的名字 |
| job_group | 集群中job的所属组的名字 |
| is_nonconcurrent | 是否并发 |
| requests_recovery | 是否接受恢复执行，默认为false，设置了RequestsRecovery为true，则会被重新执行 |

## qrtz_locks
> 存储程序的悲观锁的信息(假如使用了悲观锁)。

| **表字段** | **含义** |
| --- | --- |
| sched_name | 调度名称 |
| lock_name | 悲观锁名称 |

## qrtz_paused_trigger_grps
> 存储已暂停的 Trigger 组的信息

| **表字段** | **含义** |
| --- | --- |
| sched_name | 调度名称 |
| trigger_group | qrtz_triggers表trigger_group的外键 |


## qrtz_simple_triggers
> 存储简单的 Trigger，包括重复次数，间隔，以及已触发的次数。

| **表字段** | **含义** |
| --- | --- |
| sched_name | 调度名称 |
| trigger_name | qrtz_triggers表trigger_ name的外键 |
| trigger_group | qrtz_triggers表trigger_group的外键 |
| repeat_count | 重复的次数统计 |
| repeat_interval | 重复的间隔时间 |
| times_triggered | 已经触发的次数 |

## qrtz_simprop_triggers
> 存储CalendarIntervalTrigger和DailyTimeIntervalTrigger

| **表字段** | **含义** |
| --- | --- |
| SCHED_NAME | 调度名称 |
| TRIGGER_NAME | qrtz_triggers表trigger_ name的外键 |
| TRIGGER_GROUP | qrtz_triggers表trigger_group的外键 |
| STR_PROP_1 | String类型的trigger的第一个参数 |
| STR_PROP_2 | String类型的trigger的第二个参数 |
| STR_PROP_3 | String类型的trigger的第三个参数 |
| INT_PROP_1 | int类型的trigger的第一个参数 |
| INT_PROP_2 | int类型的trigger的第二个参数 |
| LONG_PROP_1 | long类型的trigger的第一个参数 |
| LONG_PROP_2 | long类型的trigger的第二个参数 |
| DEC_PROP_1 | decimal类型的trigger的第一个参数 |
| DEC_PROP_2 | decimal类型的trigger的第二个参数 |
| BOOL_PROP_1 | Boolean类型的trigger的第一个参数 |
| BOOL_PROP_2 | Boolean类型的trigger的第二个参数 |



# 常用表达式例子
- 0/2 * * * * ?   表示每2秒 执行任务
- 0 0/2 * * * ?    表示每2分钟 执行任务
- 0 0 2 1 * ?   表示在每月的1日的凌晨2点调整任务
- 0 15 10 ? * MON-FRI   表示周一到周五每天上午10:15执行作业
- 0 15 10 ? 6L 2002-2006   表示2002-2006年的每个月的最后一个星期五上午10:15执行作
- 0 0 10,14,16 * * ?   每天上午10点，下午2点，4点
- 0 0/30 9-17 * * ?   朝九晚五工作时间内每半小时
- 0 0 12 ? * WED    表示每个星期三中午12点
- 0 0 12 * * ?   每天中午12点触发
- 0 15 10 ? * *    每天上午10:15触发
- 0 15 10 * * ?     每天上午10:15触发
- 0 15 10 * * ?    每天上午10:15触发
- 0 15 10 * * ? 2005    2005年的每天上午10:15触发
- 0 * 14 * * ?     在每天下午2点到下午2:59期间的每1分钟触发
- 0 0/5 14 * * ?    在每天下午2点到下午2:55期间的每5分钟触发
- 0 0/5 14,18 * * ?     在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
- 0 0-5 14 * * ?    在每天下午2点到下午2:05期间的每1分钟触发
- 0 10,44 14 ? 3 WED    每年三月的星期三的下午2:10和2:44触发
- 0 15 10 ? * MON-FRI    周一至周五的上午10:15触发
- 0 15 10 15 * ?    每月15日上午10:15触发
- 0 15 10 L * ?    每月最后一日的上午10:15触发
- 0 15 10 ? * 6L    每月的最后一个星期五上午10:15触发
- 0 15 10 ? * 6L 2002-2005   2002年至2005年的每月的最后一个星期五上午10:15触发
- 0 15 10 ? * 6#3   每月的第三个星期五上午10:15触发


# 备注
## Quartz API核心接口

- Scheduler：(调度器)与scheduler交互的主要API；
- Job：(作业)你通过scheduler执行任务，你的任务类需要实现的接口；
- JobDetail：(作业实例)定义Job的实例；
- Trigger：(触发器)触发Job的执行；
- JobBuilder：定义和创建JobDetail实例的接口;
- TriggerBuilder：定义和创建Trigger实例的接口；
## triggerState
> - `org.terracotta.quartz.wrappers.TriggerWrapper#TriggerState`
> - `org.quartz.Trigger#TriggerState`

- **NONE**：表示触发器不存在或尚未添加到调度器中。
- **NORMAL**：表示触发器处于正常状态，并且正在计划执行。
- **PAUSED**：表示触发器已被暂停，不会触发执行。
- **COMPLETE**：表示触发器已经完成，不会再次触发。通常用于一次性触发器。
- **ERROR**：表示触发器在尝试执行时遇到错误。
- **BLOCKED**：表示触发器被阻塞。通常是因为前一个触发器未完成，且多个触发器指向同一个作业。
- **ACQUIRED**：表示触发器已被获取，正准备执行。
- **WAITING**：表示触发器正在等待下一次触发。
- **DELETED**：表示触发器已经被删除，不会再次触发
- **EXECUTING**：表示触发器正在执行
## triggerType
> `org.springframework.boot.actuate.quartz.QuartzEndpoint#TriggerType`

- **CRON**: 基于 cron 表达式的触发器，允许你按照类似 UNIX cron 风格的时间表来调度任务。非常适合需要在固定时间点或者固定间隔时间执行的任务。
- **SIMPLE**: 简单触发器，用于按照固定的时间间隔（例如每隔5分钟）触发任务。非常适合简单的、固定频率的调度需求。
- **CALENDAR_INTERVAL**: 日历间隔触发器，允许按照日历时间间隔来调度任务，例如每隔一天，每隔一个月等。适用于需要考虑日历时间的调度任务。
- **DAILY_TIME_INTERVAL**: 每日时间间隔触发器，用于在一天中的特定时间间隔内触发任务，例如每天的上午9点到下午5点之间每隔一小时触发一次。适合于工作时间内的任务调度
## 注意查询的问题

1. 默认的是大写查询，这样会导致在大小写敏感的数据库里用大写是查询不到生成的小写表的
```yaml
spring: 
  jpa:
    hibernate:
      naming:
        physical-strategy: cn.tannn.jdevelops.quartz.QuartzNamingStrategy
```

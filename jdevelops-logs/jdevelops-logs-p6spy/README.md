## `P6SpyOptions` 配置说明
https://p6spy.readthedocs.io/en/latest/configandusage.html?highlight=autoflush#common-property-file-settings

```XML
# 指定应用的日志拦截模块,默认为com.p6spy.engine.spy.P6SpyFactory
#modulelist=com.p6spy.engine.spy.P6SpyFactory,com.p6spy.engine.logging.P6LogFactory,com.p6spy.engine.outage.P6OutageFactory
# 真实JDBC driver , 多个以 逗号 分割 默认为空
#driverlist=
# 是否自动刷新 默认 flase
#autoflush=false
# 配置SimpleDateFormat日期格式 默认为空
#dateformat=
# 打印堆栈跟踪信息 默认flase
#stacktrace=false
# 如果 stacktrace=true，则可以指定具体的类名来进行过滤。
#stacktraceclass=
# 监测属性配置文件是否进行重新加载
#reloadproperties=false
# 属性配置文件重新加载的时间间隔，单位:秒 默认60s
#reloadpropertiesinterval=60
# 指定 Log 的 appender，取值：
#appender=com.p6spy.engine.spy.appender.Slf4JLogger
#appender=com.p6spy.engine.spy.appender.StdoutLogger
#appender=com.p6spy.engine.spy.appender.FileLogger
# 指定 Log 的文件名 默认 spy.log
#logfile=spy.log
# 指定是否每次是增加 Log，设置为 false 则每次都会先进行清空 默认true
#append=true
# 指定日志输出样式  默认为com.p6spy.engine.spy.appender.SingleLineFormat , 单行输出 不格式化语句
#logMessageFormat=com.p6spy.engine.spy.appender.SingleLineFormat
# 也可以采用  com.p6spy.engine.spy.appender.CustomLineFormat 来自定义输出样式, 默认值是%(currentTime)|%(executionTime)|%(category)|connection%(connectionId)|%(sqlSingleLine)
# 可用的变量为:
#   %(connectionId)            connection id
#   %(currentTime)             当前时间
#   %(executionTime)           执行耗时
#   %(category)                执行分组
#   %(effectiveSql)            提交的SQL 换行
#   %(effectiveSqlSingleLine)  提交的SQL 不换行显示
#   %(sql)                     执行的真实SQL语句，已替换占位
#   %(sqlSingleLine)           执行的真实SQL语句，已替换占位 不换行显示
#customLogMessageFormat=%(currentTime)|%(executionTime)|%(category)|connection%(connectionId)|%(sqlSingleLine)
# date类型字段记录日志时使用的日期格式 默认dd-MMM-yy
#databaseDialectDateFormat=dd-MMM-yy
# boolean类型字段记录日志时使用的日期格式 默认boolean 可选值numeric
#databaseDialectBooleanFormat=boolean
# 是否通过jmx暴露属性 默认true
#jmx=true
# 如果jmx设置为true 指定通过jmx暴露属性时的前缀 默认为空
# com.p6spy(.<jmxPrefix>)?:name=<optionsClassName>
#jmxPrefix=
# 是否显示纳秒 默认false
#useNanoTime=false
# 实际数据源 JNDI
#realdatasource=/RealMySqlDS
# 实际数据源 datasource class
#realdatasourceclass=com.mysql.jdbc.jdbc2.optional.MysqlDataSource
# 实际数据源所携带的配置参数 以 k=v 方式指定 以 分号 分割
#realdatasourceproperties=port;3306,serverName;myhost,databaseName;jbossdb,foo;bar
# jndi数据源配置
# 设置 JNDI 数据源的 NamingContextFactory。
#jndicontextfactory=org.jnp.interfaces.NamingContextFactory
# 设置 JNDI 数据源的提供者的 URL。
#jndicontextproviderurl=localhost:1099
# 设置 JNDI 数据源的一些定制信息，以分号分隔。
#jndicontextcustom=java.naming.factory.url.pkgs;org.jboss.naming:org.jnp.interfaces
# 是否开启日志过滤 默认false， 这项配置是否生效前提是配置了 include/exclude/sqlexpression
#filter=false
# 过滤 Log 时所包含的表名列表，以逗号分隔 默认为空
#include=
# 过滤 Log 时所排除的表名列表，以逗号分隔 默认为空
#exclude=
# 过滤 Log 时的 SQL 正则表达式名称  默认为空
#sqlexpression=
#显示指定过滤 Log 时排队的分类列表，取值: error, info, batch, debug, statement,
#commit, rollback, result and resultset are valid values
# (默认 info,debug,result,resultset,batch)
#excludecategories=info,debug,result,resultset,batch
# 是否过滤二进制字段
# (default is false)
#excludebinary=false
# P6Log 模块执行时间设置，整数值 (以毫秒为单位)，只有当超过这个时间才进行记录 Log。 默认为0
#executionThreshold=
# P6Outage 模块是否记录较长时间运行的语句 默认false
# outagedetection=true|false
# P6Outage 模块执行时间设置，整数值 （以秒为单位)），只有当超过这个时间才进行记录 Log。 默认30s
# outagedetectioninterval=integer time (seconds)
```


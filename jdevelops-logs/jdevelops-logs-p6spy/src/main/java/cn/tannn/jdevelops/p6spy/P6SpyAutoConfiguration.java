package cn.tannn.jdevelops.p6spy;

import com.p6spy.engine.spy.P6SpyLoadableOptions;
import com.p6spy.engine.spy.P6SpyOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.Map;

import static cn.tannn.jdevelops.p6spy.P6spyUtils.checkDriverClasses;

/**
 * 将 spy.properties 转为自定义 properties
 * <P> 初始化一些业务相关的属性
 * <P> <a href="https://cloud.tencent.com/developer/article/2158300">spy.properties详细说明</a>
 * <P> <a href="https://p6spy.readthedocs.io/en/latest/configandusage.html">spy.properties官方说明</a>
 *
 * @author tan
 */

public class P6SpyAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(P6SpyAutoConfiguration.class);

    @Bean
    public P6spyConfig p6spyConfig(){
        return new P6spyConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "jdevelops.p6spy.enabled", havingValue = "true", matchIfMissing = true)
    public P6SpyConfigurer p6SpyConfigurer(P6spyConfig p6spyConfig) {
        return new P6SpyConfigurer(p6spyConfig);
    }

    public  class P6SpyConfigurer {

        private P6spyConfig p6spyConfig;

        public P6SpyConfigurer(P6spyConfig p6spyConfig) {
            this.p6spyConfig = p6spyConfig;
            configureP6Spy();
        }

        /**
         * <li> modulelist: 定义要使用的P6Spy模块列表。
         * <li> driverlist: 指定要拦截的JDBC驱动程序类名。
         * <li> logfile: 设置日志文件的路径和名称。
         * <li> append: 决定是否将新的日志内容追加到现有日志文件中。
         * <li> logMessageFormat: 定义日志消息的格式。
         * <li> databaseDialectDateFormat: 设置数据库方言的日期格式。
         * <li> excludecategories: 指定要排除的日志类别。
         * <li> includeCategories: 指定要包含的日志类别。
         * <li> filter: 启用或禁用SQL语句过滤。
         * <li> exclude: 定义要排除的SQL语句模式。
         * <li> include: 定义要包含的SQL语句模式。
         * <li> sqlExpression: 设置用于过滤SQL语句的正则表达式。
         */
        private void configureP6Spy() {
            // 创建一个新的 Map 来存储当前配置
            P6SpyLoadableOptions options = P6SpyOptions.getActiveInstance();
            Map<String, String> properties = options.getDefaults();

            // 指定 Log 的文件名 默认 spy.log
            properties.put("logfile","p6spy.log");
            // 是否每次是增加 Log，设置为 false 则每次都会先进行清空 默认true
            properties.put("append","true");
            // 使用日志系统记录sql
            properties.put("appender","com.p6spy.engine.spy.appender.Slf4JLogger");
            // 是否自动刷新 默认 flase
            properties.put("autoflush",p6spyConfig.getAutoflush()+"");
            // 自定义日志打印
            properties.put("logMessageFormat",p6spyConfig.getLogMessageFormatter());
            // 驱动
            properties.put("driverlist",checkDriverClasses(p6spyConfig.getDrivers()));
            // 日期格式
            properties.put("dateformat","yyyy-MM-dd HH:mm:ss.SSS");
            // https://p6spy.readthedocs.io/en/latest/configandusage.html?highlight=autoflush#modulelist
            properties.put("modulelist",p6spyConfig.modulelist);

            // 是否开启慢SQL记录
            properties.put("outagedetection",p6spyConfig.getOutagedetection()+"");
            // 慢SQL记录标准 秒 ,只有当超过这个时间才进行记录 Log。 默认30s
            properties.put("outagedetectioninterval",p6spyConfig.outagedetectionintervalStr());
            // 日志输出级别
            properties.put("log4j.logger.p6spy","warn,STDOUT");
            // 设置使用p6spy driver来做代理
            properties.put("deregisterdrivers","true");
            // 排除的日志类别
            // 减少日志量：在生产环境中，你可能想排除 debug 和 info 类别。
            // 性能优化：排除 resultset 可以减少大结果集的日志输出。
            // 专注于特定操作：例如，只关注 statement 和 commit，排除其他类别。
            properties.put("excludecategories","info,batch,debug,commit,rollback,result,resultset");
            options.load(properties);
        }
    }



}

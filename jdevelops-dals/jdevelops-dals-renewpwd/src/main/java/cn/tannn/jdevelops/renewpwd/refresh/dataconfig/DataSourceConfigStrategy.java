package cn.tannn.jdevelops.renewpwd.refresh.dataconfig;

import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * 数据源配置策略接口
 * <p>
 * 自定义数据源配置策略示例
 * 如果你需要支持其他数据源，只需要实现 DataSourceConfigStrategy 接口和注册bean就好了
 * <p>
 * 示例代码：
 * <pre><code>
 * &#064;Configuration
 * public class CustomDataSourceStrategyConfig {
 *
 *     &#064;Bean
 *     public DataSourceConfigStrategy myCustomDataSourceStrategy() {
 *         return new MyCustomDataSourceStrategy();
 *     }
 *
 *     // 自定义策略实现
 *     static class MyCustomDataSourceStrategy implements RenewpwdDataSourceConfig.DataSourceConfigStrategy {
 *         &#064;Override
 *         public void configureDataSource(DataSource dataSource, Environment environment) throws Exception {
 *             if (dataSource instanceof MyCustomDataSource) {
 *                 MyCustomDataSource customDS = (MyCustomDataSource) dataSource;
 *
 *                 // 手动设置属性
 *                 customDS.setMaxConnections(environment.getProperty("spring.datasource.custom.max-connections", Integer.class, 10));
 *                 customDS.setTimeout(environment.getProperty("spring.datasource.custom.timeout", Long.class, 30000L));
 *
 *                 // 或使用 Binder 自动绑定整个配置块
 *                 Binder binder = Binder.get(environment);
 *                 binder.bind("spring.datasource.custom", Bindable.ofInstance(dataSource));
 *             }
 *         }
 *
 *         &#064;Override
 *         public String[] getSupportedDataSourceTypes() {
 *             return new String[]{"MyCustomDataSource"};
 *         }
 *     }
 * }
 * </code></pre>
 */
public interface DataSourceConfigStrategy {
    /**
     * 数据源
     */
    void configureDataSource(DataSource dataSource, Environment environment) throws Exception;

    /**
     * 数据源名称
     */
    String[] getSupportedDataSourceTypes();
}

package cn.tannn.jdevelops.renewpwd.refresh.dataconfig;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * Tomcat JDBC 配置策略
 */
public class TomcatConfigStrategy implements DataSourceConfigStrategy {
    @Override
    public void configureDataSource(DataSource dataSource, Environment environment) throws Exception {
        if (dataSource.getClass().getName().contains("TomcatDataSource")) {
            Binder binder = Binder.get(environment);
            binder.bind("spring.datasource.tomcat",
                    org.springframework.boot.context.properties.bind.Bindable.ofInstance(dataSource));
        }
    }

    @Override
    public String[] getSupportedDataSourceTypes() {
        return new String[]{"TomcatDataSource","DataSource"}; // Tomcat JDBC 的类名
    }
}

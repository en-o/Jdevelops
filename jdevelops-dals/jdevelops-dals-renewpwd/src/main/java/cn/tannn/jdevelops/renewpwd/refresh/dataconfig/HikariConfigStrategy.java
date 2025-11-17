package cn.tannn.jdevelops.renewpwd.refresh.dataconfig;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

import static cn.tannn.jdevelops.renewpwd.util.DatabaseUtils.isClassPresent;

/**
 * HikariCP 配置策略
 */
public class HikariConfigStrategy implements DataSourceConfigStrategy {

    @Override
    public void configureDataSource(DataSource dataSource, Environment environment) throws Exception {
        if (isClassPresent("com.zaxxer.hikari.HikariDataSource") &&
                dataSource.getClass().getName().contains("HikariDataSource")) {

            Binder binder = Binder.get(environment);
            binder.bind("spring.datasource.hikari",
                    org.springframework.boot.context.properties.bind.Bindable.ofInstance(dataSource));
        }
    }

    @Override
    public String[] getSupportedDataSourceTypes() {
        return new String[]{"HikariDataSource"};
    }
}

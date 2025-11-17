package cn.tannn.jdevelops.renewpwd.refresh.dataconfig;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * DBCP2 配置策略
 */
public class Dbcp2ConfigStrategy implements DataSourceConfigStrategy {
    @Override
    public void configureDataSource(DataSource dataSource, Environment environment) throws Exception {
        if (dataSource.getClass().getName().contains("BasicDataSource")) {
            Binder binder = Binder.get(environment);
            binder.bind("spring.datasource.dbcp2",
                    org.springframework.boot.context.properties.bind.Bindable.ofInstance(dataSource));
        }
    }

    @Override
    public String[] getSupportedDataSourceTypes() {
        return new String[]{"BasicDataSource"};
    }
}

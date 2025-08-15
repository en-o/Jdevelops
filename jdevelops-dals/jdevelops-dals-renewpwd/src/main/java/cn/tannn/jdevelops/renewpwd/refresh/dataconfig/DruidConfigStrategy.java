package cn.tannn.jdevelops.renewpwd.refresh.dataconfig;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

import static cn.tannn.jdevelops.renewpwd.util.DatabaseUtils.isClassPresent;

/**
 * Druid 配置策略
 */
public class DruidConfigStrategy implements DataSourceConfigStrategy {
    @Override
    public void configureDataSource(DataSource dataSource, Environment environment) throws Exception {
        if (isClassPresent("com.alibaba.druid.pool.DruidDataSource") &&
                dataSource.getClass().getName().contains("DruidDataSource")) {

            Binder binder = Binder.get(environment);
            binder.bind("spring.datasource.druid",
                    org.springframework.boot.context.properties.bind.Bindable.ofInstance(dataSource));
        }
    }

    @Override
    public String[] getSupportedDataSourceTypes() {
        return new String[]{"DruidDataSource"};
    }


}

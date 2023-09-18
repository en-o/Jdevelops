package cn.jdevelops.data.ddss.core;

import cn.jdevelops.data.ddss.context.DynamicContextHolder;
import cn.jdevelops.data.ddss.util.DynamicDataSourceUtil;
import cn.jdevelops.data.ddss.util.DynamicSpringBeanUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  继承AbstractRoutingDataSource 加载多个数据源
 * @author tn
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    // 存放多数据源
    public static Map<Object, Object> targetDataSources = new ConcurrentHashMap<>(40);

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicContextHolder.peek();
    }


    public static void setDataSource(String dbName) {
        if(!DynamicDataSource.targetDataSources.containsKey(dbName)){
            HikariDataSource dataSource = DynamicDataSourceUtil.getDbSourceByDbName(dbName);
            DynamicDataSource.targetDataSources.put(dbName,dataSource);
        }
        //切换动态多数据源的dbName
        DynamicContextHolder.push(dbName);
        DynamicDataSource dynamicDataSource = DynamicSpringBeanUtil.getBean(DynamicDataSource.class);
        //使得修改后的targetDataSources生效
        dynamicDataSource.afterPropertiesSet();
    }


    /**
     * 刷新数据源
     */
    public static void refreshDataSource() {
        DynamicDataSource dynamicDataSource = DynamicSpringBeanUtil.getBean(DynamicDataSource.class);
        //使得修改后的targetDataSources生效
        dynamicDataSource.afterPropertiesSet();
    }

    /**
     * 刷新数据源
     * @param deleteDbName 移除源
     */
    public static void refreshDataSource(String deleteDbName) {
        DynamicDataSourceUtil.closeDbName(deleteDbName);
        DynamicDataSource.targetDataSources.remove(deleteDbName);
        DynamicDataSource dynamicDataSource = DynamicSpringBeanUtil.getBean(DynamicDataSource.class);
        //使得修改后的targetDataSources生效
        dynamicDataSource.afterPropertiesSet();
    }
}

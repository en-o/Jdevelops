package cn.tannn.jdevelops.ddss.core;

import cn.tannn.jdevelops.ddss.context.DynamicContextHolder;
import cn.tannn.jdevelops.ddss.util.DynamicDataSourceUtil;
import cn.tannn.jdevelops.ddss.util.DynamicSpringBeanUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  继承AbstractRoutingDataSource 加载多个数据源
 * @author tn
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 存放多数据源
     */
    public static Map<Object, Object> targetDataSources = new ConcurrentHashMap<>(40);


    /**
     * Determine the current lookup key. This will typically be
     * implemented to check a thread-bound transaction context.
     * <p>Allows for arbitrary keys. The returned key needs
     * to match the stored lookup key type, as resolved by the
     * {@link #resolveSpecifiedLookupKey} method.
     */
    @Override
    protected Object determineCurrentLookupKey() {
        // 获取 缓存中的中数据源名
        return DynamicContextHolder.peek();
    }


    public static void setDataSource(String dbName) {
        if(!DynamicDataSource.targetDataSources.containsKey(dbName)){
            // 查询数据源
            HikariDataSource dataSource = DynamicDataSourceUtil.getDbSourceByDbName(dbName);
            // 添加数据源到 targetDataSources 中
            DynamicDataSource.targetDataSources.put(dbName,dataSource);
        }
        //切换动态多数据源的dbName[设置到数据源名缓存中,#determineCurrentLookupKey用]
        DynamicContextHolder.push(dbName);
        // 获取当前类的bean
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
     * 移除数据源
     * @param deleteDbName 移除源
     */
    public static void refreshDataSource(String deleteDbName) {
        // 删除数据源源
        DynamicDataSourceUtil.closeDbName(deleteDbName);
        DynamicDataSource.targetDataSources.remove(deleteDbName);
        // 刷新数据源
        DynamicDataSource dynamicDataSource = DynamicSpringBeanUtil.getBean(DynamicDataSource.class);
        //使得修改后的targetDataSources生效
        dynamicDataSource.afterPropertiesSet();
    }

    /**
     *  更新数据源
     * @param dbName 更新源
     */
    public static void renewalDataSource(String dbName) {
        // 删除数据源源
        DynamicDataSourceUtil.closeDbName(dbName);
        DynamicDataSource.targetDataSources.remove(dbName);
        setDataSource(dbName);
    }
}

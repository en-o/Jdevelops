package cn.jdevelops.data.ddss.cache;


import cn.jdevelops.data.ddss.exception.DynamicDataSourceException;
import cn.jdevelops.data.ddss.model.DynamicDatasourceEntity;
import cn.jdevelops.data.ddss.service.DynamicDatasourceService;
import cn.jdevelops.data.ddss.util.DynamicSpringBeanUtil;
import com.zaxxer.hikari.HikariDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据源缓存池
 * @author tan
 */
public class DataSourceCachePool {

    /**
     * 本地 数据源缓存
     */
    private static Map<String, HikariDataSource> dbSources = new HashMap<>();


    /**
     * 获取多数据源缓存
     * @param dbName 数据源
     * @return 元数据
     */
    public static DynamicDatasourceEntity getCacheDynamicDataSourceModel(String dbName) {
        try {
            // todo 这里可以加个redis缓存，不用每次都查询库
            DynamicDatasourceService dynamicDatasourceService = DynamicSpringBeanUtil.getBean(DynamicDatasourceService.class);
            DynamicDatasourceEntity dbSource = dynamicDatasourceService.findDyDatasourceEntity(dbName);
            // todo 如果存储的密码是加密的这里可以解密后重新set密码
            return dbSource;
        }catch (Exception e){
            throw DynamicDataSourceException.specialMessage("数据源====《"+dbName+"》 可能已被移除无法在对其进行操作");
        }
    }

    public static HikariDataSource getCacheBasicDataSource(String dbName) {
        return dbSources.get(dbName);
    }

    /**
     * put 数据源缓存
     *
     * @param dbName 数据源名
     * @param db  HikariDataSource
     */
    public static void putCacheBasicDataSource(String dbName, HikariDataSource db) {
        dbSources.put(dbName, db);
    }

    /**
     * 清空数据源缓存
     */
    public static void cleanAllCache() {
        //关闭数据源连接
        for(Map.Entry<String, HikariDataSource> entry : dbSources.entrySet()){
            HikariDataSource druidDataSource = entry.getValue();
            if(druidDataSource!=null && druidDataSource.isClosed()){
                druidDataSource.close();
            }
        }
        //清空缓存
        dbSources.clear();
    }

    public static void removeCache(String dbName) {
        //关闭数据源连接
        HikariDataSource dataSource = dbSources.get(dbName);
        if(dataSource!=null){
            dataSource.close();
        }
        //清空缓存
        dbSources.remove(dbName);
    }

}

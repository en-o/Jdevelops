package cn.jdevelops.data.ddss.util;

import cn.jdevelops.data.ddss.cache.DataSourceCachePool;
import cn.jdevelops.data.ddss.exception.DynamicDataSourceException;
import cn.jdevelops.data.ddss.model.DynamicDatasourceEntity;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * 将数据源配置构建实际的数据源
 *
 * @author tan
 */
public class DynamicDataSourceUtil {

    public static Logger logger = LoggerFactory.getLogger(DynamicDataSourceUtil.class);

    public static HikariDataSource buildDataSource(DataSourceProperties properties) {
        return DataSourceBuilder.create(properties.getClassLoader())
                .driverClassName(properties.determineDriverClassName())
                .username(properties.determineUsername())
                .password(properties.determinePassword())
                .type(HikariDataSource.class)
                .url(properties.determineUrl())
                .build();
    }


    /**
     * 通过 dbName ,获取数据源
     *
     * @param dbName 数据源名
     * @return HikariDataSource
     */
    public static HikariDataSource getDbSourceByDbName(final String dbName) {
        //先判断缓存中是否存在数据库链接
        HikariDataSource cacheDbSource = DataSourceCachePool.getCacheBasicDataSource(dbName);
        if (cacheDbSource != null && !cacheDbSource.isClosed()) {
            logger.debug("----------------------从缓存中获取DB连接-------------------");
            return cacheDbSource;
        } else {
            //获取多数据源配置
            DynamicDatasourceEntity dbSource = DataSourceCachePool.getCacheDynamicDataSourceModel(dbName);
            HikariDataSource dataSource = getJdbcDataSource(dbSource);
            if (dataSource != null && !dataSource.isClosed()) {
                DataSourceCachePool.putCacheBasicDataSource(dbName, dataSource);
            } else {
                throw DynamicDataSourceException.specialMessage("动态数据源连接失败,数据源可能被移除，dbName：" + dbName);
            }
            logger.info("--------getDbSourceBydbName------------------创建DB数据库连接-------------------");
            return dataSource;
        }
    }

    /**
     * 获取数据源【最底层方法，不要随便调用】
     *
     * @param dbSource 数据源元信息
     * @return HikariDataSource
     */
    private static HikariDataSource getJdbcDataSource(final DynamicDatasourceEntity dbSource) {
        if (dbSource == null) {
            return null;
        }
        return DataSourceBuilder.create(dbSource.getClass().getClassLoader())
                .driverClassName(dbSource.getDriverClassName())
                .username(dbSource.getDatasourceUsername())
                .password(dbSource.getDatasourcePassword())
                .type(HikariDataSource.class)
                .url(dbSource.getDatasourceUrl())
                .build();
    }

    /**
     * 关闭数据库连接
     *
     * @param dbName 数据源名
     */
    public static void closeDbName(final String dbName) {
        HikariDataSource dataSource = getDbSourceByDbName(dbName);
        try {
            if (dataSource != null && !dataSource.isClosed()) {
                try {
                    dataSource.getConnection().commit();
                } catch (Exception e) {
                    logger.warn("commit失败:{}", e.getMessage());
                }
                dataSource.getConnection().close();
                dataSource.close();
            }
        } catch (SQLException e) {
            logger.warn("closeDbName失败:{}", e.getMessage(), e);
        }
    }


    private static JdbcTemplate getJdbcTemplate(String dbName) {
        DataSource dataSource = getDbSourceByDbName(dbName);
        return new JdbcTemplate(dataSource);
    }

    /**
     * 获取连接
     *
     * @param url        url
     * @param username   username
     * @param password   password
     * @param driverName driverName
     * @return Connection
     */
    public static Connection getConn(String url, String username, String password, String driverName) {
        Connection conn;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw DynamicDataSourceException.specialMessage("无法连接，问题：" + e.getMessage(), e);
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     * @param conn Connection
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            throw DynamicDataSourceException.specialMessage("close connection failure", e);
        }
    }
}

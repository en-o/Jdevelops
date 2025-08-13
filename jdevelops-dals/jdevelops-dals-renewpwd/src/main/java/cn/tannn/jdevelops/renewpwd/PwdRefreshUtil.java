package cn.tannn.jdevelops.renewpwd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 配置刷新工具类
 * 提供Bean刷新相关的工具方法，包括Bean类型判断、配置匹配、刷新策略决策等
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/10 01:33
 */
public class PwdRefreshUtil {


    private static final Logger log = LoggerFactory.getLogger(PwdRefreshUtil.class);

    /**
     * 验证数据源配置是否有效
     */
    public static boolean validateDatasourceConfig(ConfigurableEnvironment environment ) {
        try {

            String url = environment.getProperty("spring.datasource.url");
            String username = environment.getProperty("spring.datasource.username");
            String password = environment.getProperty("spring.datasource.password");
            String driverClassName = environment.getProperty("spring.datasource.driver-class-name");

            log.debug("[renewpwd] 数据源配置: url={}, username={}, password={}",
                    url, username, password);

            if (url == null || username == null || password == null) {
                log.warn("[renewpwd] 数据源配置不完整: url={}, username={}, password={}",
                        url, username, password != null ? "***" : "null");
                return false;
            }

            // 创建临时连接测试
            return testDatabaseConnection(url, username, password, driverClassName);

        } catch (Exception e) {
            log.error("[renewpwd] 验证数据源配置时发生异常", e);
            return false;
        }
    }


    /**
     * 测试数据库连接是否有效
     */
    public static boolean testDatabaseConnection(String url, String username, String password, String driverClassName) {
        java.sql.Connection connection = null;
        try {
            // 加载数据库驱动
            if (driverClassName != null) {
                Class.forName(driverClassName);
            }

            log.info("[renewpwd] 正在验证数据库连接: url={}, username={}", url, username);

            // 设置连接超时时间
            java.util.Properties props = new java.util.Properties();
            props.setProperty("user", username);
            props.setProperty("password", password);
            props.setProperty("connectTimeout", "5000"); // 5秒连接超时
            props.setProperty("socketTimeout", "5000");   // 5秒socket超时

            connection = java.sql.DriverManager.getConnection(url, props);

            // 测试连接有效性
            if (connection.isValid(3)) {
                log.info("[renewpwd] 数据库连接验证成功");
                return true;
            } else {
                log.warn("[renewpwd] 数据库连接无效");
                return false;
            }

        } catch (java.sql.SQLException e) {
            log.error("[renewpwd] 数据库连接验证失败: {}", e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            log.error("[renewpwd] 数据库驱动类未找到: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("[renewpwd] 数据库连接验证异常", e);
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (java.sql.SQLException e) {
                    log.debug("[renewpwd] 关闭测试连接失败", e);
                }
            }
        }
    }
}

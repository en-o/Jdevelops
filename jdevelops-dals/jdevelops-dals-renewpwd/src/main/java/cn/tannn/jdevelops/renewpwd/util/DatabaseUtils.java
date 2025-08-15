package cn.tannn.jdevelops.renewpwd.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/15 14:12
 */
public class DatabaseUtils {

    private static final Logger log = LoggerFactory.getLogger(DatabaseUtils.class);

    /**
     * 判断错误码是否为密码过期错误
     * <p>  mysql ：SELECT * FROM performance_schema.events_errors_summary_global_by_error  where error_number = 'vendorCode' </p>
     * @param vendorCode 数据库返回的错误码
     * @param driverClassName 数据库驱动类名，用于区分数据库类型
     * @return true 如果是密码过期错误，false 否则
     */
    public static boolean isPasswordExpiredError(int vendorCode, String driverClassName) {
        if (driverClassName == null) {
            return false;
        }

        String driver = driverClassName.toLowerCase();

        // MySQL
        if (driver.toLowerCase().contains("mysql")) {
            return vendorCode == 1820 || vendorCode == 1862;
        }else {
            log.warn("其他数据库暂未支持，当前驱动：{}", driverClassName);
        }
        return false;
    }


    public static SQLException findDeepestSQLException(Throwable e) {
        SQLException deepest = null;
        while (e != null) {
            if (e instanceof SQLException sqlEx) {
                deepest = sqlEx;      // 每次都覆盖，循环结束后就是最深层的
            }
            e = e.getCause();
        }
        return deepest;
    }
}

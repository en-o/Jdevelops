package cn.tannn.jdevelops.renewpwd.jdbc;

import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * postgreSQL
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/19 10:41
 */
public class PgJdbc {

    private static final Logger log = LoggerFactory.getLogger(MySqlJdbc.class);


    /**
     * @param username    用户名
     * @param newPassword 新密码
     * @param statement   Statement对象
     * @param resetExpiryDay {@link RenewpwdProperties#resetExpiryDay} 重置密码过期天数
     * @return 如果更新成功返回false，否则返回true
     * @throws SQLException
     */
    public static boolean extractedUpdatePassword(String username, String newPassword, Statement statement
            , Integer resetExpiryDay) throws SQLException {
        // ps "infinity" // 设置密码永不过期
        resetExpiryDay = resetExpiryDay == null ? 30 : resetExpiryDay;
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(resetExpiryDay);
        String time = expiryDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String setPasswordSQL = String.format("ALTER USER %s WITH PASSWORD '%s' VALID UNTIL '%s'"
                , username
                , newPassword
                , time
        );

        try {
            log.debug("[renewpwd] 尝试执行: ALTER USER name WITH PASSWORD '***' VALID UNTIL '{}'", time);
            statement.executeUpdate(setPasswordSQL);
            log.info("[renewpwd] 更新密码成功");
        } catch (SQLException e) {
            log.warn("[renewpwd] pg/kingbase更新用户过期时间和密码失败: {}", e.getMessage());
            return true;
        }
        return false;
    }
}

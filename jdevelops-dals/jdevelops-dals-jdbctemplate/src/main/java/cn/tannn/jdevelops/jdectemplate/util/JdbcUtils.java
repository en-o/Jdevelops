package cn.tannn.jdevelops.jdectemplate.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一些工具、
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/25 上午10:51
 */
public class JdbcUtils {

    static Pattern pattern = Pattern.compile("FROM\\s+(\\w+)");

    /**
     * 获取sql中的表名
     * @param sql sql
     * @return 表名
     */
    public static String tableName(String sql){
        Matcher matcher = pattern.matcher(sql.toUpperCase());
        if(matcher.find()) {
            return matcher.group(1);
        }
        throw new RuntimeException("Invalid sql : "+ sql);
    }
}

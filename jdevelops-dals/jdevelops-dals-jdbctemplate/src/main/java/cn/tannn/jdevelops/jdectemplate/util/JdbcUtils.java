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


    /**
     * 判断是不是基础类型 （String, Integer ）
     *
     * @param resultType 类型
     * @return boolean
     */
    public static boolean isBasicType(String resultType) {
        return resultType.equals(String.class.getName())
                || resultType.equals(Integer.class.getName());
    }


    /**
     * 返回 sql 的 from
     *
     * @param sql sql
     * @return from xx [注意此项包含 from]
     */
    public static String extractFromClause(String sql) {
        // 查找 FROM 关键字的位置
        int fromIndex = sql.toUpperCase().indexOf("FROM");

        if (fromIndex != -1) {
            // 截取 FROM 关键字之后的所有内容
            return sql.substring(fromIndex);
        } else {
            return null; // 如果没有找到 FROM 关键字，则返回 null
        }
    }
}

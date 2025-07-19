package cn.tannn.jdevelops.jdectemplate.sql;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/3 13:13
 */
public class SqlUtil {
    /**
     * 查找主查询FROM关键字的位置，避免子查询中的FROM干扰
     */
    public static int findFromIndex(String sql) {
        String lowerSql = sql.toLowerCase();
        int fromIndex = -1;
        int parenthesesCount = 0;

        // 跳过SELECT关键字
        int selectIndex = lowerSql.indexOf("select");
        if (selectIndex == -1) {
            return -1;
        }

        // 从SELECT后开始查找FROM
        for (int i = selectIndex + 6; i <= lowerSql.length() - 5; i++) {
            char c = lowerSql.charAt(i);

            if (c == '(') {
                parenthesesCount++;
            } else if (c == ')') {
                parenthesesCount--;
            } else if (parenthesesCount == 0 && lowerSql.substring(i, i + 5).equals(" from")) {
                // 确保FROM前后都是空格或边界
                if (i > 0 && (i + 5 >= lowerSql.length() || Character.isWhitespace(lowerSql.charAt(i + 5)))) {
                    fromIndex = i;
                    break;
                }
            }
        }

        return fromIndex;
    }

    /**
     * 查找主查询ORDER BY的位置，避免子查询和窗口函数中的ORDER BY干扰
     */
    public static int findOrderByIndex(String sql) {
        String lowerSql = sql.toLowerCase();
        int parenthesesCount = 0;
        boolean inOver = false;

        // 从后往前查找，这样能更准确地找到主查询的ORDER BY
        for (int i = lowerSql.length() - 9; i >= 0; i--) {
            char c = lowerSql.charAt(i);

            if (c == ')') {
                parenthesesCount++;
            } else if (c == '(') {
                parenthesesCount--;
            }

            // 检查是否在OVER子句中
            if (parenthesesCount == 0 && i >= 4 && lowerSql.substring(i - 4, i + 1).equals(" over")) {
                inOver = true;
            } else if (inOver && parenthesesCount == 0 && c == ')') {
                inOver = false;
            }

            // 只在主查询级别且不在OVER子句中查找ORDER BY
            if (parenthesesCount == 0 && !inOver && i + 9 <= lowerSql.length() &&
                    lowerSql.substring(i, i + 9).equals(" order by")) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 查找主查询LIMIT的位置，避免子查询中的LIMIT干扰
     */
    public static int findLimitIndex(String sql) {
        String lowerSql = sql.toLowerCase();
        int parenthesesCount = 0;

        // 从后往前查找，确保找到的是主查询的LIMIT
        for (int i = lowerSql.length() - 6; i >= 0; i--) {
            char c = lowerSql.charAt(i);

            if (c == ')') {
                parenthesesCount++;
            } else if (c == '(') {
                parenthesesCount--;
            }

            // 只在主查询级别查找LIMIT
            if (parenthesesCount == 0 && i + 6 <= lowerSql.length() &&
                    lowerSql.substring(i, i + 6).equals(" limit")) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 计算需要排除的参数个数
     */
    public static int calculateExcludeParams(String sql) {
        int excludeParams = 0;

        // 检查原SQL中是否有LIMIT子句并计算需要排除的参数个数
        int originalLimitIndex = findLimitIndex(sql);
        if (originalLimitIndex != -1) {
            String limitClause = sql.substring(originalLimitIndex);
            // 检查是否包含offset参数(LIMIT offset, count格式)
            if (limitClause.contains(",")) {
                excludeParams = 2; // offset和limit两个参数
            } else {
                excludeParams = 1; // 只有limit参数
            }
        }

        return excludeParams;
    }

    /**
     * 替换第一个问号占位符
     *
     * @param sql   SQL语句
     * @param value 参数值
     * @return 替换后的SQL语句
     */
    public static String replaceFirstPlaceholder(String sql, Object value) {
        int index = sql.indexOf('?');
        if (index == -1) {
            return sql;
        }
        return sql.substring(0, index) + formatValue(value) + sql.substring(index + 1);
    }

    /**
     * 格式化参数值
     *
     * @param value 参数值
     * @return 格式化后的字符串
     */
    public static String formatValue(Object value) {
        if (value == null) {
            return "NULL";
        }

        // 处理不同类型的值
        // 处理不同类型的值
        if (value instanceof String || value instanceof Character) {
            // 确保字符串类型被单引号包裹
            return "'" + escapeString(value.toString()) + "'";
        } else if (value instanceof java.util.Date) {
            return "'" + formatDate((java.util.Date) value) + "'";
        } else if (value instanceof java.time.temporal.Temporal) {
            return "'" + value + "'";
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? "1" : "0";
        } else if (value instanceof Collection<?>) {
            return formatCollection((Collection<?>) value);
        } else if (value instanceof Object[]) {
            return formatCollection(Arrays.asList((Object[]) value));
        } else if (value instanceof Number) {
            // 数字类型直接返回字符串形式
            return value.toString();
        } else {
            // 其他类型都当作字符串处理并用单引号包裹
            return "'" + escapeString(value.toString()) + "'";
        }
    }

    /**
     * 转义字符串中的特殊字符
     *
     * @param str 原始字符串
     * @return 转义后的字符串
     */
    public static String escapeString(String str) {
        return str.replace("'", "''")
                .replace("\\", "\\\\");
    }

    /**
     * 格式化日期对象
     *
     * @param date 日期对象
     * @return 格式化的日期字符串
     */
    public static String formatDate(java.util.Date date) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 格式化集合
     *
     * @param collection 集合对象
     * @return 格式化的字符串
     */
    public static String formatCollection(Collection<?> collection) {
        if (collection.isEmpty()) {
            return "()";
        }
        StringBuilder sb = new StringBuilder("(");
        boolean first = true;
        for (Object item : collection) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(formatValue(item));
            first = false;
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * 忽略大小写查找最后一个匹配位置，考虑SQL语法结构
     * 避免子查询、窗口函数等内部结构的干扰
     *
     * @param source 源字符串
     * @param target 目标字段
     * @return 如果未找到匹配位置返回-1，否则返回最后一个匹配位置的索引
     */
    public static int findLastIgnoreCase(String source, String target) {
        if (source == null || target == null) {
            return -1;
        }

        // 对于ORDER BY和LIMIT这些关键字，使用专门的方法
        if (" ORDER BY ".equalsIgnoreCase(target.trim())) {
            return findOrderByIndex(source);
        } else if (" LIMIT ".equalsIgnoreCase(target.trim())) {
            return findLimitIndex(source);
        }

        // 对于其他情况，使用原来的逻辑
        int sourceLen = source.length();
        int targetLen = target.length();

        if (targetLen > sourceLen) {
            return -1;
        }

        // 从后往前查找
        for (int i = sourceLen - targetLen; i >= 0; i--) {
            if (source.regionMatches(true, i, target, 0, targetLen)) {
                return i;
            }
        }

        return -1;
    }
}

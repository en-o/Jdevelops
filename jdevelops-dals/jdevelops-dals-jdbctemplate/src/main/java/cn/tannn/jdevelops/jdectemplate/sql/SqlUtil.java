package cn.tannn.jdevelops.jdectemplate.sql;

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
     * 查找ORDER BY的位置，避免子查询中的ORDER BY干扰
     */
    public static int findOrderByIndex(String sql) {
        String lowerSql = sql.toLowerCase();
        int orderByIndex = -1;
        int parenthesesCount = 0;

        for (int i = 0; i <= lowerSql.length() - 9; i++) {
            char c = lowerSql.charAt(i);

            if (c == '(') {
                parenthesesCount++;
            } else if (c == ')') {
                parenthesesCount--;
            } else if (parenthesesCount == 0 && lowerSql.substring(i, i + 9).equals(" order by")) {
                orderByIndex = i;
                break;
            }
        }

        return orderByIndex;
    }

    /**
     * 查找LIMIT的位置，避免子查询中的LIMIT干扰
     */
    public static int findLimitIndex(String sql) {
        String lowerSql = sql.toLowerCase();
        int limitIndex = -1;
        int parenthesesCount = 0;

        for (int i = 0; i <= lowerSql.length() - 6; i++) {
            char c = lowerSql.charAt(i);

            if (c == '(') {
                parenthesesCount++;
            } else if (c == ')') {
                parenthesesCount--;
            } else if (parenthesesCount == 0 && lowerSql.substring(i, i + 6).equals(" limit")) {
                limitIndex = i;
                break;
            }
        }

        return limitIndex;
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
}

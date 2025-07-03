package cn.tannn.jdevelops.jdectemplate.sql;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/3 12:05
 */
public class SqlUtil {

    /**
     * 查找FROM关键字的位置（忽略字符串字面量中的FROM）
     * @param sql SQL语句
     * @return FROM关键字的位置，未找到返回-1
     */
    public static int findFromKeyword(String sql) {
        String upperSql = sql.toUpperCase();
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        int parenLevel = 0;

        for (int i = 0; i <= sql.length() - 4; i++) {
            char c = sql.charAt(i);

            // 处理字符串字面量
            if (c == '\'' && !inDoubleQuote) {
                inSingleQuote = !inSingleQuote;
            } else if (c == '"' && !inSingleQuote) {
                inDoubleQuote = !inDoubleQuote;
            }

            // 跳过字符串字面量内的内容
            if (inSingleQuote || inDoubleQuote) {
                continue;
            }

            // 处理括号层级
            if (c == '(') {
                parenLevel++;
            } else if (c == ')') {
                parenLevel--;
            }

            // 只在最外层查找FROM关键字
            if (parenLevel == 0 && i + 4 <= upperSql.length() && upperSql.substring(i, i + 4).equals("FROM")) {
                // 确保FROM前面是空白字符或其他分隔符（或者是开头）
                boolean validBefore = (i == 0) || Character.isWhitespace(sql.charAt(i - 1)) ||
                        sql.charAt(i - 1) == ')' || sql.charAt(i - 1) == ',';

                // 确保FROM后面是空白字符或其他分隔符（或者是结尾）
                boolean validAfter = (i + 4 >= sql.length()) || Character.isWhitespace(sql.charAt(i + 4)) ||
                        sql.charAt(i + 4) == '(' || sql.charAt(i + 4) == ',';

                if (validBefore && validAfter) {
                    // 返回FROM关键字的位置
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * 移除SQL语句中的ORDER BY和LIMIT子句
     *
     * @param sql 原始SQL语句
     * @return 移除ORDER BY和LIMIT后的SQL语句
     */
    public static String removeOrderByAndLimit(String sql) {
        String result = sql;

        // 移除ORDER BY子句
        result = removeClause(result, "ORDER BY");

        // 移除LIMIT子句
        result = removeClause(result, "LIMIT");

        return result;
    }


    /**
     * 移除指定的SQL子句
     *
     * @param sql    SQL语句
     * @param clause 要移除的子句关键字
     * @return 移除子句后的SQL语句
     */
    private static String removeClause(String sql, String clause) {
        String upperSql = sql.toUpperCase();
        String upperClause = " " + clause + " ";

        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        int parenLevel = 0;

        for (int i = 0; i <= sql.length() - upperClause.length(); i++) {
            char c = sql.charAt(i);

            // 处理字符串字面量
            if (c == '\'' && !inDoubleQuote) {
                inSingleQuote = !inSingleQuote;
            } else if (c == '"' && !inSingleQuote) {
                inDoubleQuote = !inDoubleQuote;
            }

            // 跳过字符串字面量内的内容
            if (inSingleQuote || inDoubleQuote) {
                continue;
            }

            // 处理括号层级
            if (c == '(') {
                parenLevel++;
            } else if (c == ')') {
                parenLevel--;
            }

            // 只在最外层查找子句关键字
            if (parenLevel == 0 && i + upperClause.length() <= upperSql.length() &&
                    upperSql.substring(i, i + upperClause.length()).equals(upperClause)) {
                // 找到了子句，移除从这里到字符串末尾的内容
                return sql.substring(0, i).trim();
            }
        }

        return sql;
    }

    /**
     * 计算需要移除的参数数量
     *
     * @param mode            ParameterMode
     * @param originalSql     原始SQL
     * @param fromClause      FROM子句
     * @param cleanFromClause 清理后的FROM子句
     * @return 需要移除的参数数量
     */
    public static int calculateParametersToRemove(ParameterMode mode, String originalSql, String fromClause, String cleanFromClause) {
        if (mode == ParameterMode.POSITIONAL) {
            // 计算被移除部分的参数占位符数量
            String removedPart = fromClause.substring(cleanFromClause.length());
            return countOccurrences(removedPart);
        } else {
            // 命名参数模式下，通过检查是否包含limit和offset参数名来判断
            String lowerOriginalSql = originalSql.toLowerCase();
            int count = 0;
            if (lowerOriginalSql.contains(":limit")) {
                count++;
            }
            if (lowerOriginalSql.contains(":offset")) {
                count++;
            }
            return count;
        }
    }

    // 辅助方法：计算字符串中特定字符的出现次数
    private static int countOccurrences(String str) {
        int count = 0;
        int lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = str.indexOf("?", lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += "?".length();
            }
        }
        return count;
    }
}

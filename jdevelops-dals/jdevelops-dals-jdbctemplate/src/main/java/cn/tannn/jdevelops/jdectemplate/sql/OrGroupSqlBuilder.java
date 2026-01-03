package cn.tannn.jdevelops.jdectemplate.sql;

import cn.tannn.jdevelops.annotations.jdbctemplate.sql.enums.ParameterMode;

/**
 * OR条件组SQL构建器
 */
public class OrGroupSqlBuilder extends BasicConditionSqlBuilder {
    protected boolean inOrGroup = false;
    protected boolean orGroupHasConditions = false;

    public OrGroupSqlBuilder(String baseSql, ParameterMode mode) {
        super(baseSql, mode);
    }

    protected String replaceTopLevelAndWithOr(String conditions) {
        if (conditions == null || conditions.trim().isEmpty()) {
            return conditions;
        }

        StringBuilder result = new StringBuilder();
        int level = 0;
        int i = 0;

        while (i < conditions.length()) {
            char c = conditions.charAt(i);

            if (c == '(') {
                level++;
                result.append(c);
                i++;
            } else if (c == ')') {
                level--;
                result.append(c);
                i++;
            } else if (level == 0 && i <= conditions.length() - 5) {
                if (conditions.substring(i, i + 5).equals(" AND ")) {
                    result.append(" OR ");
                    i += 5;
                } else {
                    result.append(c);
                    i++;
                }
            } else {
                result.append(c);
                i++;
            }
        }

        return result.toString();
    }
}

package cn.tannn.jdevelops.jdectemplate.sql;

/**
 * 基本条件SQL构建器
 */
public class BasicConditionSqlBuilder extends BaseSqlBuilder implements ConditionBuilder {
    protected boolean lastConditionWasOr = false;

    public BasicConditionSqlBuilder(String baseSql, ParameterMode mode) {
        super(baseSql, mode);
    }

    @Override
    public void appendWhereOrAnd() {
        if (!hasWhere) {
            sql.append(" WHERE ");
            hasWhere = true;
        } else {
            sql.append(" AND ");
        }
        lastConditionWasOr = false;
    }

    @Override
    public void appendOrCondition() {
        if (!hasWhere) {
            sql.append(" WHERE ");
            hasWhere = true;
        } else if (!lastConditionWasOr) {
            sql.append(" OR ");
        }
        lastConditionWasOr = false;
    }
}

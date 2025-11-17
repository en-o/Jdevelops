package cn.tannn.jdevelops.util.jpa.constant;

/**
 * 关系运算符
 *
 * @author tn
 * @date 2021-12-08 16:53
 */
public enum SQLOperator {

    /* 等于 */
    EQ,
    /* 不相等 */
    NE,
    /*  模糊 */
    LIKE,
    /** 不包含 */
    NOTLIKE,
    /*  左模糊  */
    LLIKE,
    /* 右模糊 */
    RLIKE,
    /* 大于 */
    GT,
    /* 小于 */
    LT,
    /*大于等于 */
    GTE,
    /* 小于等于 */
    LTE,
    /* 等于空值 */
    ISNULL,
    /* 空值 */
    ISNOTNULL,
    /* [x,y] */
    BETWEEN,
    /*包含*/
    IN,
    /*不包含*/
    NOTIN,
}

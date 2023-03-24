package cn.jdevelops.data.jap.enums;

import cn.jdevelops.data.jap.core.specification.OperatorWrapper;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * 关系运输符
 *
 * @author tn
 * @date 2021-12-08 16:53
 */
public enum SQLOperatorWrapper{

    /* 等于 */
    EQ(e -> e.getSpecWrapper().eq(e.getSelectKey(), e.getSelectValue())),
    /* 不相等 */
    NE(e -> e.getSpecWrapper().eq(e.getSelectKey(), e.getSelectValue())),
    /*  模糊 */
    LIKE(e -> e.getSpecWrapper().eq(e.getSelectKey(), e.getSelectValue())),
    /** 不包含 */
    NOTLIKE(e -> e.getSpecWrapper().eq(e.getSelectKey(), e.getSelectValue())),
    /*  左模糊  */
    LLIKE(e -> e.getSpecWrapper().eq(e.getSelectKey(), e.getSelectValue())),
    /* 右模糊 */
    RLIKE(e -> e.getSpecWrapper().eq(e.getSelectKey(), e.getSelectValue())),
    /* 大于 */
    GT(e -> e.getSpecWrapper().eq(e.getSelectKey(), e.getSelectValue())),
    /* 小于 */
    LT(e -> e.getSpecWrapper().eq(e.getSelectKey(), e.getSelectValue())),
    /*大于等于 */
    GTE(e -> e.getSpecWrapper().eq(e.getSelectKey(), e.getSelectValue())),
    /* 小于等于 */
    LTE(e -> e.getSpecWrapper().eq(e.getSelectKey(), e.getSelectValue())),
    /* 等于空值 */
    ISNULL(e -> e.getSpecWrapper().eq(e.getSelectKey(), e.getSelectValue())),
    /* 空值 */
    ISNOTNULL(e -> e.getSpecWrapper().eq(e.getSelectKey(), e.getSelectValue())),
    /* in */
    IN(e -> e.getSpecWrapper().in(e.getSelectKey(), (Collection<?>) e.getSelectValue())),
    /* not in  */
    NOT_IN(e -> e.getSpecWrapper().notIn(e.getSelectKey(), (Collection<?>) e.getSelectValue()))
    ;


    private Consumer<OperatorWrapper> consumer;
    SQLOperatorWrapper(Consumer<OperatorWrapper> consumer){
        this.consumer = consumer;
    }
    public Consumer<OperatorWrapper> consumer(){
        return this.consumer;
    }
}

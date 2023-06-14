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
public enum SQLOperatorWrapper {

    /* 等于 */
    EQ(e -> e.getSpecWrapper().eq( e.getSelectKey(), e.getSelectValue())),
    /* 不相等 */
    NE(e -> e.getSpecWrapper().ne( e.getSelectKey(), e.getSelectValue())),
    /*  模糊 必须 string */
    LIKE(e -> e.getSpecWrapper().likes( e.getSelectKey(), (String) e.getSelectValue())),
    /**
     * 不包含(not like) 必须 string
     */
    NOTLIKE(e -> e.getSpecWrapper().nlike( e.getSelectKey(), (String) e.getSelectValue())),
    /*  左模糊(%value) 必须 string  */
    LLIKE(e -> e.getSpecWrapper().llike( e.getSelectKey(), (String) e.getSelectValue())),
    /* 右模糊(value%) 必须 string  */
    RLIKE(e -> e.getSpecWrapper().rlike( e.getSelectKey(), (String) e.getSelectValue())),
    /* 大于 */
    GT(e -> e.getSpecWrapper().ge( e.getSelectKey(), e.getCompareValue())),
    /* 小于 */
    LT(e -> e.getSpecWrapper().lt( e.getSelectKey(), e.getCompareValue())),
    /*大于等于 */
    GTE(e -> e.getSpecWrapper().ge(e.getSelectKey(), e.getCompareValue())),
    /* 小于等于 */
    LTE(e -> e.getSpecWrapper().le(e.getSelectKey(), e.getCompareValue())),
    /* 等于空值 */
    ISNULL(e -> e.getSpecWrapper().isNull( e.getSelectKey())),
    /* 空值 */
    ISNOTNULL(e -> e.getSpecWrapper().isNull(e.getSelectKey())),
    /* in */
    IN(e -> e.getSpecWrapper().in(e.getSelectKey(), (Collection<?>) e.getSelectValue())),
    /* not in  */
    NOT_IN(e -> e.getSpecWrapper().notIn(e.getSelectKey(), (Collection<?>) e.getSelectValue())),

    /* BETWEEN,值必须是逗号隔开的的字符串  */
    BETWEEN(e -> {
        String string = e.getSelectValue().toString();
        String[] split = string.split(",");
        e.getSpecWrapper().between( e.getSelectKey(), split[0], split[1]);
    }),
    ;


    private final Consumer<OperatorWrapper> consumer;

    SQLOperatorWrapper(Consumer<OperatorWrapper> consumer) {
        this.consumer = consumer;
    }

    public Consumer<OperatorWrapper> consumer() {
        return this.consumer;
    }
}

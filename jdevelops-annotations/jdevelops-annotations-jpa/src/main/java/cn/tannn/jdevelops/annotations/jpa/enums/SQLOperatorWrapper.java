package cn.tannn.jdevelops.annotations.jpa.enums;


import cn.tannn.jdevelops.annotations.jpa.specification.OperatorWrapper;
import cn.tannn.jdevelops.annotations.jpa.specification.SpecificationWrapper;

import java.util.function.BiConsumer;

/**
 * 关系运输符
 *
 * <p> isConnect true用and(默认), fales用or
 *
 * @author tn
 * @date 2021-12-08 16:53
 */
public enum SQLOperatorWrapper {

    /* 等于 */
    EQ((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.eq(e.getSelectKey(), e.getSelectValue()));
        } else {
            specWrapper.eq(e.getSelectKey(), e.getSelectValue());
        }
    }),
    /* 不相等 */
    NE((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.ne(e.getSelectKey(), e.getSelectValue()));
        } else {
            specWrapper.ne(e.getSelectKey(), e.getSelectValue());
        }
    }),
    /*  模糊 必须 string */
    LIKE((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.likes(e.getSelectKey(), (String) e.getSelectValue()));
        } else {
            specWrapper.likes(e.getSelectKey(), (String) e.getSelectValue());
        }
    }),

    /* 不包含(not like) 必须 string */
    NOTLIKE((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.nlike(e.getSelectKey(), (String) e.getSelectValue()));
        } else {
            specWrapper.nlike(e.getSelectKey(), (String) e.getSelectValue());
        }

    }),
    /* 左模糊(%value) 必须 string  */
    LLIKE((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.llike(e.getSelectKey(), (String) e.getSelectValue()));
        } else {
            specWrapper.llike(e.getSelectKey(), (String) e.getSelectValue());
        }
    }),
    /* 右模糊(value%) 必须 string  */
    RLIKE((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.rlike(e.getSelectKey(), (String) e.getSelectValue()));
        } else {
            specWrapper.rlike(e.getSelectKey(), (String) e.getSelectValue());
        }
    }),
    /* 大于 */
    GT((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.ge(e.getSelectKey(), e.getCompareValue()));
        } else {
            specWrapper.ge(e.getSelectKey(), e.getCompareValue());
        }
    }),
    /* 小于 */
    LT((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.lt(e.getSelectKey(), e.getCompareValue()));
        } else {
            specWrapper.lt(e.getSelectKey(), e.getCompareValue());
        }
    }),
    /*大于等于 */
    GTE((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.ge(e.getSelectKey(), e.getCompareValue()));
        } else {
            specWrapper.ge(e.getSelectKey(), e.getCompareValue());
        }
    }),
    /* 小于等于 */
    LTE((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.le(e.getSelectKey(), e.getCompareValue()));
        } else {
            specWrapper.le(e.getSelectKey(), e.getCompareValue());
        }
    }),
    /* 等于空值 */
    ISNULL((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.isNull(e.getSelectKey()));
        } else {
            specWrapper.isNull(e.getSelectKey());
        }
    }),
    /* 空值 */
    ISNOTNULL((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.isNull(e.getSelectKey()));
        } else {
            specWrapper.isNull(e.getSelectKey());
        }
    }),
    /* in */
    IN((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.in(e.getSelectKey(), e.getSelectValue()));
        } else {
            specWrapper.in(e.getSelectKey(), e.getSelectValue());
        }
    }),
    /* not in  */
    NOT_IN((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.notIn(e.getSelectKey(), e.getSelectValue()));
        } else {
            specWrapper.notIn(e.getSelectKey(), e.getSelectValue());
        }
    }),

    /* BETWEEN,值必须是逗号隔开的的字符串  */
    BETWEEN((e, isConnect) -> {
        SpecificationWrapper<?> specWrapper = e.getSpecWrapper();
        String string = e.getSelectValue().toString();
        String[] split = string.split(",");
        if (isConnect != null && !isConnect) {
            specWrapper.or(or -> or.between(e.getSelectKey(), split[0], split[1]));
        } else {
            e.getSpecWrapper().between(e.getSelectKey(), split[0], split[1]);
        }
    }),


    ;


    /**
     * 运算符号(操作的数据)
     * <p> OperatorWrapper 数据
     * <p> Boolean true用and(默认), fales用or
     */
    private final BiConsumer<OperatorWrapper, Boolean> consumer;


    /**
     * 运算符号(操作的数据)
     * <p> OperatorWrapper 数据
     * <p> Boolean true用and(默认), fales用or
     *
     * @param consumer BiConsumer
     */
    SQLOperatorWrapper(BiConsumer<OperatorWrapper, Boolean> consumer) {
        this.consumer = consumer;
    }

    /**
     * 运算符号(操作的数据)
     * <p> OperatorWrapper 数据
     * <p> Boolean true用and(默认), fales用or
     */
    public BiConsumer<OperatorWrapper, Boolean> consumer() {
        return this.consumer;
    }

}

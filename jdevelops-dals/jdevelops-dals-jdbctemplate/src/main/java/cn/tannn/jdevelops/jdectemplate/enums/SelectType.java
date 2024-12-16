package cn.tannn.jdevelops.jdectemplate.enums;

/**
 * 查询方式
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/12/12 16:50
 */
public enum SelectType {
    /**
     * list查询
     */
    LIST,
    /**
     * 分页查询
     */
    PAGE,

    /**
     * MAP
     */
    MAP,

    /**
     * 数组  这种[1,2,3]
     */
    ARRAYS,

    /**
     * 只有一个数据，如： 就返回一个 数字，字符串 [ARRAYS脱掉组只留一个]
     */
    ONLY;

}

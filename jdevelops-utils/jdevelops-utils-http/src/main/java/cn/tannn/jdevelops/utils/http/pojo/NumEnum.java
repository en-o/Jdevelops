package cn.tannn.jdevelops.utils.http.pojo;

/**
 * 数字枚举
 * @author tn
 * @version 1
 * @date 2020/12/14 16:55
 */
public enum NumEnum {
    /** 0 */
    ZERO(0,"0"),
    /** 1 */
    ONE(1,"1"),
    /** 2 */
    TWO(2,"2"),
    /** 3 */
    THREE(3,"3"),
    /** 4 */
    FOUR(4,"4"),
    /** 5 */
    FIVE(5,"5"),
    /** 6 */
    SIX(6,"6"),
    /** 7 */
    SEVEN(7,"7"),
    /** 8 */
    EIGHT(8,"8"),
    /** 9 */
    NINE(9,"9"),
    /** 10 */
    TEN(10,"10"),
    /** 16 */
    SIX_TEN(16,"16"),
    /** 32 */
    THIRTY_TWO(32,"32"),
    /** 100 */
    HUNDRED(100,"100"),

    ;
    private final Integer num;
    private final String remark;

    NumEnum(Integer num, String remark) {
        this.num = num;
        this.remark = remark;
    }

    public Integer getNum() {
        return num;
    }

    public String getRemark() {
        return remark;
    }
}

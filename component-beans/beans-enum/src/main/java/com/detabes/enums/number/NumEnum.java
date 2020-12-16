package com.detabes.enums.number;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tn
 * @version 1
 * @ClassName NumEnum
 * @description 数字枚举
 * @date 2020/12/14 16:55
 */
@Getter
@AllArgsConstructor
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
    TEN(10,"10");
    private Integer num;
    private String remark;
}

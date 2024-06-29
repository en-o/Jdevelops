package cn.tannn.jdevelops.utils.core.string.enums;


/**
 * 字符串枚举
 * @author tn
 * @version 1
 * @date 2020/12/14 17:04
 */
public enum StringEnum {

    /** null串 */
    NULL_STRING("null","null串"),
    /** 空串 */
    LENGTH_ZERO(" ","空串"),
    /** empty串 */
    EMPTY_STRING("empty","empty");


    private final String code;
    private final String remark;

    StringEnum(String code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }
}

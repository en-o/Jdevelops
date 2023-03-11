package cn.jdevelops.jap.util;


import cn.jdevelops.enums.string.StringEnum;

/**
 * 对象工具类
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-11-11 11:39
 */
public class IObjects {


    /**
     * 不等于空
     * @param obj obj
     * @return  boolean
     */
    public static boolean nonNull(Object obj) {
        return obj != null && obj !="";
    }

    /**
     * 等于空
     * @param obj  obj
     * @return boolean
     */
    public static boolean isNull(Object obj) {
        return obj == null || obj == "" ;
    }


    /**
     * 判空包括 “null”
     * @param value 值
     * @return true 空
     */
    public static boolean isaBoolean(Object value) {
        return isNull(value) || StringEnum.NULL_STRING.getCode().equals(value);
    }



    /**
     * 字符判空
     * @param idFieldName CharSequence
     * @return boolean
     */
    public static boolean isBlank(final CharSequence idFieldName) {
        int strLen;
        if (idFieldName == null || StringEnum.NULL_STRING.getCode().contentEquals(idFieldName) || (strLen = idFieldName.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(idFieldName.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

package cn.jdevelops.data.jap.util;


import java.util.Objects;

/**
 * 对象工具类
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-11-11 11:39
 */
public class IObjects {


    /** null串 */
   private final static String NULL_STRING = "null";
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
        return obj == null || obj == "" || obj ==" ";
    }

    /**
     * 等于空
     * @param obj  obj
     * @param ignoreNullEnhance ignoreNull = true 有效 [true: 值是否为<null,""," ">，false: 值是否为 null]
     * @return boolean
     */
    public static boolean isNull(Object obj, Boolean ignoreNullEnhance) {
        if(Boolean.TRUE.equals(ignoreNullEnhance)){
            return obj == null || obj == "" || obj ==" ";
        }else {
            return Objects.isNull(obj);
        }

    }


    /**
     * 判空包括 “null”
     * @param value 值
     * @return true 空
     */
    public static boolean isaBoolean(Object value) {
        return isNull(value) || NULL_STRING.equals(value);
    }



    /**
     * 字符判空
     * @param cs CharSequence
     * @return boolean
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || NULL_STRING.contentEquals(cs) || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

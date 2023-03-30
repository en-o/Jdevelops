package cn.jdevelops.util.interceptor.util;


/**
 * @author tan
 */
public class StrUtil {


    /**
     * 空
     */
    public  static boolean isNull(StringBuilder sb){
        return sb.length() <= 0
                || "null".equals(sb.toString())
                || "".equals(sb.toString());
    }


    /**
     * 空
     */
    public  static boolean isNull(CharSequence cs){
        int strLen = length(cs);
        if (strLen == 0) {
            return true;
        } else {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }
}

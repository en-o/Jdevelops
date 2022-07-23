package cn.jdevelops.springs.service.util;

/**
 * 公共类
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-23 23:57
 */
public class StringUtil {
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }


    public static boolean isBlank(CharSequence cs) {
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

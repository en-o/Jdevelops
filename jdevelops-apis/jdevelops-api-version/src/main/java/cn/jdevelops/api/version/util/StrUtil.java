package cn.jdevelops.api.version.util;

/**
 * @author tnnn
 * @version V1.0
 * @date 2023-03-29 19:58
 */
public class StrUtil {


    /**
     * 字符串判断不是空
     * @param cs 字符串
     * @return true 非空
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }



    /**
     * 字符串判断空
     * @param cs 字符串
     * @return true 空
     */
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


    /**
     * 字符串长度
     * @param cs 字符串
     * @return 长度
     */
    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }
}

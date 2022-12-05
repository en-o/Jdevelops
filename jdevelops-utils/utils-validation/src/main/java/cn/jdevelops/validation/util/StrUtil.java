package cn.jdevelops.validation.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * str
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-12-05 15:16
 */
public class StrUtil {

    /**
     * 判空
     * @param str str
     * @return true不为空
     */
    public static boolean hasText(CharSequence str) {
        return str != null && str.length() > 0 && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for(int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }


    /**
     * 正则验证
     * @param pattern 正则
     * @param str 待验证的字符串
     * @return true验证通过
     */
    public static boolean verifyRegular(final Pattern pattern, final CharSequence str) {
        Matcher m = pattern.matcher(str);
        return m.matches();
    }
}



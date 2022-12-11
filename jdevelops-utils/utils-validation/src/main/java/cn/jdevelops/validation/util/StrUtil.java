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
     * 字符串常量：空字符串 {@code ""}
     */
    public static final String EMPTY = "";

    /**
     * 字符常量：空格符 {@code ' '}
     */
    public static final char SPACE = ' ';

    /**
     * 判空
     * @param str str
     * @return true不为空
     */
    public static boolean hasText(CharSequence str) {
        return str != null && str.length() > 0 && containsText(str);
    }

    /**
     * 判空
     * @param str str
     * @return true 空
     */
    public static boolean isBlank(CharSequence str) {
        return str == null || str.length() == 0 || !containsText(str);
    }



    /**
     * 非空
     * @param str CharSequence
     * @return true不空
     */
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


    /**
     * 替换指定位置的字符为*
     * @param str 字符串
     * @param start 开始位置（包含） 从1开始
     * @param end 结束位置（不包含） 从1开始
     * @return String
     */
    public static String replace(String str,int start,int end){
        StringBuilder replacedStr = new StringBuilder("*");
        // 不能为空
        if (StrUtil.isBlank(str)) {
            return StrUtil.EMPTY;
        }
        //需要截取的长度不能大于身份证号长度
        if ((start + end) > str.length()) {
            return StrUtil.EMPTY;
        }
        //需要截取的不能小于0
        if (start < 0 || end < 0) {
            return StrUtil.EMPTY;
        }
        for (int i = 1; i < (str.length() - (start+end) ); i++) {
            replacedStr.append("*");
        }
        StringBuilder sb = new StringBuilder(str);

        sb.replace(start, str.length() - end, replacedStr.toString());
        return sb.toString();
    }


    /**
     * 替换指定位置的字符为*
     * @param str 字符串
     * @param start 开始位置（包含） （结束位置=后面全部）
     * @return String
     */
    public static String replace(String str,int start){
        return replace(str, start, 0);
    }
}



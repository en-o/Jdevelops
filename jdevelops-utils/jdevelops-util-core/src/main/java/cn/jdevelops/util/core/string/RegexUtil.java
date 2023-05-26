package cn.jdevelops.util.core.string;

import java.util.regex.Pattern;

public class RegexUtil {


    /**
     * 正则匹配 (忽略大小写)
     * @param regex  正则表达式
     * @param content 字符串内容
     * @param flags  Pattern.I|Pattern.M
     * @return 正则为null或者""则不检查，返回true，内容为null返回false
     */
    public static boolean isMatcher(String regex, CharSequence content, int flags) {
        if (content == null) {
            // 提供null的字符串为不匹配
            return false;
        }
        if (isEmpty(regex)) {
            // 正则不存在则为全匹配
            return true;
        }
        // 创建Pattern对象
        Pattern pattern = Pattern.compile(regex, flags);
        return pattern.matcher(content).find();
    }

    /**
     * 正则匹配 (忽略大小写)
     * @param regex  正则表达式
     * @param content 字符串内容
     * @return 正则为null或者""则不检查，返回true，内容为null返回false
     */
    public static boolean isMatcherIgnore(String regex, CharSequence content) {
        // 忽略大小写
        return isMatcher(regex, content, Pattern.CASE_INSENSITIVE);
    }


    /**
     * 字符串简单判空
     * @param str str
     * @return boolean
     */
    private static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
}

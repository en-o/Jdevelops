package cn.jdevelops.mybatis.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author lmz
 * @date 2020/8/7  17:05
 */
public class CamlCaseUtil {
    /**
     * description:下划线转驼峰
     *
     * @param line       line
     * @param smallCamel smallCamel
     * @return java.lang.String
     * @author lmz
     * @date 2020/8/7  17:05
     */
    public static String toCamlCase(String line, boolean smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Pattern pattern = compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) :
                    Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * description: 驼峰转下划线
     *
     * @param str str
     * @return java.lang.String
     * @author lmz
     * @date 2020/8/7  17:08
     */
    public static String toLine(String str) {
        Pattern humpPattern = compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        String string = sb.toString();
        if(str.substring(0, 1).toCharArray()[0]>='A'&&str.substring(0, 1).toCharArray()[0]<='Z'){
            string = string.substring(1);
        }
        return string;
    }

    public static String replaceAll(String str, String replacement) {
        String regEx = "[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
        Pattern p = Pattern.compile(regEx);
        //这里把想要替换的字符串传进来
        Matcher m = p.matcher(str);

        return m.replaceAll(replacement).trim();
    }
}

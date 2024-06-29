package cn.tannn.jdevelops.utils.core.string;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {


    /**
     * 正则匹配 (忽略大小写)
     * @param regex  正则表达式
     * @param content 字符串内容
     * @param flags  常用：Pattern.CASE_INSENSITIVE @see <a href="https://zq99299.github.io/java-tutorial/essential/regex/pattern.html">...</a>
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




    /**
     * 不同之处具体看单元测试
     *  提取content 中 {} 里的数据并计算出现次数
     * @param content content
     * @param ignore 忽略单词
     * @return  存在且返回  key:关键字，value:出现次数
     */
    public static List<Map<String,Integer>> extractBracesList(String content, String... ignore){
        String regex = "\\{([^\\}]*?)\\}";
        // 创建Pattern对象
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        List<Map<String,Integer>> list = new ArrayList<>();
        while (matcher.find()){
            String trim = matcher.group(1).trim();
            if(!ArrayUtils.contains(ignore, trim)){
                list.add(new HashMap<String,Integer>(2){{
                    put(trim,StringNumber.countOccurrences(content,trim));
                }});
            }
        }
        return list;
    }


    /**
     * 不同之处具体看单元测试
     * 提取content 中 {} 里的数据并计算出现次数
     * @param content content
     * @param ignore 忽略单词
     * @return  存在且返回  key:关键字，value:出现次数
     */
    public static  Map<String,Integer> extractBracesMap(String content, String... ignore){
        String regex = "\\{([^\\}]*?)\\}";
        // 创建Pattern对象
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        Map<String,Integer> hashMap = new HashMap<>();
        while (matcher.find()){
            String trim = matcher.group(1).trim();
            if(!ArrayUtils.contains(ignore, trim)){
                hashMap.put(trim,StringNumber.countOccurrences(content,trim));
            }
        }
        return hashMap;
    }
}

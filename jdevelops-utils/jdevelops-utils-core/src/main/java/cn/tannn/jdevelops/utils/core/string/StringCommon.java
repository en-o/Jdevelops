package cn.tannn.jdevelops.utils.core.string;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tn
 * @version 1
 * @date 2021/2/20 10:27
 */
public class StringCommon {
    public static final String REGEX_EXTRACT_IP = "(^|[^\\d])(((2[0-4]\\d|25[0-5]|1\\d\\d|\\d\\d?)\\.){3}(2[0-4]\\d|25\\d|1\\d\\d|\\d\\d?))";
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * list转string 逗号隔开
     * @param list list
     * @return String
     */
    public static String listToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (String string : list) {
            result.append(string);
            result.append(",");
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * 转换系统对应的的地址分隔符
     *
     * @param path path
     * @return String
     */
    public static String getRealFilePath(String path) {
        return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
    }

    /**
     * 首字母大写
     * @param name 字符
     * @return String
     */
    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    /**
     * 提取字符串中的IP
     *  <a href="https://www.cnblogs.com/liugangmestery/p/7281994.html">参考</a>
     * @param line 字符串
     * @return ips
     */
    public static List<String> extractIp(String line) {
        List<String> ret = new ArrayList<>();
        if (line != null) {
            Pattern p = Pattern.compile(REGEX_EXTRACT_IP);
            Matcher m = p.matcher(line);
            while (m.find()) {
                ret.add(m.group(2));
            }
        }
        return ret;
    }

}

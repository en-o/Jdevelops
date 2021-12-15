package cn.jdevelops.string;

import java.util.List;

/**
 * @author tn
 * @version 1
 * @date 2021/2/20 10:27
 */
public class StringCommon {

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

}

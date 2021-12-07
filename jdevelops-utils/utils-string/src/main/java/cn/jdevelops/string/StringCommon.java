package cn.jdevelops.string;

import java.util.List;

/**
 * @author tn
 * @version 1
 * @date 2021/2/20 10:27
 */
public class StringCommon {

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
}

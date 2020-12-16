package com.detabes.websocket.core.util;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tn
 * @version 1
 * @ClassName SocketUtil
 * @description
 * @date 2020/12/16 13:14
 */
public class SocketUtil {

    /**
     * 从map中查询想要的map项，根据key
     */
    public static<T> Map<String, T> parseMapForFilter(Map<String, T> map, String filters) {
        if (map == null) {
            return null;
        } else {
            map = map.entrySet().stream()
                    .filter((e) -> checkKey(e.getKey(),filters))
                    .collect(Collectors.toMap(
                            (e) -> (String) e.getKey(),
                            (e) -> e.getValue()
                    ));
        }
        return map;
    }

    /**
     * 通过indexof匹配想要查询的字符
     */
    private static boolean checkKey(String key, String filters) {
        if (key.indexOf(filters)>-1){
            return true;
        }else {
            return false;
        }
    }
}

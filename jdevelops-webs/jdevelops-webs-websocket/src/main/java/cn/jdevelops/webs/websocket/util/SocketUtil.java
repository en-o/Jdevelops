package cn.jdevelops.webs.websocket.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.jdevelops.webs.websocket.CommonConstant.VERIFY_PATH_NO;
import static cn.jdevelops.webs.websocket.CommonConstant.VERIFY_PATH_YES;

/**
 * SocketUtil
 * @author tn
 * @version 1
 * @date 2020/12/16 13:14
 */
public class SocketUtil {

    private static Logger logger = LoggerFactory.getLogger(SocketUtil.class);

    /**
     * 从map中查询想要的map项，根据key
     */
    public static<T> Map<String, T> parseMapForFilter(Map<String, T> map, String filters) {
        if (map == null) {
            return Collections.emptyMap();
        } else {
            map = map.entrySet().stream()
                    .filter(e -> checkKey(e.getKey(),filters))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue
                    ));
        }
        return map;
    }

    /**
     * 通过indexof匹配想要查询的字符
     */
    private static boolean checkKey(String key, String filters) {
        return key.contains(filters);
    }


    /**
     * 禁止连接
     * @param socketPath socket 前缀路径
     * @return true 禁止连接
     */
    public static boolean banConnection(String socketPath){
        if(socketPath.contains(VERIFY_PATH_NO)||socketPath.contains(VERIFY_PATH_YES)){
            return false;
        }
        logger.error( socketPath + "路径不合法，只允许\"/socket/y/\",\"/socket/n/\"");
        return true;
    }
}

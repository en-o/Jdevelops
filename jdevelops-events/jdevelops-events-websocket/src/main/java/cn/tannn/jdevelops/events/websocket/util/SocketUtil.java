package cn.tannn.jdevelops.events.websocket.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.tannn.jdevelops.events.websocket.core.CommonConstant.VERIFY_PATH_NO;
import static cn.tannn.jdevelops.events.websocket.core.CommonConstant.VERIFY_PATH_YES;


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
     * key: name
     * value: session
     */
    public static Map<String, List<Session>> parseMapForFilter(Map<String, List<Session>> map, String filters) {
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
     * 连接路径验证，只能连接默认的两种前缀
     * @param socketPath socket 前缀路径
     * @param verifyPathNo false 关闭 VERIFY_PATH_NO 前缀【即所有只允许y存在】
     * @return true 禁止连接
     */
    public static boolean banConnection(String socketPath, boolean verifyPathNo){
        if(!verifyPathNo && socketPath.contains(VERIFY_PATH_NO)){
            logger.error( socketPath + "不需要验证登录的socket请求被禁止，只允许\"/socket/y/\"");
            return true;
        }else if(socketPath.contains(VERIFY_PATH_NO)||socketPath.contains(VERIFY_PATH_YES)){
            return false;
        }else {
            logger.error( socketPath + "路径不合法，只允许\"/socket/y/\",\"/socket/n/\"");
            return true;
        }
    }
}

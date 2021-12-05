package cn.jdevelop.map.core.map;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * map字符串
 * @author tn
 * @version 1
 * @date 2020/12/22 12:31
 */
public class MapStringUtil {

    /**
     * map拆解 拼接成 xx=xx&xx=xx
     * @param map map
     * @return String
     */
    public static String mapOrderStr(Map<String, Object> map) {
        ArrayList<Map.Entry<String, Object>> list = new ArrayList<>(map.entrySet());

        list.sort(Map.Entry.comparingByKey());

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> mapping : list) {
            sb.append(mapping.getKey()).append("=").append(mapping.getValue()).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }
    /**
     *  处理中文转义
     * map拆解 拼接成 xx=xx&xx=xx
     * @param map map
     * @return String
     */
    public static String mapOrderStrFixURLEncoder(Map<String, Object> map) throws UnsupportedEncodingException {
        ArrayList<Map.Entry<String, Object>> list = new ArrayList<>(map.entrySet());

        list.sort(Map.Entry.comparingByKey());

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> mapping : list) {
            sb.append(mapping.getKey()).append("=").append(URLEncoder.encode(String.valueOf(mapping.getValue()), "UTF-8")).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     *
     * Map转String
     * @param map map
     * @return String
     */
    public static String getMapToString(Map<String,Object> map){
        Set<String> keySet = map.keySet();
        //将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[0]);
        //给数组排序(升序)
        Arrays.sort(keyArray);
        //因为String拼接效率会很低的，所以转用StringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            // 参数值为空，则不参与签名 这个方法trim()是去空格
            if ((String.valueOf(map.get(keyArray[i]))).trim().length() > 0) {
                sb.append(keyArray[i]).append(":").append(String.valueOf(map.get(keyArray[i])).trim());
            }
            if(i != keyArray.length-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }
}

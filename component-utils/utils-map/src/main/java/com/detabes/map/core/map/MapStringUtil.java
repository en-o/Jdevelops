package com.detabes.map.core.map;

import java.util.*;

/**
 * @author tn
 * @version 1
 * @ClassName MapStringUtil
 * @description map字符串
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

        Collections.sort(list, Comparator.comparing(Map.Entry::getKey));

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> mapping : list) {
            sb.append(mapping.getKey() + "=" + mapping.getValue() + "&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     *
     * Map转String
     * @param map
     * @return
     */
    public static String getMapToString(Map<String,Object> map){
        Set<String> keySet = map.keySet();
        //将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
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

package com.detabes.apisign.util;

import com.detabes.encryption.constant.SignConstant;
import com.detabes.encryption.core.SignMD5Util;

import java.util.ArrayList;
import java.util.Map;

import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * @author tn
 * @version 1
 * @ClassName SignUtil
 * @description 签名用
 * @date 2020/12/22 10:20
 */
public class SignUtil {

    /**
     * map 转 str
     * @param map map)
     * @return String eg: xx=ss&yy=xx
     */
    public static String map2Str(Map<String, Object> map) {
        ArrayList<Map.Entry<String, Object>> list = new ArrayList<>(map.entrySet());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> mapping : list) {
            sb.append(mapping.getKey() + "=" + mapping.getValue() + "&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     *  把需要加密的 map参数放进来用  JSONObject.toJSONString 处理后换取sign
     * @param maps 参数map
     * @return sign
     */
    public static <T extends Map,LinkedHashMap> String getSignByMap2Json(T maps){
        String encrypt1 = SignMD5Util.encrypt(toJSONString(maps),true);
        return SignMD5Util.encrypt(encrypt1+ SignConstant.MD5_PRIVATE_KEY, true);
    }


    /**
     *  把需要加密的 map参数放进来用  map2Str 处理后换取sign
     * @param maps 参数map
     * @return sign
     */
    public static <T extends Map,LinkedHashMap> String getSignByMap2Str(T maps){
        String encrypt1 = SignMD5Util.encrypt(map2Str(maps),true);
        return SignMD5Util.encrypt(encrypt1+ SignConstant.MD5_PRIVATE_KEY, true);
    }

    /**
     *
     * @param jsonStr 传入json数据串换取sign
     * @return sign
     */
    public static String getSignByJson(String jsonStr){
        String encrypt1 = SignMD5Util.encrypt(jsonStr,true);
        return SignMD5Util.encrypt(encrypt1+SignConstant.MD5_PRIVATE_KEY, true);
    }


    /**
     *
     * @param map2Str 传入map2Str数据串换取sign
     * @return sign
     */
    public static String getSignBymap2Str(String map2Str){
        String encrypt1 = SignMD5Util.encrypt(map2Str,true);
        return SignMD5Util.encrypt(encrypt1+SignConstant.MD5_PRIVATE_KEY, true);
    }

}

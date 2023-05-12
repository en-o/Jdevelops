package cn.jdevelops.api.sign.util;

import cn.jdevelops.api.result.util.SpringBeanUtils;
import cn.jdevelops.api.sign.config.ApiSignConfig;
import cn.jdevelops.util.encryption.core.SignMD5Util;
import cn.jdevelops.util.encryption.core.SignShaUtil;
import com.alibaba.fastjson2.JSON;

import java.util.ArrayList;
import java.util.Map;

/**
 * 签名用
 *
 * @author tn
 * @version 1
 * @date 2020/12/22 10:20
 */
public class SignUtil {

    /**
     * map 转 str
     *
     * @param map map)
     * @return String eg: xx=ss&yy=xx
     */
    public static String map2Str(Map<String, Object> map) {
        ArrayList<Map.Entry<String, Object>> list = new ArrayList<>(map.entrySet());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> mapping : list) {
            sb.append(mapping.getKey()).append("=").append(mapping.getValue()).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 把需要加密的 map参数放进来用  JSONObject.toJSONString 处理后换取sign
     *
     * @param maps 参数map
     * @return sign
     */
    public static <T extends Map> String getMd5SignByMap2Json(T maps) {
        String encrypt1 = SignMD5Util.encrypt(JSON.toJSONString(maps), true);
        return getMd5Sign(encrypt1);
    }


    /**
     * 把需要加密的 map参数放进来用  map2Str 处理后换取sign
     *
     * @param maps 参数map
     * @return sign
     */
    public static <T extends Map> String getMd5SignByMap2Str(T maps) {
        String encrypt1 = SignMD5Util.encrypt(map2Str(maps), true);
        return getMd5Sign(encrypt1);
    }

    /**
     * @param jsonStr 传入json数据串换取sign
     * @return sign
     */
    public static String getMd5SignByJson(String jsonStr) {
        String encrypt1 = SignMD5Util.encrypt(jsonStr, true);
        return getMd5Sign(encrypt1);
    }

    /**
     * @param map2Str 传入map2Str数据串换取sign
     * @return sign
     */
    public static String getMd5SignBymap2Str(String map2Str) {
        String encrypt1 = SignMD5Util.encrypt(map2Str, true);
        return getMd5Sign(encrypt1);
    }

    /**
     * @param encrypt1 第一次加密的密钥
     * @return 真正的密钥
     */
    private static String getMd5Sign(String encrypt1) {
        return SignMD5Util.encrypt(encrypt1 + getSalt(), true);
    }

    /**
     * @param map2Str map2Str解析过的字符串
     * @return 真正的密钥
     */
    public static String getShaSign(String map2Str) {
        return SignShaUtil.encrypt(map2Str);
    }


    /**
     * 获取盐
     */
    public static String getSalt() {
        ApiSignConfig apiSignBean = SpringBeanUtils.getInstance().getBean(ApiSignConfig.class);
        return apiSignBean.getSalt();
    }

}

package cn.jdevelops.encryption.core;

import cn.jdevelops.encryption.constant.SignConstant;
import cn.jdevelops.encryption.util.RemarkUtil;
import cn.jdevelops.enums.number.NumEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;


/**
 *接口签名用
 * md5加密
 * @author tn
 * @version 1
 * @date 2020/6/9 14:30
 */
@Slf4j
public class SignMD5Util {



    public static String encrypt(String plainText) {
        return encrypt(plainText,true);
    }

    /**
     *  加密
     * @description  (16位或32位密码)
     * @param plainText 需要加密的字符
     * @param  flag true为32位,false为16位
     * @return 返回密钥
     */
    public static String encrypt(String plainText, boolean flag) throws RuntimeException {
        if (StringUtils.isEmpty(plainText)) {
            return null;
        }
        byte[] secretBytes ;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes(StandardCharsets.UTF_8)  );
        } catch (Exception e) {
            throw new RuntimeException("没有md5这个算法！",e);
        }
        // 16进制数字
        StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < NumEnum.THIRTY_TWO.getNum() - md5code.length(); i++) {
            md5code.insert(0, "0");
        }

        if (flag) {
            return md5code.toString();
        } else {
            return md5code.substring(8, 24);
        }
    }

    /**
     * 对 map 进行加密
     * @param obj obj
     * @return 返回字符串
     */
    @SuppressWarnings("unchecked")
    public static String encrypt(Object obj){
        if (obj != null) {
            Map<String, Object> map ;
            if (obj instanceof Map) {
                map = (Map<String, Object>) obj;
            } else {
                map = RemarkUtil.transBean2Map(obj);
            }
            return encrypt(map, true);
        } else {
            return null;
        }
    }

    /**
     * 16位或32位密码
     * @description  (16位或32位密码)
     * @param map 加密串
     * @param flag true为32位,false为16位
     * @return 返回密钥
     */
    public static String encrypt(Map<String, Object> map, boolean flag) {
        String param ;
        map.remove("sign");
        map.remove("encrypt");
        String result = RemarkUtil.map2Str(map);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        param = encrypt(encrypt(result)+SignConstant.MD5_PRIVATE_KEY);
        if (flag) {
            return param;
        } else {
            param = param.substring(8, 24);
        }
        return param;
    }


    /**
     * 16位或32位密码
     * @param result 加密串
     * @param flag true为32位,false为16位
     * @return 返回密钥
     */
    public static String encryptHeader(String result, boolean flag) {
        String param;
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        param = encrypt(encrypt(result)+ SignConstant.MD5_PRIVATE_KEY);
        if (flag) {
            return param;
        } else {
            param = param.substring(8, 24);
        }
        return param;
    }
    /**
     *  检查
     * @param obj 对象
     * @return 对错
     */
    @SuppressWarnings("unchecked")
    public static boolean check(Object obj){
        try {
            Map<String, Object> map;
            if(obj==null){
                return false;
            }
            if(obj instanceof Map){
                map=(Map<String, Object>) obj;
            } else{
                map = RemarkUtil.transBean2Map(obj);
            }
            String sign=(String)map.get("sign");
            if(StringUtils.isEmpty(sign)){
                return false;
            }
            String str=encrypt(map);
            return sign.equals(str);
        }catch (Exception e){
            throw new IllegalArgumentException("加密参数有误",e);
        }
    }



    /**
     *  检查
     * @param jsonString json params
     * @return 对错
     */
    public static boolean checkHeader(HttpServletRequest request, String jsonString){
        try {
            String sign = getHeaderSign(request);
            if(StringUtils.isEmpty(sign)){
                return false;
            }
            String str= encryptHeader(jsonString, true);

            return sign.equals(str);
        }catch (Exception e){
            throw new IllegalArgumentException("加密参数有误",e);
        }
    }



    /**
     * 从 request 获取token
     * @param request request
     * @return sign
     */
    public static String getHeaderSign(HttpServletRequest request) {
        final String signName = "sign";
        String sign = request.getHeader(signName);
        if (StringUtils.isNotBlank(sign)) {
            return sign;
        }
        sign = request.getParameter(signName);
        return sign;
    }

}

package cn.tannn.jdevelops.sign.util;

import cn.tannn.jdevelops.result.utils.StrUtils;
import cn.tannn.jdevelops.sign.exception.SignException;
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
public class SignMD5Util {

    private final static Integer THIRTY_TWO = 32;
    public static final String MD_5 = "md5";

    public static String encrypt(String plainText) {
        return encrypt(plainText,true);
    }

    /**
     *  加密 （(16位或32位密码)）
     * @param plainText 需要加密的字符
     * @param  flag true为32位,false为16位
     * @return 返回密钥
     */
    public static String encrypt(String plainText, boolean flag) {
        if (StringUtils.isEmpty(plainText)) {
            return null;
        }
        byte[] secretBytes ;
        try {
            secretBytes = MessageDigest.getInstance(MD_5).digest(
                    plainText.getBytes(StandardCharsets.UTF_8)  );
        } catch (Exception e) {
            throw new SignException("没有md5这个算法！",e);
        }
        // 16进制数字
        StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < THIRTY_TWO - md5code.length(); i++) {
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
     * @param salt 盐
     * @return 返回字符串
     */
    @SuppressWarnings("unchecked")
    public static String encrypt(Object obj,String salt){
        if (obj != null) {
            Map<String, Object> map ;
            if (obj instanceof Map) {
                map = (Map<String, Object>) obj;
            } else {
                map = SignUtil.transBean2Map(obj);
            }
            return encrypt(map, salt,true);
        } else {
            return null;
        }
    }

    /**
     * 16位或32位密码 （(16位或32位密码)）
     * @param map 加密串
     * @param salt  盐
     * @param flag true为32位,false为16位
     * @return 返回密钥
     */
    public static String encrypt(Map<String, Object> map,String salt, boolean flag) {
        String param ;
        map.remove("sign");
        map.remove("encrypt");
        String result = SignUtil.map2Str(map);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        param = encrypt(encrypt(result)+salt);
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
     * @param salt 盐
     * @param flag true为32位,false为16位
     * @return 返回密钥
     */
    public static String encryptHeader(String result, String salt, boolean flag) {
        String param;
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        param = encrypt(encrypt(result)+ salt);
        if (flag) {
            return param;
        } else {
            if(StrUtils.isBlank(param)){
                throw new SignException("encrypt error parameter is empty");
            }
            param = param.substring(8, 24);
        }
        return param;
    }
    /**
     *  检查
     * @param obj 对象
     * @param salt 盐
     * @return 对错
     */
    @SuppressWarnings("unchecked")
    public static boolean check(Object obj,String salt){
        try {
            Map<String, Object> map;
            if(obj==null){
                return false;
            }
            if(obj instanceof Map){
                map=(Map<String, Object>) obj;
            } else{
                map = SignUtil.transBean2Map(obj);
            }
            String sign=(String)map.get("sign");
            if(StringUtils.isEmpty(sign)){
                return false;
            }
            String str=encrypt(map,salt);
            return sign.equals(str);
        }catch (Exception e){
            throw new SignException("加密参数有误",e);
        }
    }



    /**
     *  检查
     * @param jsonString json params
     * @param salt 盐
     * @return 对错
     */
    public static boolean checkHeader(HttpServletRequest request, String jsonString,String salt){
        try {
            String sign = getHeaderSign(request);
            if(StringUtils.isEmpty(sign)){
                return false;
            }
            String str= encryptHeader(jsonString, salt,true);

            return sign.equals(str);
        }catch (Exception e){
            throw new SignException("加密参数有误",e);
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

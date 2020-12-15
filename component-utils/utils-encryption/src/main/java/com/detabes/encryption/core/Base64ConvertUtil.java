package com.detabes.encryption.core;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64工具
  * @author tn
  * @date  2020/8/14 11:50
  * @description
  */
public class Base64ConvertUtil {

    /**
     * 加密JDK1.8
     * @param str 字符串
     * @return 返回字符串
     * @throws UnsupportedEncodingException 抛出异常
     */
    public static String encode(String str) {
        byte[] encodeBytes = Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8));
        return new String(encodeBytes);
    }

    /**
     * 解密JDK1.8
     * @param str  字符串
     * @return 返回字符串
     * @throws UnsupportedEncodingException 抛出异常
     */
    public static String decode(String str) {
        byte[] decodeBytes = Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
        return new String(decodeBytes);
    }

}

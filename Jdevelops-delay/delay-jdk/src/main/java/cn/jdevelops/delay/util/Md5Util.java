package cn.jdevelops.delay.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-01-05 16:25
 */
public class Md5Util {
    /**
     * 利用java原生的摘要实现SHA256加密
     * @param str 待加密的字符串
     * @return SHA256字符串
     */
    public static String getSha256StrJava(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     * @param bytes bytes
     * @return 16进制
     */
    private static String byte2Hex(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuilder.append("0");
            }
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }
}

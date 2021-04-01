package com.detabes.encryption.core;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;

/**
 * @author tn
 * @className QrCode
 * @date 2021-02-05 13:35
 */
public class QrCodeAES {
    /**salt*/
    private final static String salt="detbes`2`f4asdf=";


    /**
     * AES加密
     */
    public static String encryptAES(String content) {
        /**
         * 生成密钥
         */
        byte[] key=salt.getBytes();
        // 构建
        AES aes = SecureUtil.aes(key);
        // 加密为16进制表示
       return aes.encryptHex(content);
    }

    /**
     * AES解密
     */
    public static String decryptAES(String encryptHex) {
        /**
         * 生成密钥
         */
        byte[] key=salt.getBytes();
        //构建
        AES aes = SecureUtil.aes(key);
        //解密为原字符串
       return aes.decryptStr(encryptHex);
    }
}

package cn.jdevelops.util.encryption.core;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;

/**
 * QrCode
 *
 * @author tn
 * @date 2021-02-05 13:35
 */
public class QrCodeAes {


    /**
     * AES加密
     * @param content 加密内容
     * @param salt 盐
     * @return 加密串
     */
    public static String encryptAes(String content, String salt) {
        /* 生成密钥 */
        byte[] key = salt.getBytes();
        // 构建
        AES aes = SecureUtil.aes(key);
        // 加密为16进制表示
        return aes.encryptHex(content);
    }

    /**
     * @param encryptHex 解密内容
     * @param salt 盐
     * @return 真实内容
     */
    public static String decryptAes(String encryptHex, String salt) {
        /* 生成密钥 */
        byte[] key = salt.getBytes();
        //构建
        AES aes = SecureUtil.aes(key);
        //解密为原字符串
        return aes.decryptStr(encryptHex);
    }
}

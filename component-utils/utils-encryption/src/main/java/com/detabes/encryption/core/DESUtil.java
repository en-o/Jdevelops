package com.detabes.encryption.core;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;


/**
 * 对称加密
 * @author tn
 * @version 1
 * @ClassName DESUtil
 * @description 对称加密
 * @date 2020/6/9 14:42
 */

public class DESUtil {


    /**
     * 密钥
     */
    private static final String SECRET_KEY = "123456789987654321";
    /**
     *  向量
     */
    private static final String IV = "ggboy123";
    /**
     * 加解密统一使用的编码方式
     */
    private static final String ENCODING = "utf-8";

    /**
     *  加密
     * @description TODO(加密)
     * @param plainText 加密串
     * @author tn
     * @date 2018年11月20日下午1:19:19
     * @return 返回密钥
     */
    public static String encode(String plainText){
        Key deskey ;
        DESedeKeySpec spec;
        try {
            spec = new DESedeKeySpec(SECRET_KEY.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            byte[] encryptData = cipher.doFinal(plainText.getBytes(ENCODING));
            return Base64Encoder.encode(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 解密
     * @description  TODO(解密)
     * @param encryptText 加密串
     * @author tn
     * @date 2018年11月20日下午1:19:37
     * @return 返回解密串
     */
    public static String decode(String encryptText){
        try{

            Key deskey ;
            DESedeKeySpec spec = new DESedeKeySpec(SECRET_KEY.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

            byte[] decryptData = cipher.doFinal(Base64Decoder.decode(encryptText));

            return new String(decryptData, ENCODING);
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
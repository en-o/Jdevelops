package cn.tannn.jdevelops.ddss.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.SecureRandom;

/**
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/15 15:01
 */
public class ObjectUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectUtils.class);


    /**
     * 判断一个对象是否是基本类型或基本类型的封装类型
     */
    public static boolean isPrimitive(Object obj) {
        try {
            return ((Class<?>) obj.getClass().getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * <p>Checks if a CharSequence is not empty (""), not null and not whitespace only.</p>
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank(" ")       = false
     * StringUtils.isNotBlank("bob")     = true
     * StringUtils.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is
     * not empty and not null and not whitespace only
     * @since 2.0
     * @since 3.0 Changed signature from isNotBlank(String) to isNotBlank(CharSequence)
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * <p>Checks if a CharSequence is empty (""), null or whitespace only.</p>
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace only
     * @since 2.0
     * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
     */
    public static boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * Gets a CharSequence length or {@code 0} if the CharSequence is
     * {@code null}.
     *
     * @param cs a CharSequence or {@code null}
     * @return CharSequence length or {@code 0} if the CharSequence is
     * {@code null}.
     * @since 2.4
     * @since 3.0 Changed signature from length(String) to length(CharSequence)
     */
    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }


    /**
     * 偏移量,可自行修改
     */
    private static final String IV_PARAMETER = "0392039203920301";

    /**
     * 解密
     *
     * @param code 带解密的密钥串
     * @param salt 解密的盐
     * @return 真实的信息(错误返回原文)
     */
    public static String decryptAES(String code, String salt) throws InvalidKeyException {
        if (salt == null || 16 != salt.length()) {
            throw new InvalidKeyException("salt必须满足16位且只能是16位");
        }
        try {
            byte[] raw = salt.getBytes(StandardCharsets.US_ASCII);
            SecretKeySpec skySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, IV_PARAMETER.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skySpec, gcmSpec);
            // 先用base64解密
            byte[] encryptedWithIv = Base64.decodeBase64(code);
            byte[] iv = new byte[12];
            byte[] encrypted = new byte[encryptedWithIv.length - 12];
            // 将一个数组[encryptedWithIv]中的元素复制到另一个数组[iv]中
            System.arraycopy(encryptedWithIv, 0, iv, 0, iv.length);
            System.arraycopy(encryptedWithIv, iv.length, encrypted, 0, encrypted.length);
            GCMParameterSpec gcmSpec2 = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, skySpec, gcmSpec2);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            LOG.error("解密失败", ex);
        }
        return code;
    }

    /**
     * 加密 - 注意每次加密都是不一样的，但是每个都可以解密
     *
     * @param code 带加密的字符串
     * @param salt 解密的盐(16位)
     * @return 加密信息(错误返回原文)
     */
    public static String encryptAES(String code, String salt) throws InvalidKeyException {
        if (salt == null || 16 != salt.length()) {
            throw new InvalidKeyException("salt必须满足16位");
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] raw = salt.getBytes();
            SecretKeySpec skySpec = new SecretKeySpec(raw, "AES");
            // 使用GCM模式，需要一个随机向量iv
            byte[] iv = new byte[12];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, skySpec, gcmSpec);
            byte[] encrypted = cipher.doFinal(code.getBytes(StandardCharsets.UTF_8));
            // 将IV和加密数据一起编码
            byte[] encryptedWithIv = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
            System.arraycopy(encrypted, 0, encryptedWithIv, iv.length, encrypted.length);
            // 此处使用BASE64做转码。
            return Base64.encodeBase64String(encryptedWithIv);
        } catch (Exception e) {
            LOG.error("加密失败", e);
        }
        return code;
    }

}

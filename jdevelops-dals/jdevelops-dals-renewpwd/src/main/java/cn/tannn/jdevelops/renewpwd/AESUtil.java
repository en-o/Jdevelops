package cn.tannn.jdevelops.renewpwd;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public final class AESUtil {

    /**
     * 16 字节的密钥，可替换为自己的
     * <p> java -DAES_ENCRYPTION_KEY=your16bytekey123 -jar app.jar  </p>
     * <p> Linux/Mac  export AES_ENCRYPTION_KEY=your16bytekey123 </p>
     * <p> Windows set AES_ENCRYPTION_KEY=your16bytekey123 </p>
     */
    private static final String KEY = System.getenv("AES_ENCRYPTION_KEY") != null
            ? System.getenv("AES_ENCRYPTION_KEY")
            : "2345678901abcdeg";

    private AESUtil() {}

    /**
     * 解密
     * @param base64CipherText Base64 编码的密文字符串
     * @return 原文字符串
     * @throws IllegalStateException 如果解密失败
     */
    public static String decrypt(String base64CipherText) {
        validateKey();
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(base64CipherText);
            byte[] original = cipher.doFinal(decoded);
            return new String(original);
        } catch (Exception e) {
            throw new IllegalStateException("AES 解密失败", e);
        }
    }

    /**
     * 加密
     * @param plainText 明文字符串
     * @return Base64 编码的密文字符串
     * @throws IllegalStateException 如果加密失败
     */
    public static String encrypt(String plainText) {
        validateKey();
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new IllegalStateException("AES 加密失败", e);
        }
    }

    private static void validateKey() {
        if (KEY == null || KEY.length() != 16) {
            throw new IllegalStateException("AES密钥必须是16字节长度");
        }
    }

    /* 仅用于本地生成加密字符串 */
    public static void main(String[] args) {
        String plain = "root";
        System.out.println("加密后: " + encrypt(plain));
        System.out.println("解密后: " + decrypt(encrypt(plain)));
    }
}

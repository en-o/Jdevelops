package cn.tannn.jdevelops.frameworks.web.starter.apiSecurity;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;


/**
 * 一定要选择16位密钥长度，也就是KEY_LENGTH=16*8
 * @author  https://blog.csdn.net/weixin_45973634/article/details/136022834
 */
public class ApiAESUtils {
    /**
     * 加密算法AES
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * key的长度，Wrong key size: must be equal to 128, 192 or 256
     * 传入时需要16、24、36
     */
    private static final int KEY_LENGTH = 16 * 8;

    /**
     * 算法名称/加密模式/数据填充方式
     * 默认：AES/ECB/PKCS5Padding
     */
    private static final String ALGORITHMS = "AES/ECB/PKCS5Padding";

    /**
     * 后端AES的key，由静态代码块赋值
     */
    public static String key;


    static {
        key = getKey();
    }

    /**
     * 获取key
     */
    public static String getKey() {
        int length = KEY_LENGTH / 8;
        StringBuilder uid = new StringBuilder(length);
        //产生32位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < length; i++) {
            //产生0-2的3位随机数
            switch (rd.nextInt(3)) {
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char) (rd.nextInt(26) + 65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char) (rd.nextInt(26) + 97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

    /**
     * AES 加密
     *
     * @param content    加密的字符串
     * @param encryptKey key值
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        //设置Cipher对象
        Cipher cipher = Cipher.getInstance(ALGORITHMS);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), KEY_ALGORITHM));

        //调用doFinal
        // 转base64
        return Base64.encodeBase64String(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));

    }

    /**
     * AES 解密
     *
     * @param encryptStr 解密的字符串
     * @param decryptKey 解密的key值
     */
    public static String decrypt(String encryptStr, String decryptKey) throws Exception {
        //base64格式的key字符串转byte
        byte[] decodeBase64 = Base64.decodeBase64(encryptStr);


        //设置Cipher对象
        Cipher cipher = Cipher.getInstance(ALGORITHMS);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), KEY_ALGORITHM));
        //调用doFinal解密
        return new String(cipher.doFinal(decodeBase64));

}
}

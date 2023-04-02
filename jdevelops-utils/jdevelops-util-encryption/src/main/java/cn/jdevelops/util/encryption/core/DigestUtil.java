package cn.jdevelops.util.encryption.core;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 摘要加密用
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-10-12 11:29
 */
public class DigestUtil {

    private static final String HEX_LOOKUP = "0123456789abcdef";

    /**
     * 需要 digest 验证时采用用的密码加密。
     * @param username 账户名
     * @param realm 领域（一般为域名之类的）
     * @param password 密码
     * @return 加密的密钥
     */
    public static String digestEncrypt(String username, String realm, String password){
        MessageDigest digest;
        String a1 = username + ":" + realm + ":" + password;
        try {
            digest = MessageDigest.getInstance("MD5");
        }catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        digest.reset();
        digest.update(a1.getBytes(StandardCharsets.UTF_8));
        return bytesToHexString(digest.digest());
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(HEX_LOOKUP.charAt((aByte & 0xF0) >> 4));
            sb.append(HEX_LOOKUP.charAt((aByte & 0x0F)));
        }
        return sb.toString();
    }
}

package cn.tannn.jdevelops.utils.jwt.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 加密
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-02-11 04:49
 */
public class EncryptionUtil {

    /**
     * springboot 自带MD5加密
     * @param str 待加密字符串
     * @return 16进制加密字符串
     */
    public static String encrypt2MD5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
    }
}

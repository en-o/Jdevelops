package cn.jdevelops.encryption.core;

import cn.jdevelops.util.encryption.core.DigestUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class DigestUtilTest {

    @Test
    public void digestEncrypt() {
        assertEquals("5a6b7335d79ac15ff284d5c0700969ce", DigestUtil.digestEncrypt("tan", "tannn.cn", "123"));
    }
    @Test
    public void digestEncrypt_online() {
        System.out.println(DigestUtil.digestEncrypt("administrator", "sureness_realm", "123"));
    }
}

package cn.tannn.jdevelops.ddss.util;


import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectUtilsTest {

    private static final String salt = "salt1231212qadqw";

    @Test
    public void testDecryptAES() throws InvalidKeyException {
        assertEquals(ObjectUtils.decryptAES("Lk6g7NuERgUwGRiM6dpdB5Wp27u+2dlnY1IIVWRf9CV2", salt),"hello");
        assertEquals(ObjectUtils.decryptAES("hyaSvU4Hv0s6GNKButON2zKOJqUkJK7tbPL8HN7Fm5D3", salt),"hello");
    }

    @Test
    public void testEncryptAES() throws InvalidKeyException {
        String hello = ObjectUtils.encryptAES("hello", salt);
        String decryptAES = ObjectUtils.decryptAES(hello, salt);
        assertEquals(decryptAES,"hello");
    }

}

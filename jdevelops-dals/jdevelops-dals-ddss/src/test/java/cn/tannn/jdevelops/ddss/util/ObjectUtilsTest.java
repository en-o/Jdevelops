package cn.tannn.jdevelops.ddss.util;


import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectUtilsTest {

    private static final String salt = "salt1231212qadqw";

    @Test
    public void testDecryptAES() throws InvalidKeyException {
        assertEquals(ObjectUtils.decryptAES("ly60culHXDlTLDmPaS8iag==", salt),"hello");
    }

    @Test
    public void testEncryptAES() throws InvalidKeyException {
        assertEquals(ObjectUtils.encryptAES("hello", salt),"ly60culHXDlTLDmPaS8iag==");
    }

}

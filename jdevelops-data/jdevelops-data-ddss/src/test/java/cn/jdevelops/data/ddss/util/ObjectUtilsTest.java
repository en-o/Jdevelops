package cn.jdevelops.data.ddss.util;

import junit.framework.TestCase;

import java.security.InvalidKeyException;

public class ObjectUtilsTest extends TestCase {

    private static final String salt = "salt1231212qadqw";

    public void testDecryptAES() throws InvalidKeyException {
        assertEquals(ObjectUtils.decryptAES("ly60culHXDlTLDmPaS8iag==", salt),"hello");
    }

    public void testEncryptAES() throws InvalidKeyException {
        assertEquals(ObjectUtils.encryptAES("hello", salt),"ly60culHXDlTLDmPaS8iag==");
    }
}

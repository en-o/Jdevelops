package com.detabes.encryption.core;

import junit.framework.TestCase;
import org.mockito.Mock;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AESUtilTest extends TestCase {

    @Mock
    AESUtil aesUtil;

    public AESUtilTest() {
        aesUtil = AESUtil.getInstance();
    }

    public void testEncrypt() {
        assertThat(aesUtil.encrypt("13123"), is("h1Sdww/AuY29sJtGxnf2kw=="));
    }

    public void testTestEncrypt() {
    }

    public void testDecrypt() {
    }

    public void testTestDecrypt() {
    }

    public void testEncodeBytes() {
    }
}
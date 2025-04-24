package cn.tannn.jdevelops.utils.jwt.module;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SignEntityTest {

    @Test
    void parsingSubject() {
        SignEntity signEntity = new SignEntity("123456", null, null);
        assertEquals("123456", signEntity.parsingSubject());
        assertEquals("123456", signEntity.getSubject());

        signEntity.setIdentity("TAN");
        assertEquals("123456", signEntity.parsingSubject());
        assertEquals("TAN__123456", signEntity.getSubject());
    }
}

package cn.tannn.jdevelops.utils.jwt.util;

import cn.tannn.jdevelops.utils.jwt.module.SignEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtUtilTest {

    @Test
    void parsingSubject() {

        SignEntity signEntity = new SignEntity("123456", null, null);
        assertEquals("123456", JwtUtil.parsingSubject(signEntity.getSubject()));
        assertEquals("123456", signEntity.getSubject());

        signEntity.setIdentity("TAN");
        assertEquals("123456", JwtUtil.parsingSubject(signEntity.getSubject()));
        assertEquals("TAN__123456", signEntity.getSubject());
    }
}

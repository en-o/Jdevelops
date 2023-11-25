package cn.jdevelops.util.jwt.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EncryptionUtilTest {

    @Test
    public void encrypt2MD5() {
        String jwtMd5 = EncryptionUtil.encrypt2MD5("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzeXN0ZW0iLCJsb2dpbk5hbWUiOiJzeXN0ZW0iLCJuYW1lIjoi57O757uf566h55CG5ZGYIiwiaXNzIjoiZGV0YWJlcyIsImV4cCI6MTY3NjE0NzQwMywiaWF0IjoxNjc2MDYxMDAzLCJqdGkiOiJiODA3Y2FlNy01YWQzLTQ1NWYtYTAzYi0wMmI1OTYwMzJkZTgifQ.pcGsNWn0qUv0GGWyVR-TmrVac_ElUq9MDtYErml5Qj0");
        assertEquals(jwtMd5,"edf01bdfdc53a2e1586ab60f98ac0710");
    }
}

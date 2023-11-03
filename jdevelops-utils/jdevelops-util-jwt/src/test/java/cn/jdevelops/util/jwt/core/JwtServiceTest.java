package cn.jdevelops.util.jwt.core;

import cn.jdevelops.util.jwt.entity.LoginJwtExtendInfo;
import cn.jdevelops.util.jwt.entity.SignEntity;
import junit.framework.TestCase;
import org.jose4j.lang.JoseException;

public class JwtServiceTest extends TestCase {

    public void testGetLoginJwtExtendInfoExpires() throws JoseException {
        SignEntity<LoginJwtExtendInfo> signEntity =
                new SignEntity<>("tan",  new LoginJwtExtendInfo("tan","tan","tan"));
        String token = JwtService.generateToken(signEntity);
        LoginJwtExtendInfo loginJwtExtendInfoExpires = JwtService.getLoginJwtExtendInfoExpires(token);
        assertEquals("LoginJwtExtendInfo{loginName='tan', userId='null', userNo='tan', userName='tan', phone='null'}",
                loginJwtExtendInfoExpires.toString());
    }
}

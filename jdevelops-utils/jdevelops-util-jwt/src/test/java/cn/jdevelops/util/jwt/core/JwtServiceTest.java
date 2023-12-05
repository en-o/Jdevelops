package cn.jdevelops.util.jwt.core;

import cn.jdevelops.util.jwt.entity.LoginJwtExtendInfo;
import cn.jdevelops.util.jwt.entity.SignEntity;
import com.google.common.collect.ImmutableMap;
import junit.framework.TestCase;
import org.jose4j.lang.JoseException;

import java.util.Map;

public class JwtServiceTest extends TestCase {

    public void testGetLoginJwtExtendInfoExpires() throws JoseException {
        SignEntity<LoginJwtExtendInfo<String>> signEntity =
                new SignEntity<>("tan",  new LoginJwtExtendInfo<String>("tan","tan","tan"));
        String token = JwtService.generateToken(signEntity);
        LoginJwtExtendInfo<Map<String,String>> loginJwtExtendInfoExpires = JwtService.getLoginJwtExtendInfoExpires(token);
        assertEquals("LoginJwtExtendInfo{loginName='tan', userId='null', userNo='tan', userName='tan', phone='null', map=null}",
                loginJwtExtendInfoExpires.toString());


        SignEntity<LoginJwtExtendInfo<Map<String,String>>> signEntity2 =
                new SignEntity<>("tan",  new LoginJwtExtendInfo<Map<String,String>>("tan","tan","tan",
                        ImmutableMap.of("key","tan")));
        String token2 = JwtService.generateToken(signEntity2);
        LoginJwtExtendInfo<Map<String,String>> loginJwtExtendInfoExpires2 = JwtService.getLoginJwtExtendInfoExpires(token2);
        assertEquals("LoginJwtExtendInfo{loginName='tan', userId='null', userNo='tan', userName='tan', phone='null', map={\"key\":\"tan\"}}",
                loginJwtExtendInfoExpires2.toString());
    }
}

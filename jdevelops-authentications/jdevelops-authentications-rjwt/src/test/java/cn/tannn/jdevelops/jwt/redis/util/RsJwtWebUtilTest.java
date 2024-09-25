package cn.tannn.jdevelops.jwt.redis.util;


import cn.tannn.jdevelops.jwt.redis.entity.sign.RedisSignEntity;
import cn.tannn.jdevelops.utils.jwt.core.JwtService;
import cn.tannn.jdevelops.utils.jwt.module.LoginJwtExtendInfo;
import cn.tannn.jdevelops.utils.jwt.module.SignEntity;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RsJwtWebUtilTest{

    @Test
    public void testGetLoginJwtExtendInfoExpires() throws JoseException {
        RedisSignEntity<LoginJwtExtendInfo<Map<String,String>>> signEntity =
                new RedisSignEntity<>(SignEntity.initMap("tan", new LoginJwtExtendInfo<>("tan", "tan", "tan")));
        String token = JwtService.generateToken(signEntity);
        LoginJwtExtendInfo<Map<String,String>> loginJwtExtendInfoExpires = JwtService.getLoginJwtExtendInfoExpires(token);
        assertEquals("LoginJwtExtendInfo{loginName='tan', userId='null', userNo='tan', userName='tan', phone='null', map=null}",
                loginJwtExtendInfoExpires.toString());



        RedisSignEntity<LoginJwtExtendInfo<Map<String,String>>> signEntity2 =
                new RedisSignEntity<>(SignEntity.initMap("tan", new LoginJwtExtendInfo<>(
                        "tan",
                        "tan",
                        "tan", new HashMap<String,String>(){{
                            put("key", "tan");
                }})));
        String token2 = JwtService.generateToken(signEntity2);
        LoginJwtExtendInfo<Map<String,String>> loginJwtExtendInfoExpires2 = JwtService.getLoginJwtExtendInfoExpires(token2);
        assertEquals("LoginJwtExtendInfo{loginName='tan', userId='null', userNo='tan', userName='tan', phone='null', map={\"key\":\"tan\"}}",
                loginJwtExtendInfoExpires2.toString());

    }
}

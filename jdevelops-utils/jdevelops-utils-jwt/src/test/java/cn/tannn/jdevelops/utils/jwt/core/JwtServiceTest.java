package cn.tannn.jdevelops.utils.jwt.core;

import cn.tannn.jdevelops.utils.jwt.module.LoginJwtExtendInfo;
import cn.tannn.jdevelops.utils.jwt.module.SignEntity;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class JwtServiceTest {

    @Test
    void generateToken() throws JoseException {
       // token生成
        String dasda = JwtService.generateToken("dasda");
        assertTrue(JwtService.validateTokenByBoolean(dasda));
    }



    @Test
    void validateTokenByBoolean() throws JoseException {
        // token生成
        String dasda = JwtService.generateToken("dasda");
        // 验证不同 tokenSecret  的 validateTokenByBoolean， 测试结果不同的 secret 不会出现混合验证成功的问题
        assertFalse(JwtService.validateTokenByBoolean("eyJraWQiOiI1NTUzODk4ZC1jMzM0LTRlNTAtYjdjZS1jMjNkMTE2MDAxNzIiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJleHAiOjE3MTI5OTIxNDMsInN1YiI6ImRhc2RhIn0.c5B6RKqfy3oboqgHUVpN_RnuHkRRxjVWe8KSAvlFlUc"));
        assertTrue(JwtService.validateTokenByBoolean(dasda));
    }


    @Test
    void getSubject() throws MalformedClaimException, JoseException {
        // token生成
        String dasda = JwtService.generateToken("dasda");
        assertEquals("dasda", JwtService.getSubject(dasda));
    }

    @Test
    void getSubjectExpires() throws  JoseException {
        // token生成
        String dasda = JwtService.generateToken("dasda");
        assertEquals("dasda", JwtService.getSubjectExpires(dasda));
    }

    @Test
    void getLoginJwtExtendInfoExpires() throws JoseException {
        SignEntity<LoginJwtExtendInfo<String>> signEntity =
                new SignEntity<>("tan",  new LoginJwtExtendInfo<String>("tan","tan","tan"));
        String token = JwtService.generateToken(signEntity);
        LoginJwtExtendInfo<Map<String,String>> loginJwtExtendInfoExpires = JwtService.getLoginJwtExtendInfoExpires(token);
        assertEquals("LoginJwtExtendInfo{loginName='tan', userId='null', userNo='tan', userName='tan', phone='null', map=null}",
                loginJwtExtendInfoExpires.toString());


        SignEntity<LoginJwtExtendInfo<Map<String,String>>> signEntity2 =
                new SignEntity<>("tan",  new LoginJwtExtendInfo<Map<String,String>>("tan","tan","tan",
                        new HashMap<String,String>(){{
                            put("key","tan");
                        }}));
        String token2 = JwtService.generateToken(signEntity2);
        LoginJwtExtendInfo<Map<String,String>> loginJwtExtendInfoExpires2 = JwtService.getLoginJwtExtendInfoExpires(token2);
        assertEquals("LoginJwtExtendInfo{loginName='tan', userId='null', userNo='tan', userName='tan', phone='null', map={\"key\":\"tan\"}}",
                loginJwtExtendInfoExpires2.toString());
    }



    @Test
    void getTokenByBean() throws JoseException {
        SignEntity<LoginJwtExtendInfo<Map<String,String>>> signEntity2 =
                new SignEntity<>("tan",
                        new LoginJwtExtendInfo<Map<String,String>>("tan","tan","tan",
                        new HashMap<String,String>(){{
                            put("key","tan");
                        }}));
        String token2 = JwtService.generateToken(signEntity2);
        assertEquals("LoginJwtExtendInfo{loginName='null', userId='null', userNo='null', userName='null', phone='null', map={\"loginName\":\"tan\",\"userNo\":\"tan\",\"userName\":\"tan\",\"map\":{\"key\":\"tan\"}}}",
                JwtService.getTokenByBean(token2, LoginJwtExtendInfo.class, HashMap.class).toString());
    }

    @Test
    void getPlatformConstantExpires() throws JoseException {
        // token生成
        String dasda = JwtService.generateToken(new SignEntity("tan", Arrays.asList(PlatformConstant.WEB_ADMIN)));
        assertEquals(Collections.singletonList(PlatformConstant.WEB_ADMIN), JwtService.getPlatformConstantExpires(dasda));
    }

    @Test
    void parseJwt() throws MalformedClaimException, JoseException {
        // token生成
        String dasda = JwtService.generateToken("dasda");
        assertEquals("dasda", JwtService.parseJwt(dasda).getSubject());
    }
}

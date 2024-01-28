package cn.jdevelops.sboot.authentication.jredis.util;

import cn.jdevelops.sboot.authentication.jredis.entity.sign.RedisSignEntity;
import cn.jdevelops.util.jwt.core.JwtService;
import cn.jdevelops.util.jwt.entity.LoginJwtExtendInfo;
import cn.jdevelops.util.jwt.entity.SignEntity;
import com.google.common.collect.ImmutableMap;
import junit.framework.TestCase;
import lombok.Data;
import org.jose4j.lang.JoseException;

import java.util.Map;

public class RsJwtWebUtilTest extends TestCase {

    @Data
    public class TestBean{
        String sex,name;
        public TestBean(String sex, String name) {
            this.sex = sex;
            this.name = name;
        }
    }


    public void testGetLoginJwtExtendInfoExpires() throws JoseException {
        RedisSignEntity<LoginJwtExtendInfo<Map<String,String>>> signEntity =
                new RedisSignEntity<>(new SignEntity<>("tan", new LoginJwtExtendInfo<>("tan", "tan", "tan")));
        String token = JwtService.generateToken(signEntity);
        LoginJwtExtendInfo<Map<String,String>> loginJwtExtendInfoExpires = JwtService.getLoginJwtExtendInfoExpires(token);
        assertEquals("LoginJwtExtendInfo{loginName='tan', userId='null', userNo='tan', userName='tan', phone='null', map=null}",
                loginJwtExtendInfoExpires.toString());



        RedisSignEntity<LoginJwtExtendInfo<Map<String,String>>> signEntity2 =
                new RedisSignEntity<>(new SignEntity<>("tan", new LoginJwtExtendInfo<>(
                        "tan",
                        "tan",
                        "tan", ImmutableMap.of("key", "tan"))));
        String token2 = JwtService.generateToken(signEntity2);
        LoginJwtExtendInfo<Map<String,String>> loginJwtExtendInfoExpires2 = JwtService.getLoginJwtExtendInfoExpires(token2);
        assertEquals("LoginJwtExtendInfo{loginName='tan', userId='null', userNo='tan', userName='tan', phone='null', map={\"key\":\"tan\"}}",
                loginJwtExtendInfoExpires2.toString());

    }
}

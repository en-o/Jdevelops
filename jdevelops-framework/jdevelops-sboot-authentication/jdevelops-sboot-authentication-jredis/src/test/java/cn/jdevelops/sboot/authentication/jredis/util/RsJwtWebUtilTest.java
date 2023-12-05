package cn.jdevelops.sboot.authentication.jredis.util;

import cn.jdevelops.sboot.authentication.jredis.entity.sign.RedisSignEntity;
import cn.jdevelops.util.jwt.core.JwtService;
import cn.jdevelops.util.jwt.entity.LoginJwtExtendInfo;
import cn.jdevelops.util.jwt.entity.SignEntity;
import com.google.common.collect.ImmutableMap;
import junit.framework.TestCase;
import lombok.Data;
import org.jose4j.lang.JoseException;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
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
    public void testGetTokenByRedisSignEntity() throws JoseException {
        RedisSignEntity<TestBean> signEntity =
                new RedisSignEntity<>(new SignEntity<>("tan",
                        new TestBean("tan", "tan")));
        String token = JwtService.generateToken(signEntity);
        // 创建HttpServletRequest的模拟对象
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        // 设置模拟请求的上下文和参数
        Mockito.when(request.getContextPath()).thenReturn("/test");
        Mockito.when(request.getMethod()).thenReturn("GET");
        Mockito.when(request.getParameter("token")).thenReturn(token);
        RedisSignEntity<TestBean> tokenByRedisSignEntity = RsJwtWebUtil.getTokenByRedisSignEntity(request, TestBean.class);

        assertEquals("RsJwtWebUtilTest.TestBean(sex=tan, name=tan)",
                tokenByRedisSignEntity.getMap().toString());
    }

    public void testTestGetTokenByRedisSignEntity() {
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

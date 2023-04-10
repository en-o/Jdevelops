//package cn.jdevelops.util.jwt.util;
//
//import cn.jdevelops.util.jwt.core.JwtService;
//import cn.jdevelops.util.jwt.entity.SignEntity;
//import cn.jdevelops.util.jwt.exception.JwtException;
//import org.jose4j.jwt.MalformedClaimException;
//import org.jose4j.jwt.consumer.InvalidJwtException;
//import org.jose4j.lang.JoseException;
//import org.junit.Test;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// *  jwt测试类 - 测试时修改下   JwtService的静态代码块
// * @author tn
// * @version 1
// * @date 2020/6/19 11:52
// */
//public class JwtServiceTest {
//
//
//
//    /**
//     * 生成token
//     */
//    @Test
//    public void generateToken() throws JoseException {
//        SignEntity signEntity = new SignEntity();
//        signEntity.setSubject("tan");
//        Map<String, Object> hashMap = new HashMap<String, Object>() {{
//            put("name", "谭宁");
//        }};
//        signEntity.setMap(hashMap);
//        // eyJraWQiOiJlZmFkY2Q4Mi1hNDMzLTQ4MDYtOTY0My1mOTMxNGE4OWUxNDQiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJleHAiOjE2ODExNzcyMDUsImp0aSI6IjB1VTJzdHJveHpNUGJ4SWVGV2x0aUEiLCJpYXQiOjE2ODEwOTA4MDUsInN1YiI6InRhbiIsIm5hbWUiOiLosK3lroEifQ.P2_NAUlhZhdoVziU1rUb9x0jd31lTtsc5a3AmHrkBoU
//        System.out.println(JwtService.generateToken(signEntity));
//    }
//
//
//    /**
//     * 生成token
//     */
//    @Test
//    public void generateToken2() throws JoseException {
//        // eyJraWQiOiI2OTNlMTdiOS05MDZjLTRlNDktYWViNy1kNTNkMzRjMjY3MTIiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJleHAiOjE2ODExNzcyNTAsInN1YiI6InRhbiJ9.1rOBhmOjLh5UqjoWN74qKXb28AyLRQIaIJGqv-VYMBU
//        System.out.println(JwtService.generateToken("tan"));
//    }
//
//
//    /**
//     * 验证token
//     */
//    @Test
//    public void validateTokenByBoolean() throws MalformedClaimException {
//        String token = "eyJraWQiOiIyMjBlOTg3ZS05ZGI0LTQzMDktYjBjMS1mNzgwYmRkMTk1NTIiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJleHAiOjE2ODA3NzQ1MDAsImp0aSI6IkRCRnp3S3BGSjEyNS0yMi1lVWVlWnciLCJpYXQiOjE2ODA2ODgxMDAsInN1YiI6InRhbiIsIm5hbWUiOiLosK3lroEifQ.aQKGWQKmvYZ-rnLWr87a8bMekk_cXb-X3XYkYeao-YQ";
//        System.out.println(JwtService.validateTokenByBoolean(token));
//    }
//
//    /**
//     * 验证token
//     */
//    @Test
//    public void validateTokenByJwtClaims() throws MalformedClaimException, JwtException {
//        String token = "eyJraWQiOiIxY2MzYTgzYi0wYjc4LTQxYjItYWUyNS01NjUzNDBmNDRiZWIiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJzdWIiOiJ0YW4iLCJleHAiOjE2ODExNzcyNTB9.VsIrp3xYTONhv2P55O7wp5n5tWh5vKAWnqaT-64SwKg";
//        System.out.println(JwtService.validateTokenByJwtClaims(token));
//    }
//
//    /**
//     * 刷新token
//     */
//    @Test
//    public void refreshToken() throws JoseException, MalformedClaimException, JwtException {
//        String token = "eyJraWQiOiI2OTNlMTdiOS05MDZjLTRlNDktYWViNy1kNTNkMzRjMjY3MTIiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJleHAiOjE2ODExNzcyNTAsInN1YiI6InRhbiJ9.1rOBhmOjLh5UqjoWN74qKXb28AyLRQIaIJGqv-VYMBU";
//        // eyJraWQiOiIxY2MzYTgzYi0wYjc4LTQxYjItYWUyNS01NjUzNDBmNDRiZWIiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJzdWIiOiJ0YW4iLCJleHAiOjE2ODExNzcyNTB9.VsIrp3xYTONhv2P55O7wp5n5tWh5vKAWnqaT-64SwKg
//        System.out.println(JwtService.refreshToken(token));
//    }
//
//
//
//    /**
//     * 获得Token中的 Subject
//     *
//     */
//    @Test
//    public void getSubject() throws JoseException, MalformedClaimException, JwtException {
//        String token = "eyJraWQiOiIxY2MzYTgzYi0wYjc4LTQxYjItYWUyNS01NjUzNDBmNDRiZWIiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJzdWIiOiJ0YW4iLCJleHAiOjE2ODExNzcyNTB9.VsIrp3xYTONhv2P55O7wp5n5tWh5vKAWnqaT-64SwKg";
//        System.out.println(JwtService.getSubject(token));
//    }
//
//
//    /**
//     * 获取 jwt 的内容 - 过期令牌也能解析
//     */
//    @Test
//    public void parseJwt() throws InvalidJwtException {
//        String token = "eyJraWQiOiIxY2MzYTgzYi0wYjc4LTQxYjItYWUyNS01NjUzNDBmNDRiZWIiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJzdWIiOiJ0YW4iLCJleHAiOjE2ODExNzcyNTB9.VsIrp3xYTONhv2P55O7wp5n5tWh5vKAWnqaT-64SwKg";
//        System.out.println(JwtService.parseJwt(token));
//    }
//
//
//
//
//
//
//
//}

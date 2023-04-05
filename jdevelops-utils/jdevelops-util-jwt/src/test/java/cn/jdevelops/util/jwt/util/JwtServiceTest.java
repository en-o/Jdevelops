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
//        // eyJhbGciOiJIUzI1NiIsImtpZCI6IjRhYzRiN2Y5LTExYTctNDZhOS1iMGM0LTM5ZmMzMDUyNTY5MSJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJleHAiOjE2ODA3NzM3MDAsImp0aSI6IjEwN1BlZXVQeEVTQUZiRjFscmliZUEiLCJpYXQiOjE2ODA2ODczMDEsInN1YiI6InRhbiIsIm5hbWUiOiLosK3lroEifQ.Hdqz1K_fmNnt5sNGTr-Ud2NkMH6uILr9Sycjd-5HF7w
//        System.out.println(JwtService.generateToken(signEntity));
//    }
//
//
//    /**
//     * 生成token
//     */
//    @Test
//    public void generateToken2() throws JoseException {
//        // eyJhbGciOiJIUzI1NiIsImtpZCI6ImU1MTNiYTAyLWU1Y2MtNDJhMC1hY2E2LTM5ODZjZmJkOTI3MiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJleHAiOjE2ODA3NzM4MDUsInN1YiI6InRhbiJ9.4PCcoQyhDVjnKqb69o22n22TNZuBJBKsrrXvhgeRqgs
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
//        String token = "eyJraWQiOiIyMjBlOTg3ZS05ZGI0LTQzMDktYjBjMS1mNzgwYmRkMTk1NTIiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJleHAiOjE2ODA3NzQ1MDAsImp0aSI6IkRCRnp3S3BGSjEyNS0yMi1lVWVlWnciLCJpYXQiOjE2ODA2ODgxMDAsInN1YiI6InRhbiIsIm5hbWUiOiLosK3lroEifQ.aQKGWQKmvYZ-rnLWr87a8bMekk_cXb-X3XYkYeao-YQ";
//        System.out.println(JwtService.validateTokenByJwtClaims(token));
//    }
//
//    /**
//     * 刷新token
//     */
//    @Test
//    public void refreshToken() throws JoseException, MalformedClaimException, JwtException {
//        String token = "eyJraWQiOiIyMjBlOTg3ZS05ZGI0LTQzMDktYjBjMS1mNzgwYmRkMTk1NTIiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJleHAiOjE2ODA3NzQ1MDAsImp0aSI6IkRCRnp3S3BGSjEyNS0yMi1lVWVlWnciLCJpYXQiOjE2ODA2ODgxMDAsInN1YiI6InRhbiIsIm5hbWUiOiLosK3lroEifQ.aQKGWQKmvYZ-rnLWr87a8bMekk_cXb-X3XYkYeao-YQ";
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
//        String token = "eyJraWQiOiIyMjBlOTg3ZS05ZGI0LTQzMDktYjBjMS1mNzgwYmRkMTk1NTIiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJleHAiOjE2ODA3NzQ1MDAsImp0aSI6IkRCRnp3S3BGSjEyNS0yMi1lVWVlWnciLCJpYXQiOjE2ODA2ODgxMDAsInN1YiI6InRhbiIsIm5hbWUiOiLosK3lroEifQ.aQKGWQKmvYZ-rnLWr87a8bMekk_cXb-X3XYkYeao-YQ";
//        System.out.println(JwtService.getSubject(token));
//    }
//
//
//    /**
//     * 获取 jwt 的内容 - 过期令牌也能解析
//     */
//    @Test
//    public void parseJwt() throws InvalidJwtException {
//        String token = "eyJraWQiOiI0MmZiZGY3MS0yMTc0LTQ0ZGMtYTExNC0yYzgxYWM4ZWY3NzIiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJleHAiOjE2ODA2ODg0MTEsImp0aSI6InVPSlRJR013MW1SaTJoSE8xN0t2YkEiLCJpYXQiOjE2ODA2ODgyOTIsInN1YiI6InRhbiIsIm5hbWUiOiLosK3lroEifQ.c5u7g1FQ8Aq04W_l_Tfxd1cYHWBPe3rjjWMaLR5L2T0";
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

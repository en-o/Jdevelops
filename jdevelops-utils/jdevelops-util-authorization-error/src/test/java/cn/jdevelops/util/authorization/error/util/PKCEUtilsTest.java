package cn.jdevelops.util.authorization.error.util;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class PKCEUtilsTest{

    @Test
    public void codeVerifierGenerator() {
        //生成code_verifier
        System.out.println(PKCEUtils.codeVerifierGenerator());
    }


    @Test
    public void codeChallengeGenerator() throws NoSuchAlgorithmException {
        // 好像只能使用一次
        //生成code_verifier
        String codeVerifier = PKCEUtils.codeVerifierGenerator();
        org.junit.Assert.assertNotNull(codeVerifier);
        System.out.println(codeVerifier);
        //生成code_challenge
        String codeChallenge = PKCEUtils.codeChallengeGenerator(codeVerifier);
        org.junit.Assert.assertNotNull(codeChallenge);
        System.out.println(codeChallenge);

    }
}

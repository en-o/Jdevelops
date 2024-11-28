package cn.tannn.jdevelops.frameworks.web.starter.apiSecurity;


import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @see https://blog.csdn.net/weixin_45973634/article/details/136022834
 */
class ApiSecurityUtilsTest {

    @Test
    void ApiEncrptTest() throws Exception {
                /** RSA：生成公钥和私钥 */
        Map<String, String> rsaKey = ApiRSAUtils.genKeyPair();
        String publicKey = rsaKey.get("publicKey");
        System.out.println("随机生成的公钥（publicKey）为：" + publicKey);
        String privateKey = rsaKey.get("privateKey");
        System.out.println("随机生成的私钥（privateKey）为：" + privateKey);

        // 模拟登录
        Map<String,String> login = new HashMap<>();
        login.put("username","admin");
        login.put("password","password");

        // 客户端：加密数据传输
        ApiEncryptRes encryptData = ApiSecurityUtils.encrypt(JSON.toJSONString(login), publicKey);
        System.out.println("客户端工作：加密数据传输: " + encryptData);

        // 服务端：解密数据
        String decrypt = ApiSecurityUtils.decrypt(encryptData.getAesKeyByRsa(), encryptData.getData(), privateKey);
        System.out.println("服务端工作：解密数据: " + decrypt);
        assertEquals("{\"password\":\"password\",\"username\":\"admin\"}",decrypt);
        Map basicsLogin = JSON.to(Map.class, decrypt);
        System.out.println("服务端工作JSON：" + basicsLogin);
    }
}

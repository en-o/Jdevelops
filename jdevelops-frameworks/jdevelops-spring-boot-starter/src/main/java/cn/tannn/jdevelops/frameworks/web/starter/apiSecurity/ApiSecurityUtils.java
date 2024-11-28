package cn.tannn.jdevelops.frameworks.web.starter.apiSecurity;

import cn.tannn.jdevelops.exception.built.BusinessException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  ApiSecurityUtils
 *  <code>
 *     <p>  public ResultVO<LoginVO> loginWeb(@RequestBody @Valid ApiEncryptRes encryptData, HttpServletRequest request) throws Exception {
 *     <p>     String decrypt = ApiSecurityUtils.decrypt(encryptData.getAesKeyByRsa(), encryptData.getData(), RSAKey.privateKey);
 *     <p>     LoginPassword login = JSON.to(LoginPassword.class, decrypt);
 *     <p>   }
 *  </code>
 *  <p>  RSAKey 是一个常量，数据来源于 ApiRSAUtils.genKeyPair()
 * @author  https://blog.csdn.net/weixin_45973634/article/details/136022834
 */
public class ApiSecurityUtils {
    private static final Logger log = LoggerFactory.getLogger(ApiSecurityUtils.class);

    /**
     * 解密
     * @param aesKeyByRsa 经过rsa加密的aeskey
     * @param decryptStr 经过aes加密的数据
     * @param privateKey 私钥
     * @return 解密后的数据
     */
    public  static String decrypt(String aesKeyByRsa,String decryptStr,String privateKey)  {
       try {
           // RSA私钥解密AES秘钥key
           byte[] bytes = ApiRSAUtils.decryptByPrivateKey(Base64.decodeBase64(aesKeyByRsa), privateKey);
           String aesKey = new String(bytes);
           // AES秘钥key解密数据
           return ApiAESUtils.decrypt(decryptStr, aesKey);
       }catch (Exception e){
           log.warn("解密失败，请核对公钥/加密方式");
           throw new BusinessException("账户密码错误，请重新登录",e);
       }
    }

    /**
     * 加密
     * @param encryptStr 要加密的数据
     * @param frontPublicKey  前端公钥
     * @return 加密后的数据
     */
    public static ApiEncryptRes encrypt(String encryptStr, String frontPublicKey) throws Exception {
        // AES：随机生成加密秘钥
        String aesKey = ApiAESUtils.getKey();
        // AES秘钥key加密数据
        String data = ApiAESUtils.encrypt(encryptStr, aesKey);
        ApiEncryptRes apiEncryptRes = new ApiEncryptRes();
        // RSA加密AES秘钥key
        String aesKeyByRsa = Base64.encodeBase64String(ApiRSAUtils.encryptByPublicKey(aesKey.getBytes(), frontPublicKey));
        apiEncryptRes.setAesKeyByRsa(aesKeyByRsa);
        apiEncryptRes.setData(data);
        apiEncryptRes.setFrontPublicKey(frontPublicKey);
        return apiEncryptRes;
    }

}

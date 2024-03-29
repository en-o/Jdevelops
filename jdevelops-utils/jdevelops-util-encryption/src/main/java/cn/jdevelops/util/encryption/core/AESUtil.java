package cn.jdevelops.util.encryption.core;


import cn.jdevelops.enums.number.NumEnum;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;


/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 *
 * @author l
 */
public class AESUtil {
	private static final Logger LOG = LoggerFactory.getLogger(AESUtil.class);
	/**
	 * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。可自行修改。
	 */
	private static final String S_KEY = "asc0123456789asc";
	/**
	 * 偏移量,可自行修改
	 */
	private static final String IV_PARAMETER = "0392039203920300";

	private static AESUtil instance = null;

	private AESUtil() {

	}

	/**
	 * 单例工厂
	 *
	 * @return cn.jdevelops.crc.util.AESOperator
	 * @author lxw
	 * @date 2020/10/10 14:23
	 */
	public static AESUtil getInstance() {
		if (instance == null) {
			instance = new AESUtil();
		}
		return instance;
	}

	/**
	 * 加密-带偏移量
	 *
	 * @param sSrc      需要加密的字符串
	 * @param secretKey 密钥
	 * @param vector    偏移量
	 * @return java.lang.String
	 * @author lxw
	 * @date 2020/10/9 17:06
	 */
	public String encrypt(String sSrc, String secretKey, String vector) {
		try {
			if (secretKey == null) {
				return null;
			}
			if (NumEnum.SIX_TEN.getNum() != secretKey.length()) {
				return null;
			}
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] raw = secretKey.getBytes();
			SecretKeySpec skySpec = new SecretKeySpec(raw, "AES");
			// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			IvParameterSpec iv = new IvParameterSpec(vector.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, skySpec, iv);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
			// 此处使用BASE64做转码。
			return Base64.encodeBase64String(encrypted);
		} catch (Exception e) {
			LOG.error("加密失败", e);
			return null;
		}
	}


	/**
	 * 加密-无偏移量
	 *
	 * @param sSrc 需要加密的字符串
	 * @return String
	 * @author lxw
	 * @date 2020/10/9 17:02
	 */
	public String encrypt(String sSrc) {
		return this.encrypt(sSrc, S_KEY, IV_PARAMETER);
	}

	/**
	 * 解密-无偏移量
	 *
	 * @param sSrc 被加密字符串
	 * @return java.lang.String
	 * @author lxw
	 * @date 2020/10/9 17:02
	 */
	public String decrypt(String sSrc) {
		return this.decrypt(sSrc, S_KEY, IV_PARAMETER);
	}

	/**
	 * 解密-有偏移量
	 *
	 * @param sSrc 被加密字符串
	 * @param key  密钥
	 * @param ivs  偏移量
	 * @return java.lang.String
	 * @author lxw
	 * @date 2020/10/9 17:09
	 */
	public String decrypt(String sSrc, String key, String ivs) {
		try {
			byte[] raw = key.getBytes(StandardCharsets.US_ASCII);
			SecretKeySpec skySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skySpec, iv);
			// 先用base64解密
			byte[] encrypted = Base64.decodeBase64(sSrc);
			byte[] original = cipher.doFinal(encrypted);
			return new String(original, StandardCharsets.UTF_8);
		} catch (Exception ex) {
			LOG.error("解密失败", ex);
			return null;
		}
	}
}

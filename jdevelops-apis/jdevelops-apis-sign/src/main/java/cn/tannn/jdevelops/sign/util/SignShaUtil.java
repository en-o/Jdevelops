package cn.tannn.jdevelops.sign.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 *
 *  接口签名用 sha加密
 * @author tn
 * @version 1
 * @date 2020/6/9 14:41
 */
public class SignShaUtil {
    private static final Logger log = LoggerFactory.getLogger(SignShaUtil.class);
    public static String encrypt(String str){
        if (null == str || str.isEmpty()){
            return null;
        }
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(new String(str.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8).getBytes());
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA1加密异常:",e);
        }
        return str;
    }

    @SuppressWarnings("unchecked")
    public static String encrypt(Object obj) {
        if(obj==null){
            return null;
        }
        Map<String, Object> map;
        if(obj instanceof Map){
            map=(Map<String, Object>) obj;
        }else{
            map = SignUtil.transBean2Map(obj);
        }
        map.remove("sign");
        map.remove("encrypt");
        String result =  SignUtil.map2Str(map);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        return encrypt(result);
    }

    @SuppressWarnings("unchecked")
    public static boolean check(Object obj){
        Map<String, Object> map;
        if(obj==null){
            return false;
        }
        if(obj instanceof Map){
            map=(Map<String, Object>) obj;
        }else{
            map = SignUtil.transBean2Map(obj);
        }
        String sign=(String)map.get("sign");
        if(sign==null){
            return false;
        }
        String str=encrypt(obj);
        return sign.equals(str);

    }

}

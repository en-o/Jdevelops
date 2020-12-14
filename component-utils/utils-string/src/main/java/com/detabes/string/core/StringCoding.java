package com.detabes.string.core;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 编码相关
 * @author tn
 * @version 1
 * @ClassName StringCoding
 * @date 2020/8/11 22:07
 */
public class StringCoding {
    /**
     *   字符转utf-8格式
     * @param str 乱码的中文
     * @return 返回中文
     */
    public static String encode(String str){
        String encode=null;
        try {
            encode = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encode;
    }
}

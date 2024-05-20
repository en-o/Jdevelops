package cn.tannn.jdevelops.uitls.aop;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 *  of hutool.URLUtil
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-08-13 23:16
 */
public class StringUtil {


    /**
     * 字符串判断不是空
     * @param cs 字符串
     * @return true 非空
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }


    /**
     * 字符串判断空
     * @param cs 字符串
     * @return true 空
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen = length(cs);
        if (strLen == 0) {
            return true;
        } else {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }


    /**
     * 字符串长度
     * @param cs 字符串
     * @return 长度
     */
    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }



    /**
     *   下划线转驼峰法
     * @param line 字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 返回字符串
     */
    public static String toCamelCase(String line,boolean smallCamel){
        if(line==null||"".equals(line)){
            return "";
        }
        StringBuilder sb=new StringBuilder();
        Pattern pattern= compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(smallCamel&&matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
            int index=word.lastIndexOf('_');
            if(index>0){
                sb.append(word.substring(1, index).toLowerCase());
            }else{
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }


    /**
     * 处理str
     * @param data string.byte
     * @param charset 编码
     * @return String
     */
    public static String str(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        } else {
            return null == charset ? new String(data) : new String(data, charset);
        }
    }

    /**
     * 判空
     * @param str str
     * @return true 空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }



    /**
     * 判空
     * @param c char
     * @return true 空
     */
    public static boolean isBlankChar(char c) {
        return isBlankChar((int)c);
    }

    /**
     * 判空
     * @param c int
     * @return true 空
     */
    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == 65279 || c == 8234 || c == 0;
    }


    /**
     * 根据编码获取字符的 byte
     * @param str str
     * @param charset 编码
     * @return byte of  byte[]
     */
    public static byte[] bytes(CharSequence str, Charset charset) {
        if (str == null) {
            return null;
        } else {
            return null == charset ? str.toString().getBytes() : str.toString().getBytes(charset);
        }
    }

    /**
     * 解码 byte
     * @param bytes bytes
     * @param isPlusToSpace bytes
     * @return byte
     */
    public static byte[] decode(byte[] bytes, boolean isPlusToSpace) {
        if (bytes == null) {
            return null;
        } else {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream(bytes.length);

            for(int i = 0; i < bytes.length; ++i) {
                int b = bytes[i];
                if (b == 43) {
                    buffer.write(isPlusToSpace ? 32 : b);
                } else if (b == 37) {
                    if (i + 1 < bytes.length) {
                        int u = digit16(bytes[i + 1]);
                        if (u >= 0 && i + 2 < bytes.length) {
                            int l = digit16(bytes[i + 2]);
                            if (l >= 0) {
                                buffer.write((char)((u << 4) + l));
                                i += 2;
                                continue;
                            }
                        }
                    }

                    buffer.write(b);
                } else {
                    buffer.write(b);
                }
            }

            return buffer.toByteArray();
        }
    }


    public static int digit16(int b) {
        return Character.digit(b, 16);
    }
}

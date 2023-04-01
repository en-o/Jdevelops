package cn.jdevelops.util.core.string;

import java.nio.charset.StandardCharsets;

/**
 * 字节相关
 * @author tn
 * @version 1
 * @date 2020/8/11 22:10
 */
public class StringByte {

    /**
     *   Hex字符串转byte
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }


    /**
     *    byte数组转hex
     * @param bytes 数组
     * @return 返回hex
     */
    public static String byteToHex(byte[] bytes){
        String strHex;
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            strHex = Integer.toHexString(aByte & 0xFF);
            // 每个字节由两个字符表示，位数不够，高位补0
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex);
            sb.append(" ");
        }
        return sb.toString().trim();
    }
    /**
     * 字节数组转float
     * 采用IEEE 754标准
     *
     * @param bytes 字节数组
     * @return 返回 float
     */
    public static Float bytes2Float(byte[] bytes) {
        //获取 字节数组转化成的16进制字符串
        String binaryStr = bytes2BinaryStr(bytes);
        //符号位S
        long s = Long.parseLong(binaryStr.substring(0, 1));
        //指数位E
        long e = Long.parseLong(binaryStr.substring(1, 9), 2);
        //位数M
        String mStr = binaryStr.substring(9);
        float m = 0;
        float a;
        float b;
        for (int i = 0; i < mStr.length(); i++) {
            a = Integer.parseInt(mStr.charAt(i) + "");
            b = (float) Math.pow(2, 1 + i);
            m = m + (a / b);
        }
        return (float) ((Math.pow(-1, s)) * (1 + m) * (Math.pow(2, (e - 127))));
    }

    /**
     * 将字节数组转换成16进制字符串
     *
     * @param bytes 字节数组
     * @return 返回16进制字符串
     */
    public static String bytes2BinaryStr(byte[] bytes) {
        StringBuilder binaryStr = new StringBuilder();
        for (byte aByte : bytes) {
            String str = Integer.toBinaryString((aByte & 0xFF) + 0x100).substring(1);
            binaryStr.append(str);
        }
        return binaryStr.toString();
    }

    /**
     * 16进制转换成为string类型字符串
     *
     * @param s 十六进制
     * @return 返回字符串
     */
    public static String hexStringToString(String s) {

        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, StandardCharsets.UTF_8);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 10进制转16
     * @param respByte 十进制
     * @return 返回十六进制
     */
    public static String tenTo16(byte[] respByte) {
        StringBuilder stringBuffer = new StringBuilder();
        for (byte b : respByte) {
            String s = Integer.toHexString(b);
            stringBuffer.append(s).append(" ");
        }
        return stringBuffer.toString();
    }


}

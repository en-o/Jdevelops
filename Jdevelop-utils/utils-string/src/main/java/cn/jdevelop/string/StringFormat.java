package cn.jdevelop.string;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 格式化相关
 * @author tn
 * @version 1
 * @date 2020/8/11 22:08
 */
@Slf4j
public class StringFormat {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     *   获取UUID，去掉`-`的
     * @return 返回没有-的32为UUID
     */
    public static String generateStr () {
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     *   获取十六位不重复的数字
     * @return 返回十六位不重复的数字
     */
    public  static String getOutTradeNo() {
        //MMddHHmmss
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT, Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        key = key + System.currentTimeMillis();
        key = key.substring(0, 15);
        return key;
    }

    /**
     *   字符串格式化
     *
     * use: format("my name is {0}, and i like {1}!", "L.cm", "java")
     *
     * int long use {0,number,#}
     *
     * @param s 带占位符的字符串
     * @param args 占位符上的数据， 顺序放入
     * @return 转换后的字符串
     */
    public static String format(String s, Object... args) {
        return MessageFormat.format(s, args);
    }


    /**
     *   清理字符串，清理出某些不可见字符
     * @param txt 需要清理的字符串
     * @return 返回干净的字符串
     */
    public static String cleanChars(String txt) {
        return txt.replaceAll("[ 　	`·•�\\f\\t\\v]", "");
    }



    /**
     *   给字符串指定位置插入数据
     * @param num 每隔几个字符插入一个字符串（中文字符）
     * @param splitStr  待指定字符串
     * @param str 原字符串
     * @return 插入指定字符串之后的字符串
     */
    public static String addStr(int num, String splitStr, String str) {

        try{
            StringBuffer sb = new StringBuffer();
            String temp = str;
            int len = str.length();
            while (len > 0) {
                int idx = getEndIndex(temp, num);
                sb.append(temp, 0, idx + 1).append(splitStr);
                temp = temp.substring(idx + 1);
                len = temp.length();
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }
    /**
     *   给字符串指定位置插入数据
     * @param str 字符串
     * @param num 每隔几个字符插入一个字符串
     * @return int 最终索引
     */
    public static int getEndIndex(String str, double num) {
        int idx = 0;
        double val = 0.00;
        // 判断是否是英文/中文
        for (int i = 0; i < str.length(); i++) {
            if (String.valueOf(str.charAt(i)).getBytes(StandardCharsets.UTF_8).length >= 3) {
                // 中文字符或符号
                val += 1.00;
            } else {
                // 英文字符或符号
                val += 0.50;
            }
            if (val >= num) {
                idx = i;
                if (val - num == 0.5) {
                    idx = i - 1;
                }
                break;
            }
        }
        if (idx == 0) {
            idx = str.length() - 1 ;
        }
        return idx;
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
     *   (单字符)驼峰转下划线
     * @param str 单字符
     * @return 返回字符串
     */
    public static String toLine(String str){
        Pattern humpPattern = compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, "_"+matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     *   (数组)驼峰转下划线
     * @param strs 数组
     * @return 返回字符串数组
     */
    public static String[] toLine(String [] strs){
        String[] res = new String[strs.length];
        for(int i = 0; i <strs.length; i ++){
            res[i] = toLine(strs[i]);
        }
        return res;
    }


    /**
     *   打印日志 （info级别）
     * @param logs 数据
     */
    public static void consoleLog(String logs){
        try {
            log.info(logs);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

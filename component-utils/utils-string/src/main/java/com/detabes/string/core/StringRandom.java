package com.detabes.string.core;



import com.detabes.enums.random.RandomType;

import java.util.Random;

import static com.detabes.string.core.StringVerify.checkLoinName;


/**
 * 随机数相关
 * @author tn
 * @version 1
 * @ClassName StringRandom
 * @date 2020/8/11 22:06
 */
public class StringRandom {

    /**
     *  随机字符串
     */
    private static final String INT_TEMP = "0123456789";
    private static final String STR_TEMP = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String ALL_TEMP = INT_TEMP + STR_TEMP;


    /**
     *   随机生成含大小写字母，数字的组合密码
     * @param length 密码的长度
     * @return 最终生成的密码
     */
    public static String generatePassword (int length) {
        // 最终生成的密码
        String password;
        char[] ss = new char[length];
        int i=0;
        while(i<length) {
            int f = new Random().nextInt(3);
            if(f==0){
                ss[i] = (char) ('A'+Math.random()*26);
            }
            else if(f==1){
                ss[i] = (char) ('a'+Math.random()*26);
            }
            else{
                ss[i] = (char) ('0'+Math.random()*10);
            }
            i++;
        }
        password=new String(ss);
        if (!StringVerify.checkPwd(password,1)) {
            generatePassword(length);
        }
        return password;
    }

    /**
     *   生成随机用户名，数字和字母组成,
     * @param  length 设置长度
     * @return 返回随机字符串
     */
    public static String generateLoginName(int length) {

        StringBuilder val = new StringBuilder();
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (random.nextInt(26) + temp));
            } else {
                val.append(random.nextInt(10));
            }
        }
        if (!checkLoinName(val.toString())) {
            generateLoginName(length);
        }
        return val.toString();
    }


    /**
     * 随机数
     */
    private static final Random RANDOM = new Random();

    /**
     *   随机数生成
     * @param count 随机多少位
     * @param randomType 类型
     * @return 返回随机字符串
     */
    public static String random(int count, RandomType randomType) {
        if (count == 0) {
            return "";
        }
        if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }
        char[] buffer = new char[count];
        for (int i = 0; i < count; i++) {
            if (randomType.equals(RandomType.INT)) {
                buffer[i] = INT_TEMP.charAt(RANDOM.nextInt(INT_TEMP.length()));
            } else if (randomType.equals(RandomType.STRING)) {
                buffer[i] = STR_TEMP.charAt(RANDOM.nextInt(STR_TEMP.length()));
            }else {
                buffer[i] = ALL_TEMP.charAt(RANDOM.nextInt(ALL_TEMP.length()));
            }
        }
        return new String(buffer);
    }

}

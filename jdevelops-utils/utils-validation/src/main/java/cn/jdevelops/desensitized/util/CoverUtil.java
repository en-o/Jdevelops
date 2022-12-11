package cn.jdevelops.desensitized.util;

import cn.jdevelops.validation.util.StrUtil;

/**
 * 敏感数据遮掩工具
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-12-11 15:40
 */
public class CoverUtil {


    /**
     * 中文姓名
     * 只显示第一个汉字，其他隐藏为2个星号，eg：谭**
     * @param fullName 姓名
     * @return 遮掩后的姓名
     */
    public static String chineseName(String fullName) {
        return StrUtil.replace(fullName, 1);
    }

    /**
     * 身份证号
     * @param idCardNum 身份证
     * @param start     保留：前面的front位数；从1开始
     * @param end       保留：后面的end位数；从1开始
     * @return 遮掩后的身份证
     */
    public static String idCardNum(String idCardNum, int start, int end) {
        return StrUtil.replace(idCardNum, start, end);
    }

    /**
     * 固定电话
     * 前四位，后两位
     * @param num 固定电话
     * @return 遮掩后的固定电话
     */
    public static String fixedPhone(String num) {
        return StrUtil.replace(num, 4, 2);
    }

    /**
     * 手机号码
     *  前三位，后4位，其他隐藏，比如133****5140
     * @param num 移动电话；
     * @return 遮掩后的移动电话；
     */
    public static String mobilePhone(String num) {
        return StrUtil.replace(num, 3, 4);
    }

    /**
     * 地址
     *  只显示到地区，不显示详细地址，比如：成都市****
     * @param address       家庭住址
     * @param sensitiveSize 敏感信息长度
     * @return 遮掩后的家庭地址
     */
    public static String address(String address, int sensitiveSize) {
        return StrUtil.replace(address, sensitiveSize);
    }

    /**
     * 电子邮箱
     *  邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：t**@163.com
     * @param email 邮箱
     * @return 遮掩后的邮箱
     */
    public static String email(String email) {
        if (StrUtil.isBlank(email)) {
            return StrUtil.EMPTY;
        }
        int index = email.indexOf('@');
        if (index <= 1) {
            return email;
        }
        return StrUtil.replace(email, 1, email.length()-index);
    }

    /**
     * 密码直接返回 ******
     *
     * @param password 密码
     * @return 遮掩后的密码
     */
    public static String password(String password) {
        if (StrUtil.isBlank(password)) {
            return StrUtil.EMPTY;
        }
        return "******";
    }


}

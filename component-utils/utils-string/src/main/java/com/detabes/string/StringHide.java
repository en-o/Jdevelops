package com.detabes.string;

import com.detabes.string.enums.FiltrationCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 遮掩
 * @author tn
 * @version 1
 * @date 2021/1/30 20:22
 */
public class StringHide {

    private static Logger logger = LoggerFactory.getLogger(StringHide.class);


    /**
     * 姓名遮掩
     * @param userName 姓名
     * @param index 第几位开始遮掩
     * @return String
     */
    public static String hideName(String userName,int index) {
        if (StringUtils.isBlank(userName)) {
            return "";
        }
        String name = StringUtils.left(userName, index);
        return StringUtils.rightPad(name, StringUtils.length(userName), "*");
    }

    /**
     * 姓名遮掩 默认保留首尾
     * @param userName 姓名
     * @return String
     */
    public static String hideName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return "";
        }else if(userName.length()==2) {
            String name = StringUtils.left(userName, 1);
            return StringUtils.rightPad(name, StringUtils.length(userName), "*");
        }else if(userName.length()==1){
            return userName;
        }
        return userName.charAt(0) +"*"+ userName.substring(userName.length() - 1);
    }

    /**
     * 身份证遮掩
     * @param cardNo 身份证
     * @param index 第几位开始
     * @param retain 保留后几位
     * @return String
     */
    public static String hideCerCardNum(String cardNo,int index,int retain) {
        if (StringUtils.isBlank(cardNo)) {
            return "";
        }
        return StringUtils.left(cardNo, index)
                .concat(StringUtils
                        .removeStart(StringUtils.leftPad(StringUtils.right(cardNo, retain),
                                StringUtils.length(cardNo), "*"), "***"));
    }

    /**
     * 护照保留前2后3位，护照一般为8或9位
     * @param id 护照
     * @return String
     */
    public static String idPassport(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.substring(0, 2) + new String(new char[id.length() - 5]).replace("\0", "*") + id.substring(id.length() - 3);
    }

    /**电话遮掩
     *
     * @param phoneNum 手机号
     * @param retain 保留几位
     * @return String
     */
    public static String hidePhone(String phoneNum,int retain) {
        if (StringUtils.isBlank(phoneNum)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(phoneNum, retain), StringUtils.length(phoneNum), "*");
    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
     *
     * @param phoneNum phoneNum
     * @return String
     */
    public static String mobilePhone(String phoneNum) {
        if (StringUtils.isBlank(phoneNum)) {
            return "";
        }
        return StringUtils.left(phoneNum, 3)
                .concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(phoneNum, 4),
                        StringUtils.length(phoneNum), "*"), "***"));
    }

    /**
     * [固定电话] 后四位，其他隐藏<例子：****1234>
     * @param phone phone
     * @return String
     */
    public static String fixedPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(phone, 4), StringUtils.length(phone), "*");
    }


    /**
     * 邮箱遮掩
     * @param email 邮箱
     * @return String
     */
    public static String email(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 1){
            return email;
        }
        else{
            return StringUtils.rightPad(StringUtils.left(email, 1), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
        }

    }

    /**
     * 银行卡号遮掩
     * @return String
     */
    public static String bankCard(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        return StringUtils.left(cardNum, 4).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"), "******"));
    }



    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     *
     * @param address 地址
     * @param retain 遮掩几位
     * @return String
     */
    public static String address(String address, int retain) {
        if (StringUtils.isBlank(address)) {
            return "";
        }
        int length = StringUtils.length(address);
        return StringUtils.rightPad(StringUtils.left(address, length - retain), length, "*");
    }


    /**
     * .
     * 字段信息脱敏处理
     *
     * @param objStr     原始字段
     * @param methodName 过滤具体方法
     * @return 返回已脱敏字段
     */
    public static String filtration(Object objStr, FiltrationCodeEnum methodName) {
        String newStr = "";
        String oldStr = String.valueOf(objStr);
        if (StringUtils.isBlank(oldStr)) {
            return oldStr;
        }
        try {
            switch (methodName.getMethod()) {
                case "bankCard": //银行卡号
                    newStr = oldStr.replaceAll("(\\w{4})\\w*(\\w{4})", "$1***********$2");
                    break;
                case "fixedPhone"://固定电话
                    newStr = oldStr.substring(0, (oldStr.indexOf('-') + 1)) + "******" + oldStr.substring(oldStr.length() - 2);
                    break;
                case "address": //住址数字
                    newStr = oldStr.replaceAll("[\\d一二三四五六七八九十壹贰叁肆伍陆柒捌玖拾佰]+", "***");
                    break;
                case "chineseName": //中文姓名
                    String name = StringUtils.left(oldStr, 1);
                    newStr = StringUtils.rightPad(name, StringUtils.length(oldStr), "*");
                    break;
                case "email": //邮箱
                    int index = oldStr.indexOf('@');
                    if (index == 2) {//下标为2：@前面数字小于两位完全拼接显示
                        newStr = oldStr.substring(0, index) + "****" + oldStr.substring(index);
                    } else {
                        newStr = oldStr.replaceAll("(^\\w{3})[^@]*(@.*$)", "$1****$2");
                    }
                    break;
                case "maskIdCard": //证件号
                    if (oldStr.length() == 15) {
                        newStr = oldStr.replaceAll("(\\w{3})\\w*(\\w{2})", "$1**********$2");
                    } else if (oldStr.length() == 18) {
                        newStr = oldStr.replaceAll("(\\w{3})\\w*(\\w{2})", "$1*************$2");
                    }
                    break;
                case "maskPhone": //手机号
                    if (oldStr.matches("^(852|853)\\d*$")) {//港澳手机号处理
                        newStr = oldStr.substring(3, 5) + "****" + oldStr.substring((oldStr.length() - 2));
                    } else if (oldStr.matches("^(886)\\d*$")) {//台湾手机号处理 //8860930849111
                        newStr = oldStr.substring(3, 5) + "****" + oldStr.substring((oldStr.length() - 3));
                    } else {
                        newStr = oldStr.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
                    }
                    break;
                default:
                    logger.error("方法未定义,字段脱敏失败");
            }
        } catch (Exception e) {
            logger.error(String.format("处理导出字段脱敏异常[Error=%s]...", e.getMessage()));
        }
        return newStr;
    }


}

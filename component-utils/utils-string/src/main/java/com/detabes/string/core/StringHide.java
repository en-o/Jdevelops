package com.detabes.string.core;

import org.apache.commons.lang3.StringUtils;

/**
 * 遮掩
 * @author tn
 * @version 1
 * @date 2021/1/30 20:22
 */
public class StringHide {

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
        return userName.substring(0, 1) +"*"+ userName.substring(userName.length() - 1);
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
}

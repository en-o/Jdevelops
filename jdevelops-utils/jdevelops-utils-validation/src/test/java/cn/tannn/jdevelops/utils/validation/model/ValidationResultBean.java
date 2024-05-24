package cn.tannn.jdevelops.utils.validation.model;

import cn.tannn.jdevelops.utils.desensitized.annotation.Cover;
import cn.tannn.jdevelops.utils.desensitized.enums.CoverRuleEnum;


/**
 * @author tnnn
 * @version V1.0
 * @date 2022-12-05 14:47
 */
public class ValidationResultBean {

    /**
     * 手机号
     */
    @Cover(rule = CoverRuleEnum.MOBILE_PHONE)
    String iphone;


    /**
     * 手机号
     */
    @Cover(rule = CoverRuleEnum.PASSWORD)
    String iphone2;


    /**
     * 固定电话
     */
    @Cover(rule = CoverRuleEnum.FIXED_PHONE)
    String fphone;

    /**
     * 身份证
     */
    @Cover(rule = CoverRuleEnum.ID_CARD)
    String idCard;

    /**
     * 中文姓名
     */
    @Cover(rule = CoverRuleEnum.CHINESE_NAME)
    String cname;


    /**
     * 地址
     */
    @Cover(rule = CoverRuleEnum.ADDRESS)
    String address;

    /**
     * 密码
     */
    @Cover(rule = CoverRuleEnum.PASSWORD)
    String password;

    /**
     * 邮件
     */
    @Cover(rule = CoverRuleEnum.EMAIL)
    String email;


    public ValidationResultBean() {
        this.iphone = "13321285210";
        this.fphone = "03168228737";
        this.idCard = "200220129212056022";
        this.cname = "谭宁";
        this.address = "成都市金牛区二环路北三段";
        this.password = "123456";
        this.email = "c66@163.com";
    }

    public ValidationResultBean(String iphone, String fphone, String idCard, String cname, String address, String password, String email) {
        this.iphone = iphone;
        this.fphone = fphone;
        this.idCard = idCard;
        this.cname = cname;
        this.address = address;
        this.password = password;
        this.email = email;
    }

    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
    }

    public String getIphone2() {
        return iphone2;
    }

    public void setIphone2(String iphone2) {
        this.iphone2 = iphone2;
    }

    public String getFphone() {
        return fphone;
    }

    public void setFphone(String fphone) {
        this.fphone = fphone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ValidationResultBean{" +
                "iphone='" + iphone + '\'' +
                ", iphone2='" + iphone2 + '\'' +
                ", fphone='" + fphone + '\'' +
                ", idCard='" + idCard + '\'' +
                ", cname='" + cname + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

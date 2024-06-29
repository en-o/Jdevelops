package cn.tannn.jdevelops.utils.model;


import cn.tannn.jdevelops.utils.validation.account.Account;
import cn.tannn.jdevelops.utils.validation.cname.Cname;
import cn.tannn.jdevelops.utils.validation.datetime.DateTime;
import cn.tannn.jdevelops.utils.validation.idcard.IdCard;
import cn.tannn.jdevelops.utils.validation.mobile.Mobile;
import cn.tannn.jdevelops.utils.validation.password.Password;

/**
 * @author tnnn
 * @version V1.0
 * @date 2022-12-05 14:47
 */

public class ValidationUserBean {

    /**
     * 手机号
     */
    @Mobile
    String iphone;

    /**
     * 身份证
     */
    @IdCard
    String idCard;

    /**
     * 中文姓名
     */
    @Cname
    String cname;


    /**
     * 时间yyyy-MM-dd HH:mm:ss
     */
    @DateTime
    String dateTime;

    /**
     * 密码
     */
    @Password
    String password;


    /**
     * 账户名
     */
    @Account
    String account;


    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "ValidationUserBean{" +
                "iphone='" + iphone + '\'' +
                ", idCard='" + idCard + '\'' +
                ", cname='" + cname + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", password='" + password + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}

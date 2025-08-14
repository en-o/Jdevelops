package cn.tannn.jdevelops.renewpwd.pojo;

/**
 * 密码过期信息
 */
public class PwdExpireInfo {
    /**
     * 新密码密码
     */
    private final String newPassword;


    /**
     * 当前密码是否过期
     */
    private final boolean currentIsExpireSoon;

    public PwdExpireInfo(String newPassword, boolean currentIsExpireSoon) {
        this.newPassword = newPassword;
        this.currentIsExpireSoon = currentIsExpireSoon;
    }

    public String getNewPassword() {
        return newPassword;
    }


    public boolean isCurrentIsExpireSoon() {
        return currentIsExpireSoon;
    }

    @Override
    public String toString() {
        return "PwdExpireInfo{" +
                "newPassword='" + newPassword + '\'' +
                ", currentIsExpireSoon=" + currentIsExpireSoon +
                '}';
    }
}

package cn.tannn.jdevelops.renewpwd.pojo;

/**
 * 密码过期信息
 */
public class PwdExpireInfo {
    /**
     * 当前密码是否过期
     */
    private final boolean currentIsExpireSoon;

    public PwdExpireInfo(String newPassword, boolean currentIsExpireSoon) {
        this.currentIsExpireSoon = currentIsExpireSoon;
    }


    public boolean isCurrentIsExpireSoon() {
        return currentIsExpireSoon;
    }

    @Override
    public String toString() {
        return "PwdExpireInfo{" +
                ", currentIsExpireSoon=" + currentIsExpireSoon +
                '}';
    }
}

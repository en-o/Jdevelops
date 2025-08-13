package cn.tannn.jdevelops.renewpwd.pojo;

import java.time.LocalDateTime;

/**
 * 密码过期信息
 */
public class PwdExpireInfo {
    /**
     * 新密码
     */
    private final String newPassword;
    /**
     * 旧密码(当前使用中)是否10分钟内过期
     */
    private final boolean expireSoon;
    /**
     * 旧密码(当前使用中)过期时间
     */
    private final LocalDateTime expireTime;

    public PwdExpireInfo(String newPassword, boolean expireSoon, LocalDateTime expireTime) {
        this.newPassword = newPassword;
        this.expireSoon = expireSoon;
        this.expireTime = expireTime;
    }


    public String getNewPassword() {
        return newPassword;
    }

    public boolean isExpireSoon() {
        return expireSoon;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }
}

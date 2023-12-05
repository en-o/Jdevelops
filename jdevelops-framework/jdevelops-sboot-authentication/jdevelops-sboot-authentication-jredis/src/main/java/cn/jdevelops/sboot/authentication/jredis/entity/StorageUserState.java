package cn.jdevelops.sboot.authentication.jredis.entity;

/**
 * 存储用户状态
 * 判断顺序：锁定 -> 禁用
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/12/4 16:07
 */
public class StorageUserState {

    /**
     * subject  token.subject[用户唯一编码，建议登录名]
     */
    String subject;

    /**
     * 禁用[账号禁用，跟锁定有点像，但是这个是没有失效，除非管理员主动解除], 同类状态有：
     * - 删除
     * - 停用
     */
    private boolean disabledAccount;

    /**
     * 锁定[过度的尝试，判断未异常操作进行账号锁定，一般是有时效的]
     */
    private boolean excessiveAttempts;


    public StorageUserState(String subject, boolean disabledAccount, boolean excessiveAttempts) {
        this.subject = subject;
        this.disabledAccount = disabledAccount;
        this.excessiveAttempts = excessiveAttempts;
    }

    /**
     *  默认所有状态不用，内置 false
     * @param subject  token.subject[用户唯一编码，建议登录名]
     */
    public StorageUserState(String subject) {
        this.subject = subject;
        this.disabledAccount = false;
        this.excessiveAttempts = false;
    }

    public boolean isDisabledAccount() {
        return disabledAccount;
    }

    public void setDisabledAccount(boolean disabledAccount) {
        this.disabledAccount = disabledAccount;
    }

    public boolean isExcessiveAttempts() {
        return excessiveAttempts;
    }

    public void setExcessiveAttempts(boolean excessiveAttempts) {
        this.excessiveAttempts = excessiveAttempts;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "StorageUserState{" +
                "subject='" + subject + '\'' +
                ", disabledAccount=" + disabledAccount +
                ", excessiveAttempts=" + excessiveAttempts +
                '}';
    }
}

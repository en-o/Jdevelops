package cn.tannn.jdevelops.jwt.redis.entity;

import cn.tannn.jdevelops.utils.jwt.constant.UserStatusMark;

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
     * 当前用户的异常状态-user.status
     * <p> 1[正常] : 不处理
     * <p> 2[锁定]
     * <p> 3[删除[禁用]]
     */
    Integer status;

    /**
     * 异常状态码-Exception.code
     */
    Integer code;


    /**
     * 状态标记说明
     * @see UserStatusMark
     */
    String statusMark;

    /**
     * 用户无状态
     *
     * @param subject subject  token.subject[用户唯一编码，建议登录名]
     */
    public StorageUserState(String subject) {
        this.subject = subject;
    }

    /**
     * 用户存在异常
     *
     * @param subject    subject  token.subject[用户唯一编码，建议登录名]
     * @param status     当前用户状态
     * @param statusMark 状态标记
     */
    public StorageUserState(String subject, int status, int code, String statusMark) {
        this.subject = subject;
        this.code = code;
        this.status = status;
        this.statusMark = statusMark;
    }

    /**
     * 用户存在异常
     *
     * @param subject    subject  token.subject[用户唯一编码，建议登录名]
     * @param status     当前用户状态
     * @param statusMark 状态标记
     */
    public StorageUserState(String subject, int status, String statusMark) {
        this.subject = subject;
        this.code = 400;
        this.status = status;
        this.statusMark = statusMark;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusMark() {
        return statusMark;
    }

    public void setStatusMark(String statusMark) {
        this.statusMark = statusMark;
    }

    public Integer getCode() {
        return code == null ? 400 : code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "StorageUserState{" +
                "subject='" + subject + '\'' +
                ", status=" + status +
                ", code=" + code +
                ", statusMark='" + statusMark + '\'' +
                '}';
    }
}

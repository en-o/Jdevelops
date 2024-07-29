package cn.tannn.jdevelops.utils.jwt.constant;

/**
 * 用户异常状态码示例
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/7/29 下午1:36
 */
public interface UserStatusMark {

    // 锁定
    String BLACKLIST = "非白名单用户,请联系管理员";
    String BANNED = "账户违规封禁,请联系管理";
    String WAIT_AUDIT = "账号待审核,请联系管理";
    String USER_FAULT = "账户失效，请联系管理员";
    String EXCESSIVE_ATTEMPTS = "帐号由于多次认证失败被管理员被禁用,请联系管理员";
    String LOGIN_FREQUENTLY = "频繁登录请稍后再试";
    String AUDIT_FAIL = "审核不通过,请联系管理";


    // 逻辑删除
    String DISABLED = "账户已停用";
    String DELETE = "管理员删除用户";

}

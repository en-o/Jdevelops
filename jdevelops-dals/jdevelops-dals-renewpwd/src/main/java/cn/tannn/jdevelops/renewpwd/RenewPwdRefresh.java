package cn.tannn.jdevelops.renewpwd;

import java.util.List;

/**
 * 密码续命处理
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/2 下午8:49
 */
public interface RenewPwdRefresh {
    /**
     * 修复过期密码并生效 - 上下文取密码
     * <p> 用当前过期密码修改备用密码，备用密码没有则重置过期密码</p>
     */
    void fixPassword();




}

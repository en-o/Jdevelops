package cn.tannn.jdevelops.renewpwd;

import cn.tannn.jdevelops.renewpwd.pojo.DbType;

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
     * <p> 默认mysql</p>
     * <p> 用当前过期密码修改备用密码，备用密码没有则重置过期密码</p>
     */
    void fixPassword();

    /**
     * 修复过期密码并生效 - 上下文取密码
     * <p> 用当前过期密码修改备用密码，备用密码没有则重置过期密码</p>
     * @param dbType {@link DbType}
     */
    void fixPassword(DbType dbType);

    /**
     * 修复错误密码并生效 - 上下文取密码
     * <p> 当前连接错误的情况下，验证备份密码是否可用，可用则替换，不可用就不管了（备份密码没有也不管了）
     */
    void updatePassword();



}

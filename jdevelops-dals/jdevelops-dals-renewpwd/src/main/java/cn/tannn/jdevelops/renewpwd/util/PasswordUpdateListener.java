package cn.tannn.jdevelops.renewpwd.util;


/**
 * 密码更新监听器(利用了回调机制，实现了密码更新后的自动通知和刷新。)
 * <P> 1. 定义一个密码更新监听器接口（如 PasswordUpdateListener），声明 onPasswordUpdated 方法。
 * <P> 2. 在 ExecuteJdbcSql 中添加静态监听器字段和注册方法，密码更新成功时回调监听器。
 * <P> 3. 让 DatabasePwdEnvironmentPostProcessor 实现监听器接口，并在初始化时注册自己为监听器。
 * <P> 4. 当 ExecuteJdbcSql.updateUserPassword 成功后，自动调用监听器的 onPasswordUpdated 方法，DatabasePwdEnvironmentPostProcessor 在回调中用新密码重新初始化 RenewPasswordService
 */
public interface PasswordUpdateListener {
    void onPasswordUpdated(String newPassword);
}

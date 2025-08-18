package cn.tannn.jdevelops.renewpwd.exception;


import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * ConnectionProxyFactory
 * 用于创建 Connection 的代理实例，拦截所有数据库连接操作。
 * 实现全局异常处理和性能监控。
 */
final class ConnectionProxyFactory {
    static Connection createProxy(SQLExceptionHandlingDataSourceProxy proxy, Connection target) {
        return (Connection) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                new Class[]{Connection.class},
                new ConnectionInvocationHandler(proxy, target)
        );
    }
}

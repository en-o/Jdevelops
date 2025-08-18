package cn.tannn.jdevelops.renewpwd.exception;

import java.lang.reflect.Proxy;
import java.sql.Statement;


/**
 * StatementProxyFactory
 * 用于创建 {@link Statement}  的代理实例，拦截 SQL 操作。
 * 实现全局异常处理和性能监控。
 */
final class StatementProxyFactory {
    static Statement createProxy(SQLExceptionHandlingDataSourceProxy proxy, Statement stmt) {
        return (Statement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                new Class[]{Statement.class},
                new StatementInvocationHandler(proxy, stmt)
        );
    }
}

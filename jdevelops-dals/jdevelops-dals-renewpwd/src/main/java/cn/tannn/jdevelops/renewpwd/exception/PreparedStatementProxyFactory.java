package cn.tannn.jdevelops.renewpwd.exception;

import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;


/**
 * PreparedStatementProxyFactory
 * 用于创建 PreparedStatement 的代理实例，拦截 SQL 执行和参数设置。
 * 实现全局异常处理和性能监控。
 */
final class PreparedStatementProxyFactory {
    static PreparedStatement createProxy(SQLExceptionHandlingDataSourceProxy proxy,
                                         PreparedStatement stmt, Object[] sqlArgs) {
        return (PreparedStatement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                new Class[]{PreparedStatement.class},
                new PreparedStatementInvocationHandler(proxy, stmt, sqlArgs)
        );
    }
}

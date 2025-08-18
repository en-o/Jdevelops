package cn.tannn.jdevelops.renewpwd.exception;

import java.lang.reflect.Proxy;
import java.sql.CallableStatement;

/**
 * CallableStatementProxyFactory
 * 用于创建 CallableStatement 的代理实例，拦截 SQL 执行和参数设置。
 * 实现全局异常处理和性能监控。
 */
final class CallableStatementProxyFactory {
    static CallableStatement createProxy(SQLExceptionHandlingDataSourceProxy proxy,
                                         CallableStatement stmt, Object[] sqlArgs) {
        return (CallableStatement) Proxy.newProxyInstance(
                stmt.getClass().getClassLoader(),
                new Class[]{CallableStatement.class},
                new CallableStatementInvocationHandler(proxy, stmt, sqlArgs)
        );
    }
}

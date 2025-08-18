package cn.tannn.jdevelops.renewpwd.exception;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ConnectionInvocationHandler
 * 负责拦截 Connection 的方法调用，提供全局异常处理和性能监控。
 * 通过代理模式确保所有数据库连接操作的统一处理。
 */
final class ConnectionInvocationHandler implements InvocationHandler {
    private final SQLExceptionHandlingDataSourceProxy proxy;
    private final Connection targetConnection;

    ConnectionInvocationHandler(SQLExceptionHandlingDataSourceProxy proxy, Connection targetConnection) {
        this.proxy = proxy;
        this.targetConnection = targetConnection;
    }

    @Override
    public Object invoke(Object proxyObj, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        long startTime = System.currentTimeMillis();
        try {
            Object result = method.invoke(targetConnection, args);

            if (result instanceof PreparedStatement) {
                return PreparedStatementProxyFactory.createProxy(
                        this.proxy, (PreparedStatement) result, args);
            } else if (result instanceof java.sql.CallableStatement) {
                return CallableStatementProxyFactory.createProxy(
                        this.proxy, (java.sql.CallableStatement) result, args);
            } else if (result instanceof Statement) {
                return StatementProxyFactory.createProxy(this.proxy, (Statement) result);
            }
            return result;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            Throwable cause = e.getCause();
            if (cause instanceof SQLException) {
                SQLExceptionHandlingHelper.handleConnectionException(
                        proxy, (SQLException) cause, methodName, args, duration);
            }
            throw e;
        }
    }
}

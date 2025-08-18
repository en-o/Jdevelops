package cn.tannn.jdevelops.renewpwd.exception;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Statement;

/**
 * StatementInvocationHandler
 * 负责拦截  {@link Statement} 的方法调用，提供全局异常处理和性能监控。
 * 通过代理模式确保 SQL 操作的统一处理。
 */
final class StatementInvocationHandler implements InvocationHandler {
    private final SQLExceptionHandlingDataSourceProxy proxy;
    private final java.sql.Statement targetStatement;

    StatementInvocationHandler(SQLExceptionHandlingDataSourceProxy proxy,
                               java.sql.Statement targetStatement) {
        this.proxy = proxy;
        this.targetStatement = targetStatement;
    }

    @Override
    public Object invoke(Object proxyObj, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        long startTime = System.currentTimeMillis();
        try {
            Object result = method.invoke(targetStatement, args);

            if (SQLExceptionHandlingHelper.isExecuteMethod(methodName)) {
                long duration = System.currentTimeMillis() - startTime;
                proxy.totalOperations.incrementAndGet();
                SQLExceptionHandlingHelper.logSuccessfulExecution(proxy, methodName, args, duration);

                if (duration > proxy.getConfig().getException().getSlowQueryThreshold()) {
                    proxy.totalSlowQueries.incrementAndGet();
                    SQLExceptionHandlingHelper.handleSlowQuery(proxy, methodName, args, duration);
                }
            }
            return result;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            proxy.totalExceptions.incrementAndGet();
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLException) {
                SQLExceptionHandlingHelper.handleStatementException(
                        proxy, (java.sql.SQLException) cause, methodName, args, duration);
            }
            throw e;
        }
    }
}

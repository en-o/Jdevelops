package cn.tannn.jdevelops.renewpwd.exception;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * CallableStatementInvocationHandler
 * 负责拦截 数据库存储过程交互 的方法调用，提供全局异常处理和性能监控。
 * 通过代理模式确保存储过程调用的统一处理。
 */
final class CallableStatementInvocationHandler implements InvocationHandler {
    private final SQLExceptionHandlingDataSourceProxy proxy;
    private final java.sql.CallableStatement targetStatement;
    private final Object[] sqlArgs;

    CallableStatementInvocationHandler(SQLExceptionHandlingDataSourceProxy proxy,
                                       java.sql.CallableStatement targetStatement,
                                       Object[] sqlArgs) {
        this.proxy = proxy;
        this.targetStatement = targetStatement;
        this.sqlArgs = sqlArgs;
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
                SQLExceptionHandlingHelper.logSuccessfulExecution(
                        proxy, "存储过程:" + methodName, sqlArgs, duration);

                if (duration > proxy.getConfig().getException().getSlowQueryThreshold()) {
                    proxy.totalSlowQueries.incrementAndGet();
                    SQLExceptionHandlingHelper.handleSlowQuery(
                            proxy, "存储过程:" + methodName, sqlArgs, duration);
                }
            }
            return result;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            proxy.totalExceptions.incrementAndGet();
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLException) {
                SQLExceptionHandlingHelper.handleStatementException(
                        proxy, (java.sql.SQLException) cause, "存储过程:" + methodName, sqlArgs, duration);
            }
            throw e;
        }
    }
}

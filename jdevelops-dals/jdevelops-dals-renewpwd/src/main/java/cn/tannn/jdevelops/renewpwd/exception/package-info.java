/**
 * 此包中的类通过代理模式实现了对数据库操作的全局异常处理和性能监控。
 *
 * 构建执行顺序如下：
 *
 * 1. **SQLExceptionHandlingDataSourceProxy**:
 *    - 初始化代理数据源，拦截 `Connection` 的获取。
 *    - 通过 `ConnectionProxyFactory` 创建 `Connection` 的代理实例。
 *
 * 2. **ConnectionProxyFactory**:
 *    - 创建 `Connection` 的代理实例。
 *    - 使用 `ConnectionInvocationHandler` 拦截 `Connection` 的方法调用。
 *
 * 3. **ConnectionInvocationHandler**:
 *    - 拦截 `Connection` 的方法调用。
 *    - 根据返回值类型，使用以下工厂创建相应的代理：
 *      - `PreparedStatementProxyFactory`：创建 `PreparedStatement` 的代理。
 *      - `CallableStatementProxyFactory`：创建 `CallableStatement` 的代理。
 *      - `StatementProxyFactory`：创建 `Statement` 的代理。
 *
 * 4. **PreparedStatementProxyFactory**:
 *    - 创建 `PreparedStatement` 的代理实例。
 *    - 使用 `PreparedStatementInvocationHandler` 拦截方法调用。
 *
 * 5. **CallableStatementProxyFactory**:
 *    - 创建 `CallableStatement` 的代理实例。
 *    - 使用 `CallableStatementInvocationHandler` 拦截方法调用。
 *
 * 6. **StatementProxyFactory**:
 *    - 创建 `Statement` 的代理实例。
 *    - 使用 `StatementInvocationHandler` 拦截方法调用。
 *
 * 7. **Invocation Handlers**:
 *    - `PreparedStatementInvocationHandler`、`CallableStatementInvocationHandler` 和 `StatementInvocationHandler`：
 *      - 拦截方法调用，记录 SQL 执行日志。
 *      - 处理异常并记录慢查询。
 *
 * 8. **SQLExceptionHandlingHelper**:
 *    - 提供静态工具方法，用于日志记录、异常分类处理、慢查询检测和告警发送。
 */
package cn.tannn.jdevelops.renewpwd.exception;

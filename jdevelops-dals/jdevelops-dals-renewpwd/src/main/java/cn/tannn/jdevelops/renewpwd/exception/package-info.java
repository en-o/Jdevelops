/**
 * 此包中的类通过代理模式实现了对数据库操作的全局异常处理和性能监控。
 *
 * 构建执行顺序如下：
 *
 * 1. **SQLExceptionHandlingDataSourceProxy**:
 *    - 初始化代理数据源，拦截 `Connection` 的获取。
 *    - 对异常连接异常进行拦截
 */
package cn.tannn.jdevelops.renewpwd.exception;

package cn.tannn.jdevelops.jdectemplate.xmlmapper.proxy;

import cn.tannn.jdevelops.annotations.jdbctemplate.xml.*;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageRequest;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageResult;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.registry.XmlMapperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * XML Mapper 动态代理拦截器
 * <p>专门处理 @XmlMapper 注解的接口方法调用</p>
 * <p>不干扰原有的 @Query 注解功能</p>
 *
 * @author tnnn
 */
public class XmlMapperProxyInterceptor implements InvocationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(XmlMapperProxyInterceptor.class);

    /**
     * XML Mapper 命名空间（接口全限定名）
     */
    private final String namespace;

    /**
     * XML Mapper 注册器
     */
    private final XmlMapperRegistry registry;

    public XmlMapperProxyInterceptor(String namespace, XmlMapperRegistry registry) {
        this.namespace = namespace;
        this.registry = registry;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 处理 Object 类的方法
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }

        // 处理默认方法
        if (method.isDefault()) {
            return invokeDefaultMethod(proxy, method, args);
        }

        // 处理 XML Mapper 方法
        return executeXmlMapperMethod(method, args);
    }

    /**
     * 执行 XML Mapper 方法
     */
    private Object executeXmlMapperMethod(Method method, Object[] args) {
        // 获取方法上的 XML 注解
        XmlPageSelect pageSelect = method.getAnnotation(XmlPageSelect.class);
        XmlSelect select = method.getAnnotation(XmlSelect.class);
        XmlInsert insert = method.getAnnotation(XmlInsert.class);
        XmlUpdate update = method.getAnnotation(XmlUpdate.class);
        XmlDelete delete = method.getAnnotation(XmlDelete.class);

        String statementId;
        boolean tryc;

        if (pageSelect != null) {
            // 分页查询
            return executePageQuery(pageSelect, method, args);
        } else if (select != null) {
            statementId = select.value();
            tryc = select.tryc();
            return executeQuery(statementId, method, args, tryc);
        } else if (insert != null) {
            statementId = insert.value();
            tryc = insert.tryc();
            return executeUpdate(statementId, method, args, tryc);
        } else if (update != null) {
            statementId = update.value();
            tryc = update.tryc();
            return executeUpdate(statementId, method, args, tryc);
        } else if (delete != null) {
            statementId = delete.value();
            tryc = delete.tryc();
            return executeUpdate(statementId, method, args, tryc);
        } else {
            throw new IllegalStateException(
                    "Method " + method.getName() + " must be annotated with @XmlPageSelect, @XmlSelect, @XmlInsert, @XmlUpdate or @XmlDelete");
        }
    }

    /**
     * 执行分页查询
     */
    private Object executePageQuery(XmlPageSelect pageSelect, Method method, Object[] args) {
        try {
            // 1. 提取参数：查询参数 和 PageRequest
            Object queryParameter = null;
            PageRequest pageRequest = null;

            if (args != null) {
                for (Object arg : args) {
                    if (arg instanceof PageRequest) {
                        pageRequest = (PageRequest) arg;
                    } else {
                        queryParameter = arg;
                    }
                }
            }

            // 验证参数
            if (pageRequest == null) {
                throw new IllegalArgumentException(
                        "Method " + method.getName() + " requires a PageRequest parameter for @XmlPageSelect annotation");
            }

            // 验证返回类型
            if (!PageResult.class.isAssignableFrom(method.getReturnType())) {
                throw new IllegalStateException(
                        "Method " + method.getName() + " with @XmlPageSelect must return PageResult type");
            }

            // 2. 获取结果类型（PageResult的泛型类型）
            Class<?> resultType = getResultType(method);

            // 3. 获取 SQL 语句 ID
            String dataStatementId = pageSelect.dataStatement();
            String countStatementId = pageSelect.countStatement();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Executing XML Mapper page query: {}.{}, countStatement: {}",
                        namespace, dataStatementId, countStatementId.isEmpty() ? "auto" : countStatementId);
            }

            // 4. 执行分页查询
            return registry.executePageQuery(
                    namespace,
                    dataStatementId,
                    countStatementId.isEmpty() ? null : countStatementId,
                    queryParameter,
                    pageRequest,
                    resultType,
                    pageSelect.tryc()
            );

        } catch (Exception e) {
            if (pageSelect.tryc()) {
                LOG.error("XML Mapper page query error (tryc=true): {}.{}",
                        namespace, pageSelect.dataStatement(), e);
                // 返回空的分页结果
                PageRequest defaultPage = new PageRequest(1, 10);
                return new PageResult<>(1, 10, 0L, java.util.Collections.emptyList());
            } else {
                throw new RuntimeException(
                        "Failed to execute XML Mapper page query: " + namespace + "." + pageSelect.dataStatement(), e);
            }
        }
    }

    /**
     * 执行查询操作
     */
    private Object executeQuery(String statementId, Method method, Object[] args, boolean tryc) {
        try {
            // 获取返回类型
            Class<?> resultType = getResultType(method);

            // 获取参数（支持单个参数或多个参数）
            Object parameter = getParameter(args);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Executing XML Mapper query: {}.{}, resultType: {}",
                        namespace, statementId, resultType.getName());
            }

            // 执行查询
            Object result = registry.executeQuery(namespace, statementId, parameter, resultType);

            // 根据方法返回类型处理结果
            return adaptResult(result, method);
        } catch (Exception e) {
            if (tryc) {
                LOG.error("XML Mapper query error (tryc=true): {}.{}", namespace, statementId, e);
                return null;
            } else {
                throw new RuntimeException("Failed to execute XML Mapper query: " + namespace + "." + statementId, e);
            }
        }
    }

    /**
     * 根据方法返回类型适配查询结果
     */
    @SuppressWarnings("unchecked")
    private Object adaptResult(Object result, Method method) {
        if (result == null) {
            return null;
        }

        Type returnType = method.getGenericReturnType();

        // 如果返回类型是 List，确保返回 List
        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();

            if (java.util.List.class.isAssignableFrom(rawType)) {
                // 期望返回 List
                if (result instanceof java.util.List) {
                    return result;
                } else {
                    // 单个对象，包装成 List
                    return java.util.Collections.singletonList(result);
                }
            }
        }

        // 如果返回类型不是 List，确保返回单个对象
        if (result instanceof java.util.List) {
            java.util.List<?> list = (java.util.List<?>) result;
            return list.isEmpty() ? null : list.get(0);
        }

        return result;
    }

    /**
     * 执行更新操作（insert/update/delete）
     */
    private Object executeUpdate(String statementId, Method method, Object[] args, boolean tryc) {
        try {
            // 获取参数
            Object parameter = getParameter(args);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Executing XML Mapper update: {}.{}", namespace, statementId);
            }

            // 获取方法返回类型
            Class<?> returnType = method.getReturnType();

            // 执行更新
            Object result = registry.executeUpdate(namespace, statementId, parameter);

            // 根据返回类型适配结果
            return adaptUpdateResult(result, returnType);
        } catch (Exception e) {
            if (tryc) {
                LOG.error("XML Mapper update error (tryc=true): {}.{}", namespace, statementId, e);
                return getDefaultReturnValue(method.getReturnType());
            } else {
                throw new RuntimeException("Failed to execute XML Mapper update: " + namespace + "." + statementId, e);
            }
        }
    }

    /**
     * 适配更新操作的返回结果
     */
    private Object adaptUpdateResult(Object result, Class<?> returnType) {
        if (result == null) {
            return getDefaultReturnValue(returnType);
        }

        // 如果返回类型是 void，直接返回 null
        if (returnType == void.class || returnType == Void.class) {
            return null;
        }

        // 如果结果是 Number 类型
        if (result instanceof Number) {
            Number number = (Number) result;

            // 根据返回类型转换
            if (returnType == int.class || returnType == Integer.class) {
                return number.intValue();
            } else if (returnType == long.class || returnType == Long.class) {
                return number.longValue();
            } else if (returnType == short.class || returnType == Short.class) {
                return number.shortValue();
            } else if (returnType == byte.class || returnType == Byte.class) {
                return number.byteValue();
            } else if (returnType == double.class || returnType == Double.class) {
                return number.doubleValue();
            } else if (returnType == float.class || returnType == Float.class) {
                return number.floatValue();
            } else if (returnType == String.class) {
                return String.valueOf(number);
            }
        }

        return result;
    }

    /**
     * 获取默认返回值
     */
    private Object getDefaultReturnValue(Class<?> returnType) {
        if (returnType == void.class || returnType == Void.class) {
            return null;
        }
        if (returnType == boolean.class) {
            return false;
        }
        if (returnType.isPrimitive()) {
            return 0;
        }
        return null;
    }

    /**
     * 获取结果类型
     */
    private Class<?> getResultType(Method method) {
        Type returnType = method.getGenericReturnType();

        if (returnType instanceof ParameterizedType) {
            // 泛型类型，如 List<User>、PageResult<User>
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                return (Class<?>) actualTypeArguments[0];
            }
        }

        // 普通类型，如 User、Integer
        return method.getReturnType();
    }

    /**
     * 获取参数对象
     * <p>如果只有一个参数，直接返回；如果有多个参数，包装成 Map</p>
     */
    private Object getParameter(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        if (args.length == 1) {
            return args[0];
        }
        // 多个参数时，可以考虑包装成 Map，键为 param1, param2...
        // 这里简化处理，只取第一个参数
        return args[0];
    }

    /**
     * 调用默认方法
     */
    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
        // Java 8+ default method
        return java.lang.reflect.InvocationHandler.invokeDefault(proxy, method, args);
    }
}

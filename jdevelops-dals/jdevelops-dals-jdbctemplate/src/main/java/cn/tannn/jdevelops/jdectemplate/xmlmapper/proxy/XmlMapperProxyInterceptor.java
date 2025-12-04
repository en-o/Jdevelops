package cn.tannn.jdevelops.jdectemplate.xmlmapper.proxy;

import cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlDelete;
import cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlInsert;
import cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlSelect;
import cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlUpdate;
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
        XmlSelect select = method.getAnnotation(XmlSelect.class);
        XmlInsert insert = method.getAnnotation(XmlInsert.class);
        XmlUpdate update = method.getAnnotation(XmlUpdate.class);
        XmlDelete delete = method.getAnnotation(XmlDelete.class);

        String statementId = null;
        boolean tryc = false;

        if (select != null) {
            statementId = select.value();
            tryc = select.tryc();
            return executeQuery(statementId, method, args, tryc);
        } else if (insert != null) {
            statementId = insert.value();
            tryc = insert.tryc();
            return executeUpdate(statementId, args, tryc);
        } else if (update != null) {
            statementId = update.value();
            tryc = update.tryc();
            return executeUpdate(statementId, args, tryc);
        } else if (delete != null) {
            statementId = delete.value();
            tryc = delete.tryc();
            return executeUpdate(statementId, args, tryc);
        } else {
            throw new IllegalStateException(
                    "Method " + method.getName() + " must be annotated with @XmlSelect, @XmlInsert, @XmlUpdate or @XmlDelete");
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
            return registry.executeQuery(namespace, statementId, parameter, resultType);
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
     * 执行更新操作（insert/update/delete）
     */
    private Object executeUpdate(String statementId, Object[] args, boolean tryc) {
        try {
            // 获取参数
            Object parameter = getParameter(args);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Executing XML Mapper update: {}.{}", namespace, statementId);
            }

            // 执行更新
            return registry.executeUpdate(namespace, statementId, parameter);
        } catch (Exception e) {
            if (tryc) {
                LOG.error("XML Mapper update error (tryc=true): {}.{}", namespace, statementId, e);
                return 0;
            } else {
                throw new RuntimeException("Failed to execute XML Mapper update: " + namespace + "." + statementId, e);
            }
        }
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

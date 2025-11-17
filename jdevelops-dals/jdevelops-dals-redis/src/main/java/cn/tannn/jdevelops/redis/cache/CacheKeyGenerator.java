package cn.tannn.jdevelops.redis.cache;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

public class CacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return generateKey(
                target.getClass().getName(), // Use full class name including package
                method.getName(),
                params
        );
    }

    public static String generateKey(String className, String methodName, Object... params) {
        StringBuilder key = new StringBuilder();
        key.append(className)
                .append(":")
                .append(methodName);
        if (params != null) {
            for (Object param : params) {
                key.append(":");
                if (param == null) {
                    key.append("null");
                } else {
                    // 使用对象的类名和hashCode
                    key.append(param.getClass().getName())
                            .append("#")
                            .append(param.hashCode());
                }
            }
        }
        return key.toString();
    }
}

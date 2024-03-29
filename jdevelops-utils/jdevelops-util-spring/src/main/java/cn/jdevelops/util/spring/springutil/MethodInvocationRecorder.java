/*
 * Copyright 2016-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.jdevelops.util.spring.springutil;

import lombok.*;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.core.CollectionFactory;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;

/**
 * API to record method invocations via method references on a proxy.
 *
 * @author Oliver Gierke
 * @since 2.2
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodInvocationRecorder {

    public static PropertyNameDetectionStrategy DEFAULT = DefaultPropertyNameDetectionStrategy.INSTANCE;

    private Optional<RecordingMethodInterceptor> interceptor;

    /**
     * Creates a new MethodInvocationRecorder . For ad-hoc instantation prefer the static
     * forProxyOf(Class).
     */
    private MethodInvocationRecorder() {
        this(Optional.empty());
    }

    /**
     * Creates a new Recorded for the given type.
     *
     * @param type must not be {@literal null}.
     */
    public static <T> Recorded<T> forProxyOf(Class<T> type) {

        Assert.notNull(type, "Type must not be null!");
        Assert.isTrue(!Modifier.isFinal(type.getModifiers()), "Type to record invocations on must not be final!");

        return new MethodInvocationRecorder().create(type);
    }

    /**
     * Creates a new Recorded for the given type based on the current  MethodInvocationRecorder setup.
     *
     * @param type type
     */
    @SuppressWarnings("unchecked")
    private <T> Recorded<T> create(Class<T> type) {

        RecordingMethodInterceptor interceptor = new RecordingMethodInterceptor();

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addAdvice(interceptor);

        if (!type.isInterface()) {
            proxyFactory.setTargetClass(type);
            proxyFactory.setProxyTargetClass(true);
        } else {
            proxyFactory.addInterface(type);
        }

        T proxy = (T) proxyFactory.getProxy(type.getClassLoader());

        return new Recorded<>(proxy, new MethodInvocationRecorder(Optional.of(interceptor)));
    }

    private Optional<String> getPropertyPath(List<PropertyNameDetectionStrategy> strategies) {
        return interceptor.flatMap(it -> it.getPropertyPath(strategies));
    }

    private class RecordingMethodInterceptor implements org.aopalliance.intercept.MethodInterceptor {

        private InvocationInformation information = InvocationInformation.NOT_INVOKED;

        /*
         * (non-Javadoc)
         * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
         */
        @Override
        @SuppressWarnings("null")
        public Object invoke(MethodInvocation invocation) throws Throwable {

            Method method = invocation.getMethod();
            Object[] arguments = invocation.getArguments();

            if (ReflectionUtils.isObjectMethod(method)) {
                return method.invoke(this, arguments);
            }

            ResolvableType type = ResolvableType.forMethodReturnType(method);
            Class<?> rawType = type.resolve(Object.class);

            if (Collection.class.isAssignableFrom(rawType)) {

                Class<?> clazz = type.getGeneric(0).resolve(Object.class);

                InvocationInformation information = registerInvocation(method, clazz);

                Collection<Object> collection = CollectionFactory.createCollection(rawType, 1);
                collection.add(information.getCurrentInstance());

                return collection;
            }

            if (Map.class.isAssignableFrom(rawType)) {

                Class<?> clazz = type.getGeneric(1).resolve(Object.class);
                InvocationInformation information = registerInvocation(method, clazz);

                Map<Object, Object> map = CollectionFactory.createMap(rawType, 1);
                map.put("_key_", information.getCurrentInstance());

                return map;
            }

            return registerInvocation(method, rawType).getCurrentInstance();
        }

        private Optional<String> getPropertyPath(List<PropertyNameDetectionStrategy> strategies) {
            return this.information.getPropertyPath(strategies);
        }

        private InvocationInformation registerInvocation(Method method, Class<?> proxyType) {

            Recorded<?> create = Modifier.isFinal(proxyType.getModifiers()) ? new Unrecorded() : create(proxyType);
            InvocationInformation information = new InvocationInformation(create, method);

            return this.information = information;
        }
    }

    @Value
    private static class InvocationInformation {

        static final InvocationInformation NOT_INVOKED = new InvocationInformation(new Unrecorded(), null);

        @NonNull Recorded<?> recorded;
        @Nullable Method invokedMethod;

        @Nullable
        Object getCurrentInstance() {
            return recorded.currentInstance;
        }

        Optional<String> getPropertyPath(List<PropertyNameDetectionStrategy> strategies) {

            Method invokedMethod = this.invokedMethod;

            if (invokedMethod == null) {
                return Optional.empty();
            }

            String propertyName = getPropertyName(invokedMethod, strategies);
            Optional<String> next = recorded.getPropertyPath(strategies);

            return Optionals.firstNonEmpty(() -> next.map(it -> propertyName.concat(".").concat(it)), //
                    () -> Optional.of(propertyName));
        }

        private static String getPropertyName(Method invokedMethod, List<PropertyNameDetectionStrategy> strategies) {

            return strategies.stream() //
                    .map(it -> it.getPropertyName(invokedMethod)) //
                    .findFirst() //
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("No property name found for method %s!", invokedMethod)));
        }
    }

    public interface PropertyNameDetectionStrategy {
        /**
         *  getPropertyName
         * @param method method
         * @return String
         */
        @Nullable
        String getPropertyName(Method method);
    }

    private enum DefaultPropertyNameDetectionStrategy implements PropertyNameDetectionStrategy {
        /**
         * spring的我也不知道干嘛的
         */
        INSTANCE;

        @Nonnull
        @Override
        public String getPropertyName(Method method) {
            return getPropertyName(method.getReturnType(), method.getName());
        }

        private static String getPropertyName(Class<?> type, String methodName) {

            String pattern = getPatternFor(type);
            String replaced = methodName.replaceFirst(pattern, "");

            return StringUtils.uncapitalize(replaced);
        }

        private static String getPatternFor(Class<?> type) {
            return type.equals(boolean.class) ? "^(is)" : "^(get|set)";
        }
    }

    @ToString
    @RequiredArgsConstructor
    public static class Recorded<T> {

        private final @Nullable T currentInstance;
        private final @Nullable MethodInvocationRecorder recorder;

        public Optional<String> getPropertyPath() {
            return getPropertyPath(MethodInvocationRecorder.DEFAULT);
        }

        public Optional<String> getPropertyPath(PropertyNameDetectionStrategy strategy) {

            MethodInvocationRecorder recorder = this.recorder;

            return recorder == null ? Optional.empty() : recorder.getPropertyPath(Collections.singletonList(strategy));
        }

        public Optional<String> getPropertyPath(List<PropertyNameDetectionStrategy> strategies) {

            MethodInvocationRecorder recorder = this.recorder;

            return recorder == null ? Optional.empty() : recorder.getPropertyPath(strategies);
        }

        /**
         * Applies the given Converter to the recorded value and remembers the property accessed.
         *
         * @param converter must not be {@literal null}.
         */
        public <S> Recorded<S> record(Function<? super T, S> converter) {

            Assert.notNull(converter, "Function must not be null!");

            return new Recorded<>(converter.apply(currentInstance), recorder);
        }




        public interface ToCollectionConverter<T, S> extends Function<T, Collection<S>> {}

        public interface ToMapConverter<T, S> extends Function<T, Map<?, S>> {}
    }

    static class Unrecorded extends Recorded<Object> {

        @SuppressWarnings("null")
        private Unrecorded() {
            super(null, null);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.util.MethodInvocationRecorder.Recorded#getPropertyPath(java.util.List)
         */
        @Override
        public Optional<String> getPropertyPath(List<PropertyNameDetectionStrategy> strategies) {
            return Optional.empty();
        }
    }
}

package cn.jdevelops.spring.entity;

import cn.jdevelops.spring.springutil.MethodInvocationRecorder;

import java.io.Serializable;
import java.util.function.Function;

/**
 * TypedBeanTest.java
 * @author tn
 */
public class TypedBean<T> implements Serializable {

    private static final long serialVersionUID = -3550403511206745880L;
    private final MethodInvocationRecorder.Recorded<T> recorded;

    public static <T> TypedBean<T> of(Class<T> type) {
        return new TypedBean<>(type);
    }

    public TypedBean(Class<T> type) {
        this(MethodInvocationRecorder.forProxyOf(type));
    }

    private TypedBean(MethodInvocationRecorder.Recorded<T> recorded) {
        this.recorded = recorded;
    }

    public <S> String getKey(Function<T, S> property) {
        return new TypedBean<>(this.recorded.record(property)).toString();
    }

    @Override
    public String toString() {
        return this.recorded.getPropertyPath().orElseGet(String::new);
    }
}
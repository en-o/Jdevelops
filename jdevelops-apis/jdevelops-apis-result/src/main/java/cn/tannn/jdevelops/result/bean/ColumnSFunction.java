package cn.tannn.jdevelops.result.bean;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 获取属性名
 * @author tnnn
 */
@FunctionalInterface
public interface ColumnSFunction<T, R> extends Function<T, R>, Serializable {
}

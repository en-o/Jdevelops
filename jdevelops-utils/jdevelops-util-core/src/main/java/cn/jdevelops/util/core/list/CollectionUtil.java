package cn.jdevelops.util.core.list;

/**
 * 集合
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/109:33
 */
public class CollectionUtil {


    /**
     * 判空
     * @param array 数组
     * @return true空
     * @param <T> 数组类型
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }
}

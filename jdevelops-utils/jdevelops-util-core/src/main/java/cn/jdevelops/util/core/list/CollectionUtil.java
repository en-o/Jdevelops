package cn.jdevelops.util.core.list;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * 判空
     * Null-safe check if the specified collection is empty.
     * <p>
     * Null returns true.[true空]
     * </p>
     *
     * @param coll  the collection to check, may be null
     * @return true if empty or null
     * @since 3.2
     */
    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * Null-safe check if the specified collection is not empty.
     * <p>
     * Null returns false.
     * </p>
     *
     * @param coll  the collection to check, may be null
     * @return true if non-null and non-empty
     * @since 3.2
     */
    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * 根据list的长度返回 map所需的 initialCapacity
     * @return 如果list为空返回默认为10
     */
    public static int initialCapacity(final Collection<?> coll){
        if(isEmpty(coll)){
            return 10;
        }else {
           return coll.size();
        }
    }

    /**
     * 取交集 [AB] (list 需要重写 equals， set 需要重写 hash)
     * <pre>
     *     intersection([1,2,3,4],[4,5]) -》 [4]
     * </pre>
     * @param c1 list
     * @param c2 list
     * @return 交集 list [空返回空对象]
     */
    public static Collection<?> intersection(Collection<?> c1, Collection<?> c2){

        if(isEmpty(c1)|| isEmpty(c2)){
            return Collections.emptyList();
        }
        // 为了不改变传入的值，因为 rm 会造成原来的数据值被改变
        Collection<?> temp1 = ObjectUtils.clone(c1);
        Collection<?> temp2 = ObjectUtils.clone(c2);
        Collection<?> result = ObjectUtils.clone(c1);
        // 删除成功表示，有交集
        if(temp1.removeAll(temp2) && (result.removeAll(temp1))){
                // 获取被删除的的交集
                return result;
        }
        return Collections.emptyList();
    }


    /**
     * 取并集  (list 需要重写 equals， set 需要重写 hash)
     * <pre>
     *     union([1,2,3,4],[4,5]) -》 [1,2,3,4,5]
     *     union([1,2,3,4],[]) -》 [1,2,3,4]
     *     union([],[4,5]) -》 [4,5]
     * </pre>
     * @param c1 list
     * @param c2 list
     * @return 交集 list [空返回空对象]
     */
    public static Collection<?> union(Collection<?> c1, Collection<?> c2){
        if(isEmpty(c1) && isEmpty(c2)){
            return Collections.emptyList();
        }else if(isEmpty(c2)){
            return c1;
        }else if(isEmpty(c1)){
            return c2;
        }
        // 为了不改变传入的值，因为 rm 会造成原来的数据值被改变
        Collection<?> temp1 = ObjectUtils.clone(c1);
        Collection<?> temp2 = ObjectUtils.clone(c2);
        // 删除成功表示，有交集. 不管删除删除成功，都会去掉交集 然后相加就是并集了（A+B-AB）
        temp1.removeAll(temp2);
        // 使用 Java 8+ Stream 合并两个 Collection
        Collection<?> combinedCollection = Stream.of(temp1, temp2)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return combinedCollection;
    }

}
